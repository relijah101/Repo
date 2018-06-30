package morogoro_lims.controller.management;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.User;
import morogoro_lims.model.query.Query;

public class ManageDash implements Initializable{
    @FXML BorderPane main_dash;
    @FXML transient CheckMenuItem defaultCheckBox, redenCheckBox, darkenCheckBox, leftPaneCheckBox;
    @FXML transient Accordion menuAccordion;
    @FXML private Label usernameLbl;
    private transient static User user;
    private transient Scene scene;
    public ManageDash(){}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void enablePane(){
        if(main_dash.isDisabled())
            main_dash.setDisable(false);
        else
            main_dash.setDisable(true);
        
    }
    public void initUser(User user){
        ManageDash.user = user;
        usernameLbl.setText(user.getUsername());
    }
    public static User getUser(){
        return user;
    }
    
    public void resetCenter(){
        main_dash.setCenter(null);
    }
    public void getPane(String location){
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource(location));
            main_dash.setCenter(pane);
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
    }
    
    @FXML
    public void onChangePassword(){
        getPane("/morogoro_lims/view/management/ChangePassword.fxml");
    }
    @FXML
    public void onLeftMenu(){
        if(main_dash.getLeft() != null){
            main_dash.setLeft(null);
        }else{
            main_dash.setLeft(menuAccordion);
        }
    }
    @FXML
    public void about(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/morogoro_lims/view/About.fxml"));
        try{
            loader.load();
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
        Stage stage = new Stage();
        stage.setResizable(false);
        Misc.setIcon(stage);
        stage.setTitle("TLSB: Morogoro Regional Library");
        stage.setScene(new Scene(loader.getRoot()));
        stage.show();
    }
    //MANAGEMENT
    @FXML
    public void onReport(){
        getPane("/morogoro_lims/view/management/Report.fxml");
    }
    //Left pane
    @FXML
    public void onLeftPane(){
        if(leftPaneCheckBox.isSelected()){
            leftPaneCheckBox.setSelected(false);
        }else{
            leftPaneCheckBox.setSelected(true);
        }
    }
    
    //Logout and Exit
    @FXML
    public void onLogout(MouseEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/morogoro_lims/view/Login.fxml"));
        try{
            loader.load();
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.getRoot()));
        
        Query.log(0, user.getRegNumber(), user.getUsername(), "Ametoka.");
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);            
        
    }
    @FXML
    public void onExit(){
        Platform.exit();
    }
}
