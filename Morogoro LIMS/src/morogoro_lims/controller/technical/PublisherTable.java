
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
import javafx.scene.text.Text;
import morogoro_lims.model.Author;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.connect.Query;

public class PublisherTable implements Initializable{
    private final Query<Publisher> query = new Query();
    @FXML private TextField searchPublisher;
    @FXML private TableView<Publisher> publisherTable;
    @FXML private TableColumn<Publisher, String> numberCol, publisherCol;
    
    private final ObservableList<Publisher> pubList = query.select(Query.PUBLISHER_TABLE, 1);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        publisherTable.getItems().clear();        
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
                if(newValue.isEmpty()){
                    return true;
                }
                
                String lowerCaseValue = newValue.toLowerCase();
                return pub.getPublisher().toLowerCase().equals(lowerCaseValue);
            });
        });
        SortedList sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(publisherTable.comparatorProperty());
        publisherTable.setItems(sortedList);
    }
}
