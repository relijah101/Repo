package morogoro_lims.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.technical.AddBook;
import morogoro_lims.controller.technical.AddPublisher;
import morogoro_lims.model.User;

public class Dashboard implements Initializable{
    @FXML private BorderPane main_dash;
    private static User user;
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
    public void onRecordBook(){
        resetRight();
        getPane("/morogoro_lims/view/Technical/AddBook.fxml");
        System.out.println(this.getUser().getRegNumber());
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
    
    public void onViewActiveLibrarian(){        
        getPane("/morogoro_lims/view/Admin/LibrarianTable.fxml");
        initRight("/morogoro_lims/view/Admin/LibrarianViewer.fxml");
    }
    
    public void onViewInactiveLibrarian(){
        getPane("/morogoro_lims/view/Admin/InActiveLibrarianTable.fxml");
    }
    
    public void onViewLogs(){
        resetRight();
        getPane("/morogoro_lims/view/Admin/Logs.fxml");
    }    
    
    public void onSettings(){
        resetRight();
        getPane("");
    }
}
