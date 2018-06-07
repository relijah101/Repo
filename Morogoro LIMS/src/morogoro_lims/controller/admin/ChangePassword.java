/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morogoro_lims.controller.admin;

import morogoro_lims.controller.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import morogoro_lims.model.User;
import morogoro_lims.model.query.Query;

/**
 *
 * @author redenta benedict
 */
public class ChangePassword implements Initializable{
    @FXML TextField currentPwdFld, newPwdFld, rNewPwdFld;
    @FXML Button saveBtn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void reset(){
        currentPwdFld.setText("");
        newPwdFld.setText("");
        rNewPwdFld.setText("");
    }
    @FXML
    public void onClear(){
        reset();
    }
    @FXML
    public void onSave(){
        String cPwd = currentPwdFld.getText();
        String nPwd = newPwdFld.getText();
        String rPwd = rNewPwdFld.getText();
        
        if(cPwd.isEmpty()){
            Misc.display("Hakikisha neno siri la zamani limejazwa.", 1);
            currentPwdFld.requestFocus();
            return;
        }
        if(nPwd.isEmpty()){
            Misc.display("Hakikisha neno siri limejazwa.", 1);
            newPwdFld.requestFocus();
            return;
        }
        if(rPwd.isEmpty()){
            Misc.display("Hakikisha umerudia kuandika neno siri jipya.", 1);
            rNewPwdFld.requestFocus();
            return;
        }
        if(false == PatternMatch.pwd(nPwd)){
            Misc.display("Hakikisha neno siri halijapungua herufi.", 1);
            newPwdFld.requestFocus();
            return;
        }
        if(!(rPwd.equals(nPwd))){
            Misc.display("Neno siri jipya halifanani.", 1);
            rNewPwdFld.requestFocus();
            return;
        }
        
        //Compare old pwd 
        User user = AdminDash.getUser();
        Query q = new Query();
        if(!(cPwd.equals(q.getPwd(user.getRegNumber())))){
            Misc.display("Neno siri la sasa sio sahihi.", 1);
            currentPwdFld.requestFocus();
            return;
        }
        if(q.updatePwd(user.getRegNumber(), nPwd)){
            reset();
        }
    }
}
