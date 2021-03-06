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

public class LibrarianTable implements Initializable{
    private final Query<Librarian> query = new Query();
    ObservableList<Librarian> librarianList = query.select(Query.LIBRARIAN_TABLE, 1);
    
    @FXML private TableView<Librarian> librarianTable;
    @FXML private TableColumn<Librarian, String> fNameCol, mNameCol, lNameCol, depCol, phone1Col, phone2Col, addrCol, streetCol;
    @FXML private TextField searchLibrarian;
    
    private Long libId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!librarianList.isEmpty()){
            librarianTable.setItems(librarianList);      
        }else{   
            librarianTable.setPlaceholder(new StackPane(new Text("Hakuna mkutubi aliyesajiliwa")));
        }
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
    public void onSearchLibrarian(){
        FilteredList<Librarian> filteredData = new FilteredList<>(librarianList, l->true);
        searchLibrarian.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(librarian->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(librarian.getStreet().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(librarian.getFirstName().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(librarian.getMiddleName().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(librarian.getLastName().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(librarian.getDepartment().toLowerCase().equals(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Librarian> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(librarianTable.comparatorProperty());
        librarianTable.setItems(sortedList);
    }
    @FXML
    public void onLibClicked(){
       if(librarianTable.getSelectionModel().getSelectedItem() != null){
            libId = librarianTable.getSelectionModel().getSelectedItem().getId();
        }       
    }
    @FXML
    public void onViewLibrarian(){        
        if(librarianTable.getSelectionModel().getSelectedItem() != null){
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
    public void onBlockLibrarian(){
        if(librarianTable.getSelectionModel().getSelectedItem() != null){
            
            
        }  
    }   
}
