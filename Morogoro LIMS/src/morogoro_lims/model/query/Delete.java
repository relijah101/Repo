package morogoro_lims.model.query;

import java.util.HashMap;
import java.util.Map;
import static morogoro_lims.model.query.Update.sql;

public class Delete {
    public Delete(){}
    
    public static String sql(String tableName, Map<String, String> condition){
        StringBuilder deleteSqlString = new StringBuilder();
        
        deleteSqlString.append("DELETE ");
        deleteSqlString.append("FROM ");
        deleteSqlString.append(tableName).append(" ");
        deleteSqlString.append("WHERE ");
        
        if(!condition.isEmpty()){
            for(Map.Entry<String, String> entry : condition.entrySet()){
                deleteSqlString.append(entry.getKey()).append("=").append(entry.getValue());
                deleteSqlString.append(" AND ");
            }
        }
        
        deleteSqlString = new StringBuilder(deleteSqlString.subSequence(0, (deleteSqlString.length()-5)));
        
        return deleteSqlString.toString();
    }
    
    public static void main(String[] args){
        Map<String, String> cond = new HashMap<>();
        cond.put("id", "1");
        
        System.out.println(sql("book", cond));
    }
}
