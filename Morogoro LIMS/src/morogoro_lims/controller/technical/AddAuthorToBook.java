package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.connect.Query;

public class AddAuthorToBook implements Initializable{
    private final Query<Book> bookQuery = new Query();
    private final Query<Author> authorQuery = new Query();
    @FXML TableView<Book> bookTable;
    @FXML TableColumn<Book, String> bookNumberCol, bookTitleCol;
    @FXML TextField searchBook, searchAuthor, bookNumberFld, bookTitleFld;
    
    @FXML TableView<Author> authorTable;
    @FXML TableColumn<Author, String> authorNameCol, authorIdCol;
    
    @FXML ListView<String> authorList;
    @FXML MenuItem addAuthor, removeAuthor;
    
    private Long bookId, authorId;
    ObservableList<String> authors = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBookTable();
        initBookCols();
        initAuthorTable();
        initAuthorCols();
        
        authorList.setItems(authors);
        addAuthor.setDisable(true);
    }
    public void initBookTable(){
        bookTable.getItems().clear();
        ObservableList<Book> bookList = bookQuery.select(Query.BOOK_TABLE, 1);
        if(!bookList.isEmpty())
            bookTable.setItems(bookList);
        else
            bookTable.setPlaceholder(new Text("Hakuna kitabu kilichorekodiwa."));
    }
    public void initAuthorTable(){
        authorTable.getItems().clear();
        ObservableList<Author> authorList = authorQuery.select(Query.AUTHOR_TABLE, 1);
        if(!authorList.isEmpty())
            authorTable.setItems(authorList);
        else
            authorTable.setPlaceholder(new Text("Hakuna mwandishi aliyerekodiwa."));
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
        
    }
    @FXML
    public void onSearchAuthor(){
        
    }
    @FXML
    public void onAddAuthorToBook(){
        authors.add(authorTable.getSelectionModel().getSelectedItem().getFullName());
        authorTable.getSelectionModel().clearSelection();
    }
    @FXML
    public void onRemoveAuthor(){
        authors.remove(authorList.getSelectionModel().getSelectedIndex());
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
        
    }
}
