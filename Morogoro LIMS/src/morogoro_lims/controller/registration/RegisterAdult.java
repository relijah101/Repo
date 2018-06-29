package morogoro_lims.controller.registration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Adult;
import morogoro_lims.model.query.Query;

public class RegisterAdult implements Initializable{
    private final Query<Adult> query = new Query();
    @FXML private TableView<Adult> registerAdultTable;
    @FXML private TableColumn<Adult, String> idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchAdult, regNumberFld, receiptFld, startDateFld, officeFld, sponsorFld, sponsorTitleFld, referenceFld;
    @FXML private ComboBox<String> endDateFld;
    private ObservableList<Adult> adultList = FXCollections.observableArrayList();
    private Long adultId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        initTable();
        initCols();
    }
    public void reset(){
        searchAdult.setText("");
        regNumberFld.setText("");
        receiptFld.setText("");
        startDateFld.setText("");
        officeFld.setText("");
        sponsorFld.setText("");
        sponsorTitleFld.setText("");
        referenceFld.setText("");
        endDateFld.getSelectionModel().select(-1);
        registerAdultTable.getSelectionModel().clearSelection();
    }
    public void initTable(){
        adultList = query.select(Query.ADULT_TABLE, 0);
        if(adultList.isEmpty())
            registerAdultTable.setPlaceholder(new Text("Hakuna mwanachama wa kusajili."));
        else
            registerAdultTable.setItems(adultList);
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
        if(registerAdultTable.getSelectionModel().getSelectedItem() != null){
            startDateFld.setText(Misc.today());
            endDateFld.setItems(Misc.getEndDates());
            endDateFld.getSelectionModel().select(0);
            adultId = registerAdultTable.getSelectionModel().getSelectedItem().getId();
            regNumberFld.setText("MWN/MM/00" + adultId + "/" + new SimpleDateFormat("y").format(new Date()));
        }
    }
    @FXML
    public void onSearchAdult(){
        FilteredList<Adult> filteredData = new FilteredList<>(adultList, f->true);
        searchAdult.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(ad->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(ad.getStreet().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(ad.getFirstName().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(ad.getMiddleName().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(ad.getLastName().toLowerCase().equals(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Adult> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(registerAdultTable.comparatorProperty());
        registerAdultTable.setItems(sortedList);
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onSave(){
        if(endDateFld.getSelectionModel().getSelectedItem() == null){
            Misc.display("Hakikisha umekamilisha kujaza fomu ya usajili.", 2);
            endDateFld.requestFocus();
            return;
        }
        String reg = regNumberFld.getText();
        String receipt = receiptFld.getText();
        String start = startDateFld.getText();
        String end = endDateFld.getSelectionModel().getSelectedItem();
        String office = officeFld.getText();
        String sponsor = sponsorFld.getText();
        String title = sponsorTitleFld.getText();
        String reference = referenceFld.getText();
        
        if(receipt.isEmpty()){
            Misc.display("Hakikisha umejaza namba ya risiti.", 2);
            receiptFld.requestFocus();
            return;
        }
        if(office.isEmpty()){
            Misc.display("Hakikisha umejaza jina la ofisi.", 2);
            officeFld.requestFocus();
            return;
        }
        if(sponsor.isEmpty()){
            Misc.display("Hakikisha umejaza jina la mdhamini.", 2);
            sponsorFld.requestFocus();
            return;
        }
        if(title.isEmpty()){
            Misc.display("Hakikisha umejaza cheo cha mdhamini.", 2);
            sponsorTitleFld.requestFocus();
            return;
        }
        if(reference.isEmpty()){
            Misc.display("Hakikisha umejaza namba ya kumbukumbu.", 2);
            referenceFld.requestFocus();
            return;
        }
        if(receipt.isEmpty()){
            Misc.display("Hakikisha umejaza namba ya risiti.", 2);
            receiptFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(office)){
            Misc.display("Hakikisha jina la ofisi.", 2);
            officeFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(sponsor)){
            Misc.display("Muundo wa jina la mdhamini si sahihi.", 2);
            sponsorFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(title)){
            Misc.display("Muundo wa cheo cha mdhamini si sahihi.", 2);
            sponsorTitleFld.requestFocus();
            return;
        }
        
        if(adultId == null){
            Misc.display("Hakikisha umechagua mwanachama na kukamilisha fomu ya usajili.", 2);
            return;
        }
        Adult adult = new Adult(adultId, reg, receipt, start, end, office, sponsor, title, reference);
        if(query.insert(adult, Query.ADULT_TABLE)){
            if(query.updateStatus(Query.MEMBER_TABLE, "1", adultId)){
                String fname = registerAdultTable.getSelectionModel().getSelectedItem().getFirstName().toUpperCase();
                String mname = registerAdultTable.getSelectionModel().getSelectedItem().getMiddleName().toUpperCase();
                String lname = registerAdultTable.getSelectionModel().getSelectedItem().getLastName().toUpperCase();
                Misc.display("Mwanachama " + fname + " " + mname + " " + lname + " amesajiliwa kikamilifu.", 0);
                reset();
                initTable();
            } 
        }
    }
}
