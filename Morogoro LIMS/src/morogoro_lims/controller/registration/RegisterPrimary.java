package morogoro_lims.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class RegisterPrimary implements Initializable{
    @FXML private TableView registerPrimaryTable;
    @FXML private TableColumn idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchPrimary, regNumberFld, receiptFld, startDateFld, parentNameFld, parentFld;
    @FXML private ComboBox<String> endDateFld;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        
    }
    public void initCols(){
        
    }
    @FXML
    public void onAdultTableClicked(){
        
    }
    @FXML
    public void onSearchAdult(){
        
    }
    @FXML
    public void onCancel(){
        
    }
    @FXML
    public void onSave(){
        
    }
}
