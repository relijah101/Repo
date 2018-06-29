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
import morogoro_lims.model.Secondary;
import morogoro_lims.model.query.Query;

public class ViewRegisteredSecondary implements Initializable{
    @FXML TableView<Secondary> membersTable;
    @FXML TableColumn<Secondary, String> regCol, firstnameCol, middlenameCol, lastnameCol, libCol; 
    @FXML TextField searchFld;
    private Long secId;
    private final Query<Secondary> query = new Query<>();
    private ObservableList<Secondary> secondaryList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    
    public void initTable(){
        secondaryList.clear();
        secondaryList = query.getMembersFull(Query.SECONDARY_TABLE, "1");
        if(secondaryList.isEmpty()){
            membersTable.setPlaceholder(new Text("Hakuna mwanachama aliyesajiliwa."));
        }else{
            membersTable.setItems(secondaryList);
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
        FilteredList<Secondary> filteredList = new FilteredList<>(secondaryList, e->true);
        searchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(sec -> {
                if(newValue == null || newValue.isEmpty()) return true;
                
                if(sec.getRegNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getIdNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
                
                return false;
            });
        });
        SortedList<Secondary> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(membersTable.comparatorProperty());
        membersTable.setItems(sortedList);
    }
    @FXML
    public void onTableClicked(){
        if(membersTable.getSelectionModel().getSelectedItem() != null){
            secId = membersTable.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onSeeMore(){
        if(secId != null){
            Secondary secondary = membersTable.getSelectionModel().getSelectedItem();
            FXMLLoader secondaryLoader = new FXMLLoader();
            secondaryLoader.setLocation(getClass().getResource("/morogoro_lims/view/registration/SecondaryViewer.fxml"));
            try {
                secondaryLoader.load();
            } catch (IOException ioe) {
                Misc.display(ioe.getLocalizedMessage(), 2);
            }  
            SecondaryViewer viewer = secondaryLoader.getController();
            viewer.initData(secondary);
            
            Scene scene = new Scene(secondaryLoader.getRoot());
            Stage stage = new Stage();
            Misc.setIcon(stage);
            stage.setTitle("Taarifa za " + secondary.getFirstName()+" "+secondary.getMiddleName()+" "+secondary.getLastName());
            stage.setScene(scene);
            stage.showAndWait(); 
            initTable();
        }
    }
}
