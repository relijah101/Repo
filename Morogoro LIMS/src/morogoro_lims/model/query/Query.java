package morogoro_lims.model.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import morogoro_lims.controller.Login;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Adult;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.Department;
import morogoro_lims.model.Info;
import morogoro_lims.model.Lending;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.Logs;
import morogoro_lims.model.Member;
import morogoro_lims.model.Primary;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.Returning;
import morogoro_lims.model.Secondary;
import morogoro_lims.model.User;
import morogoro_lims.model.connect.DBCon;

public class Query<T> extends Handler{
    //Initialize logger variable to log information
    private static final Logger LOGGER = Logger.getLogger(Query.class.getName());
    //Initialize variables to hold table names
    public static final String BOOK_TABLE = "book";
    public static final String AUTHOR_TABLE = "author";
    public static final String BOOK_STATUS_TABLE = "book_status";
    public static final String BOOK_AUTHOR_TABLE = "book_author";
    public static final String CATEGORY_TABLE = "category";
    public static final String PUBLISHER_TABLE = "publisher";
    
    public static final String DEPARTMENT_TABLE = "department";
    public static final String LIBRARIAN_TABLE = "librarian";
    
    public static final String MEMBER_TABLE = "member";
    public static final String ADULT_TABLE = "member_adult";
    public static final String PRIMARY_TABLE = "member_primary";
    public static final String SECONDARY_TABLE = "member_sec";
    
    public static final String REGISTERED_TABLE = "registered";
    public static final String REGISTRATION_TABLE = "registration";
    public static final String RENEW_TABLE = "renew";
    public static final String SPONSOR_TABLE = "sponsor";
    
    public static final String RETURN_TABLE = "returning";
    public static final String LEND_TABLE = "lending";
    public static final String REFERENCE_ISSUE = "reference_lend";
    
    public static final String LOGS_TABLE = "logs";
    public static final String INFO_TABLE = "info";
    public static final String SHORTCUT_TABLE = "shortcuts";
    
    private static Connection con;
    private static PreparedStatement statement;
    private static ResultSet result;
    
    private static String sql; 
    public static User user;
    
    public Query() {
        LOGGER.setLevel(Level.INFO);
        for(Handler l : LOGGER.getHandlers()){
            LOGGER.removeHandler(l);
        }
        LOGGER.addHandler(this);
        user = Login.getUser();
    }  
    public static final String INFO_LEVEL = "INFO";
    public static final String WARNING_LEVEL = "WARNING";
    public static final String SEVERE_LEVEL = "SEVERE";
    
    @Override
    //Overriden method to log the information
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
    //Translate levels to swahili so that they can be stored as swahili text
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
    
    //Login validation
    public User login(String name, String pass){
        Map<String, String> logMap = new HashMap<>();
        logMap.put("firstname", "?");
        logMap.put("librarian.department","department.id");
        logMap.put("status", "?");
        
        Set<String> libTables = new TreeSet();
        libTables.add(LIBRARIAN_TABLE);
        libTables.add(DEPARTMENT_TABLE);
        
        Set<String> colSet = new TreeSet<>();
        colSet.add("reg_number");
        colSet.add("firstname");
        colSet.add("password");
        colSet.add("department.department");
        
        String userSql = Select.sql(libTables, colSet, logMap);
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(userSql);
            statement.setString(1, name);
            //statement.setString(2, pass);
            statement.setInt(2, 1);
            result = statement.executeQuery();
            if(result.next()){
                if(result.getString("password").equals(pass)){
                    user = new User(result.getString("firstname"),result.getString("reg_number"), 
                        result.getString("department"));
                    log(1, user.getRegNumber(), user.getUsername(), "Ameingia");
                    return user;
                }
            }
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
            log(3, name, pass, sqle.getLocalizedMessage());
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}            
        }
        return null;
    }
    
    //Get user's current password
    public String getPwd(String reg){
        String pwdSql = "SELECT password FROM librarian WHERE reg_number = ? AND status = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(pwdSql);
            statement.setString(1, reg);
            statement.setString(2, "1");
            result = statement.executeQuery();
            if(result.next())
                return result.getString("password");
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
        }
        return null;
    }
    //Update current user's password
    public boolean updatePwd(String reg, String pwd){
        String pwdSql = "UPDATE librarian SET password = ? WHERE reg_number = ? AND status = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(pwdSql);
            statement.setString(1, pwd);
            statement.setString(2, reg);
            statement.setString(3, "1");
            statement.execute();
            Misc.display("Neno siri limebadilishwa.", 0);
            log(1, user.getRegNumber(), user.getUsername(), "Amebadili neno siri.");
            return true;
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
            log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili neno siri. "+sqle.getLocalizedMessage());
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
        }
        return false;
    }
    
    //Initialize columns for registering members
    public Map<String, String> getRegCols(){
        Map<String, String> dataCols = new HashMap<>();
        dataCols.put("member_id", "?");
        dataCols.put("registration_number", "?");
        dataCols.put("librarian_id", "?");
        return dataCols;
    }
    public Map<String, String> registrationCols(){
        Map<String, String> dataCols = new HashMap<>();
        dataCols.put("member_reg_number", "?");
        dataCols.put("receipt", "?");
        dataCols.put("reg_date", "?");
        dataCols.put("end_date", "?");
        return dataCols;
    }
    
    //Method to insert information to the database
    public boolean insert(T t, String table){ 
        switch(table){
            //Insert table
            case BOOK_TABLE:
                Book book = (Book) t;
                Map<String, String> bookColsData = new HashMap<>();
                bookColsData.put("class_number", "?");
                bookColsData.put("title", "?");
                bookColsData.put("category_id", "?");
                bookColsData.put("edition", "?");
                bookColsData.put("publisher_id", "?");
                bookColsData.put("isbn", "?");
                bookColsData.put("copies", "?");
                bookColsData.put("reference", "?");
                sql = Insert.sql(BOOK_TABLE, bookColsData);
                
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, book.getClassNumber());
                    statement.setLong(2, book.getPublisherClass().getId());
                    statement.setString(3, book.getReference());
                    statement.setInt(4, book.getCopies());
                    statement.setLong(5, book.getCategoryClass().getId());
                    statement.setString(6, book.getIsbn());
                    statement.setInt(7, book.getEdition());  
                    statement.setString(8, book.getTitle());       
                    statement.execute();
                    Misc.display("Taarifa za kitabu zimehifadhiwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza kitabu (" + book.getTitle() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuongeza kitabu.\nHakikisha taarifa za kitabu hazijarudiwa.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza kitabu: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            //Insert author
            case AUTHOR_TABLE:
                Author author = (Author) t;
                Map<String, String> authorDataCols = new HashMap<>();
                authorDataCols.put("firstname", "?");
                authorDataCols.put("middlename", "?");
                authorDataCols.put("lastname", "?");
                
                sql = Insert.sql(AUTHOR_TABLE,authorDataCols);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, author.getFirstName());
                    statement.setString(2, author.getMiddleName());
                    statement.setString(3, author.getLastName());
                    statement.execute();
                    Misc.display("Taarifa za mwandishi zimehifadhiwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza mwandishi (" + author.getFullName() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuongeza mwandishi.\nHakikisha taarifa za mwandishi hazijarudiwa.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mwandishi: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;    
            //Insert category
            case CATEGORY_TABLE:
                Category category = (Category) t;
                Map<String, String> catColsData = new HashMap<>();
                catColsData.put("name", "?");
                catColsData.put("description", "?");
                sql = Insert.sql(CATEGORY_TABLE, catColsData);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, category.getCategory());
                    statement.setString(2, category.getDescription());
                    statement.execute();
                    Misc.display("Taarifa za kategory zimehifadhiwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza kategori (" + category.getCategory() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuongeza kategori.\nHakikisha taarifa za kategori hazijarudiwa.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza kategori: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            //Insert publisher
            case PUBLISHER_TABLE:
                Publisher publisher = (Publisher) t;
                Map<String, String> pubColsData = new HashMap<>();
                pubColsData.put("name", "?");
                sql = Insert.sql(PUBLISHER_TABLE, pubColsData);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, publisher.getPublisher());
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amerekodi mchapishaji (" + publisher.getPublisher() +")");
                    Misc.display("Taarifa za mchapishaji zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuongeza mchapishaji.\nHakikisha taarifa za mchapishaji hazijarudiwa.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza mchapishaji: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            //Insert librarian
            case LIBRARIAN_TABLE:
                Librarian librarian = (Librarian) t;
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
                sql = Insert.sql(LIBRARIAN_TABLE, libColsData);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, librarian.getReg());
                    statement.setString(2, librarian.getFirstName());
                    statement.setString(3, librarian.getPhone2());
                    statement.setString(4, librarian.getMiddleName());
                    statement.setBytes(5, librarian.getPhoto());
                    statement.setString(6, librarian.getLastName());
                    statement.setString(7, librarian.getPhone1());
                    statement.setString(8, librarian.getPassword());
                    statement.setString(9, librarian.getStreet());       
                    statement.setString(10, librarian.getPostalAddr());
                    statement.setLong(11, librarian.getDepId());                    
                    statement.setString(12, librarian.getRegion());                    
                    statement.setInt(13, librarian.getStatus());
                    statement.execute();
                    Misc.display("Taarifa za mwandishi zimehifadhiwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza mkutubi (" + librarian.getLastName() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuongeza mkutubi.\nHakikisha taarifa za mkutubi hazijarudiwa.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mkutubi: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;  
            case DEPARTMENT_TABLE:
                Department department = (Department) t;
                Map<String, String> depColsData = new HashMap<>();
                depColsData.put("department", "?");
                sql = Insert.sql(DEPARTMENT_TABLE, depColsData);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, department.getName());
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amerekodi kitengo (" + department.getName() +")");
                    Misc.display("Kitengo kimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuongeza kitengo.\nHakikisha taarifa za kitengo hazijarudiwa.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza kitengo: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;            
            case ADULT_TABLE:
                Adult adult = (Adult) t;
                sql = Insert.sql(REGISTERED_TABLE, getRegCols());
                
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setLong(1, adult.getId());
                    statement.setString(2, adult.getRegNumber());
                    statement.setString(3, user.getRegNumber());
                    statement.execute();
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuhifadhi namba ya usajili", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi namba ya usajili ya mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
                sql = Insert.sql(REGISTRATION_TABLE, registrationCols());
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, adult.getEndDate());
                    statement.setString(2, adult.getStartDate());
                    statement.setString(3, adult.getReceipt());
                    statement.setString(4, adult.getRegNumber());                     
                    statement.execute();
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kufanya uandikishaji.\nHakikisha taarifa hazijajirudia", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kusajili mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
                try{
                    Map<String, String> adultColsData = new HashMap<>();
                    adultColsData.put("member_reg_number", "?");
                    adultColsData.put("office_name", "?");
                    adultColsData.put("sponsor_name", "?");
                    adultColsData.put("title", "?");
                    adultColsData.put("reference_no", "?");

                    sql = Insert.sql(SPONSOR_TABLE, adultColsData);
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, adult.getOffice());
                    statement.setString(2, adult.getRegNumber());                    
                    statement.setString(3, adult.getSponsor());
                    statement.setString(4, adult.getTitle());
                    statement.setString(5, adult.getReference());
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amemwandikisha mwanachama ("+adult.getRegNumber()+")");
                    return true;
                }catch(SQLException sqle){
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kumwandikisha mwanachama ("+sqle.getLocalizedMessage()+")");  
                    Misc.display("Imeshindikana kuhifadhi taarifa za mdhamini."+sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }              
            break;
            case PRIMARY_TABLE:
                Primary primary = (Primary) t;               
                
                sql = Insert.sql(REGISTERED_TABLE, getRegCols());
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setLong(1, primary.getId());
                    statement.setString(2, primary.getRegNumber());
                    statement.setString(3, user.getRegNumber());                    
                    statement.execute();
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuhifadhi namba ya uandikishaji.\nHakikisha taarifa hazijajirudia", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi namba ya uandikishaji ya mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
                
                sql = Insert.sql(REGISTRATION_TABLE, registrationCols());
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, primary.getEndDate());
                    statement.setString(2, primary.getStartDate());
                    statement.setString(3, primary.getReceipt());
                    statement.setString(4, primary.getRegNumber());
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amemwandikisha mwanachama (" + primary.getRegNumber() + ")");                    
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kufanya uandikishaji.\nHakikisha taarifa hazijajirudia", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kusajili mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }        
                
            break;
            case SECONDARY_TABLE:
                Secondary secondary = (Secondary) t;
                sql = Insert.sql(REGISTERED_TABLE, getRegCols());
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setLong(1, secondary.getId());
                    statement.setString(2, secondary.getRegNumber());
                    statement.setString(3, user.getRegNumber());
                    statement.execute();   
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuhifadhi namba ya uandikishaji.\nHakikisha taarifa hazijajirudia", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi namba ya usajili ya mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
                try{
                    con = DBCon.getConnection();
                    sql = Insert.sql(REGISTRATION_TABLE, registrationCols());
                    statement = con.prepareStatement(sql);
                    statement.setString(1, secondary.getEndDate());
                    statement.setString(2, secondary.getStartDate());
                    statement.setString(3, secondary.getReceipt());
                    statement.setString(4, secondary.getRegNumber()); 
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amemwandikisha mwanachama (" + secondary.getRegNumber() + ")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kufanya uandikishaji.\nHakikisha taarifa hazijajirudia", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kusajili mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case BOOK_AUTHOR_TABLE:
                Object[] obj = (Object[]) t;
                Long bookId = (Long) obj[0];
                ObservableList<Author> authors = (ObservableList<Author>) obj[1];
                String authorSql = "INSERT INTO book_author (book_id, author_id) VALUES (?,?)";
                for(Author i : authors){                    
                    try{
                        con = DBCon.getConnection();
                        statement = con.prepareStatement(authorSql);
                        statement.setLong(1, bookId);
                        statement.setLong(2, i.getId());
                        statement.execute();                        
                    }catch(SQLException sqle){
                        Misc.display("Imeshindikana kuhifadhi mwandishi kwenye kitabu", 2);
                        log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mwandishi kwenye kitabu: "+sqle.getLocalizedMessage());
                    }finally{
                        try{statement.close(); con.close();}catch(SQLException sqle){}
                    }
                }
            break;
            case RETURN_TABLE:
                Object[] returnObj = (Object[]) t;
                ObservableList<Book> bookToReturn = (ObservableList<Book>) returnObj[0];
                String today = (String) returnObj[1];
                String returnSql = "INSERT INTO returning (lend_id, librarian_id, return_date, late_ontime)"
                        + "VALUES(?,?,?,?)";
                //for(Book b : bookToReturn){                    
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(returnSql);
                    for(Book b : bookToReturn){
                        statement.setLong(1, b.getLendId());
                        statement.setString(2, user.getRegNumber());
                        statement.setString(3, today);
                        statement.setInt(4, 1);
                        statement.execute();
                        statement.close();
                        log(1, user.getRegNumber(), user.getUsername(), "Amerekodi urudishwaji wa kitabu : "+b.getClassNumber()+")");
                    }
                    Misc.display("Vitabu vimerudishwa kikamilifu.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuhifadhi urudishwaji wa vitabu", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi urudishwaji wa vitabu: "+sqle.getLocalizedMessage());
                }finally{
                    try{con.close();}catch(SQLException sqle){}
                }
                //}
                
            break;
            case LEND_TABLE:
                Object[] lendObj = (Object[]) t;
                ObservableList<Book> bookToIssue = (ObservableList<Book>) lendObj[0];
                String memberReg = (String) lendObj[1];
                String endDate = (String) lendObj[2];
                String to = (String) lendObj[3];
                
                String lendSql = "INSERT INTO lending (book_id, member_reg_number, librarian_id, lend_date, return_date)"
                        + "VALUES(?,?,?,?,?)";
                                   
                try{
                    con = DBCon.getConnection();
                    for(Book b : bookToIssue){ 
                        statement = con.prepareStatement(lendSql);
                        statement.setLong(1, b.getId());
                        statement.setString(2, memberReg);
                        statement.setString(3, user.getRegNumber());
                        statement.setString(4, to);
                        statement.setString(5, endDate);
                        statement.execute();   
                        statement.close();
                        log(1, user.getRegNumber(), user.getUsername(), "Amerekodi uazimishwaji wa kitabu : "+b.getClassNumber()+")");
                    }
                    Misc.display("Vitabu vimeazimishwa kikamilifu.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuhifadhi uazimishaji wa vitabu", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi uazimishaji wa vitabu: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
        }  
        return false;
    }
    //Method to select information from the database
    public ObservableList<T> select(String t, int i){
        switch(t){
            case BOOK_TABLE:
                ObservableList bookList = FXCollections.observableArrayList();
                //Book book = (Book) this.t;
                String bookSql = "SELECT book.id as book_id, book.class_number, book.title, book.edition, book.isbn, book.copies, book.reference, "
                        + "category.id as cat_id, category.name as cat_name, category.description, "
                        + "publisher.id as pub_id, publisher.name as pub_name "
                        + "FROM " + BOOK_TABLE + ", category, publisher "
                        + "WHERE book.category_id =  category.id "
                        + "AND book.publisher_id = publisher.id";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(bookSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Category cat = new Category(result.getLong("cat_id"), result.getString("cat_name"), result.getString("description"));
                        Publisher pub = new Publisher(result.getLong("pub_id"), result.getString("pub_name"));
                        Book book = new Book(result.getLong("book_id"), result.getString("class_number"), result.getString("title"), cat,
                                result.getInt("edition"), 
                                result.getInt("copies"), pub,result.getString("isbn"),result.getString("reference")
                        );
                        bookList.add(book);
                    }
                    return bookList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case AUTHOR_TABLE:
                ObservableList authorList = FXCollections.observableArrayList();
                String authorSql = "SELECT * FROM " + AUTHOR_TABLE;
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(authorSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Author author = new Author(result.getLong("id"), result.getString("firstname"), 
                                result.getString("middlename"),result.getString("lastname")
                        );
                        authorList.add(author);
                    }
                    return authorList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case CATEGORY_TABLE:
                ObservableList catList = FXCollections.observableArrayList();
                String catSql = "SELECT * FROM " + CATEGORY_TABLE;
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(catSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Category category = new Category(result.getLong("id"),result.getString("name"),result.getString("description"));
                        catList.add(category);
                    }
                    return catList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case PUBLISHER_TABLE:
                ObservableList pubList = FXCollections.observableArrayList();
                String pubSql = "SELECT * FROM " + PUBLISHER_TABLE;
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(pubSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Publisher publisher = new Publisher(result.getLong("id"),result.getString("name"));
                        pubList.add(publisher);
                    }
                    return pubList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case LIBRARIAN_TABLE:
                ObservableList libList = FXCollections.observableArrayList();
                //Book book = (Book) this.t;
                String libSql = "SELECT librarian.id, librarian.reg_number, librarian.firstname, librarian.middlename, "
                        + "librarian.lastname, librarian.postal_addr, librarian.phone1, librarian.phone2, "
                        + "librarian.street, librarian.region, librarian.password, librarian.status, librarian.photo, "
                        + "department.id AS dep_id, department.department "
                        + "FROM " + LIBRARIAN_TABLE + ", department "
                        + "WHERE status = ? "
                        + "AND librarian.department = department.id";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(libSql);
                    statement.setString(1,""+i);
                    result = statement.executeQuery();
                    while(result.next()){
                        Department department = new Department(result.getLong("dep_id"), result.getString("department"));
                        Librarian librarian = new Librarian(result.getLong("id"), result.getString("reg_number"),result.getString("firstname"), 
                            result.getString("middlename"), result.getString("lastname"), department, result.getString("postal_addr"), 
                            result.getString("phone1"), result.getString("phone2"), result.getString("password"), 
                            result.getString("street"), result.getString("region"), result.getInt("status"), result.getBytes("photo")
                        );
                        libList.add(librarian);
                    }
                    return libList;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuona wakutubi.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuona wakutubi." + sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case MEMBER_TABLE:
                ObservableList memberList = FXCollections.observableArrayList();
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
                memberCond.put(REGISTERED_TABLE+".registration_number", REGISTRATION_TABLE+".member_reg_number");
                memberCond.put(LIBRARIAN_TABLE+".reg_number", REGISTERED_TABLE+".librarian_id" );
                
                String memberSql = Select.sql(memberTables, memberCols, memberCond);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(memberSql);
                    statement.setString(1, i+"");
                    result = statement.executeQuery();
                    while(result.next()){
                        Member member = new Member(
                            result.getLong("member_id"), result.getString("registration_number"),
                            result.getString("firstname"), result.getString("middlename"), result.getString("lastname"), 
                            result.getString("postal_addr"), result.getString("phone1"), result.getString("phone2"),
                            result.getString("type_of_id"), result.getString("id_number"),
                            result.getString("street"), result.getString("region"),
                            result.getString("member_status"),
                            result.getString("receipt"), result.getString("reg_date"), result.getString("end_date"),
                            result.getString("lib_id"),
                            result.getString("lib_fname"),
                            result.getBytes("photo")
                        );
                        memberList.add(member);
                    }
                    return memberList;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuonesha listi ya wanachama wote.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuonesha listi ya wanachama wote." + sqle.getLocalizedMessage());
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case ADULT_TABLE:
                ObservableList adultList = FXCollections.observableArrayList();
                Set<String> adultCol = new TreeSet<>();
                adultCol.add("*");
                
                Set<String> adultTables = new TreeSet();
                adultTables.add(MEMBER_TABLE);
                adultTables.add(ADULT_TABLE);
                
                Map<String, String> adultCond = new HashMap<>();
                adultCond.put("status", "?");
                adultCond.put(MEMBER_TABLE+".id", ADULT_TABLE+".member_id");
                
                
                String adultSql = Select.sql(adultTables, adultCol, adultCond);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(adultSql);
                    statement.setString(1, i+"");
                    result = statement.executeQuery();
                    while(result.next()){
                        Adult adult = new Adult(
                            result.getLong("member_id"), 
                            result.getString("firstname"), result.getString("middlename"), result.getString("lastname"), 
                            result.getString("postal_addr"),result.getString("house_number"), 
                            result.getString("phone1"), result.getString("phone2"), 
                            result.getString("type_of_id"), result.getString("id_number"), 
                            result.getString("street"), result.getString("region")                                
                        );
                        adultList.add(adult);
                    }
                    return adultList;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuona watu wazima.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuona watu wazima." + sqle.getLocalizedMessage());
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case PRIMARY_TABLE:
                ObservableList primList = FXCollections.observableArrayList();
                Set<String> primCol = new TreeSet<>();
                primCol.add("*");
                
                Set<String> tables = new TreeSet();
                tables.add(MEMBER_TABLE);
                tables.add(PRIMARY_TABLE);
                
                Map<String, String> primCond = new HashMap<>();
                primCond.put("status", "?");
                primCond.put(MEMBER_TABLE+".id", PRIMARY_TABLE+".member_id");

                String primSql = Select.sql(tables, primCol, primCond);
                try {
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(primSql);
                    statement.setString(1, i + "");
                    result = statement.executeQuery();
                    while (result.next()) {
                        Primary primary = new Primary(
                                result.getLong("member_id"), 
                                result.getString("firstname"),result.getString("middlename"), result.getString("lastname"), 
                                result.getString("postal_addr"),
                                result.getString("phone1"), result.getString("phone2"), 
                                result.getString("type_of_id"), result.getString("id_number"), 
                                result.getString("street"),result.getString("region"), 
                                result.getString("p_firstname"), result.getString("p_middlename"), result.getString("p_lastname"), 
                                result.getString("school_name"), result.getString("school_address"), result.getString("class")
                        );
                        primList.add(primary);
                    }
                    return primList;
                } catch (SQLException sqle) {
                    Misc.display("Imeshindikana kuona wanafunzi msingi.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuona wanafunzi msingi." + sqle.getLocalizedMessage());
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case SECONDARY_TABLE:
                ObservableList secList = FXCollections.observableArrayList();
                Set<String> secCol = new TreeSet<>();
                secCol.add("*");
                
                Set<String> secTables = new TreeSet();
                secTables.add(MEMBER_TABLE);
                secTables.add(SECONDARY_TABLE);

                Map<String, String> secCond = new HashMap<>();
                secCond.put("status", "?");
                secCond.put(MEMBER_TABLE+".id", SECONDARY_TABLE+".member_id");
                
                String secSql = Select.sql(secTables, secCol, secCond);
                try {
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(secSql);
                    statement.setString(1, i + "");
                    result = statement.executeQuery();
                    while (result.next()) {
                        Secondary sec = new Secondary(
                            result.getLong("member_id"),
                            result.getString("firstname"),result.getString("middlename"), result.getString("lastname"), 
                            result.getString("postal_addr"),result.getString("phone1"), result.getString("phone2"), 
                            result.getString("type_of_id"),result.getString("id_number"), 
                            result.getString("street"), result.getString("region"),
                            result.getString("class"), result.getString("school_name"), result.getString("school_address")
                        );
                        secList.add(sec);
                    }
                    return secList;
                } catch (SQLException sqle) {
                    Misc.display("Imeshindikana kuona wanafunzi sekondari.", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuona wanafunzi sekondari." + sqle.getLocalizedMessage());
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case LOGS_TABLE:
                ObservableList logsList = FXCollections.observableArrayList();
                String logSql = "SELECT logs.id, logs.date, logs.action, logs.info, "
                        + "librarian.reg_number, librarian.firstname, librarian.middlename, librarian.lastname "
                        + "FROM " + LOGS_TABLE + ", librarian "
                        + "WHERE logs.librarian_id = librarian.reg_number";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(logSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Logs log = new Logs(result.getLong("id"), result.getString("reg_number"), 
                            result.getString("firstname")+" "+ result.getString("lastname"), result.getString("date"),
                            result.getString("action"), result.getString("info")
                    );
                        
                        logsList.add(log);
                    }
                    return logsList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case DEPARTMENT_TABLE:
                ObservableList depList = FXCollections.observableArrayList();
                String depSql = "SELECT * FROM " + DEPARTMENT_TABLE;
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(depSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Department dep = new Department(result.getLong("id"), result.getString("department"));
                        depList.add(dep);
                    }                    
                    return depList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case INFO_TABLE:
                ObservableList infoList = FXCollections.observableArrayList();
                String infoSql = "SELECT * FROM " + INFO_TABLE;
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(infoSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        Info info = new Info(result.getString("institution"), result.getString("phone1"), 
                            result.getString("phone2"),result.getString("city"), result.getString("address"), result.getString("email")
                        );
                        infoList.add(info);
                    }                    
                    return infoList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case BOOK_AUTHOR_TABLE:
                ObservableList bookAuthor = FXCollections.observableArrayList();
                String bASql = "SELECT * FROM book, author, book_author "
                        + "WHERE book.id = book_author.book_id "
                        + "AND author.id = book_author.author_id";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(bASql);
                    result = statement.executeQuery();
                    while(result.next()){
                        
                    }                    
                    return bookAuthor;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case LEND_TABLE:
                ObservableList lendList = FXCollections.observableArrayList();
                String lendSql = "SELECT lending.id AS lend_id, " +
                    "book.class_number, book.title, " +
                    "registered.registration_number, member.firstname, member.middlename, member.lastname, " +
                    "librarian.reg_number, librarian.firstname as lib_name, " +
                    "lending.lend_date, lending.return_date " +
                    "FROM lending, book, member, registered, librarian " +
                    "WHERE lending.member_reg_number = registered.registration_number " +
                    "AND registered.member_id = member.id " +
                    "AND lending.librarian_id = librarian.reg_number " +
                    "AND lending.book_id = book.id";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(lendSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        String memberName = result.getString("firstname")+" "+result.getString("middlename")+" "+result.getString("lastname");
                        lendList.add(new Lending(result.getLong("lend_id"),
                            result.getString("class_number"),result.getString("title"),result.getString("registration_number"),
                            memberName,result.getString("reg_number"),result.getString("lib_name"),
                            result.getString("lend_date"),result.getString("return_date")
                        ));
                    }                    
                    return lendList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case RETURN_TABLE:
                ObservableList returnList = FXCollections.observableArrayList();
                String returnSql = "SELECT returning.lend_id, " +
                    "registered.registration_number as reg, member.firstname, member.middlename, member.lastname, " +
                    "book.class_number, book.title, " +
                    "librarian.reg_number, librarian.firstname as lib_fname, " +
                    "returning.return_date, returning.late_ontime " +
                    "FROM returning, lending, librarian, book, member, registered " +
                    "WHERE returning.lend_id = lending.id " +
                    "AND lending.book_id = book.id " +
                    "AND returning.librarian_id = librarian.reg_number " +
                    "AND lending.member_reg_number = registered.registration_number " +
                    "AND registered.member_id = member.id";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(returnSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        String fullName = result.getString("firstname")+" "+result.getString("middlename")+" "+result.getString("lastname");
                        returnList.add(new Returning(result.getLong("lend_id"),
                            result.getString("class_number"), result.getString("title"),
                            result.getString("reg"), fullName,
                            result.getString("reg_number"), result.getString("lib_fname"),
                            result.getString("return_date")    
                        ));
                    }                    
                    return returnList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
        }
        return null;
    }
    
    //Method to get authors for a book
    public ObservableList<Book> getBookAuthor(ObservableList<Book> bookList, int i){
        ObservableList<ObservableList<Author>> bookAuthors = FXCollections.observableArrayList();
        ObservableList<Author> authors = FXCollections.observableArrayList();
        ObservableList<Book> booksWithAuthors = FXCollections.observableArrayList();
        String getAuthorsSql = "SELECT author.id as authorId, author.firstname, author.middlename, author.lastname "
                + "FROM author, book, book_author "
                + "WHERE book.class_number = ? "
                + "AND author.id = book_author.author_id "
                + "AND book_id = book_author.book_id";
        try{
            con = DBCon.getConnection();
            
            for(Book b: bookList){
                statement = con.prepareStatement(getAuthorsSql);
                statement.setString(1, b.getClassNumber());
                result = statement.executeQuery();
                while(result.next()){
                    Author author = new Author(result.getLong("authorId"),
                        result.getString("firstname"),result.getString("middlename"), result.getString("lastname"));
                    authors.add(author);
                }
                bookAuthors.add(authors);
                Book bookWithAuthor = new Book(b.getId(), b.getTitle(), bookAuthors);
                booksWithAuthors.add(bookWithAuthor);
            }
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);            
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
        }        
        
        return booksWithAuthors;
    }
    
    //Method to get registered members full information
    public ObservableList<T> getMembersFull(String table, String status){
        switch(table){
            case ADULT_TABLE:
                ObservableList regAdult = FXCollections.observableArrayList();
                String sql = "SELECT member.id as member_id, member.firstname, member.middlename, member.lastname, member.postal_addr, " +
                    "member.street, member.region, member.phone1, member.phone2, " +
                    "member.type_of_id, member.id_number, member.status, member.photo as member_photo, member_adult.house_number, registered.registration_number, " +
                    "librarian.reg_number as reg, librarian.firstname as libname, receipt, reg_date, end_date, office_name, sponsor_name, title, reference_no " +
                    "FROM member, librarian, member_adult, registered, registration, sponsor " +
                    "WHERE member.id = member_adult.member_id " +
                    "AND librarian.reg_number = registered.librarian_id " +
                    "AND member.id = registered.member_id " +
                    "AND registered.registration_number = registration.member_reg_number " +
                    "AND registered.registration_number = sponsor.member_reg_number " +
                    "AND member.status = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, status);
                    result = statement.executeQuery();
                    while(result.next()){
                        Adult adult = new Adult(
                        result.getLong("member_id"), result.getString("registration_number"),
                        result.getString("firstname"),result.getString("middlename"),result.getString("lastname"),
                        result.getString("postal_addr"),result.getString("house_number"),result.getString("phone1"),
                        result.getString("phone2"),result.getString("type_of_id"), result.getString("id_number"),
                        result.getString("street"),result.getString("region"),result.getString("status"),
                        result.getString("receipt"),result.getString("reg_date"), result.getString("end_date"),
                        result.getString("reg"), result.getString("libname"),
                        result.getBytes("member_photo"),result.getString("office_name"),result.getString("sponsor_name"),
                        result.getString("title"), result.getString("reference_no")                                
                        );
                        regAdult.add(adult);
                    }
                    return regAdult;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case SECONDARY_TABLE:
                ObservableList regSec = FXCollections.observableArrayList();
                String secSql = "SELECT member.id as member_id, member.firstname, member.middlename, member.lastname, member.postal_addr, member.street, member.region, " +
                    "member.type_of_id, member.id_number, member.phone1, member.phone2, member.status, member.photo as member_photo, school_address, school_name, class, " +
                    "registered.registration_number, " +
                    "librarian.reg_number as reg, librarian.firstname as libname, receipt, reg_date, end_date " +
                    "FROM member, librarian, member_sec, registered, registration " +
                    "WHERE member.id = member_sec.member_id " +
                    "AND librarian.reg_number = registered.librarian_id " +
                    "AND member.id = registered.member_id " +
                    "AND registered.registration_number = registration.member_reg_number " +
                    "AND member.status = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(secSql);
                    statement.setString(1, status);
                    result = statement.executeQuery();
                    while(result.next()){
                        Secondary sec = new Secondary(
                        result.getLong("member_id"), result.getString("registration_number"),
                        result.getString("firstname"),result.getString("middlename"),result.getString("lastname"),
                        result.getString("postal_addr"),result.getString("phone1"),
                        result.getString("phone2"),result.getString("type_of_id"), result.getString("id_number"),
                        result.getString("street"),result.getString("region"),result.getString("status"),
                        result.getString("class"), result.getString("school_name"), result.getString("school_address"),
                        result.getString("receipt"),result.getString("reg_date"), result.getString("end_date"),
                        result.getString("reg"), result.getString("libname"),
                        result.getBytes("member_photo")                          
                        );
                        regSec.add(sec);
                    }
                    return regSec;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case PRIMARY_TABLE:
                ObservableList regPr = FXCollections.observableArrayList();
                String prSql = "SELECT member.id as member_id, member.firstname, member.middlename, member.lastname, member.postal_addr, member.street, member.region, " +
                    "member.type_of_id, member.id_number, member.status, member.photo as member_photo, school_address, school_name, class, registered.registration_number, " +
                    "p_firstname, p_middlename, p_lastname, p_photo, member.phone1, member.phone2, " +
                    "librarian.reg_number as reg, librarian.firstname as libname, receipt, reg_date, end_date " +
                    "FROM member, librarian, member_primary, registered, registration " +
                    "WHERE member.id = member_primary.member_id " +
                    "AND librarian.reg_number = registered.librarian_id " +
                    "AND member.id = registered.member_id " +
                    "AND registered.registration_number = registration.member_reg_number " +
                    "AND member.status = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(prSql);
                    statement.setString(1, status);
                    result = statement.executeQuery();
                    while(result.next()){
                        Primary pri = new Primary(
                        result.getLong("member_id"), result.getString("registration_number"),
                        result.getString("firstname"),result.getString("middlename"),result.getString("lastname"),
                        result.getString("postal_addr"),result.getString("phone1"),
                        result.getString("phone2"),result.getString("type_of_id"), result.getString("id_number"),
                        result.getString("street"),result.getString("region"),result.getString("status"),
                        result.getString("p_firstname"),result.getString("p_middlename"),result.getString("p_lastname"),
                        result.getString("school_name"), result.getString("school_address"), result.getString("class"),
                        result.getString("receipt"),result.getString("reg_date"), result.getString("end_date"),
                        result.getBytes("p_photo"),
                        result.getString("reg"), result.getString("libname"),
                        result.getBytes("member_photo")                          
                        );
                        regPr.add(pri);
                    }
                    return regPr;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
        }
        return null;
    }
    //Method to delete authors from a book
    public boolean deleteBookAuthor(Long bookId){
        String baSql = "DELETE FROM "+ BOOK_AUTHOR_TABLE + " WHERE book_id = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(baSql);
            statement.setLong(1, bookId);
            statement.execute();
            Misc.display("Taarifa za waandishi wa kitabu zimefutwa.", 0);
            log(1, user.getRegNumber(), user.getUsername(), "Amefuta waandishi wa vitabu. ("+bookId+")");
            return true;
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
            log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta waandishi wa vitabu. ("+bookId+")");
        }finally{
            try{statement.close(); con.close();}catch(SQLException sqle){}
        }
        return false;
    }
    
    //Method to update information in the database
    public boolean update(T t, String table){
        switch(table){
            case BOOK_TABLE:
                Book book = (Book) t;
                String bookSql = "UPDATE book "
                        + "SET class_number = ?, title = ?, category_id = ?, edition = ?, "
                        + "publisher_id = ?, isbn = ?, copies = ?, reference = ? "
                        + "WHERE class_number = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(bookSql);
                    statement.setString(1, book.getClassNumber());
                    statement.setString(2, book.getTitle());
                    statement.setLong(3, book.getCategoryClass().getId());
                    statement.setInt(4, book.getEdition());
                    statement.setLong(5, book.getPublisherClass().getId());
                    statement.setString(6, book.getIsbn());
                    statement.setInt(7, book.getCopies());
                    statement.setString(8, book.getReference());
                    statement.setString(9, book.getClassNumber());
                    statement.execute();
                    Misc.display("Taarifa za kitabu zimebadilishwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadili taarifa za kitabu. (" + book.getTitle() + ")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili kitabu "+sqle.getLocalizedMessage());
                }
            break;
            case AUTHOR_TABLE:
                Author author = (Author) t;
                String authorSql = "UPDATE author "
                        + "SET firstname = ?, middlename = ?, lastname = ? "
                        + "WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(authorSql);
                    statement.setString(1, author.getFirstName());
                    statement.setString(2, author.getMiddleName());
                    statement.setString(3, author.getLastName());
                    statement.setLong(4, author.getId());
                    statement.execute();
                    Misc.display("Taarifa za mwandishi zimebadiliswa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadilisha taarifa za mwandishi. (" + author.getId() + ")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili za mwandishi "+sqle.getLocalizedMessage());
                }
            break;
            case CATEGORY_TABLE:
                Category category = (Category) t;
                String categorySql = "UPDATE category "
                        + "SET name = ?, description = ? "
                        + "WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(categorySql);
                    statement.setString(1, category.getCategory());
                    statement.setString(2, category.getDescription());
                    statement.setLong(3, category.getId());
                    statement.execute();
                    Misc.display("Kategori imebadilishwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadilisha kategori. (" + category.getId() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili kategori "+sqle.getLocalizedMessage());
                }
            break;
            case PUBLISHER_TABLE:
                Publisher publisher = (Publisher) t;
                String publisherSql = "UPDATE publisher "
                        + "SET name = ? "
                        + "WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(publisherSql);
                    statement.setString(1, publisher.getPublisher());
                    statement.setLong(2, publisher.getId());
                    statement.execute();
                    Misc.display("Taarifa za mchapishaji zimebadilishwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadili taarifa za mchapishaji. (" + publisher.getId() + ")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili mchapishaji "+sqle.getLocalizedMessage());
                }
            break;
            case LIBRARIAN_TABLE:
                Librarian librarian = (Librarian) t;
                
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
                
                sql = Update.sql(LIBRARIAN_TABLE, libColsData, libCond);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, librarian.getReg());
                    statement.setString(2, librarian.getFirstName());
                    statement.setString(3, librarian.getPhone2());
                    statement.setString(4, librarian.getMiddleName());
                    statement.setBytes(5, librarian.getPhoto());
                    statement.setString(6, librarian.getLastName());
                    statement.setString(7, librarian.getPhone1());
                    statement.setString(8, librarian.getPassword());
                    statement.setString(9, librarian.getStreet());       
                    statement.setString(10, librarian.getPostalAddr());
                    statement.setLong(11, librarian.getDepId());                    
                    statement.setString(12, librarian.getRegion());                    
                    statement.setInt(13, librarian.getStatus());
                    statement.setString(14, librarian.getReg());
                    statement.execute();
                    Misc.display("Taarifa za mkutubi zimebadilishwa kikamilifu.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadilisha taarifa za mkutubi (" + librarian.getLastName() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mkutubi: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case INFO_TABLE:
                Info info = (Info) t;
                Map<String, String> infoColsData = new HashMap<>();
                infoColsData.put("institution", "?");
                infoColsData.put("phone1", "?");
                infoColsData.put("phone2", "?");
                infoColsData.put("address", "?");
                infoColsData.put("city", "?");
                infoColsData.put("email", "?");
                
                Map<String, String> infoCond = new HashMap<>();
                infoCond.put("id", "?");
                sql = Update.sql(INFO_TABLE, infoColsData, infoCond);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, info.getName());
                    statement.setString(2, info.getAddress());
                    statement.setString(3, info.getCity());
                    statement.setString(4, info.getPhone2());
                    statement.setString(5, info.getEmail());
                    statement.setString(6, info.getPhone1());
                    statement.setString(7, "1");
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadili taarifa za taasisi");
                    Misc.display("Taarifa zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kubadili taarifa za taasisi", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili taarifa: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case ADULT_TABLE:
                Adult adult = (Adult) t;
                String adultSql = "UPDATE " + MEMBER_TABLE 
                        + " SET firstname=?, middlename=?, lastname=?, postal_addr=?, phone1=?, phone2=?, type_of_id=?,"
                        + " id_number=?, street=?, region=?, status=?, photo=? " 
                        + " WHERE id = ? ";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(adultSql);
                    statement.setString(1, adult.getFirstName());
                    statement.setString(2, adult.getMiddleName());
                    statement.setString(3, adult.getLastName());
                    statement.setString(4, adult.getPostal());
                    statement.setString(5, adult.getPhone1());
                    statement.setString(6, adult.getPhone2());
                    statement.setString(7, adult.getIdType());
                    statement.setString(8, adult.getIdNumber());
                    statement.setString(9, adult.getStreet());
                    statement.setString(10, adult.getRegion());
                    statement.setString(11, adult.getStatus());
                    statement.setBytes(12, adult.getPhoto());
                    statement.setLong(13, adult.getId());
                    boolean success = statement.execute();
                    statement.close();
                    if(success){
                        String adult2Sql = "UPDATE member_adult SET house_number=? WHERE member_id=?";
                        statement = con.prepareStatement(adult2Sql);
                        statement.setString(1, adult.getHouseNumber());
                        statement.setLong(2, adult.getId());
                        statement.execute();
                        return true;
                    }
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kubadili taarifa za mwanachama", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili taarifa za mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case SECONDARY_TABLE:
                Secondary sec = (Secondary) t;
                String secSql = "UPDATE " + MEMBER_TABLE 
                        + " SET firstname=?, middlename=?, lastname=?, postal_addr=?, phone1=?, phone2=?, type_of_id=?,"
                        + " id_number=?, street=?, region=?, status=?, photo=? " 
                        + " WHERE id = ? ";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(secSql);
                    statement.setString(1, sec.getFirstName());
                    statement.setString(2, sec.getMiddleName());
                    statement.setString(3, sec.getLastName());
                    statement.setString(4, sec.getPostal());
                    statement.setString(5, sec.getPhone1());
                    statement.setString(6, sec.getPhone2());
                    statement.setString(7, sec.getIdType());
                    statement.setString(8, sec.getIdNumber());
                    statement.setString(9, sec.getStreet());
                    statement.setString(10, sec.getRegion());
                    statement.setString(11, sec.getStatus());
                    statement.setBytes(12, sec.getPhoto());
                    statement.setLong(13, sec.getId());
                    boolean success = statement.execute();
                    statement.close();
                    if(success){
                        String sec2Sql = "UPDATE member_sec SET school_address=?,school_name=?,class=? WHERE member_id=?";
                        statement = con.prepareStatement(sec2Sql);
                        statement.setString(1, sec.getSchoolAddr());
                        statement.setString(2, sec.getSchool());
                        statement.setString(3, sec.getGrade());
                        statement.setLong(4, sec.getId());
                        statement.execute();
                        return true;
                    }
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kubadili taarifa za mwanachama", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili taarifa za mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case PRIMARY_TABLE:
                Primary primary = (Primary) t;
                String priSql = "UPDATE " + MEMBER_TABLE 
                        + " SET firstname=?, middlename=?, lastname=?, postal_addr=?, phone1=?, phone2=?, type_of_id=?,"
                        + " id_number=?, street=?, region=?, status=?, photo=? " 
                        + " WHERE id = ? ";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(priSql);
                    statement.setString(1, primary.getFirstName());
                    statement.setString(2, primary.getMiddleName());
                    statement.setString(3, primary.getLastName());
                    statement.setString(4, primary.getPostal());
                    statement.setString(5, primary.getPhone1());
                    statement.setString(6, primary.getPhone2());
                    statement.setString(7, primary.getIdType());
                    statement.setString(8, primary.getIdNumber());
                    statement.setString(9, primary.getStreet());
                    statement.setString(10, primary.getRegion());
                    statement.setString(11, primary.getStatus());
                    statement.setBytes(12, primary.getPhoto());
                    statement.setLong(13, primary.getId());
                    boolean success = statement.execute();
                    statement.close();
                    if(success){
                        String sec2Sql = "UPDATE member_primary " +
                            "SET school_address=?, school_name=?, class=?, p_firstname=?, p_middlename=?, p_lastname=?, "
                          + "p_photo=? WHERE member_id=?";
                        statement = con.prepareStatement(sec2Sql);
                        statement.setString(1, primary.getSchoolAddr());
                        statement.setString(2, primary.getSchoolName());
                        statement.setString(3, primary.getGrade());
                        statement.setString(4, primary.getParentFirstName());
                        statement.setString(5, primary.getParentMiddleName());
                        statement.setString(6, primary.getParentLastName());
                        statement.setBytes(7, primary.getParentPhoto());
                        statement.setLong(8, primary.getId());
                        statement.execute();
                        return true;
                    }
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kubadili taarifa za mwanachama", 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili taarifa za mwanachama: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
        }
        return false;       
    }
    //Method to delete information from the database
    public boolean delete(String table, Long id){
        switch(table){
            case BOOK_TABLE:
                String bookSql = "DELETE FROM book WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(bookSql);
                    statement.setLong(1, id);
                    statement.execute();
                    Misc.display("Kitabu kimefutwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amefuta kitabu.");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta kitabu "+sqle.getLocalizedMessage());
                }
            break;
            case AUTHOR_TABLE:
                String authorSql = "DELETE FROM author WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(authorSql);
                    statement.setLong(1, id);
                    statement.execute();
                    Misc.display("Mwandishi amefutwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amefuta mwandishi.");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta mwandishi "+sqle.getLocalizedMessage());
                }
            break;
            case CATEGORY_TABLE:
                String categorySql = "DELETE FROM category WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(categorySql);
                    statement.setLong(1, id);
                    statement.execute();
                    Misc.display("Kategori imefutwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amefuta kategori.");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta kategori "+sqle.getLocalizedMessage());
                }
            break;
            case PUBLISHER_TABLE:
                String publisherSql = "DELETE FROM publisher WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(publisherSql);
                    statement.setLong(1, id);
                    statement.execute();
                    Misc.display("Mchapishaji amefutwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amefuta mchapishaji.");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta mchapishaji "+sqle.getLocalizedMessage());
                }
            break;
            case LOGS_TABLE:
                String logSql = "DELETE FROM logs WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(logSql);
                    statement.setLong(1, id);
                    statement.execute();
                    Misc.display("Rekodi ya shughuli imefutwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta mchapishaji "+sqle.getLocalizedMessage());
                }
            break;
            case DEPARTMENT_TABLE:
                String depSql = "DELETE FROM department WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(depSql);
                    statement.setLong(1, id);
                    statement.execute();
                    Misc.display("Kitengo kimefutwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amefuta kitengo ("+id+")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta kitengo "+sqle.getLocalizedMessage());
                }
            break;
        }
        return false;        
    }
    //Method to get selected item from the database
    public T getSelectedItem(String table, Long id){
        switch(table){
            case LOGS_TABLE:
                String logSql = "SELECT logs.id, librarian.reg_number, librarian.firstname, librarian.middlename, librarian.lastname, "
                        + "logs.date, logs.action, logs.info "
                        + "FROM logs, librarian "
                        + "WHERE logs.id = ? AND logs.librarian_id = librarian.reg_number";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(logSql);
                    statement.setLong(1, id);
                    result = statement.executeQuery();
                    if(result.next()){
                        return (T) new Logs(result.getLong("id"),result.getString("reg_number"), 
                                result.getString("firstname")+" "+result.getString("middlename")+" "+result.getString("lastname"), 
                                result.getString("date"), result.getString("action"), result.getString("info")
                        );
                    }
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 3);
                }
            break;            
        }
        return null;
    }
    
    //Method to split log message into component parts
    public static void log(int level, String reg, String name, String action){
        String date = Misc.todayNow();
        String logData = reg + "->" + name + "->" + date + "->" + action;
        switch(level){
            case 1:
                LOGGER.log(Level.INFO, logData);
                break;
            case 2:
                LOGGER.log(Level.WARNING, logData);
                break;
            case 3:
                LOGGER.log(Level.SEVERE, logData);
                break;
        }
    }
    
    //Get the number of books borrowed by a single member
    public int getSize(String regNumber){
        int val = 0;
        String sizeSql = "SELECT COUNT(lending.id) as count "
                + "FROM lending "
                + "WHERE lending.id NOT IN (SELECT lend_id FROM returning) "
                + "AND lending.member_reg_number = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(sizeSql);
            statement.setString(1, regNumber);
            result = statement.executeQuery();
            if(result.next()){
                val = result.getInt("count");
            }     
            return val;
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}
        }
        return val;
    }
    
    //BLOCK/UNBLOCK LIBRARIAN
    public static boolean blockLibrarian(String reg, String status){
        String blockSql = "UPDATE librarian SET status = ? WHERE reg_number = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(blockSql);
            statement.setString(1, status);
            statement.setString(2, reg);
            statement.execute();
            if(status.equals("0")){
                Misc.display("Mkutubi amesitiswa", 0);
                log(1, user.getRegNumber(), user.getUsername(), "Amemsitisha mkutubi! ("+reg+")");
            }else{
                Misc.display("Mkutubi amewezeshwa", 0);
                log(1, user.getRegNumber(), user.getUsername(), "Amemwezesha mkutubi! ("+reg+")");
            }           
            return true;
        }catch(SQLException sqle){
            if(status.equals("0")){
                Misc.display("Imeshindikana kumsitisha mkutubi!", 2);
                log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kumsitisha mkutubi "+sqle.getLocalizedMessage());
            }else{
               Misc.display("Imeshindikana kumwezesha mkutubi!", 2);
                log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kumwezesha mkutubi "+sqle.getLocalizedMessage());
            }       
            
        }finally{
            try{statement.close();con.close();}catch(SQLException sqle){}
        }
        return false;
    }
    
    //Update members status
    public boolean updateStatus(String table, String status, Long id){
        String query = "UPDATE " + table + " SET status = ? WHERE id = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(query);
            statement.setString(1, status);
            statement.setLong(2, id);
            statement.execute();
            return true;
        }catch(SQLException sqle){
            Misc.display("Imeshindikana kubadili status ya mwanachama"+sqle.getLocalizedMessage(), 2);
        }finally{
            try{statement.close();con.close();}catch(SQLException sqle){}
        } 
        return false;
    }
    
    public ObservableList<T> getCountReport(String table){
        switch(table){
            case BOOK_TABLE:
                ObservableList bookReport = FXCollections.observableArrayList();
                String bookSql = "SELECT book.class_number, book.title, book.copies FROM book";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(bookSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        bookReport.add(new Book(result.getString("class_number"),
                             result.getString("title"), result.getInt("copies")
                        ));
                    }
                    return bookReport;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuonesha orodha: "+sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close();con.close();}catch(SQLException sqle){}
                } 
            break;
            case CATEGORY_TABLE:
                ObservableList catReport = FXCollections.observableArrayList();
                String catSql = "SELECT COUNT(book.id) as total, category.name "
                    + "FROM book, category  "
                    + "WHERE book.category_id = category.id GROUP BY category.name";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(catSql);
                    result = statement.executeQuery();
                    while(result.next()){
                        catReport.add(new Category(result.getString("name"), result.getInt("total")));
                    }
                    return catReport;
                }catch(SQLException sqle){
                    Misc.display("Imeshindikana kuonesha orodha: "+sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close();con.close();}catch(SQLException sqle){}
                }
            break;
        }
        return null;
    }
}

