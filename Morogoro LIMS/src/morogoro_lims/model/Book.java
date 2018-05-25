package morogoro_lims.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Book {
    Category cat;
    Publisher pub;
    private final LongProperty id;
    private final StringProperty classNumber;
    private final StringProperty title;
    private final StringProperty category;
    private final IntegerProperty edition;
    private final IntegerProperty copies;
    private final StringProperty publisher;
    private final StringProperty isbn;
    private final BooleanProperty reference;
    private final ObservableList<Author> authors = FXCollections.observableArrayList();

    public Book(Long id, String classNumber, String title, Category category, int edition, int copies, Publisher publisher, String isbn, boolean reference) {
        this.cat = category;
        this.pub = publisher;
        this.id = new SimpleLongProperty(id);
        this.classNumber = new SimpleStringProperty(classNumber);
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category.getCategory());
        this.edition = new SimpleIntegerProperty(edition);
        this.copies = new SimpleIntegerProperty(copies);
        this.publisher = new SimpleStringProperty(publisher.getPublisher());
        this.isbn = new SimpleStringProperty(isbn);
        this.reference = new SimpleBooleanProperty(reference);
    }
    //Getters
    public Long getId() {return id.get();}
    public String getClassNumber() {return classNumber.get();}
    public String getTitle() {return title.get();}
    public Long getCategoryId(){return cat.getId();}
    public String getCategory() {return category.get();}
    public int getEdition() {return edition.get();}
    public int getCopies() {return copies.get();}
    public Long getPublisherId(){return pub.getId();}
    public String getPublisher() {return publisher.get();}
    public String getIsbn() {return isbn.get();}
    public boolean getReference() {return reference.get();}
    public ObservableList<Author> getAuthors() {return authors;}

    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getClassNumberProperty() {return classNumber;}
    public StringProperty getTitleProperty() {return title;}
    public StringProperty getCategoryProperty() {return category;}
    public IntegerProperty getEditionProperty() {return edition;}
    public IntegerProperty getCopiesProperty() {return copies;}
    public StringProperty getPublisherProperty() {return publisher;}
    public StringProperty getIsbnProperty() {return isbn;}
    public BooleanProperty getReferenceProperty() {return reference;}
    //public ObservableList<Author> getAuthors() {return authors;}
    
}
