package morogoro_lims.model.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import morogoro_lims.controller.Login;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.logging.LoggerClass;
import morogoro_lims.model.Adult;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.Department;
import morogoro_lims.model.Info;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.Logs;
import morogoro_lims.model.Member;
import morogoro_lims.model.Primary;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.Secondary;
import morogoro_lims.model.User;
import morogoro_lims.model.connect.DBCon;

public class Query<T>{
    private static final Logger LOGGER = Logger.getLogger(Query.class.getName());
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
    public static final String LEND_SIZE = "lend_size";
    public static final String LEND_TABLE = "lending";
    public static final String REFERENCE_ISSUE = "reference_lend";
    
    public static final String LOGS_TABLE = "logs";
    public static final String INFO_TABLE = "info";
    public static final String SHORTCUT_TABLE = "shortcuts";
    
    private static Connection con;
    private static PreparedStatement statement;
    private static ResultSet result;
    
    private Select select;
    private static String sql; 
    private final Handler handler;
    public static User user;
    
    public Query() {
        handler = new LoggerClass();
        for(Handler h: LOGGER.getHandlers()){
            if(h.equals(new ConsoleHandler())){
                LOGGER.removeHandler(h);
            }
        }
        LOGGER.addHandler(handler);
        
        user = Login.getUser();
    }  
    public User login(String name, String pass){
        Map<String, String> logMap = new HashMap<>();
        logMap.put("firstname", "?");
        logMap.put("password", "?");
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
            statement.setString(2, pass);
            statement.setInt(3, 1);
            result = statement.executeQuery();
            if(result.next()){
                user = new User(result.getString("firstname"),result.getString("reg_number"), 
                        result.getString("department"));
                log(1, user.getRegNumber(), user.getUsername(), "Ameingia");
                return user;
            }
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
            log(3, name, pass, sqle.getLocalizedMessage());
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}            
        }
        return null;
    }
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
                    statement.setLong(2, book.getPubId());
                    statement.setInt(3, (book.getReference()) ? 1 : 0);
                    statement.setInt(4, book.getCopies());
                    statement.setLong(5, book.getCatId());
                    statement.setString(6, book.getIsbn());
                    statement.setInt(7, book.getEdition());  
                    statement.setString(8, book.getTitle());       
                    statement.execute();
                    Misc.display("Taarifa za kitabu zimehifadhiwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza kitabu (" + book.getTitle() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
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
                    Misc.display(sqle.getLocalizedMessage(), 2);
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
                    Misc.display(sqle.getLocalizedMessage(), 2);
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
                    Misc.display(sqle.getLocalizedMessage(), 2);
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
                    sqle.printStackTrace();
                    Misc.display(sqle.getLocalizedMessage(), 2);
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
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza kitengo: "+sqle.getLocalizedMessage());
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
                
                sql = Insert.sql(INFO_TABLE, infoColsData);
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, info.getName());
                    statement.setString(2, info.getAddress());
                    statement.setString(3, info.getCity());
                    statement.setString(4, info.getPhone2());
                    statement.setString(5, info.getEmail());
                    statement.setString(6, info.getPhone1());
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
                    Misc.display("Imeshindikana kufanya uandikishaji", 2);
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
                    return true;
                }catch(SQLException sqle){
                    log(1, user.getRegNumber(), user.getUsername(), "Amemwandikisha mwanachama ("+adult.getRegNumber()+")");  
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
                    Misc.display("Imeshindikana kuhifadhi namba ya uandikishaji", 2);
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
                    Misc.display("Imeshindikana kufanya uandikishaji", 2);
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
                    Misc.display("Imeshindikana kuhifadhi namba ya uandikishaji", 2);
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
                    Misc.display("Imeshindikana kufanya uandikishaji", 2);
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
                        log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kkuhifadhi mwandishi kwenye kitabu: "+sqle.getLocalizedMessage());
                    }finally{
                        try{statement.close(); con.close();}catch(SQLException sqle){}
                    }
                }
            break;
            case RETURN_TABLE:
                
            break;
            case LEND_TABLE:
                
            break;
        }  
        return false;
    }
    //SELECT QUERIES
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
                                result.getInt("copies"), pub,result.getString("isbn"),result.getBoolean("reference")
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
                memberCond.put(REGISTERED_TABLE+".librarian_id", LIBRARIAN_TABLE+".reg_number");
                
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
                            result.getString("lib_fname"), result.getString("lib_mname"), result.getString("lib_lname"),
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
        }
        return null;
    }
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
    
    public boolean update(T t, String table){
        switch(table){
            case BOOK_TABLE:
                Book book = (Book) t;
                String bookSql = "UPDATE book "
                        + "SET class_number = ?, title = ?, category_id = ?, edition = ?, "
                        + "publisher_id = ?, isbn = ?, copies = ?, reference = ? "
                        + "WHERE id = ?";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(bookSql);
                    statement.setString(1, book.getClassNumber());
                    statement.setString(2, book.getTitle());
                    statement.setLong(3, book.getCategoryId());
                    statement.setInt(4, book.getEdition());
                    statement.setLong(5, book.getPublisherId());
                    statement.setString(6, book.getIsbn());
                    statement.setInt(7, book.getCopies());
                    statement.setBoolean(8, book.getReference());
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
                    Misc.display("Taarifa za mchapishaji zimefutwa.", 0);
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
                    Misc.display("Taarifa za mwandishi zimebadilishwa kikamilifu.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Amebadilisha taarifa za mkutubi (" + librarian.getLastName() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mkutubi: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
        }
        return false;       
    }
    
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
    
    public T getSelectedItem(String table, Long id){
        switch(table){
            case BOOK_TABLE:
                
            break;
            case AUTHOR_TABLE:
                
            break;
            case CATEGORY_TABLE:
                
            break;
            case PUBLISHER_TABLE:
                
            break;
            case DEPARTMENT_TABLE:
                
            break;
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
    
    //LOG 
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
    
    //GET SIZE
    public int getSize(String regNumber){
        int val = 0;
        String sizeSql = "SELECT COUNT(lending.id) as count "
                + "FROM lending, returning "
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
}
