package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.controller.admin.Dashboard;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.User;
import morogoro_lims.model.connect.Query;

public class AddPublisher implements Initializable{
    Dashboard dash = new Dashboard();
    @FXML private TextField nameFld;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    @FXML
    public void onCancel(){
        nameFld.setText("");
    }
    @FXML
    public void onSavePublisher(){
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
        
        Publisher pub = new Publisher(1L, publisher);
        
        Query<Publisher> query = new Query();
        boolean val = query.insert(pub, Query.PUBLISHER_TABLE);
        
        if(true == val)   nameFld.setText("");    
    }
}
