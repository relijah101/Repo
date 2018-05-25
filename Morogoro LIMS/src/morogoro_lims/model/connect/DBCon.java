package morogoro_lims.model.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import morogoro_lims.controller.Misc;

public class DBCon {
    private static final String URL = "jdbc:mysql://localhost/library_database";
    private static final String USER = "root";
    private static final String PWD = "mysql";
    private static Connection con;
    
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PWD);
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            Misc.display("Hakuna mawasiliano. Hakikisha server imewashwa.", 2);
        }
        return null;
    }
    
    public static void main(String [] args){
        Misc.display("Hello", 0);
    }
}
