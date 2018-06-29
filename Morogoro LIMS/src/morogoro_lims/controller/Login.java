package morogoro_lims.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import morogoro_lims.controller.admin.AdminDash;
import morogoro_lims.controller.lending.LendDash;
import morogoro_lims.controller.management.ManageDash;
import morogoro_lims.controller.registration.RegDash;
import morogoro_lims.controller.technical.TechDash;
import morogoro_lims.model.Info;
import morogoro_lims.model.User;
import morogoro_lims.model.query.Query;

public class Login implements Initializable{
    private final Query<Info> query = new Query();
    @FXML private TextField usernameFld;
    @FXML private PasswordField passwordFld;
    @FXML private Text loginStatusText, titleText, addressText, cityText, phoneText, emailText;   
    
    private Stage loginStage;
    private int count = 0;
    private ObservableList<Info> infoData = query.select(Query.INFO_TABLE, 1);
    private static User user;
    private FXMLLoader loader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleText.setText(infoData.get(0).getName().toUpperCase());
        addressText.setText(infoData.get(0).getAddress().toUpperCase());
        cityText.setText(infoData.get(0).getCity().toUpperCase());
        phoneText.setText(infoData.get(0).getPhone1()+"; "+infoData.get(0).getPhone2());
        emailText.setText(infoData.get(0).getEmail());
    }
    
    @FXML
    private void onLoggingIn(MouseEvent event){        
        String name = usernameFld.getText();
        String pass = passwordFld.getText();
        
        user = query.login(name, pass);
        if(user == null){
            count++;            
            if(count == 3){
                loginStatusText.setText(" Tafadhali jaribu tena baadae.");
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException ie){
                    Misc.display(ie.getLocalizedMessage(), 2);
                }
                Platform.exit();
            }
            loginStatusText.setText("Umekosea jina au neno la siri. Tafadhali jaribu tena.");
            usernameFld.setText("");
            passwordFld.setText("");
            usernameFld.requestFocus();
        }else{
            usernameFld.setText("");
            passwordFld.setText("");
            switch(user.getRole()){
                case "Management":
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/morogoro_lims/view/management/Dashboard.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ioe){
                        Misc.display(ioe.getLocalizedMessage(), 2);
                    }

                    ManageDash manageBoard = loader.getController();
                    manageBoard.initUser(user);
                    open(event);
                break;
                case "Administration":
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/morogoro_lims/view/Admin/Dashboard.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ioe){
                        Misc.display(ioe.getLocalizedMessage(), 2);
                    }

                    AdminDash adminBoard = loader.getController();
                    adminBoard.initUser(user);
                    open(event);
                break;
                case "Lending":
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/morogoro_lims/view/lending/Dashboard.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ioe){
                        Misc.display(ioe.getLocalizedMessage(), 2);
                    }

                    LendDash lendBoard = loader.getController();
                    lendBoard.initUser(user);
                    open(event);
                break;
                case "Technical":
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/morogoro_lims/view/Technical/Dashboard.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ioe){
                        Misc.display(ioe.getLocalizedMessage(), 2);
                    }

                    TechDash techBoard = loader.getController();
                    techBoard.initUser(user);
                    open(event);
                break;
                case "Registration":
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/morogoro_lims/view/registration/Dashboard.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ioe){
                        Misc.display(ioe.getLocalizedMessage(), 2);
                    }

                    RegDash regBoard = loader.getController();
                    regBoard.initUser(user);
                    open(event);
                break;
            }         
            
            
        }
    }
    public void open(MouseEvent event){       
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Mfumo wa Kumeneji Taarifa za Maktaba Ya Mkoa wa Morogoro.");
        stage.setScene(new Scene(parent));
        stage.show();

        loginStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        hideStage();
    }
    public void showStage(){
        loginStage.show();
    }
    public void hideStage(){
        loginStage.hide();
    }
    public static User getUser(){
        return user;
    }
}
