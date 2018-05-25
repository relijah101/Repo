package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Book;
import morogoro_lims.model.connect.Query;

public class BookTable implements Initializable{
    private final Query<Book> query = new Query();
    @FXML TextField searchBook;
    @FXML TableView<Book> bookTable;
    @FXML TableColumn<Book, String> numberCol, titleCol, categoryCol, editionCol, publisherCol, copiesCol, isbnCol;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        bookTable.getItems().clear();
        ObservableList<Book> bookList = query.select(Query.BOOK_TABLE, 1);
        if(!bookList.isEmpty())
            bookTable.setItems(bookList);
        else
            bookTable.setPlaceholder(new Text("Hakuna kitabu kilichorekodiwa."));
    }
    public void initCols(){
        numberCol.setCellValueFactory(new PropertyValueFactory<>("classNumber"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title")); 
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category")); 
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition")); 
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher")); 
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("copies")); 
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    }
    @FXML
    public void onSearchBook(){
        
    }
}
