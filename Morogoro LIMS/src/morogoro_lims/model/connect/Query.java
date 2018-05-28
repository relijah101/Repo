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
import morogoro_lims.model.Info;
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
    public static final String DEPARTMENT_TABLE = "department";
    public static final String LIBRARIAN_TABLE = "librarian";
    public static final String MEMBER_TABLE = "member";
    public static final String REGISTERED_TABLE = "registered";
    public static final String REGISTRATION_TABLE = "registration";
    public static final String BOOK_STATUS_TABLE = "book_status";
    public static final String SPONSOR_TABLE = "sponsor";
    public static final String RETURN_TABLE = "return";
    public static final String LEND_TABLE = "lending";
    public static final String RENEW_TABLE = "renew";
    public static final String LOGS_TABLE = "logs";
    public static final String INFO_TABLE = "info";
    
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
            statement.setByte(3, Byte.parseByte("1"));
            result = statement.executeQuery();
            if(result.next()){
                User login = new User(result.getString("firstname"),result.getString("reg_number"));
                log(1, login.getRegNumber(), login.getUsername(), "Ameingia");
                return login;
            }
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
            log(3, name, pass, sqle.getLocalizedMessage());
        }finally{
            try{result.close(); statement.close(); con.close();}catch(SQLException sqle){}            
        }
        return null;
    }
    public boolean insert(T t, String table){ 
        switch(table){
            case BOOK_TABLE:
                Book book = (Book) t;
                sql = "INSERT INTO "+ BOOK_TABLE + "(class_number, title, category_id, edition, publisher_id, isbn, copies, reference) "
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
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza kitabu (" + book.getTitle() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza kitabu: "+sqle.getLocalizedMessage());
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
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza mwandishi (" + author.getFullName() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mwandishi: "+sqle.getLocalizedMessage());
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
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza kategori (" + category.getCategory() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuongeza kategori: "+sqle.getLocalizedMessage());
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
                    statement.setLong(5, librarian.getDepId());
                    statement.setString(6, librarian.getPostalAddr());
                    statement.setString(7, librarian.getPhone1());
                    statement.setString(8, librarian.getPhone2());
                    statement.setString(9, librarian.getStreet());
                    statement.setString(10, librarian.getRegion());
                    statement.setString(11, librarian.getPassword());
                    statement.setByte(12, librarian.getStatus());
                    statement.setBytes(13, librarian.getPhoto());
                    statement.execute();
                    Misc.display("Taarifa za mwandishi zimehifadhiwa.", 0);
                    log(1, user.getRegNumber(), user.getUsername(), "Ameongeza mkutubi (" + librarian.getLastName() +")");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kuhifadhi mkutubi: "+sqle.getLocalizedMessage());
                }finally{
                    try{statement.close(); con.close();}catch(SQLException sqle){}
                }
            break;  
            case DEPARTMENT_TABLE:
                Department department = (Department) t;
                String depSql = "INSERT INTO " + DEPARTMENT_TABLE + " (department) VALUES (?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(depSql);
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
                String infoSql = "INSERT INTO " + INFO_TABLE + " (institution, phone1, phone2, address, city, email) "
                        + "VALUES (?,?,?,?,?,?)";
                try{
                    con = DBCon.getConnection();
                    statement = con.prepareStatement(infoSql);
                    statement.setString(1, info.getName());
                    statement.setString(2, info.getPhone1());
                    statement.setString(3, info.getPhone2());
                    statement.setString(4, info.getAddress());
                    statement.setString(5, info.getCity());
                    statement.setString(6, info.getEmail());
                    statement.execute();
                    log(1, user.getRegNumber(), user.getUsername(), "Amerebadili taarifa za taasisi");
                    Misc.display("Taarifa zimehifadhiwa.", 0);
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kubadili taarifa: "+sqle.getLocalizedMessage());
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
                            result.getString("street"), result.getString("region"), result.getByte("status"), result.getBytes("photo")
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
                }
            break;
        }
        return null;
    }
    public boolean insertBookAuthor(Long bookId, ObservableList<Long> authorId){
        for(Long l : authorId){
        String baSql = "INSERT INTO "+ BOOK_AUTHOR_TABLE + "(book_id, author_id) VALUES (?,?)";
        try{
            con = DBCon.getConnection();
            statement = con.prepareStatement(baSql);
            statement.setLong(1, bookId);
            statement.setLong(2, l);
            statement.execute();
            Misc.display("Taarifa za waandishi wa kitabu zimehifadhiwa.", 0);
            return true;
        }catch(SQLException sqle){
            Misc.display(sqle.getLocalizedMessage(), 2);
        }finally{
            try{statement.close(); con.close();}catch(SQLException sqle){}
        }
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
                    //log(1, user.getRegNumber(), user.getUsername(), "Amefuta mchapishaji.");
                    return true;
                }catch(SQLException sqle){
                    Misc.display(sqle.getLocalizedMessage(), 2);
                    log(3, user.getRegNumber(), user.getUsername(), "Imeshindikana kufuta mchapishaji "+sqle.getLocalizedMessage());
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
    public void log(int level, String reg, String name, String action){
        String date = Misc.today();
        String logData = reg + "->" + name + "->" + date + "->" + action;
        switch(level){
            case 1:
                logger.log(Level.INFO, logData);
                break;
            case 2:
                logger.log(Level.WARNING, logData);
                break;
            case 3:
                logger.log(Level.SEVERE, logData);
                break;
        }
    }
}
