import java.io.File;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class Chooser extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        Button add = new Button("Add");
        TextField addField = new TextField();
        
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose a photo:");
        chooser.getExtensionFilters().addAll(
            new ExtensionFilter("JPEG","*.jpg", "*.jpeg", "*.jpe", "*.jfif"),
            new ExtensionFilter("GIF", "*.gif"),
            new ExtensionFilter("TIFF", "*.tif","*.tiff"),
            new ExtensionFilter("PNG", "*.png")    
        );
        add.setOnAction(e -> {
            File chosenFile = chooser.showOpenDialog(null);
            if (chosenFile != null) {
                if(chosenFile.length() > (1022*1024)){
                    Alert alert = new Alert(AlertType.WARNING, "Picha isizidi 1 MB\nIliyopo ni "+(chosenFile.length()/(1024*1024))+" MB", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                addField.setText(chosenFile.getName());
            }
        });       

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(add, addField);      

        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.setTitle("Choose a file");
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}