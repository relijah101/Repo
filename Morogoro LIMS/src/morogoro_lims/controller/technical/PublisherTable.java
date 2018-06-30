
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
import morogoro_lims.model.Publisher;
import morogoro_lims.model.query.Query;

public class PublisherTable implements Initializable{
    private final Query<Publisher> query = new Query();
    private Long publisherId;
    Stage stage;
            
    @FXML private TextField searchPublisher;
    @FXML private TableView<Publisher> publisherTable;
    @FXML private TableColumn<Publisher, String> numberCol, publisherCol;
    
    private ObservableList<Publisher> pubList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        pubList.clear();
        pubList = query.select(Query.PUBLISHER_TABLE, 1);      
        if(!pubList.isEmpty())
            publisherTable.setItems(pubList);
        else
            publisherTable.setPlaceholder(new Text("Hakuna mchapishaji alirekodiwa."));
    }
    public void initCols() {
        numberCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    }
    @FXML
    public void onSearchPublisher(){
        FilteredList<Publisher> filteredData = new FilteredList<>(pubList, a->true);
        searchPublisher.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pub -> {
                if(newValue.isEmpty())return true;
                
                String lowerCaseValue = newValue.toLowerCase();
                if(pub.getPublisher().toLowerCase().contains(lowerCaseValue))return true;
                
                return false;
            });
        });
        SortedList sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(publisherTable.comparatorProperty());
        publisherTable.setItems(sortedList);
    }
    @FXML
    public void onTableClicked(){
        if(publisherTable.getSelectionModel().getSelectedItem() != null){
            publisherId = publisherTable.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onEditPublisher(){
        if(publisherId != null){
            Publisher pub = publisherTable.getSelectionModel().getSelectedItem();
            stage = new Stage();
            Misc.setIcon(stage);
            stage.setTitle("Badili taarifa za mchapishaji: "+ pub.getPublisher());
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/morogoro_lims/view/Technical/EditPublisher.fxml"));
            try{
                loader.load();
            }catch(IOException ioe){
                Misc.display(ioe.getLocalizedMessage(), 2);
            }
            EditPublisher editPub = loader.getController();
            editPub.initData(pub);
            stage.setScene(new Scene(loader.getRoot()));
            stage.showAndWait();
            initTable();
        }
    }
    @FXML
    public void onRemovePublisher(){
        if(publisherId != null){
            if(query.delete(Query.PUBLISHER_TABLE, publisherId)){
                initTable();
            }
        }
    }
}
