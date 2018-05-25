package morogoro_lims.controller.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Logs;
import morogoro_lims.model.connect.DBCon;

public class LoggerClass extends Handler{
    public static final String INFO_LEVEL = "INFO";
    public static final String WARNING_LEVEL = "WARNING";
    public static final String SEVERE_LEVEL = "SEVERE";
    private Connection con = null;
    private PreparedStatement statement = null;
    private ResultSet result = null;
    @Override
    public void publish(LogRecord lr) {
        String[] data = lr.getMessage().split("->");
        Logs log = new Logs(data[0], data[1], data[2], data[3], getLevel(lr.getLevel().getName()));
        
        String sql = "INSERT INTO logs (librarian_id, date, action, info) VALUES (?,?,?,?)";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(sql);
            statement.setString(1, log.getRegNumber());
            statement.setString(2, log.getDate());
            statement.setString(3, log.getAction());
            statement.setString(4, log.getInfo());
            statement.execute();
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
        }finally{
            try{statement.close(); con.close();}catch(SQLException sqle){}
        }
    }
    
    public String getLevel(String level){
        String levelValue;
        switch(level){
            case INFO_LEVEL:
                levelValue = "Amefanikisha";
                break;
            case WARNING_LEVEL:
                levelValue = "Onyo";
                break;
            case SEVERE_LEVEL:
                levelValue = "Imeshindikana";
                break;
            default:
                levelValue = "";
                break;
        }
        return levelValue;
    }
    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
    
}
