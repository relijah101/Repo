package morogoro_lims.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import morogoro_lims.model.Logs;
import morogoro_lims.model.connect.Query;

public class LogsTable implements Initializable{
    private final Query<Logs> query = new Query();
    @FXML private TableView<Logs> logsTable;
    @FXML private TableColumn<Logs, String> regNumberCol, fullNameCol, dateCol, actionCol, infoCol;
    @FXML private TextField searchRecord;
    private Long logsId;
    private ObservableList<Logs> logList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        logsTable.getItems().clear();
        logList = query.select(Query.LOGS_TABLE, 1);
        if(!logList.isEmpty())
            logsTable.setItems(logList);
        else
            logsTable.setPlaceholder(new Text("Hakuna shughuli iliyorekodiwa."));
        
    }
    public void initCols(){
        regNumberCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
    }
    
    @FXML
    public void onLogsTableClick(){
        logsId = logsTable.getSelectionModel().getSelectedItem().getId();
    }
    @FXML
    public void onSeeLogInfo(){        
        Logs logsData = (Logs) new Query().getSelectedItem(Query.LOGS_TABLE, logsId);
        Text logId = new Text("Id : "+ logsData.getId()+"");
        Text regNumber = new Text("MK # : "+ logsData.getRegNumber());
        Text name = new Text("Jina : "+logsData.getName());
        Text date = new Text("Tarehe : "+logsData.getDate());
        Text action = new Text("Kitendo : "+logsData.getAction());
        Text info = new Text("Taarifa : "+logsData.getInfo());
        Button okButton = new Button("OK");
        
        VBox logBox = new VBox(5);
        logBox.getChildren().addAll(logId, regNumber, name, date, action, info, okButton);
        
        Dialog logDialog = new Dialog();
        logDialog.setTitle("Rekodi.");         
        logDialog.setGraphic(logBox);
        logDialog.show();
        okButton.setOnAction(e->{logDialog.close();});
    }
    @FXML
    public void onDeleteLogInfo(){
        boolean val = new Query().delete(Query.LOGS_TABLE, logsId);
        if(val) initTable();
    }
    @FXML
    public void onSearchRecord(){
        FilteredList<Logs> filteredList = new FilteredList<>(logList, e->true);
        searchRecord.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(log -> {
                if(newValue == null || newValue.isEmpty()) return true;
                
                if(log.getRegNumber().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(log.getName().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(log.getDate().toLowerCase().equals(newValue.toLowerCase())) return true;
                if(log.getInfo().toLowerCase().equals(newValue.toLowerCase())) return true;
                
                return false;
            });
        });
        SortedList<Logs> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(logsTable.comparatorProperty());
        logsTable.setItems(sortedList);
    }
}
