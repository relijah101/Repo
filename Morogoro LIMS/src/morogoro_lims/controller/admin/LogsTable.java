package morogoro_lims.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Logs;
import morogoro_lims.model.connect.Query;

public class LogsTable implements Initializable{
    private final Query<Logs> query = new Query();
    @FXML private TableView<Logs> logsTable;
    @FXML private TableColumn<Logs, String> regNumberCol, fullNameCol, dateCol, actionCol, infoCol;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        logsTable.getItems().clear();
        ObservableList<Logs> logList = query.select(Query.LOGS_TABLE, 1);
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
}
