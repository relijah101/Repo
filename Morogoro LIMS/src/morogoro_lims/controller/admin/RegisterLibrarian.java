package morogoro_lims.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegisterLibrarian implements Initializable{
    @FXML private TextField picNameFld, regNumberFld, fNameFld, mNameFld, lNameFld,phone1Fld, phone2Fld, streetFld, passwordFld; 
    @FXML private TextArea addressFld;
    @FXML private ComboBox departmentFld;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    @FXML
    public void onUploadPhoto(){
        
    }
    @FXML
    public void onSaveLibrarian(){
        
    }
}
