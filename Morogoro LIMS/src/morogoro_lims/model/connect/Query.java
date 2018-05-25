package morogoro_lims.model.connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.admin.Dashboard;
import morogoro_lims.controller.logging.LoggerClass;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.Department;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.Logs;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.User;

public class Query<T>{
    private static final Logger logger = Logger.getLogger(Query.class.getName());
    public static final String BOOK_TABLE = "book";
    public static final String AUTHOR_TABLE = "author";
    public static final String BOOK_AUTHOR_TABLE = "book_author";
    public static final String CATEGORY_TABLE = "category";
    public static final String PUBLISHER_TABLE = "publisher";
    public static final String LIBRARIAN_TABLE = "librarian";
    public static final String REGISTERED_TABLE = "registered";
    public static final String REGISTRATION_TABLE = "registration";
    public static final String SPONSOR_TABLE = "sponsor";
    public static final String RENEW_TABLE = "renew";
    public static final String LOGS_TABLE = "logs";
    
    private static Connection con = null;
    private static PreparedStatement statement = null;
    private static ResultSet result = null;
    private final Handler handler = new LoggerClass();
    private static String sql; 
    private final User user;
    public Query() {
        logger.addHandler(handler);
        user = Dashboard.getUser();
    }
        
    public User login(String name, String pass){
        
        String loginSql = "SELECT reg_number, firstname "
                + "FROM librarian "
                + "WHERE firstname = ? AND password = ? AND status = ?";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(loginSql);
            statement.setString(1, name);
            statement.setString(2, pass);
            statement.setString(3, "1");
            result = statement.executeQuery();
            if(result.next()){
                User login = new User(result.getString("firstname"),result.getString("reg_number"));
                logger.log(Level.INFO, login.getRegNumber() + "->" + login.getUsername() 
                + "->" + Misc.today() + "->" + "Ameingia");
                return login;
            }
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
            logger.log(Level.SEVERE, name + "->" + pass 
                + "->" + Misc.today() + "->" + " Amekosea jina au neno la siri.");
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}            
        }
        return null;
    }
    public boolean insert(T t, String table){ 
        switch(table){
            case BOOK_TABLE:
                Book book = (Book) t;
                sql = "INSERT INTO "+ BOOK_TABLE + "(class_number, title, category_id, edition, publisher_id, isbn, copies, refererence) "
                        + "VALUES (?,?,?,?,?,?,?,?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, book.getTitle());
                    statement.setString(2, book.getClassNumber());
                    statement.setLong(3, book.getCategoryId());
                    statement.setInt(4, book.getEdition());
                    statement.setLong(5, book.getPublisherId());
                    statement.setString(6, book.getIsbn());
                    statement.setInt(7, book.getCopies());
                    statement.setInt(8, (book.getReference()) ? 1 : 0);
                    statement.execute();
                    Misc.display("Taarifa za kitabu zimehifadhiwa.", 0);
                    logger.log(Level.INFO, user.getRegNumber()+"->"+ user.getRegNumber()+ "->" 
                            + Misc.today()+"->"+"Ameongeza kitabu (" + book.getTitle() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case AUTHOR_TABLE:
                Author author = (Author) t;
                sql = "INSERT INTO "+ AUTHOR_TABLE + "(firstname, middlename, lastname) VALUES (?,?,?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, author.getFirstName());
                    statement.setString(2, author.getMiddleName());
                    statement.setString(3, author.getLastName());
                    statement.execute();
                    Misc.display("Taarifa za mwandishi zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case BOOK_AUTHOR_TABLE:
                Author auth = (Author) t;
                Long bookId = Long.parseLong(table);
                sql = "INSERT INTO "+ BOOK_AUTHOR_TABLE + "(book_id, author_id) VALUES (?,?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setLong(1, bookId);
                    statement.setLong(2, auth.getId());
                    statement.execute();
                    Misc.display("Taarifa za waandishi wa kitabu zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case CATEGORY_TABLE:
                Category category = (Category) t;
                sql = "INSERT INTO "+ CATEGORY_TABLE + "(name, description) VALUES (?,?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, category.getCategory());
                    statement.setString(2, category.getDescription());
                    statement.execute();
                    Misc.display("Taarifa za kategory zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case PUBLISHER_TABLE:
                Publisher publisher = (Publisher) t;
                sql = "INSERT INTO "+ PUBLISHER_TABLE + "(name) VALUES (?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, publisher.getPublisher());
                    statement.execute();
                    logger.log(Level.INFO, user.getRegNumber() + "->" + user.getUsername()+ "->" 
                            + Misc.today() + "-> Amerekodi mchapishaji (" + publisher.getPublisher() +")");
                    Misc.display("Taarifa za mchapishaji zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case LIBRARIAN_TABLE:
                Librarian librarian = (Librarian) t;
                sql = "INSERT INTO "+ LIBRARIAN_TABLE + "(reg_number, firstname, middlename, lastname, department, postal_addr, "
                        + "phone1, phone2, street, region, password, status, photo) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(sql);
                    statement.setString(1, librarian.getReg());
                    statement.setString(2, librarian.getFirstName());
                    statement.setString(3, librarian.getMiddleName());
                    statement.setString(4, librarian.getLastName());
                    statement.setString(5, librarian.getDepartment());
                    statement.setString(6, librarian.getPostalAddr());
                    statement.setString(7, librarian.getPhone1());
                    statement.setString(8, librarian.getPhone2());
                    statement.setString(9, librarian.getStreet());
                    statement.setString(10, librarian.getRegion());
                    statement.setString(11, librarian.getPassword());
                    statement.setString(12, librarian.getStatus());
                    statement.setBytes(13, librarian.getPhoto());
                    statement.execute();
                    Misc.display("Taarifa za mwandishi zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;            
        }  
        return false;
    }
    
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
                            result.getString("street"), result.getString("region"), result.getString("status"), result.getBytes("photo")
                        );
                        libList.add(librarian);
                    }
                    return libList;
                }catch(SQLException sqle){
                    Misc.display(sqle.getMessage(), 2);
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;
            case REGISTERED_TABLE:
                
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
        }
        return null;
    }
}
