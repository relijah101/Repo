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
import morogoro_lims.controller.admin.AdminDash;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.query.Query;

public class EditPublisher implements Initializable{
    AdminDash dash = new AdminDash();
    @FXML private TextField nameFld;
    Publisher publisher;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void initData(Publisher pub){
        this.publisher = pub;
        nameFld.setText(pub.getPublisher());
    }
    @FXML
    public void onCancel(){
        nameFld.setText(publisher.getPublisher());
    }
    @FXML
    public void onSavePublisher(ActionEvent event){
        String publisher = nameFld.getText();
        if(publisher.isEmpty()){
            Misc.display("Hakikisha umejaza jina la mchapishaji.", 1);
            nameFld.requestFocus();
            return;
        }
        if(false == PatternMatch.text(publisher)){
            Misc.display("Muundo wa jina la mchapishaji umekosewa. Tafadhali jaribu tena.", 1);
            nameFld.requestFocus();
            return;
        }
        
        Publisher pub = new Publisher(this.publisher.getId(), publisher);
        
        Query<Publisher> query = new Query();
        boolean val = query.update(pub, Query.PUBLISHER_TABLE);
        
        if(true == val){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
        } 
    }
}
