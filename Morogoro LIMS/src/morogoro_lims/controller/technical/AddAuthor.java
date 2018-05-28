package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Author;
import morogoro_lims.model.connect.Query;

public class AddAuthor implements Initializable{
    private final Query<Author> query = new Query();
    @FXML TextField firstNameFld, middleNameFld, lastNameFld;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    private void reset(){
        firstNameFld.setText("");
        middleNameFld.setText("");
        lastNameFld.setText("");
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onSave(){
        String fname = firstNameFld.getText();
        String mname = middleNameFld.getText();
        String lname = lastNameFld.getText();
        if(fname.isEmpty()){
            Misc.display("Hakikisha umejaza jina la kwanza la mwandishi", 1);
            firstNameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(fname) || false == PatternMatch.text(mname) || false == PatternMatch.text(lname)){
            Misc.display("Hakikisha muundo wa jina umefuatwa", 1);
            firstNameFld.requestFocus();
            return;
        }
        
        Author author = new Author(fname, mname, lname);
        if(query.insert(author, Query.AUTHOR_TABLE)) reset();
    }
}
