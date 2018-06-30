package morogoro_lims.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
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
    ObservableList<Librarian> librarianList = FXCollections.observableArrayList();
    @FXML private TableView<Librarian> librarianTable;
    @FXML private TableColumn<Librarian, String> fNameCol, mNameCol, lNameCol, depCol, phone1Col, phone2Col, addrCol, streetCol;
    @FXML private TextField searchLibrarian;
    
    private Long libId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        librarianTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        initTable();
        initCols();
    }
    public void initTable(){
        librarianList.clear();
        librarianList = query.select(Query.LIBRARIAN_TABLE, 1);
        if (!librarianList.isEmpty()) {
            librarianTable.setItems(librarianList);
        } else {
            librarianTable.setPlaceholder(new StackPane(new Text("Hakuna mkutubi aliyesajiliwa")));
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
    public void onSearchLibrarian(){
        FilteredList<Librarian> filteredData = new FilteredList<>(librarianList, l->true);
        searchLibrarian.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(librarian->{
                if(newValue == null || newValue.isEmpty()) return true;
                
                if(librarian.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(librarian.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(librarian.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(librarian.getDepartment().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(librarian.getReg().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(librarian.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
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
            Misc.setIcon(stage);
            stage.setTitle("Taarifa za " + lib.getFirstName()+" "+lib.getMiddleName()+" "+lib.getLastName());
            stage.setScene(scene);
            stage.show();            
        }    
    }
    @FXML
    public void onBlockLibrarian(){
        if(librarianTable.getSelectionModel().getSelectedItem() != null){
            libId = librarianTable.getSelectionModel().getSelectedItem().getId();
            String reg = librarianTable.getSelectionModel().getSelectedItem().getReg();
            if(Query.blockLibrarian(reg, "0")){
               initTable(); 
            }
        }  
    }   
    
    @FXML
    public void onResetPassword(){
        if(librarianTable.getSelectionModel().getSelectedItem() != null){
            String reg = librarianTable.getSelectionModel().getSelectedItem().getReg();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 16; i++){
                sb.append((char) getRandomNumber());
            }
            String generatedResetPwd = sb.toString();
            
            String hashedGeneratedResetPwd = Misc.getSHA512Password(generatedResetPwd);
            if(query.updatePwd(reg, hashedGeneratedResetPwd)){
                Misc.display("Neno siri jipya ni\n"+generatedResetPwd+"\nAndika mahali neno la siri, na kumbuka kubadilisha", 0);
            }
        } 
    }
    
    public static int getRandomNumber(){
        int min = 33;
        int max = 126;
        Random r = new Random();
        return r.nextInt((max - min) + 1) + 33;
    }
}
