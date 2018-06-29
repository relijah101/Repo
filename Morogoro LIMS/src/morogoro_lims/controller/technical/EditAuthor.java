package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Author;
import morogoro_lims.model.query.Query;

public class EditAuthor implements Initializable{
    private final Query<Author> query = new Query();
    private Author author;
    @FXML TextField firstNameFld, middleNameFld, lastNameFld;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    private void reset(){
        firstNameFld.setText(this.author.getFirstName());
        middleNameFld.setText(this.author.getMiddleName());
        lastNameFld.setText(this.author.getLastName());
    }
    public void initData(Author author){
        this.author = author;
        firstNameFld.setText(this.author.getFirstName());
        middleNameFld.setText(this.author.getMiddleName());
        lastNameFld.setText(this.author.getLastName());
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onSave(ActionEvent event){
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
        
        Author selectedAuthor = new Author(this.author.getId(), fname, mname, lname);
        if(query.update(selectedAuthor, Query.AUTHOR_TABLE)){
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
