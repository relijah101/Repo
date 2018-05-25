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
import morogoro_lims.model.Author;
import morogoro_lims.model.connect.Query;

public class AuthorTable implements Initializable{
    private final Query<Author> query = new Query();
    @FXML private TableView<Author> authorTable;
    @FXML private TableColumn<Author, String> numberCol, firstNameCol, middleNameCol, lastNameCol;
    @FXML private TextField searchAuthor;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();        
    }
    
    public void initTable(){
        authorTable.getItems().clear();
        ObservableList<Author> authorList = query.select(Query.AUTHOR_TABLE, 1);
        if(!authorList.isEmpty())
            authorTable.setItems(authorList);
        else
            authorTable.setPlaceholder(new Text("Hakuna mwandishi alirekodiwa."));
    }
    public void initCols(){
        numberCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }    
    @FXML
    public void onSearchAuthor(){
        
    }
}
