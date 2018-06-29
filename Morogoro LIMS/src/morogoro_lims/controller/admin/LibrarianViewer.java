package morogoro_lims.controller.admin;

import java.io.ByteArrayInputStream;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Department;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.query.Query;

public class LibrarianViewer implements Initializable{
    private final Query<String> depSql = new Query();
    private final Query<Librarian> query = new Query();
    @FXML public AnchorPane viewerPane;
    @FXML TextField picNameFld, regFld, fNameFld, mNameFld, lNameFld, phone1Fld, phone2Fld, addressFld, streetFld;  
    @FXML ComboBox depFld;
    @FXML private ImageView imageViewPane;
    
    private Librarian librarian;
    private final ObservableList<String> depList = depSql.select(Query.DEPARTMENT_TABLE, 1);
    private byte[] photo;
    private Image image;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        depFld.setItems(depList);
    }
    public void setData(Librarian librarian){       
        String check = "";
        this.librarian = librarian;      
        regFld.setText(this.librarian.getReg());
        String libDep = this.librarian.getDepartment();
        for(int i = 1; i < depList.size(); i++){
            check = i+"/ "+libDep;
            if(depList.contains(check)){
                libDep = check;
            }
        }
        depFld.getSelectionModel().select(check);
        fNameFld.setText(this.librarian.getFirstName());
        mNameFld.setText(this.librarian.getMiddleName());
        lNameFld.setText(this.librarian.getLastName());
        phone1Fld.setText(this.librarian.getPhone1());
        phone2Fld.setText(this.librarian.getPhone2());
        addressFld.setText(this.librarian.getPostalAddr());
        streetFld.setText(this.librarian.getStreet());       
        
        if(this.librarian.getPhoto() != null){ 
            photo = this.librarian.getPhoto();
            ByteArrayInputStream byteInput = new ByteArrayInputStream(photo);
            imageViewPane.setImage(new Image(byteInput));            
        }
    }
    public void reset(){
        regFld.setText(this.librarian.getReg());
        depFld.getSelectionModel().select(this.librarian.getDepartment());
        fNameFld.setText(this.librarian.getFirstName());
        mNameFld.setText(this.librarian.getMiddleName());
        lNameFld.setText(this.librarian.getLastName());
        phone1Fld.setText(this.librarian.getPhone1());
        phone2Fld.setText(this.librarian.getPhone2());
        addressFld.setText(this.librarian.getPostalAddr());
        streetFld.setText(this.librarian.getStreet());
    }
    @FXML
    public void onChangePhoto(){    
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chagua picha: ");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("TIFF", "*.tif", "*.tiff"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File chosenFile = chooser.showOpenDialog(null);
        if(chosenFile != null){
            if (chosenFile.length() > (1022 * 1024)) {
                Misc.display("Picha isizidi 1 MB\nIliyopo ni " + (chosenFile.length() / (1024 * 1024)) + " MB", 1);
                return;
            }
            picNameFld.setText(chosenFile.getName());
            try{
                FileInputStream inputStream = new FileInputStream(chosenFile);
                ByteArrayOutputStream byteInput = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                
                for(int readNum; (readNum = inputStream.read()) != -1;){
                    byteInput.write(buffer, 0, readNum);
                }
                photo = null;
                photo = byteInput.toByteArray();
                image = new Image(chosenFile.toURI().toString());
                imageViewPane.setImage(image);
            }catch(IOException ioe){
                Misc.display("Imeshindikana kupata picha.", 2);
                picNameFld.setText("");
                imageViewPane.setImage(null);
            }
        }else{
            Misc.display("Haujachagua picha yoyote!", 0);
        }
    }
    @FXML
    public void onUpdateInfo(){
        String reg = regFld.getText();
        String fname = fNameFld.getText();
        String mname = mNameFld.getText();
        String lname = lNameFld.getText();
        String phone1 = phone1Fld.getText();
        String phone2 = phone2Fld.getText();
        String address = addressFld.getText();
        String street = streetFld.getText();
        String dep = depFld.getSelectionModel().getSelectedItem().toString();
        
        if(fname.isEmpty()){
            Misc.display("Hakikisha jina la kwanza limejazwa", 1);
            fNameFld.requestFocus();
            return;
        }
        if(mname.isEmpty()){
            Misc.display("Hakikisha jina la kati limejazwa", 1);
            mNameFld.requestFocus();
            return;
        }
        if(lname.isEmpty()){
            Misc.display("Hakikisha jina la ukoo limejazwa", 1);
            lNameFld.requestFocus();
            return;
        }
        if(phone1.isEmpty()){
            Misc.display("Hakikisha namba ya simu imejazwa", 1);
            phone1Fld.requestFocus();
            return;
        }        
        if(address.isEmpty()){
            Misc.display("Hakikisha anuani imejazwa", 1);
            addressFld.requestFocus();
            return;
        }
        if(street.isEmpty()){
            Misc.display("Hakikisha mtaa umejazwa", 1);
            streetFld.requestFocus();
            return;
        }
        if(depFld.getSelectionModel().getSelectedItem() == null){
            Misc.display("Hakikisha kitengo kimejazwa", 1);
            depFld.requestFocus();
            return;
        }
        
        if(false == PatternMatch.text(fname)){
            Misc.display("Muundo wa jina la kwanza umekosewa. Tafadhali jaribu tena", 1);
            fNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(mname)){
            Misc.display("Muundo wa jina la kati umekosewa. Tafadhali jaribu tena", 1);
            mNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(lname)){
            Misc.display("Muundo wa jina la ukoo umekosewa. Tafadhali jaribu tena", 1);
            lNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.phone(phone1)){
            Misc.display("Muundo wa namba ya simu umekosewa. Tafadhali jaribu tena", 1);
            phone1Fld.requestFocus();
            return;
        }        
        if(false == PatternMatch.number(address)){
            Misc.display("Muundo wa anuani umekosewa. Tafadhali jaribu tena", 1);
            addressFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(street)){
            Misc.display("Muundo wa jina la mtaa umekosewa. Tafadhali jaribu tena", 1);
            streetFld.requestFocus();
            return;
        }
        Department depart = new Department(Long.parseLong(depFld.getSelectionModel().getSelectedItem().toString().split("/ ")[0]),dep.split("/ ")[1]);
        Librarian lib = new Librarian(reg, fname, mname, lname, depart, address, phone1, phone2, "", street, "Morogoro", 1, photo);
        boolean val = query.update(lib, Query.LIBRARIAN_TABLE);
        if(val) reset();
    }
    @FXML
    public void onClear(){
        reset();
    }
}
