package morogoro_lims.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import morogoro_lims.model.Adult;

public class AdultViewer implements Initializable{
    @FXML TextField regFld, fNameFld, mNameFld, lNameFld, phone1Fld, phone2Fld, addressFld, streetFld, idNumberFld, idTypeFld, picNameFld;
    @FXML TextField endFld, startFld, receiptFld, sponsorFld, referenceFld, titleFld, officeFld;
    @FXML ImageView imageViewPane;
    private Adult adult;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void initData(Adult adult){
        this.adult = adult;
        regFld.setText(this.adult.getRegNumber());
        fNameFld.setText(this.adult.getFirstName());
        mNameFld.setText(this.adult.getMiddleName());
        lNameFld.setText(this.adult.getLastName());
        phone1Fld.setText(this.adult.getPhone1());
        phone2Fld.setText(this.adult.getPhone2());
        addressFld.setText(this.adult.getPostal());
        streetFld.setText(this.adult.getStreet()); 
        idTypeFld.setText(this.adult.getIdType());
        idNumberFld.setText(this.adult.getIdNumber());
        officeFld.setText(this.adult.getOffice());
        endFld.setText(this.adult.getEndDate());
        startFld.setText(this.adult.getStartDate());
        receiptFld.setText(this.adult.getReceipt());
        sponsorFld.setText(this.adult.getSponsor());
        referenceFld.setText(this.adult.getReference());
        titleFld.setText(this.adult.getTitle());
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
