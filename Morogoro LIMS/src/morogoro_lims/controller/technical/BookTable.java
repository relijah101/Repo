package morogoro_lims.controller.technical;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Book;
import morogoro_lims.model.query.Query;

public class BookTable implements Initializable{
    private final Query<Book> query = new Query();
    private ObservableList<Book>  bookList = FXCollections.observableArrayList();
    private Long bookId;
    @FXML TextField searchBook;
    @FXML TableView<Book> bookTable;
    @FXML TableColumn<Book, String> numberCol, titleCol, categoryCol, editionCol, publisherCol, copiesCol, isbnCol;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        bookList.clear();
        bookList = query.select(Query.BOOK_TABLE, 1);
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
        FilteredList<Book> filteredData = new FilteredList<>(bookList, a->true);
        searchBook.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                
                if(newValue.isEmpty()) return true;
                String lowerCaseValue = newValue.toLowerCase();
                if(book.getClassNumber().toLowerCase().contains(lowerCaseValue))return true;
                if(book.getTitle().toLowerCase().contains(lowerCaseValue))return true;
                if(book.getCategory().toLowerCase().contains(lowerCaseValue))return true;
                if(book.getPublisher().toLowerCase().contains(lowerCaseValue))return true;
                
                return false;
            });
        });
        SortedList<Book> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedList);
    }
    @FXML
    public void onTableClicked(){
        if(bookTable.getSelectionModel().getSelectedItem() != null){
            bookId = bookTable.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onUpdateBook(){
        if(bookTable.getSelectionModel().getSelectedItem() != null){
            Book book = bookTable.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();
            stage.setTitle("Badili taarifa za kitabu "+ book.getClassNumber() +")");
            Misc.setIcon(stage);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/morogoro_lims/view/Technical/EditBook.fxml"));
            try{
                loader.load();
            }catch(IOException ioe){
                Misc.display(ioe.getLocalizedMessage(), 2);
            }
            EditBook editBook = loader.getController();
            editBook.initData(book);
            stage.setScene(new Scene(loader.getRoot()));
            stage.showAndWait();
            initTable();
        }
    }
    @FXML
    public void onRemoveBook(){
        if(bookTable.getSelectionModel().getSelectedItem() != null){
            bookId = bookTable.getSelectionModel().getSelectedItem().getId();
            if(query.delete(Query.BOOK_TABLE, bookId)){
                initTable();
            }
        }
    }
}
