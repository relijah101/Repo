package morogoro_lims.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Department;
import morogoro_lims.model.Info;
import morogoro_lims.model.query.Query;

public class Settings implements Initializable{
    @FXML TextField depFld, nameFld, addressFld, regionFld, phoneFld1, phoneFld2, emailFld;
    @FXML ListView<Department> departmentListView;
    @FXML Text nameText, addressText, regionText, phone1Text, phone2Text, emailText;
    
    private final Query<Department> depSql = new Query();
    private final Query<Info> infoSql = new Query();
    private ObservableList<Department> depList;
    private ObservableList<Info> infoList;
    private Long depId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDep();
        initSettings();
    }
    public void initDep(){
        if(!(depList = depSql.select(Query.DEPARTMENT_TABLE, 1)).isEmpty()){
            departmentListView.setItems(depList);
        } 
    }
    public void initSettings(){
        if(!(infoList = infoSql.select(Query.INFO_TABLE, 1)).isEmpty()){
            nameText.setText(infoList.get(0).getName());
            addressText.setText(infoList.get(0).getAddress());
            regionText.setText(infoList.get(0).getCity());
            phone1Text.setText(infoList.get(0).getPhone1());
            phone2Text.setText(infoList.get(0).getPhone2());
            emailText.setText(infoList.get(0).getEmail());
        } 
    }
    public void resetDep(){
        depFld.setText("");
    }
    public void resetSettings(){
        nameFld.setText("");
        addressFld.setText("");
        regionFld.setText("");
        phoneFld1.setText("");
        phoneFld1.setText("");
        emailFld.setText("");
    }
    @FXML
    public void onClearDep(){
        resetDep();
    }
    @FXML
    public void onSaveDep(){
        String dep = depFld.getText();
        if(dep.isEmpty()){
            Misc.display("Hakikisha jina la kitengo limejazwa.", 1);
            depFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(dep)){
            Misc.display("Muundo wa jina la kitengo si sahihi.", 1);
            depFld.requestFocus();
            return;
        }
        Department department = new Department(dep);
        if(depSql.insert(department, Query.DEPARTMENT_TABLE)){
            resetDep();
            initDep();
        }
        
    }
    @FXML
    public void onClearSettings(){
        resetSettings();
    }
    @FXML
    public void onSaveSettings(){
        String companyName = nameFld.getText();
        String address = addressFld.getText();
        String region = regionFld.getText();
        String phone1 = phoneFld1.getText();
        String phone2 = phoneFld2.getText();
        String email = emailFld.getText();
        
        if(companyName.isEmpty()){
            Misc.display("Hakikisha umejaza jina la taasisi.", 0);
            nameFld.requestFocus();
            return;
        }
        if(address.isEmpty()){
            Misc.display("Hakikisha anuani imejazwa.", 0);
            addressFld.requestFocus();
            return;
        }
        if(region.isEmpty()){
            Misc.display("Hakikisha mkoa umejazwa", 0);
            regionFld.requestFocus();
            return;
        }
        if(phone1.isEmpty() || phone2.isEmpty()){
            Misc.display("Hakikisha namba ya simu angalau moja imejazwa", 0);
            phoneFld1.requestFocus();
            return;
        }
        if(email.isEmpty()){
            Misc.display("Hakikisha barua pepe imejazwa", 0);
            emailFld.requestFocus();
            return;
        }        
        if(false == PatternMatch.text(companyName)){
            Misc.display("Muundo wa jina la taasisi si sahihi.", 0);
            nameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.number(address)){
            Misc.display("Muundo wa anuani ya taasisi si sahihi.", 0);
            addressFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(region)){
            Misc.display("Muundo wa jina la mkoa si sahihi", 0);
            regionFld.requestFocus();
            return;
        }
        if((false == PatternMatch.phone(phone1)) || (false == PatternMatch.phone(phone2))){
            Misc.display("Muundo wa namba ya simu si sahihi", 0);
            phoneFld1.requestFocus();
            return;
        }
        if(false == PatternMatch.email(email)){
            Misc.display("Muundo wa barua pepe si sahihi", 0);
            emailFld.requestFocus();
            return;
        }
        
        Info info = new Info(companyName, phone1, phone2, region, address, email);
        if(infoSql.insert(info, Query.INFO_TABLE)){
            resetSettings();
            initSettings();
        }
    }
    @FXML
    public void onListViewClicked(){
        if(departmentListView.getSelectionModel().getSelectedItem() != null){
            depId = departmentListView.getSelectionModel().getSelectedItem().getId();
        }
    }
    @FXML
    public void onEditDep(){
        if(depId != null){
            
        }
    }
    @FXML
    public void onDeleteDep(){
        if(depId != null){
            if(depSql.delete(Query.DEPARTMENT_TABLE, depId)){
                resetDep();
                initDep();
            }
        }
    }
}
