package morogoro_lims.model.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Select {
    public Select(){}
    
    public static String sql(String tableName, Set<String> colName, Map<String, String> condition){
        StringBuilder selectSqlString = new StringBuilder();
        
        selectSqlString.append("SELECT ");
        
        if(!colName.isEmpty()){
            Iterator<String> it = colName.iterator();
            while(it.hasNext()){
                selectSqlString.append(it.next()).append(",");                
            }
        }
        
        selectSqlString = new StringBuilder(selectSqlString.subSequence(0, (selectSqlString.length()-1)));
        selectSqlString.append(" FROM ");
        selectSqlString.append(tableName).append(" ");
        
        if(!condition.isEmpty()){
            selectSqlString.append("WHERE").append(" ");
            for(Map.Entry<String, String> entry: condition.entrySet()){
                selectSqlString.append(entry.getKey()).append("=").append(entry.getValue());
                selectSqlString.append(" AND ");
            }
        }else{
            return selectSqlString.toString();
        }
        
        selectSqlString = new StringBuilder(selectSqlString.subSequence(0, (selectSqlString.length()-5)));
        
        return selectSqlString.toString();
    }
    
    public static void main(String[] args){
        Set<String> set = new TreeSet<>();
        set.add("title");
        set.add("author");
        set.add("category");
        
        Set<String> newSet = new TreeSet<>();
        newSet.add("*");
        
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        
        System.out.println(sql("book", newSet, new HashMap<>()));
        System.out.println(sql("book", set, map));
    }
}
