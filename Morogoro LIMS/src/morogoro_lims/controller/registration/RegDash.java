package morogoro_lims.controller.registration;

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
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.User;
import morogoro_lims.model.query.Query;

public class RegDash implements Initializable{
    @FXML BorderPane main_dash;
    @FXML transient CheckMenuItem defaultCheckBox, redenCheckBox, darkenCheckBox, leftPaneCheckBox;
    @FXML MenuItem viewRegisteredAdultMenuItem, viewRegisteredPrimaryMenuItem, viewRegisteredSecondaryMenuItem;
    @FXML transient Accordion menuAccordion;
    @FXML private Label usernameLbl;
    private transient static User user;
    private transient Scene scene;
    public RegDash(){}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewRegisteredAdultMenuItem.setOnAction(e ->{
            getPane("/morogoro_lims/view/registration/ViewRegisteredAdult.fxml");
        }); 
        viewRegisteredPrimaryMenuItem.setOnAction(e ->{
            getPane("/morogoro_lims/view/registration/ViewRegisteredPrimary.fxml");
        }); 
        viewRegisteredSecondaryMenuItem.setOnAction(e ->{
            getPane("/morogoro_lims/view/registration/ViewRegisteredSecondary.fxml");
        }); 
    }
    
    public void enablePane(){
        if(main_dash.isDisabled())
            main_dash.setDisable(false);
        else
            main_dash.setDisable(true);
        
    }
    public void initUser(User user){
        RegDash.user = user;
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
    
    //MAINMENU
    @FXML
    public void onLeftMenu(){
        if(main_dash.getLeft() != null){
            main_dash.setLeft(null);
        }else{
            main_dash.setLeft(menuAccordion);
        }
    }
    @FXML
    public void onChangePassword(){
        getPane("/morogoro_lims/view/registration/ChangePassword.fxml");
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
        Misc.setIcon(stage);
        stage.setResizable(false);
        stage.setTitle("TLSB: Morogoro Regional Library");
        stage.setScene(new Scene(loader.getRoot()));
        stage.show();
    }
    //Registration
    @FXML
    public void onRegisterAdult(){
        getPane("/morogoro_lims/view/registration/RegisterAdult.fxml");
    }
    @FXML
    public void onRegisterPrimary(){
        getPane("/morogoro_lims/view/registration/RegisterPrimary.fxml");
    }
    @FXML
    public void onRegisterSecondary(){
        getPane("/morogoro_lims/view/registration/RegisterSecondary.fxml");
    }
    @FXML
    public void onViewRegisteredAdult(){
        getPane("/morogoro_lims/view/registration/ViewRegisteredAdult.fxml");
    }
    @FXML
    public void onViewRegisteredPrimary(){
        getPane("/morogoro_lims/view/registration/ViewRegisteredPrimary.fxml");
    }
    @FXML
    public void onViewRegisteredSecondary(){
        getPane("/morogoro_lims/view/registration/ViewRegisteredSecondary.fxml");
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
