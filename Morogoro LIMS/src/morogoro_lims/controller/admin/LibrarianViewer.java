package morogoro_lims.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import morogoro_lims.model.Librarian;

public class LibrarianViewer implements Initializable{
    @FXML public AnchorPane viewerPane;
    @FXML TextField regFld, departmentFld, fNameFld, mNameFld, lNameFld, phone1Fld, phone2Fld, addressFld, streetFld;  
    
    Librarian librarian;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void setData(Librarian librarian){
        this.librarian = librarian;
        
        regFld.setText(librarian.getReg());
        departmentFld.setText(this.librarian.getDepartment());
        fNameFld.setText(this.librarian.getFirstName());
        mNameFld.setText(this.librarian.getMiddleName());
        lNameFld.setText(this.librarian.getLastName());
        phone1Fld.setText(this.librarian.getPhone1());
        phone2Fld.setText(this.librarian.getPhone2());
        addressFld.setText(this.librarian.getPostalAddr());
        //streetFld.setText(this.librarian.getStreet());
        streetFld.setText("hello");
        System.out.println(this.librarian.getFirstName());
    }
    
    @FXML
    public void onUpdate(){
        
    }
    @FXML
    public void onDelete(){
        
    }
}
