package morogoro_lims.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.query.Query;

public class InActiveLibrarianTable implements Initializable{
    private final Query<Librarian> query = new Query();   
    
    @FXML private TableView<Librarian> librarianTable;
    @FXML private TableColumn<Librarian, String> fNameCol, mNameCol, lNameCol, depCol, phone1Col, phone2Col, addrCol, streetCol;
    @FXML private TextField searchFld;
    private ObservableList<Librarian> librarianList;
    Long libId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        librarianList = query.select(Query.LIBRARIAN_TABLE, 0);
        if(!librarianList.isEmpty()){
            librarianTable.setItems(librarianList);      
        }else{   
            librarianTable.setPlaceholder(new StackPane(new Text("Hakuna mkutubi aliyesitishwa")));
        }
    }
    public void initCols(){
        fNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        mNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        depCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("postalAddr"));
        phone1Col.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        phone2Col.setCellValueFactory(new PropertyValueFactory<>("phone2"));
        streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
    }

    @FXML
    public void onViewLibrarian(){
        if(libId != null){
            Librarian lib = librarianTable.getSelectionModel().getSelectedItem();
            FXMLLoader librarianLoader = new FXMLLoader();
            librarianLoader.setLocation(getClass().getResource("/morogoro_lims/view/Admin/LibrarianViewer.fxml"));
            try {
                librarianLoader.load();
            } catch (IOException ioe) {
                Misc.display(ioe.getLocalizedMessage(), 2);
            }  
            LibrarianViewer viewer = librarianLoader.getController();
            viewer.setData(lib);
            
            Scene scene = new Scene(librarianLoader.getRoot());
            Stage stage = new Stage();
            stage.setTitle("Taarifa za " + lib.getFirstName()+" "+lib.getMiddleName()+" "+lib.getLastName());
            stage.setScene(scene);
            stage.show();        
        }
    }
    @FXML
    public void onUnBlockLibrarian(){
        if(libId != null){            
            String reg = librarianTable.getSelectionModel().getSelectedItem().getReg();
            if(Query.blockLibrarian(reg, "1")){
               initTable(); 
            }
            
        } 
    }
    @FXML
    public void onLibClicked(){
        if(librarianTable.getSelectionModel().getSelectedItem() != null){
            libId = librarianTable.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onSearching(){
        FilteredList<Librarian> filteredList = new FilteredList<>(librarianList, p->true);
        searchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(lib->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(lib.getReg().contains(newValue.toLowerCase())) return true;
                if(lib.getFirstName().contains(newValue.toLowerCase())) return true;
                if(lib.getMiddleName().contains(newValue.toLowerCase())) return true;
                if(lib.getLastName().contains(newValue.toLowerCase())) return true;
                if(lib.getDepartment().contains(newValue.toLowerCase())) return true;
                if(lib.getStreet().contains(newValue.toLowerCase())) return true;
                
                return false;
            });
        });
        SortedList<Librarian> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(librarianTable.comparatorProperty());
        librarianTable.setItems(sortedList);
    }
}
