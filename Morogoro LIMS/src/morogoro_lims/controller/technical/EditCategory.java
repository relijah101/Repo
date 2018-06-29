package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Category;
import morogoro_lims.model.query.Query;

public class EditCategory implements Initializable{
    private final Query<Category> query = new Query();
    @FXML TextField numberFld, categoryFld;
    @FXML TextArea descriptionFld;
    private Category cat;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberFld.setDisable(true);
    }
    private void reset(){
        numberFld.setText(cat.getId()+"");
        categoryFld.setText(cat.getCategory());
        descriptionFld.setText(cat.getDescription());
    }
    public void initData(Category category){
        this.cat = category;
        numberFld.setText(cat.getId()+"");
        categoryFld.setText(cat.getCategory());
        descriptionFld.setText(cat.getDescription());
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onSave(ActionEvent event){
        if(categoryFld.getText().isEmpty()){
            Misc.display("Hakikisha umejaza jina la kategori.", 1);
            categoryFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(categoryFld.getText())){
            Misc.display("Muundo wa jina la kategori umekosewa. Tafadhali jaribu tena", 1);
            categoryFld.requestFocus();
            return;
        }     
 
        Category category = new Category(cat.getId(), categoryFld.getText(), descriptionFld.getText());
        if(query.update(category, Query.CATEGORY_TABLE)){
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.hide();
        }
    }
}
