package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Logs {
    private LongProperty id;
    private final StringProperty regNumber;
    private final StringProperty name;
    private final StringProperty date;
    private final StringProperty action;
    private final StringProperty info;

    public Logs(Long id, String regNumber, String name, String date, String action, String info) {
        this.id = new SimpleLongProperty(id);
        this.regNumber = new SimpleStringProperty(regNumber);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.action = new SimpleStringProperty(action);
        this.info = new SimpleStringProperty(info);
    }

    public Logs(String regNumber, String name, String date, String action, String info) {
        this.regNumber = new SimpleStringProperty(regNumber);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.action = new SimpleStringProperty(action);
        this.info = new SimpleStringProperty(info);
    }
    
    //Getters
    public Long getId() {return id.get();}    
    public String getRegNumber() {return regNumber.get();}
    public String getName(){return name.get();}
    public String getDate() {return date.get();}
    public String getAction() {return action.get();}
    public String getInfo() {return info.get();}    
    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getRegNumberProperty() {return regNumber;}
    public StringProperty getNameProperty(){return name;}
    public StringProperty getDateProperty() {return date;}
    public StringProperty getActionProperty() {return action;}
    public StringProperty getInfoProperty() {return info;}
}
