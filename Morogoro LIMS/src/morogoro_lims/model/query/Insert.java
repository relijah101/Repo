package morogoro_lims.model.query;

import java.util.HashMap;
import java.util.Map;

public class Insert{
    public Insert(){}
    
    public static String sql(String tableName, Map<String, String> colsAndData){
        StringBuilder insertSqlString = new StringBuilder();
        /**
         * Removing column that holds NULL value or blank value
         */
        
        /**
         * Making the insert query
         */
        insertSqlString.append("INSERT INTO ");
        insertSqlString.append(tableName).append(" (");
        
        if(!colsAndData.isEmpty()){
            for(Map.Entry<String, String> entry : colsAndData.entrySet()){
                insertSqlString.append(entry.getKey());
                insertSqlString.append(",");
            }
        }
        
        
        insertSqlString = new StringBuilder(insertSqlString.subSequence(0, (insertSqlString.length()-1)));
        insertSqlString.append(")");
        insertSqlString.append(" VALUES(");
        
        if(!colsAndData.isEmpty()){
            for(Map.Entry<String, String> entry : colsAndData.entrySet()){
                insertSqlString.append(entry.getValue());
                insertSqlString.append(",");
            }
        }
        
        insertSqlString = new StringBuilder(insertSqlString.subSequence(0, (insertSqlString.length()-1)));
        insertSqlString.append(")");
        
        
        return insertSqlString.toString();
    }
    
    public static void main(String[] args){
        Map<String, String> dataCols = new HashMap<>();
        dataCols.put("member_id", "?");
        dataCols.put("registration_number", "?");
        dataCols.put("librarian_id", "?");
        
        System.out.println(sql("table", dataCols));
    }
}
