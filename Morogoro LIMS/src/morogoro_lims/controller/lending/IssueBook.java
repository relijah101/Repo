package morogoro_lims.controller.lending;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Book;
import morogoro_lims.model.Member;
import morogoro_lims.model.connect.DBCon;
import morogoro_lims.model.query.Query;

public class IssueBook implements Initializable{
    @FXML private TextField searchMemberFld, searchBookFld, memberNameFld;
    @FXML private ListView<Member> memberList;
    @FXML private ListView<Book> bookList, issueList, unReturnedBooks;
    @FXML private Button addBookBtn, removeBookBtn;
    @FXML private ComboBox<String> returnDateFld;
    
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
        addBookBtn.setDisable(true);
        removeBookBtn.setDisable(true);
        memberList.setItems(memberQuery.select(Query.MEMBER_TABLE, 1));
        bookList.setItems(bookQuery.select(Query.BOOK_TABLE, 0));
    }
    
    public void reset(){
        memberList.getSelectionModel().clearSelection();
        bookList.getSelectionModel().clearSelection();
        issueList.getSelectionModel().clearSelection();
        issueList.getItems().clear();
        searchMemberFld.setText("");
        searchBookFld.setText("");
        memberNameFld.setText("");
        addBookBtn.setDisable(true);
        removeBookBtn.setDisable(true);
    }   
    
    @FXML
    public void onMemberListClicked(){
        if(memberList.getSelectionModel().getSelectedItem() != null){
            memberId = memberList.getSelectionModel().getSelectedItem().getId();
            memberReg = memberList.getSelectionModel().getSelectedItem().getRegNumber();
            String fname = memberList.getSelectionModel().getSelectedItem().getFirstName();
            String mname = memberList.getSelectionModel().getSelectedItem().getMiddleName();
            String lname = memberList.getSelectionModel().getSelectedItem().getLastName();
            memberNameFld.setText(fname + " " + mname + " " + lname);
            System.out.println(memberReg);
            size = query.getSize(memberReg);
            ObservableList unReturnedBooksList = FXCollections.observableArrayList();
            if(size != 0){
                String sql = "SELECT * "
                        + "FROM registered, lending, returning, book "
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
                        String data = result.getString("class_number");
                        data += "/ ";
                        data += result.getString("title");
                        unReturnedBooksList.add(data);
                    }
                    unReturnedBooks.setItems(unReturnedBooksList);
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage()+"juu", 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            }else{
                
            }
        }
    }        
    @FXML
    public void onBookListClicked(){
        if(bookList.getSelectionModel().getSelectedItem() != null){
            if(memberList.getSelectionModel().getSelectedItem() != null){
                bookId = bookList.getSelectionModel().getSelectedItem().getId();
                addBookBtn.setDisable(false);
                removeBookBtn.setDisable(false);
            }else{
                Misc.display("Hakikisha umechagua mwanachama!", 1);
            }            
        }
    }
    
    @FXML
    public void onAddBook(){
        if(bookList.getSelectionModel().getSelectedItem() != null){
            if(issueList.getItems().contains(bookList.getSelectionModel().getSelectedItem())){
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
            issueList.getItems().add(bookList.getSelectionModel().getSelectedItem());
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
        
        ObservableList<Book> bookToIssue = issueList.getItems();
        String returnDate = returnDateFld.getSelectionModel().getSelectedItem();
    }
    @FXML
    public void onClear(){
        reset();
    }
}


