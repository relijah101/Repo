package morogoro_lims.controller.management;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Book;
import morogoro_lims.model.query.Query;

public class BookReport implements Initializable{
    @FXML TableView<Book> bookTable;
    @FXML TableColumn<Book, String> titleCol;
    @FXML TableColumn<Book, Integer> totalCol;
    @FXML TextField searchBookFld;
    @FXML Text bookTotalText;
    
    private final Query<Book> query = new Query();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        bookList.clear();
        bookList = query.getCountReport(Query.BOOK_TABLE);
        if(bookList.isEmpty()){
            bookTable.setPlaceholder(new Text("Hakuna rekodi"));
        }else{
            bookTable.setItems(bookList);
        }
    }
    public void initCols(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    @FXML
    public void onPrintReport(){
        Misc.printPageSetup(bookTable);
    }
    
    @FXML
    public void onSearching(){
        FilteredList<Book> filteredList = new FilteredList<>(bookList, p->true);
        searchBookFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(book->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(book.getTitle().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Book> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedList);
    }
}
