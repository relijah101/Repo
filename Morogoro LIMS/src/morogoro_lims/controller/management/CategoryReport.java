package morogoro_lims.controller.management;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.query.Query;

public class CategoryReport implements Initializable{
    @FXML TableView<Category> categoryTable;
    @FXML TableColumn<Category, String> categoryCol;
    @FXML TableColumn<Category, Integer> totalCol;
    @FXML TextField searchCategoryFld;
    @FXML Text categoryTotalText;
    
    private final Query<Category> query = new Query();
    private ObservableList<Category> catList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    public void initTable(){
        catList.clear();
        catList = query.getCountReport(Query.CATEGORY_TABLE);
        if(catList.isEmpty()){
            categoryTable.setPlaceholder(new Text("Hakuna rekodi"));
        }else{
            categoryTable.setItems(catList);
        }
    }
    public void initCols(){
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalBooks"));
    }
    @FXML
    public void onPrintReport(){
        Misc.printPageSetup(categoryTable);
    }
    
    @FXML
    public void onSearching(){
        FilteredList<Category> filteredList = new FilteredList<>(catList, p->true);
        searchCategoryFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(cat->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(cat.getCategory().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Category> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        categoryTable.setItems(sortedList);
    }
}
