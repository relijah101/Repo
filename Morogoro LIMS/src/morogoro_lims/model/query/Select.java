package morogoro_lims.model.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static morogoro_lims.model.query.Query.ADULT_TABLE;
import static morogoro_lims.model.query.Query.DEPARTMENT_TABLE;
import static morogoro_lims.model.query.Query.LIBRARIAN_TABLE;
import static morogoro_lims.model.query.Query.MEMBER_TABLE;
import static morogoro_lims.model.query.Query.REGISTERED_TABLE;
import static morogoro_lims.model.query.Query.REGISTRATION_TABLE;

public class Select {
    public Select(){}
    
    public static String sql(Set<String> tables, Set<String> colName, Map<String, String> condition){
        StringBuilder selectSqlString = new StringBuilder();
        
        selectSqlString.append("SELECT ");
        Iterator<String> it;
        if(!colName.isEmpty()){
            it = colName.iterator();
            while(it.hasNext()){
                selectSqlString.append(it.next()).append(",");                
            }
        }
        
        selectSqlString = new StringBuilder(selectSqlString.subSequence(0, (selectSqlString.length()-1)));
        selectSqlString.append(" FROM ");
        
        if(!tables.isEmpty()){
            it = tables.iterator();
            while(it.hasNext()){
                selectSqlString.append(it.next()).append(",");                
            }
        }
        
        selectSqlString = new StringBuilder(selectSqlString.subSequence(0, (selectSqlString.length()-1)));
        selectSqlString.append(" ");
        
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
        Set<String> adultCol = new TreeSet<>();
                adultCol.add("*");
                
                Set<String> adultTables = new TreeSet();
                adultTables.add(MEMBER_TABLE);
                adultTables.add(ADULT_TABLE);
                
                Map<String, String> adultCond = new HashMap<>();
                adultCond.put("status", "?");
                adultCond.put(MEMBER_TABLE+".id", ADULT_TABLE+".member_id");
        System.out.println(sql(adultTables, adultCol, adultCond));
    }
}
