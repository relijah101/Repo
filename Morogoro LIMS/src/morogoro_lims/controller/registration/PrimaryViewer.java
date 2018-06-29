package morogoro_lims.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import morogoro_lims.model.Primary;

public class PrimaryViewer implements Initializable{
    @FXML TextField regFld, fNameFld, mNameFld, lNameFld, phone1Fld, phone2Fld, addressFld, streetFld, idNumberFld, idTypeFld, picNameFld;
    @FXML TextField endFld, startFld, receiptFld, schoolFld, schoolAddressFld, classFld;
    @FXML TextField pFNameFld, pMNameFld, pLNameFld;
    private Primary primary;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void initData(Primary primary){
        this.primary = primary;
        regFld.setText(this.primary.getRegNumber());
        fNameFld.setText(this.primary.getFirstName());
        mNameFld.setText(this.primary.getMiddleName());
        lNameFld.setText(this.primary.getLastName());
        phone1Fld.setText(this.primary.getPhone1());
        phone2Fld.setText(this.primary.getPhone2());
        addressFld.setText(this.primary.getPostal());
        streetFld.setText(this.primary.getStreet()); 
        idTypeFld.setText(this.primary.getIdType());
        idNumberFld.setText(this.primary.getIdNumber());
        //picNameFld.setText(this.adult.getMiddleName());
        endFld.setText(this.primary.getEndDate());
        startFld.setText(this.primary.getStartDate());
        receiptFld.setText(this.primary.getReceipt());
        pFNameFld.setText(this.primary.getParentFirstName());
        pMNameFld.setText(this.primary.getParentMiddleName());
        pLNameFld.setText(this.primary.getParentLastName());
        schoolFld.setText(this.primary.getSchoolName());
        schoolAddressFld.setText(this.primary.getSchoolAddr());
        classFld.setText(this.primary.getGrade());
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
