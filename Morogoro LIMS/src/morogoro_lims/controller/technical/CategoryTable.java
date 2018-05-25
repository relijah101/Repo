package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Category;
import morogoro_lims.model.connect.Query;

public class CategoryTable implements Initializable{
    private final Query<Category> query = new Query();
    @FXML TextField searchCategory;
    @FXML TableView<Category> categoryTable;
    @FXML TableColumn<Category, String> numberCol, categoryCol, descriptionCol;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    
    public void initTable(){
        categoryTable.getItems().clear();
        ObservableList<Category> catList = query.select(Query.CATEGORY_TABLE, 1);
        if(!catList.isEmpty())
            categoryTable.setItems(catList);
        else
            categoryTable.setPlaceholder(new Text("Hakuna kategori aliyorekodiwa."));
    }
    public void initCols(){
        numberCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category")); 
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description")); 
    }
    @FXML
    public void onSearchCategory(){
        
    }
}
