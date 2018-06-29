package morogoro_lims.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Department;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.query.Query;

public class RegisterLibrarian implements Initializable{
    private final Query<Librarian> query = new Query();
    private final Query<String> depSql = new Query();
    @FXML private TextField picNameFld, regNumberFld, fNameFld, mNameFld, lNameFld, addressFld, phone1Fld, phone2Fld, streetFld; 
    @FXML private PasswordField passwordFld;
    @FXML private ComboBox departmentFld;
    
    byte[] photo = null;
    ObservableList<String> depList = depSql.select(Query.DEPARTMENT_TABLE, 1);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        departmentFld.setItems(depList);
    }    
    public void reset(){
        picNameFld.setText("");
        regNumberFld.setText(""); 
        fNameFld.setText(""); 
        mNameFld.setText("");
        lNameFld.setText("");
        addressFld.setText("");
        phone1Fld.setText("");
        phone2Fld.setText("");
        streetFld.setText("");
        passwordFld.setText("");
        photo = null;
        departmentFld.getSelectionModel().select(-1);
    }
    
    @FXML
    public void onUploadPhoto(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chagua picha: ");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("TIFF", "*.tif", "*.tiff"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File chosenFile = chooser.showOpenDialog(null);
        if (chosenFile != null) {
            if (chosenFile.length() > (1022 * 1024)) {
                Misc.display("Picha isizidi 1 MB\nIliyopo ni " + (chosenFile.length() / (1024 * 1024)) + " MB", 1);
                return;
            }
            picNameFld.setText(chosenFile.getName());
            
            try{
                FileInputStream fileInput = new FileInputStream(chosenFile);
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024]; 
                
                for(int readNum; (readNum = fileInput.read())!= -1;){
                    byteOutput.write(buffer, 0, readNum);
                }
                photo = byteOutput.toByteArray();
            }catch(IOException ioe){
                Misc.display("Picha haijapatikana\n" + ioe.getLocalizedMessage(), 1);
            }
        }
    }
    @FXML
    public void onSaveLibrarian(){
        if(departmentFld.getSelectionModel().getSelectedIndex() == -1){
            Misc.display("Hakikisha fomu imejazwa", 1);
            return;
        }
        String regNumber = regNumberFld.getText();
        String fname = fNameFld.getText();
        String mname = mNameFld.getText();
        String lname = lNameFld.getText();
        String department = departmentFld.getSelectionModel().getSelectedItem().toString();
        String address = addressFld.getText();
        String phone1 = phone1Fld.getText();
        String phone2 = phone2Fld.getText();
        String street = streetFld.getText();
        String pwd = passwordFld.getText();
        if(regNumber.isEmpty()){
            Misc.display("Hakikisha namba ya mkutubi imejazwa", 1);
            regNumberFld.requestFocus();
            return;
        }
        if(fname.isEmpty()){
            Misc.display("Hakikisha jina la kwanza la mkutubi imejazwa", 1);
            fNameFld.requestFocus();
            return;
        }
        if(mname.isEmpty()){
            Misc.display("Hakikisha jina la kati la mkutubi ;imejazwa", 1);
            mNameFld.requestFocus();
            return;
        }
        if(lname.isEmpty()){
            Misc.display("Hakikisha jina la ukoo la mkutubi limejazwa", 1);
            lNameFld.requestFocus();
            return;
        }
        if(phone1.isEmpty()){
            Misc.display("Hakikisha namba ya simu  imejazwa", 1);
            phone1Fld.requestFocus();
            return;
        }
        if(street.isEmpty()){
            Misc.display("Hakikisha sehemu ya mtaa imejazwa", 1);
            streetFld.requestFocus();
            return;
        }
        if(departmentFld.getSelectionModel().getSelectedIndex() == -1){
            Misc.display("Hakikisha umechagua kitengo cha mkutubi", 1);
            departmentFld.requestFocus();
            return;
        }
        if(pwd.isEmpty()){
            Misc.display("Hakikisha neno la siri limejazwa", 1);
            passwordFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(fname)){
            Misc.display("Muundo wa jina la kwanza umekosewa. Tafadhali jaribu tena.", 1);
            fNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(mname)){
            Misc.display("Muundo wa jina la kati umekosewa. Tafadhali jaribu tena.", 1);
            mNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(lname)){
            Misc.display("Muundo wa jina la ukoo umekosewa. Tafadhali jaribu tena.", 1);
            lNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.phone(phone1)){
            Misc.display("Muundo wa namba ya simu 1 umekosewa. Tafadhali jaribu tena.", 1);
            phone1Fld.requestFocus();
            return;
        }
        if(!phone2.isEmpty()){
            if(false == PatternMatch.phone(phone2)){
                Misc.display("Muundo wa namba ya simu 2 umekosewa. Tafadhali jaribu tena.", 1);
                phone2Fld.requestFocus();
                return;
            }
        }
        if(address.isEmpty()){
            if(false == PatternMatch.number(address)){
                Misc.display("Muundo wa anuani umekosewa. Tafadhali jaribu tena.", 1);
                addressFld.requestFocus();
                return;
            }            
        }
        if(false == PatternMatch.text(street)){
            Misc.display("Muundo wa jina la mtaa umekosewa. Tafadhali jaribu tena.", 1);
            streetFld.requestFocus();
            return;
        }
        if(false == PatternMatch.pwd(pwd)){
            Misc.display("Muundo wa neno la siri umekosewa. Tafadhali jaribu tena.", 1);
            passwordFld.requestFocus();
            return;
        }
        Department depart = new Department(Long.parseLong(department.split("/ ")[0]),department.split("/ ")[1]);
        Librarian librarian = new Librarian(regNumber, fname, mname, lname, depart, address, phone1, phone2, pwd, street, "Morogoro", 1, photo);
        boolean val = query.insert(librarian, Query.LIBRARIAN_TABLE);
        if(val) reset();
    }
}
