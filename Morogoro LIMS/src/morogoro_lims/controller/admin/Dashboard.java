package morogoro_lims.controller.admin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.User;

public class Dashboard implements Initializable{
    @FXML BorderPane main_dash;
    @FXML transient CheckMenuItem defaultCheckBox, redenCheckBox, darkenCheckBox, leftPaneCheckBox;
    @FXML transient Accordion menuAccordion;
    private transient static User user;
 
    public Dashboard(){
        
    }
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
        Dashboard.user = user;
    }
    public static User getUser(){
        return user;
    }
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
    public void getPane(String location){
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource(location));
            main_dash.setCenter(pane);
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
    }
    @FXML
    public void onFind(){
        TextField searchMenu = new TextField();
        searchMenu.setPrefWidth(300);
        VBox menuBox = new VBox(5);
        
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Tafuta.");
        dialog.setHeaderText("Tafuta menu.");
        dialog.setGraphic(menuBox);
      
        menuBox.getChildren().addAll(searchMenu);
        dialog.show();
        
    }
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
    //Registration
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
    //Technical
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
        getPane("");
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
    //Themes
    @FXML
    public void onDefault(){
        redenCheckBox.setSelected(false);
        darkenCheckBox.setSelected(false);
    }    
    @FXML
    public void onReden(){
        defaultCheckBox.setSelected(false);
        darkenCheckBox.setSelected(false);
    }
    @FXML
    public void onDarken(){
        defaultCheckBox.setSelected(false);
        darkenCheckBox.setSelected(false);
    }
    
    //Logout and Exit
    @FXML
    public void onLogout(){
        
    }
    @FXML
    public void onExit(){
        Platform.exit();
    }
}
