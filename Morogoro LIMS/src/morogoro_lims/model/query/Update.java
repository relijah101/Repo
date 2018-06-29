package morogoro_lims.model.query;

import java.util.HashMap;
import java.util.Map;

public class Update {
    public Update(){}
    
    public static String sql(String tableName, Map<String, String> colsAndData, Map<String, String> condition){
        StringBuilder updateSqlString = new StringBuilder();
        
        /**
         * Removing column that holds NULL value or Blank value
         */
        /*if(!colsAndData.isEmpty()){
            for(Map.Entry<String, String> entry : colsAndData.entrySet()){
                if(entry.getValue() == null || entry.getValue().isEmpty()){
                    colsAndData.remove(entry.getKey());
                }
            }
        }*/
        
        /**
         * Removing column that holds NULL value or Blank value..
         */
        if(!condition.isEmpty()){
            for(Map.Entry<String, String> entry : condition.entrySet()){
                if(entry.getValue().isEmpty() || entry.getValue() == null){
                    condition.remove(entry.getKey());
                }
            }
        }
        
        /**
         * Making the UPDATE query
         */
        updateSqlString.append("UPDATE");
        updateSqlString.append(" ").append(tableName).append(" ");
        updateSqlString.append("SET").append(" ");
        
        if(!colsAndData.isEmpty()){
            for(Map.Entry<String, String> entry: colsAndData.entrySet()){
                updateSqlString.append(entry.getKey()).append("=").append(entry.getValue());
                updateSqlString.append(",");
            }
        }
        updateSqlString = new StringBuilder(updateSqlString.subSequence(0, (updateSqlString.length()-1)));
        updateSqlString.append(" ").append("WHERE").append(" ");
        
        if(!condition.isEmpty()){
            for(Map.Entry<String, String> entry : condition.entrySet()){
                updateSqlString.append(entry.getKey()).append("=").append(entry.getValue());
                updateSqlString.append(",");
            }
        }
        
        updateSqlString = new StringBuilder(updateSqlString.subSequence(0, (updateSqlString.length()-1)));
        return updateSqlString.toString();
    }
    
    public static void main(String[] args){
        Map<String, String> libColsData = new HashMap<>();
        libColsData.put("reg_number", "?");
        libColsData.put("firstname", "?");
        libColsData.put("middlename", "?");
        libColsData.put("lastname", "?");
        libColsData.put("department", "?");
        libColsData.put("postal_addr", "?");
        libColsData.put("phone1", "?");
        libColsData.put("phone2", "?");
        libColsData.put("street", "?");
        libColsData.put("region", "?");
        libColsData.put("password", "?");
        libColsData.put("status", "?");
        libColsData.put("photo", "?");

        Map<String, String> libCond = new HashMap<>();
        libCond.put("librarian.reg_number", "?");
        
        System.out.println(sql("book", libColsData, libCond));
    }
}
