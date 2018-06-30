package morogoro_lims.controller.technical;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Category;
import morogoro_lims.model.query.Query;

public class CategoryTable implements Initializable{
    private Long catId;
    private final Query<Category> query = new Query();
    private ObservableList<Category>  catList = FXCollections.observableArrayList();
    @FXML TextField searchCategory;
    @FXML TableView<Category> categoryTable;
    @FXML TableColumn<Category, String> numberCol, categoryCol, descriptionCol;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCols();
    }
    
    public void initTable(){
        catList.clear();
        catList = query.select(Query.CATEGORY_TABLE, 1);
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
        FilteredList<Category> filteredList = new FilteredList<>(catList, c -> true);
        searchCategory.textProperty().addListener((observableValue, oldValue, newValue)->{
            filteredList.setPredicate(cat->{
                if(newValue.isEmpty())return true;
                
                if(cat.getCategory().contains(newValue.toLowerCase())) return true;
                
                return false;
            });
        });
        SortedList<Category> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        categoryTable.setItems(sortedList);
    }
    @FXML
    public void onUpdateCategory(ActionEvent event){
        if(categoryTable.getSelectionModel().getSelectedItem() != null){
            Category selectedCat = categoryTable.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();
            stage.setTitle("Badili taarifa za kategori: ("+ selectedCat.getCategory() +")");
            Misc.setIcon(stage);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/morogoro_lims/view/Technical/EditCategory.fxml"));
            try{
                loader.load();
            }catch(IOException ioe){
                Misc.display(ioe.getLocalizedMessage(), 2);
            }

            EditCategory editCat = loader.getController();
            editCat.initData(selectedCat);
            
            stage.setScene(new Scene(loader.getRoot()));
            stage.showAndWait();
            initTable();
        }
    }
    @FXML
    public void onRemoveCategory(){
        if(categoryTable.getSelectionModel().getSelectedItem() != null){
            catId = categoryTable.getSelectionModel().getSelectedItem().getId();
            
            if(query.delete(Query.CATEGORY_TABLE, catId)){
                initTable();
            }
        }
    }
}
