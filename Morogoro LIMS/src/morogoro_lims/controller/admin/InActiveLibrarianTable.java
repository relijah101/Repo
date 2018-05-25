package morogoro_lims.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.connect.Query;

public class InActiveLibrarianTable implements Initializable{
    private final Query<Librarian> query = new Query();
    ObservableList<Librarian> librarianList = query.select(Query.LIBRARIAN_TABLE, 0);
    
    @FXML private TableView<Librarian> librarianTable;
    @FXML private TableColumn<Librarian, String> fNameCol, mNameCol, lNameCol, depCol, phone1Col, phone2Col, addrCol, streetCol;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!librarianList.isEmpty()){
            librarianTable.setItems(librarianList);      
        }else{   
            librarianTable.setPlaceholder(new StackPane(new Text("Hakuna mkutubi aliyesitishwa")));
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
    
    public void onLibClicked(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/morogoro_lims/view/Admin/LibrarianViewer.fxml"));
        
        try{
            loader.load();
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
        
        LibrarianViewer viewer = loader.getController();
        
        viewer.setData(librarianTable.getSelectionModel().getSelectedItem());
    }
}
