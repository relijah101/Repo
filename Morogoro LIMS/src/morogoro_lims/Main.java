package morogoro_lims;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{
    Stage stage;
    @Override
    public void start(Stage stage) throws Exception {       
        AnchorPane dashboard = FXMLLoader.load(getClass().getResource("/morogoro_lims/view/Login.fxml"));
        Scene scene = new Scene(dashboard); 
        stage.setScene(scene);
        
        this.stage = stage;
        stage.setTitle("Mfumo wa Kumeneji Taarifa za Maktaba Ya Mkoa wa Morogoro.");
        stage.show();
    }
        
    public static void main(String[] args){
        launch(args);
    }    
}
