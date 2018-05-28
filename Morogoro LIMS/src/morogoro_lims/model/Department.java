package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Department {
    private LongProperty id;
    private final StringProperty name;

    public Department(Long id, String name) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
    }
    public Department(String name) {
        this.name = new SimpleStringProperty(name);
    }
    //Getters
    public Long getId() {return id.get();}
    public String getName() {return name.get();}
    //Property Getters;
    public LongProperty getIdProperty() {return id;}
    public StringProperty getNameProperty() {return name;}
    //toString
    @Override
    public String toString(){
        return id.get()+"/ "+name.get();
    }
}
