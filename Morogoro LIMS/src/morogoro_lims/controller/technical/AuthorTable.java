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
import morogoro_lims.model.Author;
import morogoro_lims.model.query.Query;

public class AuthorTable implements Initializable{
    private final Query<Author> query = new Query();
    @FXML private TableView<Author> authorTable;
    @FXML private TableColumn<Author, String> numberCol, firstNameCol, middleNameCol, lastNameCol;
    @FXML private TextField searchAuthor;
    
    private ObservableList<Author> authorList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();        
    }
    
    public void initTable(){
        authorList.clear();
        authorList = query.select(Query.AUTHOR_TABLE, 1);
        if(!authorList.isEmpty())
            authorTable.setItems(authorList);
        else
            authorTable.setPlaceholder(new Text("Hakuna mwandishi aliyerekodiwa."));
    }
    public void initCols(){
        numberCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }    
    @FXML
    public void onSearchAuthor(){        
        FilteredList<Author> filteredData = new FilteredList<>(authorList, a->true);
        searchAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                
                if(newValue.isEmpty()) return true;
                String lowerCaseValue = newValue.toLowerCase();
                if(author.getFirstName().toLowerCase().equals(lowerCaseValue))return true;
                if(author.getMiddleName().toLowerCase().equals(lowerCaseValue))return true;
                if(author.getLastName().toLowerCase().equals(lowerCaseValue))return true;
                
                return false;
            });
        });
        SortedList sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(authorTable.comparatorProperty());
        authorTable.setItems(sortedList);
    }
    @FXML
    public void onUpdateAuthor(){
        if(authorTable.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/morogoro_lims/view/Technical/EditAuthor.fxml"));
            try{
                loader.load();
            }catch(IOException ioe){
                Misc.display(ioe.getLocalizedMessage(), 2);
            }
            Author selectedAuthor = authorTable.getSelectionModel().getSelectedItem();
            EditAuthor editAuthor = loader.getController();
            editAuthor.initData(selectedAuthor);
            
            Stage stage = new Stage();
            Misc.setIcon(stage);
            stage.setTitle("Badilisha taarifa za mwandishi:");
            stage.setScene(new Scene(loader.getRoot()));
            stage.showAndWait();
            initTable();
        }
    }
    @FXML
    public void onRemoveAuthor(){
        if(authorTable.getSelectionModel().getSelectedItem() != null){
            Long authorId = authorTable.getSelectionModel().getSelectedItem().getId();
            if(query.delete(Query.AUTHOR_TABLE, authorId)){
                initTable();
            }
        }
    }
}
