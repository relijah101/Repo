package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * @author Elia James
 * 
 * A model class for publisher object
 */
public class Publisher{
    private LongProperty id;
    private final StringProperty publisher;

    public Publisher(Long id, String publisher) {
        this.id = new SimpleLongProperty(id);
        this.publisher = new SimpleStringProperty(publisher);
    }
    public Publisher(String publisher) {
        this.publisher = new SimpleStringProperty(publisher);
    }
    //Getters
    public Long getId() {return id.get();}
    public String getPublisher() {return publisher.get();}       
    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getPublisherProperty() {return publisher;}

}
