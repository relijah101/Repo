package morogoro_lims.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Adult;
import morogoro_lims.model.query.Query;

public class RegisterAdult implements Initializable{
    private final Query<Adult> query = new Query();
    @FXML private TableView<Adult> registerAdultTable;
    @FXML private TableColumn<Adult, String> idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchAdult, regNumberFld, receiptFld, startDateFld, parentNameFld, parentFld;
    @FXML private ComboBox<String> endDateFld;
    private final ObservableList<Adult> adultList = query.select(Query.ADULT_TABLE, 0);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
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
        
    }
    @FXML
    public void onSearchAdult(){
//        FilteredList<> filteredData = new FilteredList<>(, l->true);
//        searchAdult.textProperty().addListener((observable, oldValue, newValue)->{
//            filteredData.setPredicate(librarian->{
//                if(newValue == null || newValue.isEmpty()) return true;
//                if(librarian.getStreet().toLowerCase().equals(newValue.toLowerCase())) return true;
//                if(librarian.getFirstName().toLowerCase().equals(newValue.toLowerCase())) return true;
//                if(librarian.getMiddleName().toLowerCase().equals(newValue.toLowerCase())) return true;
//                if(librarian.getLastName().toLowerCase().equals(newValue.toLowerCase())) return true;
//                if(librarian.getDepartment().toLowerCase().equals(newValue.toLowerCase())) return true;
//                return false;
//            });
//        });
//        SortedList<> sortedList = new SortedList<>(filteredData);
//        sortedList.comparatorProperty().bind(registerAdultTable.comparatorProperty());
//        registerAdultTable.setItems(sortedList);
    }
    @FXML
    public void onCancel(){
        
    }
    @FXML
    public void onSave(){
        
    }
}
