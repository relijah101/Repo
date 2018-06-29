package morogoro_lims;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import morogoro_lims.controller.admin.AdminDash;

public class Main extends Application{
    Stage stage;
    AdminDash dash = new AdminDash();
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

//    public Main(){
//        try{
//            FileOutputStream fos = new FileOutputStream("mor.data");
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(dash);
//            oos.close();
//            
//            FileInputStream fis = new FileInputStream("mor.data");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            dash = (AdminDash) ois.readObject();
//            ois.close();
//        }catch(IOException | ClassNotFoundException e){
//            Misc.display(e.getLocalizedMessage(), 2);
//        }
//    }
}
