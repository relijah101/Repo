package morogoro_lims.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Misc {
    public static void display(String text, int x){
        switch(x){
            case 0:
                Alert infoAlert = new Alert(AlertType.INFORMATION);
                infoAlert.setTitle("Umefanikisha.");
                infoAlert.setHeaderText("Ujumbe");
                infoAlert.setContentText(text);
                infoAlert.getButtonTypes().clear();
                infoAlert.getButtonTypes().add(ButtonType.OK);
                infoAlert.showAndWait();
                break;
            case 1:
                Alert warningAlert = new Alert(AlertType.WARNING, text, ButtonType.OK);
                warningAlert.setTitle("Onyo.");
                warningAlert.setHeaderText("Ujumbe");
                warningAlert.setContentText(text);
                warningAlert.getButtonTypes().clear();
                warningAlert.getButtonTypes().add(ButtonType.OK);
                warningAlert.showAndWait();
                break;
            case 2:
                Alert errorAlert = new Alert(AlertType.ERROR, text, ButtonType.OK);
                errorAlert.setTitle("Tatizo.");
                errorAlert.setHeaderText("Ujumbe");
                errorAlert.showAndWait();
                break;
        }
    }
    
    public static String todayNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d H:m:s");
        Date date = new Date();
        return sdf.format(date);
    }
    public static String today(){
        SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }
    public static ObservableList<String> getEndDates(){
        ObservableList<String> dates = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd");
        Date today = new Date();
        for(int i = 1; i < 10; i++){
            Date date = Misc.addYear(today, i);            
            dates.add(sdf.format(date));
        }
        return dates;
    }
    public static Date addYear(Date date, int y){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, y);
        return gc.getTime();
    }
}
