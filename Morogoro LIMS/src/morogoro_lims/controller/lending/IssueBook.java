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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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

public class IssueBook implements Initializable{
    @FXML private TextField searchMemberFld, searchBookFld, memberNameFld;
    @FXML private ListView<Book> issueList, unReturnedBooks;
    
    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, String>  memberRegCol, memberNameCol;
    
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> bookNumberCol, bookTitleCol;
    
    @FXML private Button addBookBtn, removeBookBtn;
    @FXML private ComboBox<String> returnDateFld;
    
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final Query<Member> memberQuery = new Query();
    private final Query<Book> bookQuery = new Query();
    private final Query query = new Query();
    private Long memberId;
    private String memberReg, memberName;
    private Long bookId;
    private String bookTitle;
    private int size;
    private static Connection con;
    private static PreparedStatement statement;
    private static ResultSet result;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBookTable();
        initBookTableCols();
        
        initMemberTable();
        initMemberTableCols();
        addBookBtn.setDisable(true);
        removeBookBtn.setDisable(true);
        returnDateFld.setItems(Misc.getEndWeeks());
    }
    public void initBookTable(){
        bookList.clear();
        bookList = bookQuery.select(Query.BOOK_TABLE, 0);
        if(bookList.isEmpty()){
            bookTable.setPlaceholder(new Text("Hakuna rekodi ya kitabu."));
        }else{
            bookTable.setItems(bookList);
        }
    }
    public void initBookTableCols(){
        bookNumberCol.setCellValueFactory(new PropertyValueFactory<>("classNumber"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    }
    
    public void initMemberTable(){
        memberList.clear();
        memberList = memberQuery.select(Query.MEMBER_TABLE, 1);
        if(memberList.isEmpty()){
            membersTable.setPlaceholder(new Text("Hakuna mwanachama"));
        }else{
            membersTable.setItems(memberList);
        }
    }
    public void initMemberTableCols(){
        memberRegCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        memberNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }
    
    public void reset(){
        membersTable.getSelectionModel().clearSelection();
        bookTable.getSelectionModel().clearSelection();
        issueList.getSelectionModel().clearSelection();
        issueList.getItems().clear();
        searchMemberFld.setText("");
        searchBookFld.setText("");
        memberNameFld.setText("");
        addBookBtn.setDisable(true);
        removeBookBtn.setDisable(true);
        returnDateFld.getSelectionModel().select(0);
    }   
    
    @FXML
    public void onMembersTableClicked(){
        if(membersTable.getSelectionModel().getSelectedItem() != null){
            addBookBtn.setDisable(false);
            removeBookBtn.setDisable(false);
            memberId = membersTable.getSelectionModel().getSelectedItem().getId();
            memberReg = membersTable.getSelectionModel().getSelectedItem().getRegNumber();
            String fname = membersTable.getSelectionModel().getSelectedItem().getFirstName();
            String mname = membersTable.getSelectionModel().getSelectedItem().getMiddleName();
            String lname = membersTable.getSelectionModel().getSelectedItem().getLastName();
            memberNameFld.setText(fname + " " + mname + " " + lname);
            size = query.getSize(memberReg);
            ObservableList unReturnedBooksList = FXCollections.observableArrayList();
            unReturnedBooksList.clear();
            unReturnedBooks.getItems().clear();
            if(size != 0){
                String sql = "SELECT * "
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
                        Book data = new Book(result.getString("class_number"), result.getString("title"), 1L);
                        unReturnedBooksList.add(data);
                    }
                    unReturnedBooks.setItems(unReturnedBooksList);
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            }
        }
    }        
    @FXML
    public void onBookListClicked(){
        if(bookTable.getSelectionModel().getSelectedItem() != null){
            if(membersTable.getSelectionModel().getSelectedItem() != null){
                bookId = bookTable.getSelectionModel().getSelectedItem().getId();
                addBookBtn.setDisable(false);
                removeBookBtn.setDisable(false);
            }else{
                Misc.display("Hakikisha umechagua mwanachama!", 1);
            }            
        }
    }
    
    @FXML
    public void onAddBook(){
        if(bookTable.getSelectionModel().getSelectedItem() != null){
            if(issueList.getItems().contains(bookTable.getSelectionModel().getSelectedItem())){
                Misc.display("Kitabu kimeshaongezwa kwenye listi.", 0);
                return;                
            }
            if((issueList.getItems().size() + size) >=5){
                Misc.display("Idadi ya vitabu imezidi.", 0);
                return; 
            }
            if(issueList.getItems().size() >= 5){
                Misc.display("Idadi ya vitabu imezidi 5.", 0);
                return; 
            }
            issueList.getItems().add(bookTable.getSelectionModel().getSelectedItem());
        }
    }
    @FXML
    public void onRemoveBook(){
        if(issueList.getSelectionModel().getSelectedItem() != null){
            issueList.getItems().remove(issueList.getSelectionModel().getSelectedItem());
            issueList.getSelectionModel().clearSelection();
        }
    }
    
    @FXML
    public void onSave(){
        if(issueList.getItems().isEmpty()){
            Misc.display("Hakikisha umeweka vitabu kwenye listi!", 1);
            return;
        }
        if(returnDateFld.getSelectionModel().getSelectedItem() == null){
            Misc.display("Weka muda wa kurudisha kitabu", 1);
            returnDateFld.requestFocus();
            return;
        }
        ObservableList<Book> bookToIssue = issueList.getItems();
        String issueDate = Misc.today();
        String returnDate = returnDateFld.getSelectionModel().getSelectedItem();
        
        Object[] object = {bookToIssue, memberReg, issueDate, returnDate};
        if(query.insert(object, Query.LEND_TABLE)){
            reset();
            initBookTable();
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
        sortedList.comparatorProperty().bind(membersTable.comparatorProperty());
        membersTable.setItems(sortedList);
    }
    @FXML 
    public void onSearchingBook(){
        FilteredList<Book> filteredList = new FilteredList<>(bookList, p->true);
        searchMemberFld.textProperty().addListener((observableValue, oldValue, newValue)->{
            filteredList.setPredicate(bk->{
                if(newValue.isEmpty() || newValue == null) return true;
                
                String lowerCase = newValue.toLowerCase();
                if(bk.getClassNumber().contains(lowerCase)) return true;
                if(bk.getTitle().contains(lowerCase)) return true;
                
                return false;
            });
        });
        SortedList<Book> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedList);
    }
}


