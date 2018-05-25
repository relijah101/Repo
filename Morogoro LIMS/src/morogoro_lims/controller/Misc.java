package morogoro_lims.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Misc {
    public static void display(String text, int x){
        switch(x){
            case 0:
                Alert infoAlert = new Alert(AlertType.INFORMATION);
                infoAlert.setHeaderText("Ujumbe");
                infoAlert.setContentText(text);
                infoAlert.getButtonTypes().clear();
                infoAlert.getButtonTypes().add(ButtonType.OK);
                infoAlert.showAndWait();
                break;
            case 1:
                Alert warningAlert = new Alert(AlertType.WARNING, text, ButtonType.OK);
                warningAlert.setHeaderText("Ujumbe");
                warningAlert.setContentText(text);
                warningAlert.getButtonTypes().clear();
                warningAlert.getButtonTypes().add(ButtonType.OK);
                warningAlert.showAndWait();
                break;
            case 2:
                Alert errorAlert = new Alert(AlertType.ERROR, text, ButtonType.OK);
                errorAlert.setHeaderText("Ujumbe");
                errorAlert.showAndWait();
                break;
        }
    }
    
    public static String today(){
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d H:m:s");
        Date date = new Date();
        return sdf.format(date);
    }
}
