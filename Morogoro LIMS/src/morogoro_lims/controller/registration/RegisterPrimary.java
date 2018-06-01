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
import morogoro_lims.model.Primary;
import morogoro_lims.model.query.Query;

public class RegisterPrimary implements Initializable{
    private final Query<Primary> query = new Query();
    @FXML private TableView<Primary> registerPrimaryTable;
    @FXML private TableColumn<Primary, String> idCol, firstNameCol, middleNameCol, lastNameCol, addressCol, phoneCol, idTypeCol, idNumberCol;
    @FXML private TextField searchPrimary, regNumberFld, receiptFld, startDateFld, parentNameFld, parentFld;
    @FXML private ComboBox<String> endDateFld;
    
    private final ObservableList<Primary> primList = query.select(Query.PRIMARY_TABLE, 0);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
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
        
    }
    @FXML
    public void onSearchAdult(){
        
    }
    @FXML
    public void onCancel(){
        
    }
    @FXML
    public void onSave(){
        
    }
}
