package morogoro_lims.model;

import javafx.beans.property.LongProperty;
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
    private final StringProperty category;
    private final StringProperty description;

    public Category(Long id, String category, String description) {
        this.id = new SimpleLongProperty(id);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }
    public Category(String category, String description) {
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }
    //Getters
    public Long getId() {return id.get();}
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
