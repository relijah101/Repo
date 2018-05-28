package morogoro_lims.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class RegisterAdult implements Initializable{
    @FXML private TableView registerAdultTable;
    @FXML private TableColumn idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchAdult, regNumberFld, receiptFld, startDateFld, parentNameFld, parentFld;
    @FXML private ComboBox<String> endDateFld;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        
    }
    public void initCols(){
        
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
