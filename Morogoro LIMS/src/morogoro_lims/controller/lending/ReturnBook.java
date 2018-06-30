package morogoro_lims.controller.lending;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Book;
import morogoro_lims.model.Member;
import morogoro_lims.model.connect.DBCon;
import morogoro_lims.model.query.Query;

public class ReturnBook implements Initializable{
    @FXML TextField searchMemberFld;
    @FXML TableView<Member> membersTable;
    @FXML TableColumn<Member, String> memberRegCol, memberNameCol;
    @FXML ListView<Book> unReturnedBooksListView, addBookListView;
    @FXML String memberReg;
    @FXML String classNumber, classNumber2;
    @FXML private Button removeBookBtn, upBookBtn;
    private final ObservableList<Book> returningBooks = FXCollections.observableArrayList();
    private ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final Query query = new Query();
    private static Connection con = null;
    private static PreparedStatement statement = null;
    private static ResultSet result = null;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
        unReturnedBooksListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addBookListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addBookListView.setItems(returningBooks);
    }
    public void initTable(){
        memberList.clear();
        memberList = getMemberWithIssuedBook();
        if(memberList.isEmpty()){
            membersTable.setPlaceholder(new Text("Hakuna rekodi."));
        }else{
            membersTable.setItems(memberList);
        }
    }
    public void initCols(){
        memberRegCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        memberNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }
    public ObservableList<Book> getMemberWithBooks(){
        ObservableList<Book> unReturnedBooksList = FXCollections.observableArrayList();
        String sql = "SELECT book.class_number, book.title, lending.id "
                        + "FROM registered, lending, book "
                        + "WHERE registered.registration_number = ? "
                        + "AND registered.registration_number = lending.member_reg_number "
                        + "AND lending.id NOT IN (SELECT returning.lend_id FROM returning) "
                        + "AND lending.book_id = book.id";
            try{
                con = DBCon.getConnection();
                statement = con.prepareStatement(sql);
                statement.setString(1, memberReg);
                result = statement.executeQuery();
                while(result.next()){
                    Book data = new Book(result.getString("class_number"), result.getString("title"), result.getLong("id"));
                    unReturnedBooksList.add(data);
                }
                unReturnedBooksListView.setItems(unReturnedBooksList);
            }catch(SQLException sqle){
                Misc.display(sqle.getLocalizedMessage(), 2);
            }finally{
                try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
            }
        return unReturnedBooksList;
    }
    public ObservableList<Member> getMemberWithIssuedBook(){
        ObservableList<Member> member = FXCollections.observableArrayList();
        String sql = "SELECT member.id as member_id, registered.registration_number, member.firstname as fname, member.middlename as mname, " +
            "member.lastname as lname, librarian.reg_number, librarian.firstname, lending.id as lend_id, lending.lend_date, " +
            "lending.return_date " +
            "FROM registered, lending, book, member, librarian " +
            "WHERE registered.registration_number = lending.member_reg_number " +
            "AND lending.librarian_id = librarian.reg_number " +
            "AND registered.member_id = member.id " +
            "AND lending.id NOT IN (SELECT returning.lend_id FROM returning) " +
            "AND lending.book_id = book.id ";
            try{
                con = DBCon.getConnection();
                statement = con.prepareStatement(sql);
                result = statement.executeQuery();
                while(result.next()){
                    Member data = new Member(result.getString("registration_number"), 
                    result.getString("fname"), result.getString("mname"),
                    result.getString("lname")
                    );
                    member.add(data);
                }
            }catch(SQLException sqle){
                Misc.display(sqle.getLocalizedMessage(), 2);
            }finally{
                try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
            }
        return member;
    }
    public void reset(){       
        returningBooks.clear();
        membersTable.getSelectionModel().clearSelection(); 
        addBookListView.getSelectionModel().clearSelection(); 
        unReturnedBooksListView.getSelectionModel().clearSelection();
        searchMemberFld.setText("");
        removeBookBtn.setDisable(true);
    } 
    @FXML
    public void onMembersTableClicked(){
        if(membersTable.getSelectionModel().getSelectedItem() != null){            
            memberReg = membersTable.getSelectionModel().getSelectedItem().getRegNumber();            
            unReturnedBooksListView.setItems(getMemberWithBooks());
        }
    }
    @FXML
    public void onDownBook(){
        if(classNumber != null){
            Book rb = unReturnedBooksListView.getSelectionModel().getSelectedItem();
            if(returningBooks.contains(rb)){
                Misc.display("Kitabu kimeshachaguliwa.", 1);
                return;
            }
            returningBooks.add(rb);
            unReturnedBooksListView.getItems().remove(rb);
        }
    }
    @FXML
    public void onUpBook(){
        if(classNumber2 != null){
            Book rb = addBookListView.getSelectionModel().getSelectedItem();
            returningBooks.remove(rb);
            unReturnedBooksListView.getItems().add(rb);
        }
    }
    @FXML
    public void onUnReturnedBooksClicked(){
        if(unReturnedBooksListView.getSelectionModel().getSelectedItem() != null){
            removeBookBtn.setDisable(false);
            classNumber = unReturnedBooksListView.getSelectionModel().getSelectedItem().getClassNumber();
        }
    }
    @FXML 
    public void onClickAddBookListView(){
        if(addBookListView.getSelectionModel().getSelectedItem() != null){
            classNumber2 = addBookListView.getSelectionModel().getSelectedItem().getClassNumber();
            System.out.println(classNumber2);
        }
    }
    @FXML
    public void onSave(){
        if(returningBooks.isEmpty()){
            Misc.display("Hakikisha umechagua mwanachama na vitabu vya kurudisha.", 1);
            return;
        }
        String today = Misc.today();
        Object obj[] = {returningBooks, today};
        if(query.insert(obj, Query.RETURN_TABLE)){            
            reset();
            initTable();
        }
    }
    @FXML
    public void onClear(){
        reset();
    }
    @FXML
    public void onSearchingMember(){
        FilteredList<Member> filteredList = new FilteredList<>(memberList, p->true);
        searchMemberFld.textProperty().addListener((observableValue, oldValue, newValue)->{
            filteredList.setPredicate(mem->{
                if(newValue.isEmpty() || newValue == null) return true;
                
                String lowerCase = newValue.toLowerCase();
                if(mem.getRegNumber().contains(lowerCase)) return true;
                if(mem.getFirstName().contains(lowerCase)) return true;
                if(mem.getMiddleName().contains(lowerCase)) return true;
                if(mem.getLastName().contains(lowerCase)) return true;
                
                return false;
            });
        });
        SortedList<Member> sortedList = new SortedList<>(filteredList);
        membersTable.setItems(sortedList);
    }
}
