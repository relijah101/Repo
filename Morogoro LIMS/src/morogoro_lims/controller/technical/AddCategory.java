package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Category;
import morogoro_lims.model.query.Query;

public class AddCategory implements Initializable{
    private final Query<Category> query = new Query();
    @FXML TextField numberFld, categoryFld;
    @FXML TextArea descriptionFld;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberFld.setDisable(true);
    }
    private void reset(){
        numberFld.setText("");
        categoryFld.setText("");
        descriptionFld.setText("");
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onSave(){
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
 
        Category cat = new Category(categoryFld.getText(), descriptionFld.getText());
        if(query.insert(cat, Query.CATEGORY_TABLE))  reset();               
    }
}
