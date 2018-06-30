package morogoro_lims.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Book {
    Category cat;
    Publisher pub;
    private LongProperty id;
    private LongProperty lendId;
    private LongProperty catId;
    private LongProperty pubId;
    private StringProperty classNumber;
    private StringProperty title;
    private StringProperty category;
    private IntegerProperty edition;
    private IntegerProperty copies;
    private IntegerProperty total;
    private StringProperty publisher;    
    private StringProperty isbn;
    private StringProperty reference;
    private ObservableList<Author> authors = FXCollections.observableArrayList();
    private ObservableList<ObservableList<Author>> bookAuthors = FXCollections.observableArrayList();
    
    public Book(Long lendId){
        this.lendId = new SimpleLongProperty();
    }
    public Book(Long bookId, String title, ObservableList<ObservableList<Author>> bookAuthors){
        this.id = new SimpleLongProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.bookAuthors = bookAuthors;
    }
    public Book(String classNumber, String title, Long lendId){
        this.title = new SimpleStringProperty(title);
        this.classNumber = new SimpleStringProperty(classNumber);
        this.lendId = new SimpleLongProperty(lendId);
    }
    public Book(String classNumber, String title, int total){
        this.title = new SimpleStringProperty(title);
        this.classNumber = new SimpleStringProperty(classNumber);
        this.total = new SimpleIntegerProperty(total);
    }
    public Book(Long id, String classNumber, String title, Category category, int edition, int copies, Publisher publisher, String isbn, String reference) {
        this.cat = category;
        this.pub = publisher;
        this.id = new SimpleLongProperty(id);        
        this.classNumber = new SimpleStringProperty(classNumber);
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category.getCategory());
        this.edition = new SimpleIntegerProperty(edition);
        this.copies = new SimpleIntegerProperty(copies);
        this.publisher = new SimpleStringProperty(publisher.getPublisher());
        this.pubId = new SimpleLongProperty(publisher.getId());
        this.isbn = new SimpleStringProperty(isbn);
        this.reference = new SimpleStringProperty(reference);
    }
    public Book(String classNumber, String title, Category category, int edition, int copies, Publisher publisher, String isbn, String reference) {
        this.cat = category;
        this.pub = publisher;
        this.classNumber = new SimpleStringProperty(classNumber);
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category.getCategory());
        this.edition = new SimpleIntegerProperty(edition);
        this.copies = new SimpleIntegerProperty(copies);
        this.publisher = new SimpleStringProperty(publisher.getPublisher());
        this.pubId = new SimpleLongProperty(publisher.getId());
        this.isbn = new SimpleStringProperty(isbn);
        this.reference = new SimpleStringProperty(reference);
    }
    public Book(String classNumber, String title, Long catId, int edition, int copies, Long pubId, String isbn, String reference) {
        this.classNumber = new SimpleStringProperty(classNumber);
        this.title = new SimpleStringProperty(title);
        this.catId = new SimpleLongProperty(catId);
        this.edition = new SimpleIntegerProperty(edition);
        this.copies = new SimpleIntegerProperty(copies);
        this.pubId = new SimpleLongProperty(pubId);
        this.isbn = new SimpleStringProperty(isbn);
        this.reference = new SimpleStringProperty(reference);
    }
    public Book(String classNumber, String title, ObservableList author) {
        this.classNumber = new SimpleStringProperty(classNumber);
        this.title = new SimpleStringProperty(title);
        this.authors = author;
    }
    //Getters
    public Long getId() {return id.get();}
    public Long getLendId() {return lendId.get();}
    public String getClassNumber() {return classNumber.get();}
    public String getTitle() {return title.get();}
    public Long getCatId(){return catId.get();}
    public Long getCategoryId(){return cat.getId();}
    public String getCategory() {return category.get();}
    public int getEdition() {return edition.get();}
    public int getCopies() {return copies.get();}
    public int getTotal() {return total.get();}
    public Long getPubId() {return pubId.get();}
    public Long getPublisherId(){return pub.getId();}
    public String getPublisher() {return publisher.get();}
    public String getIsbn() {return isbn.get();}
    public String getReference() {return reference.get();}
    public Category getCategoryClass(){return cat;}
    public Publisher getPublisherClass(){return pub;}
    public ObservableList<ObservableList<Author>> getBookAuthors() {return bookAuthors;}

    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getClassNumberProperty() {return classNumber;}
    public StringProperty getTitleProperty() {return title;}
    public StringProperty getCategoryProperty() {return category;}
    public IntegerProperty getEditionProperty() {return edition;}
    public IntegerProperty getCopiesProperty() {return copies;}
    public StringProperty getPublisherProperty() {return publisher;}
    public StringProperty getIsbnProperty() {return isbn;}
    public StringProperty getReferenceProperty() {return reference;}
    //public ObservableList<Author> getAuthors() {return authors;}

    @Override
    public String toString() {
        return classNumber.get() + "\t" + title.get();
    }
    
    
}
