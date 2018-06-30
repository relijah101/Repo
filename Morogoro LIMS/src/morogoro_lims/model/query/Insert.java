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
        Map<String, String> bookColsData = new HashMap<>();
                bookColsData.put("class_number", "?");
                bookColsData.put("title", "?");
                bookColsData.put("category_id", "?");
                bookColsData.put("edition", "?");
                bookColsData.put("publisher_id", "?");
                bookColsData.put("isbn", "?");
                bookColsData.put("copies", "?");
                bookColsData.put("reference", "?");
        
        System.out.println(sql("table", bookColsData));
    }
}
