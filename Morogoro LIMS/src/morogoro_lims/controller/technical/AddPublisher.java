package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
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
        Publisher pub = new Publisher(1L, publisher);
        
        Query<Publisher> query = new Query();
        boolean val = query.insert(pub, Query.PUBLISHER_TABLE);
        if(val)   nameFld.setText("");
    }
}
