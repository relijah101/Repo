package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.query.Query;

public class AddAuthorToBook implements Initializable{
    private final Query<Book> bookQuery = new Query();
    private final Query<Author> authorQuery = new Query();
    @FXML TableView<Book> bookTable;
    @FXML TableColumn<Book, String> bookNumberCol, bookTitleCol;
    @FXML TextField searchBook, searchAuthor, bookNumberFld, bookTitleFld;
    
    @FXML TableView<Author> authorTable;
    @FXML TableColumn<Author, String> authorNameCol, authorIdCol;
    
    @FXML ListView<Author> authorList;
    @FXML MenuItem addAuthor, removeAuthor;
    
    private Long bookId, authorId;
    ObservableList<Author> authors = FXCollections.observableArrayList();
    ObservableList<Author> authorsList;
    ObservableList<Book> bookList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBookTable();
        initBookCols();
        initAuthorTable();
        initAuthorCols();
        
        authorList.setItems(authors);
        addAuthor.setDisable(true);
    }
    public void reset(){
        searchBook.setText("");
        searchAuthor.setText("");
        bookNumberFld.setText("");
        bookTitleFld.setText("");
        authorTable.getSelectionModel().clearSelection();
        bookTable.getSelectionModel().clearSelection();
        authors.clear();
    }
    public void initBookTable(){
        bookTable.getItems().clear();
        bookList = bookQuery.select(Query.BOOK_TABLE, 1);
        if(!bookList.isEmpty())
            bookTable.setItems(bookList);
        else{
            bookTable.setPlaceholder(new Text("Hakuna kitabu kilichorekodiwa."));
            bookTable.setDisable(true);
        }
    }
    public void initAuthorTable(){
        authorTable.getItems().clear();
        authorsList = authorQuery.select(Query.AUTHOR_TABLE, 1);
        if(!authorsList.isEmpty())
            authorTable.setItems(authorsList);
        else{
            authorTable.setPlaceholder(new Text("Hakuna mwandishi aliyerekodiwa."));
            searchAuthor.setDisable(true);
        }
    }
    public void initBookCols(){
        bookNumberCol.setCellValueFactory(new PropertyValueFactory<>("classNumber"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    }
    public void initAuthorCols(){
        authorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }    
    @FXML
    public void onSearchBook(){
        FilteredList<Book> filteredList = new FilteredList<>(bookList, e->true);
        searchBook.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(book->{
                //If search book is empty. Display all books
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(book.getClassNumber().equals(lowerCaseFilter)){
                    return true;
                }else if(book.getTitle().equals(lowerCaseFilter)){
                    return true;
                }else if(book.getCategory().equals(lowerCaseFilter)){
                    return true;
                }else if(book.getPublisher().equals(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });    
        SortedList sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedList);
    }
    @FXML
    public void onSearchAuthor(){
        FilteredList<Author> filteredList = new FilteredList<>(authorsList, e->true);
        searchAuthor.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(author->{
                //If search author is empty. Display all authors
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(author.getFirstName().equals(lowerCaseFilter)){
                    return true;
                }else if(author.getMiddleName().equals(lowerCaseFilter)){
                    return true;
                }else if(author.getLastName().equals(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });    
        SortedList sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(authorTable.comparatorProperty());
        authorTable.setItems(sortedList);
    }
    @FXML
    public void onAddAuthorToBook(){
        if(authors.contains(authorTable.getSelectionModel().getSelectedItem())){
            Misc.display("Mwandishi ameshaongezwa tayari.", 1);
            return;
        }
        authors.add(authorTable.getSelectionModel().getSelectedItem());
        authorTable.getSelectionModel().clearSelection();
    }
    @FXML
    public void onRemoveAuthor(){
        int index = authorList.getSelectionModel().getSelectedIndex();
        if(index == -1){
            return;
        }
        authors.remove(index);
    }
    @FXML
    public void authorTableClicked(){
        authorId = authorTable.getSelectionModel().getSelectedItem().getId();        
    }
    @FXML
    public void bookTableClicked(){
        bookId = bookTable.getSelectionModel().getSelectedItem().getId();
        addAuthor.setDisable(false);
        bookNumberFld.setText(bookTable.getSelectionModel().getSelectedItem().getClassNumber());
        bookTitleFld.setText(bookTable.getSelectionModel().getSelectedItem().getTitle());
    }
    @FXML
    public void onCancel(){
        bookTable.getSelectionModel().clearSelection();
        authorTable.getSelectionModel().clearSelection();
        searchAuthor.setText("");
        searchBook.setText("");
        bookNumberFld.setText("");
        bookTitleFld.setText("");
        authors.clear();
    }
    @FXML
    public void onSave(){
        if(bookId != null && !authors.isEmpty()){
            Object[] obj = {bookId, authors};
        
            Query<Object> query = new Query<>();
            query.insert(obj, Query.BOOK_AUTHOR_TABLE);
            reset();
            Misc.display("Waandishi wa kitabu wamehifadhiwa.", 0);
        }        
    }
}
