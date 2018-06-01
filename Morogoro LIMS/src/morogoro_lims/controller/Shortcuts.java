package morogoro_lims.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Shortcut;
import morogoro_lims.model.query.Query;

public class Shortcuts implements Initializable{
    private final Query<Shortcut> query = new Query<>();
    @FXML private TableView<Shortcut> shortcutTable;
    @FXML private TableColumn<Shortcut, String> actionCol, keysCol;
    
    private final ObservableList<Shortcut> keyList = query.select(Query.SHORTCUT_TABLE, 0);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        if (keyList.isEmpty()) {
            shortcutTable.setPlaceholder(new Text("Hakuna mikato."));
        } else {
            shortcutTable.setItems(keyList);
        }
    }
    public void initCols(){
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        keysCol.setCellValueFactory(new PropertyValueFactory<>("key"));
    }
}
