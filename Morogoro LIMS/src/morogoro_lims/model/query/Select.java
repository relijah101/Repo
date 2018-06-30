package morogoro_lims.model.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static morogoro_lims.model.query.Query.ADULT_TABLE;
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
        Set<String> memberTables = new TreeSet();
                memberTables.add(MEMBER_TABLE);
                memberTables.add(REGISTERED_TABLE);
                memberTables.add(REGISTRATION_TABLE);
                memberTables.add(LIBRARIAN_TABLE);
                        
                Set<String> memberCols = new TreeSet();
                memberCols.add("member.id as member_id");
                memberCols.add("member.firstname");
                memberCols.add("member.middlename");
                memberCols.add("member.lastname");
                memberCols.add("member.postal_addr");
                memberCols.add("member.phone1");
                memberCols.add("member.phone2");
                memberCols.add("member.type_of_id");
                memberCols.add("member.id_number");
                memberCols.add("member.street");
                memberCols.add("member.region");
                memberCols.add("member.status as member_status");
                memberCols.add("member.photo");                
                memberCols.add("registered.registration_number");
                memberCols.add("registration.receipt");
                memberCols.add("registration.reg_date");
                memberCols.add("registration.end_date");                
                memberCols.add("registered.librarian_id as lib_id");
                memberCols.add("librarian.firstname as lib_fname");
                memberCols.add("librarian.middlename as lib_mname");
                memberCols.add("librarian.lastname as lib_lname");
                
                Map<String, String> memberCond = new HashMap<>();
                memberCond.put(MEMBER_TABLE+".status", "?");
                memberCond.put(MEMBER_TABLE+".id", REGISTERED_TABLE+".member_id");
                memberCond.put(REGISTERED_TABLE+".librarian_id", LIBRARIAN_TABLE+".reg_number");
        System.out.println(sql(memberTables, memberCols, memberCond));
    }
}
