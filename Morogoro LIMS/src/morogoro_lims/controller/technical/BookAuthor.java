package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.query.Query;

public class BookAuthor implements Initializable{
    @FXML TableView<Book> bookAuthorsTable;
    @FXML TableColumn<Book, String> titleCol;
    @FXML TableColumn<Book, ObservableList<ObservableList<Author>>> authorCol;
    @FXML TextField searchFld;
    private final Query query=new Query<>();
    
    private Long bookId;
    ObservableList<Book> bookList = query.getBookAuthor(query.select(Query.BOOK_TABLE, 1), 1);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();  
        initCols();
    }
    public void initTable(){
        bookList.clear();
        if(bookList.isEmpty()){
          bookAuthorsTable.setPlaceholder(new StackPane(new Text("Hakuna rekodi ya kitabu.")));
        }else{
            bookAuthorsTable.setItems(bookList);
        }
        
    }
    public void initCols(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("bookAuthors"));
    }
    @FXML
    public void onSearching(){
        FilteredList filteredList = new FilteredList(bookList,r->true);
        searchFld.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(b -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value  = newValue.toLowerCase();
                return false;
            });
        });
        SortedList sortedList = new SortedList(filteredList);
    }
    @FXML
    public void onTableClicked(){
        if(bookAuthorsTable.getSelectionModel().getSelectedItem() != null){
            bookId=bookAuthorsTable.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onRemoveAuthor(){
        
    }
}
