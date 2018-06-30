package morogoro_lims.controller.admin;

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

public class AdminDash implements Initializable{
    @FXML BorderPane main_dash;
    @FXML transient CheckMenuItem defaultCheckBox, redenCheckBox, darkenCheckBox, leftPaneCheckBox;
    @FXML MenuItem viewRegisteredAdultMenuItem, viewRegisteredPrimaryMenuItem, viewRegisteredSecondaryMenuItem;
    @FXML transient Accordion menuAccordion;
    @FXML private Label usernameLbl;
   
    private transient static User user;
    public AdminDash(){}
    @Override
    //Initial method
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
    //Check if the center is enabled the disable and vice versa
    public void enablePane(){
        if(main_dash.isDisabled())
            main_dash.setDisable(false);
        else
            main_dash.setDisable(true);
        
    }
    //Initialize the user information during login
    public void initUser(User user){
        AdminDash.user = user;
        usernameLbl.setText(user.getUsername());
    }
    
    //Get information of the user that has logged in
    public static User getUser(){
        return user;
    }
    //Initialize the right pane of the main border pane
    public void initRight(String location){        
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource(location));
            main_dash.setRight(pane);
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
    }
    public void resetRight(){
        if(main_dash.getRight() != null)
            main_dash.setRight(null);
    }
    public void resetCenter(){
        main_dash.setCenter(null);
    }
    //Get center of the borderpane and set a certain pane
    public void getPane(String location){
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource(location));
            main_dash.setCenter(pane);
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
    }
    
    //Change your password
    @FXML
    public void onChangePassword(){
        getPane("/morogoro_lims/view/Admin/ChangePassword.fxml");
    }
    @FXML
    public void onLeftMenu(){
        if(main_dash.getLeft() != null){
            main_dash.setLeft(null);
        }else{
            main_dash.setLeft(menuAccordion);
        }
    }
    //Management Module
    @FXML
    public void onReport(){
        resetCenter();
        getPane("/morogoro_lims/view/management/Report.fxml");
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
    //Registration Module
    @FXML
    public void onRegisterAdult(){
        resetRight();
        getPane("/morogoro_lims/view/registration/RegisterAdult.fxml");
    }
    public void onRegisterPrimary(){
        resetRight();
        getPane("/morogoro_lims/view/registration/RegisterPrimary.fxml");
    }
    public void onRegisterSecondary(){
        resetRight();
        getPane("/morogoro_lims/view/registration/RegisterSecondary.fxml");
    }
    //Technical Module
    @FXML
    public void onRecordBook(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AddBook.fxml");
    }
    @FXML
    public void onRecordCategory(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AddCategory.fxml");
    }
    @FXML
    public void onRecordAuthor(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AddAuthor.fxml");
    }
    @FXML
    public void onRecordPublisher(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AddPublisher.fxml");
    }
    @FXML
    public void onAddAthorToBook(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AddAuthorToBook.fxml");
    }
    @FXML
    public void onAvailableBooks(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/BookTable.fxml");
    }
    @FXML
    public void onAvailableCategory(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/CategoryTable.fxml");
    }
    @FXML
    public void onAvailableAuthor(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AuthorTable.fxml");
    }
    @FXML
    public void onAvailablePublisher(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/PublisherTable.fxml");
    }
    @FXML
    public void onBooksAndAuthor(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AuthorWithBooks.fxml");
    }
    //Lending Module
    @FXML
    public void onIssueBook(){
        resetRight();
        getPane("/morogoro_lims/view/lending/IssueBook.fxml");
    }    
    @FXML
    public void onReturnBook(){
        resetRight();
        getPane("/morogoro_lims/view/lending/ReturnBook.fxml");
    }
    @FXML
    public void onHistory(){
        resetRight();
        getPane("/morogoro_lims/view/lending/History.fxml");
    }
    //Administration Module
    @FXML
    public void onAddLibrarian(){
        resetRight();
        getPane("/morogoro_lims/view/Admin/RegisterLibrarian.fxml");      
    }
    @FXML
    public void onViewActiveLibrarian(){        
        getPane("/morogoro_lims/view/Admin/LibrarianTable.fxml");
        //initRight("/morogoro_lims/view/Admin/LibrarianViewer.fxml");
    }
    @FXML
    public void onViewInactiveLibrarian(){
        getPane("/morogoro_lims/view/Admin/InActiveLibrarianTable.fxml");
    }
    @FXML
    public void onViewLogs(){
        resetRight();
        getPane("/morogoro_lims/view/Admin/Logs.fxml");
    }    
    @FXML
    public void onDeleteAllLogs(){
        resetRight();
        
        getPane("/morogoro_lims/view/Admin/Logs.fxml");
    }
    @FXML
    public void onSettings(){
        resetRight();
        getPane("/morogoro_lims/view/Admin/Settings.fxml");
    }
    //Remove or Set the left main menu
    @FXML
    public void onLeftPane(){
        if(leftPaneCheckBox.isSelected()){
            leftPaneCheckBox.setSelected(false);
        }else{
            leftPaneCheckBox.setSelected(true);
        }
    }
    
    //Logout and Exit Module
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
