package morogoro_lims.controller.registration;

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
import morogoro_lims.model.Adult;
import morogoro_lims.model.Logs;
import morogoro_lims.model.query.Query;

public class ViewRegisteredAdult implements Initializable{
    @FXML TableView<Adult> membersTable;
    @FXML TableColumn<Adult, String> regCol, firstnameCol, middlenameCol, lastnameCol, libCol;
    @FXML TextField searchFld;
    private Long adultId;
    private ObservableList<Adult> adultList = FXCollections.observableArrayList();
    private final Query<Adult> query = new Query<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        adultList.clear();
        adultList = query.getMembersFull(Query.ADULT_TABLE, "1");
        if(adultList.isEmpty()){
            membersTable.setPlaceholder(new Text("Hakuna mwanachama aliyesajiliwa."));
        }else{
            membersTable.setItems(adultList);
        }
    }
    public void initCols(){
        regCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middleName")); 
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        libCol.setCellValueFactory(new PropertyValueFactory<>("libName"));
    }
    @FXML
    public void onSearching(){
        FilteredList<Adult> filteredList = new FilteredList<>(adultList, e->true);
        searchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(ad -> {
                if(newValue == null || newValue.isEmpty()) return true;
                
                if(ad.getRegNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getIdNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
                
                return false;
            });
        });
        SortedList<Adult> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(membersTable.comparatorProperty());
        membersTable.setItems(sortedList);
    }
    @FXML
    public void onTableClicked(){
        if(membersTable.getSelectionModel().getSelectedItem() != null){
            adultId = membersTable.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onSeeMore(){
        if(adultId != null){
            Adult adult = membersTable.getSelectionModel().getSelectedItem();
            FXMLLoader adultLoader = new FXMLLoader();
            adultLoader.setLocation(getClass().getResource("/morogoro_lims/view/registration/AdultViewer.fxml"));
            try {
                adultLoader.load();
            } catch (IOException ioe) {
                Misc.display(ioe.getLocalizedMessage(), 2);
            }  
            AdultViewer viewer = adultLoader.getController();
            viewer.initData(adult);
            
            Scene scene = new Scene(adultLoader.getRoot());
            Stage stage = new Stage();
            Misc.setIcon(stage);
            stage.setTitle("Taarifa za " + adult.getFirstName()+" "+adult.getMiddleName()+" "+adult.getLastName());
            stage.setScene(scene);
            stage.showAndWait(); 
            initTable();
        }
    }
}
