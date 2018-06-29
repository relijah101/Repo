package morogoro_lims.controller.registration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Primary;
import morogoro_lims.model.Secondary;
import morogoro_lims.model.query.Query;

public class RegisterSecondary implements Initializable{
    private final Query<Secondary> query = new Query<>();
    @FXML private TableView<Secondary> registerSecondaryTable;
    @FXML private TableColumn<Secondary, String> idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchSecondary, regNumberFld, receiptFld, startDateFld, schoolFld, schoolAddrFld, classFld;
    @FXML private ComboBox<String> endDateFld;
    
    private ObservableList<Secondary> secList;
    private Long secId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void reset(){
        searchSecondary.setText("");
        regNumberFld.setText("");
        receiptFld.setText("");
        startDateFld.setText("");
        schoolFld.setText("");
        schoolAddrFld.setText("");
        classFld.setText("");
        registerSecondaryTable.getSelectionModel().clearSelection();
        endDateFld.getSelectionModel().select(-1);
    }
    public void initTable(){
        secList  = query.select(Query.SECONDARY_TABLE, 0);
        if (secList.isEmpty()) {
            registerSecondaryTable.setPlaceholder(new Text("Hakuna mwanachama wa kusajili."));
        } else {
            registerSecondaryTable.setItems(secList);
        }
    }
    public void initCols(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        idTypeCol.setCellValueFactory(new PropertyValueFactory<>("idType"));
        idNumberCol.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    }
    @FXML
    public void onAdultTableClicked(){
        if(registerSecondaryTable.getSelectionModel().getSelectedItem() != null){
            startDateFld.setText(Misc.today());
            endDateFld.setItems(Misc.getEndDates());
            endDateFld.getSelectionModel().select(0);
            secId = registerSecondaryTable.getSelectionModel().getSelectedItem().getId();
            String reg = "MWN/MS/00";
            reg += registerSecondaryTable.getSelectionModel().getSelectedItem().getId();
            reg += "/"+new SimpleDateFormat("y").format(new Date());
            regNumberFld.setText(reg);
            
            schoolFld.setText(registerSecondaryTable.getSelectionModel().getSelectedItem().getSchool());
            schoolAddrFld.setText(registerSecondaryTable.getSelectionModel().getSelectedItem().getSchoolAddr());
            classFld.setText(registerSecondaryTable.getSelectionModel().getSelectedItem().getGrade());
        }
    }
    @FXML
    public void onSearchAdult(){
        FilteredList<Secondary> filteredData = new FilteredList<>(secList, f->true);
        searchSecondary.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(sec->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(sec.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Secondary> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(registerSecondaryTable.comparatorProperty());
        registerSecondaryTable.setItems(sortedList);
    }
    @FXML
    public void onCancel(){
        
    }
    @FXML
    public void onSave(){
        if(secId == null){
           Misc.display("Hakikisha umechagua mwanachama wa kusajili.", 1);
            return;
        }
        if(endDateFld.getSelectionModel().getSelectedItem() == null){
            Misc.display("Hakikisha umekamilisha kujaza fomu ya usajili.", 1);
            return;
        }
        String reg = regNumberFld.getText();
        String receipt = receiptFld.getText();
        String start = startDateFld.getText();
        String end =  endDateFld.getSelectionModel().getSelectedItem();
        
        if(reg.isEmpty()){
            Misc.display("Hakikisha namba ya usajili imejazwa", 1);
            regNumberFld.requestFocus();
            return;
        }
        if(receipt.isEmpty()){
            Misc.display("Hakikisha namba ya risiti imejazwa", 1);
            receiptFld.requestFocus();
            return;
        }
        if(end.isEmpty()){
            Misc.display("Hakikisha tarehe ya mwisho imejazwa", 1);
            endDateFld.requestFocus();
            return;
        }
        Secondary secondary = new Secondary(secId, reg, receipt, start, end);
        if(query.insert(secondary, Query.SECONDARY_TABLE)){
            if(query.updateStatus(Query.MEMBER_TABLE, "1", secId)){
                String fname = registerSecondaryTable.getSelectionModel().getSelectedItem().getFirstName().toUpperCase();
                String mname = registerSecondaryTable.getSelectionModel().getSelectedItem().getMiddleName().toUpperCase();
                String lname = registerSecondaryTable.getSelectionModel().getSelectedItem().getLastName().toUpperCase();
                reset();
                initTable();
                Misc.display("Mwanachama " + fname + " " + mname + " " + lname + " ameandikishwa kikamilifu.", 0);
            }
        }        
    }
}

