package morogoro_lims.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
                //Misc.setIcon((Stage)infoAlert.getGraphic().getScene().getWindow());
                infoAlert.showAndWait();
                break;
            case 1:
                Alert warningAlert = new Alert(AlertType.WARNING, text, ButtonType.OK);
                warningAlert.setTitle("Onyo.");
                warningAlert.setHeaderText("Ujumbe");
                warningAlert.setContentText(text);
                warningAlert.getButtonTypes().clear();
                warningAlert.getButtonTypes().add(ButtonType.OK);
                //Misc.setIcon((Stage)warningAlert.getGraphic().getScene().getWindow());
                warningAlert.showAndWait();
                break;
            case 2:
                Alert errorAlert = new Alert(AlertType.ERROR, text, ButtonType.OK);
                errorAlert.setTitle("Tatizo.");
                errorAlert.setHeaderText("Ujumbe");
                //Misc.setIcon((Stage)errorAlert.getGraphic().getScene().getWindow());
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
    public static String today2(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/Y");
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
    public static ObservableList<String> getEndWeeks(){
        ObservableList<String> dates = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd");
        Date today = new Date();
        for(int i = 1; i < 10; i++){
            Date date = Misc.addWeek(today, i);            
            dates.add(sdf.format(date));
        }
        return dates;
    }
    public static Date addWeek(Date date, int y){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.WEEK_OF_MONTH, y);
        return gc.getTime();
    }
    public static void setIcon(Stage stage){
        String loc = "/morogoro_lims/view/Image/TLSB-Logo-Small.png";
        stage.getIcons().add(new Image(Misc.class.getResourceAsStream(loc)));
    }
    
    public static void printPageSetup(Node node){
        //Create a printer job for the default printer
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job == null){
            Misc.display("Imeshindikana kupata printer.", 2);
            return;
        }       
        
        Stage stage = new Stage();
        Misc.setIcon(stage);
        stage.setTitle("Mipangilio ya kuchapa.");
        
        boolean proceed = job.showPageSetupDialog(stage);
        
        if(proceed){
            Misc.print(job, node);
        }else{
            Misc.display("Umehairisha kuchapa", 0);
        }
    }
    
    public static void print(PrinterJob job, Node node){        
        //print the table
        Node table = node;
        if(job.printPage(table)){
            job.endJob();
            Misc.display("Imekamilisha kuchapa", 0);
        }else{
            Misc.display("Imeshindikana kuchapa.", 2);
        }
    }
    
    public static String getSHA512Password(String password){
        String generatedPassword = null; 
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            
            byte[] bytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }catch(NoSuchAlgorithmException nsae){
            Misc.display(nsae.getLocalizedMessage(), 2);
        }
        return generatedPassword;
    }
}
