package morogoro_lims.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import morogoro_lims.model.Secondary;

public class SecondaryViewer implements Initializable{
    @FXML TextField regFld, fNameFld, mNameFld, lNameFld, phone1Fld, streetFld, idNumberFld, idTypeFld, picNameFld;
    @FXML TextField endFld, startFld, receiptFld, schoolFld, schoolAddressFld, classFld;
    private Secondary secondary;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void initData(Secondary secondary){
        this.secondary = secondary;
        regFld.setText(this.secondary.getRegNumber());
        fNameFld.setText(this.secondary.getFirstName());
        mNameFld.setText(this.secondary.getMiddleName());
        lNameFld.setText(this.secondary.getLastName());
        phone1Fld.setText(this.secondary.getPhone1());
        streetFld.setText(this.secondary.getStreet()); 
        idTypeFld.setText(this.secondary.getIdType());
        idNumberFld.setText(this.secondary.getIdNumber());
        endFld.setText(this.secondary.getEndDate());
        startFld.setText(this.secondary.getStartDate());
        receiptFld.setText(this.secondary.getReceipt());
        schoolFld.setText(this.secondary.getSchool());
        schoolAddressFld.setText(this.secondary.getSchoolAddr());
        classFld.setText(this.secondary.getGrade());
    }
    @FXML
    private void onUpdateInfo(){
        
    }
    @FXML
    private void onClear(){
        
    }
    @FXML
    private void onChangePhoto(){
        
    }
}
