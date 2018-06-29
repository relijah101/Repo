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
import morogoro_lims.model.Adult;
import morogoro_lims.model.Primary;
import morogoro_lims.model.query.Query;

public class RegisterPrimary implements Initializable{
    private final Query<Primary> query = new Query();
    @FXML private TableView<Primary> registerPrimaryTable;
    @FXML private TableColumn<Primary, String> idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchPrimary, regNumberFld, receiptFld, startDateFld, parentNameFld, schoolFld;
    @FXML private ComboBox<String> endDateFld;
    
    private ObservableList<Primary> primList;
    private Long primaryId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();        
    }
    public void reset(){
        searchPrimary.setText("");
        regNumberFld.setText("");
        receiptFld.setText("");
        startDateFld.setText(Misc.today());
        parentNameFld.setText(""); 
        schoolFld.setText("");
        endDateFld.getSelectionModel().select(-1);
    }
    public void initTable(){
        primList = query.select(Query.PRIMARY_TABLE, 0);
        if (primList.isEmpty())
            registerPrimaryTable.setPlaceholder(new Text("Hakuna mwanachama wa kusajili."));
        else
            registerPrimaryTable.setItems(primList);
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
        if(registerPrimaryTable.getSelectionModel().getSelectedItem() != null){
            startDateFld.setText(Misc.today());
            endDateFld.setItems(Misc.getEndDates());
            endDateFld.getSelectionModel().select(0);
            primaryId = registerPrimaryTable.getSelectionModel().getSelectedItem().getId();
            String reg = "MWN/MP/00";
            reg += registerPrimaryTable.getSelectionModel().getSelectedItem().getId();
            reg += "/"+new SimpleDateFormat("y").format(new Date());
            regNumberFld.setText(reg);
            String fname = registerPrimaryTable.getSelectionModel().getSelectedItem().getParentFirstName();
            String mname = registerPrimaryTable.getSelectionModel().getSelectedItem().getMiddleName();
            String lname = registerPrimaryTable.getSelectionModel().getSelectedItem().getLastName();
            parentNameFld.setText(fname+" "+mname+" "+lname);
            schoolFld.setText(registerPrimaryTable.getSelectionModel().getSelectedItem().getSchoolName());
        }
    }
    @FXML
    public void onSearchAdult(){
        FilteredList<Primary> filteredData = new FilteredList<>(primList, f->true);
        searchPrimary.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(pr->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(pr.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pr.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pr.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pr.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Primary> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(registerPrimaryTable.comparatorProperty());
        registerPrimaryTable.setItems(sortedList);
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onSave(){
        if(primaryId == null){
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
        
        Primary primary = new Primary(primaryId, reg, receipt, start, end);
        if(query.insert(primary, Query.PRIMARY_TABLE)){
            if(query.updateStatus(Query.MEMBER_TABLE, "1", primaryId)){
                String fname = registerPrimaryTable.getSelectionModel().getSelectedItem().getFirstName().toUpperCase();
                String mname = registerPrimaryTable.getSelectionModel().getSelectedItem().getMiddleName().toUpperCase();
                String lname = registerPrimaryTable.getSelectionModel().getSelectedItem().getLastName().toUpperCase();
                reset();
                initTable();
                Misc.display("Mwanachama " + fname + " " + mname + " " + lname + " ameandikishwa kikamilifu.", 0);
            }
        }
    }
}
