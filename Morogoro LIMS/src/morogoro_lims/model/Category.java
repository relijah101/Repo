package morogoro_lims.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * @author Elia James
 * 
 * A model class for category object
 */
public class Category {
    private LongProperty id;
    private IntegerProperty totalBooks;
    private StringProperty category;
    private StringProperty description;
    
    public Category(Long id, String category, String description) {
        this.id = new SimpleLongProperty(id);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }
    public Category(String category, String description) {
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }
    public Category(String category, int totalBooks){
        this.category = new SimpleStringProperty(category);
        this.totalBooks = new SimpleIntegerProperty(totalBooks);
    }
    //Getters
    public Long getId() {return id.get();}
    public int getTotalBooks() {return totalBooks.get();}
    public String getCategory() {return category.get();}
    public String getDescription() {return description.get();}
    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getCategoryProperty() {return category;}
    public StringProperty getDescriptionProperty() {return description;}
    //toString
    @Override
    public String toString() {
        return id.get() + "/ " + category.get();
    }
}
