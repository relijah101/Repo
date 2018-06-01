package morogoro_lims.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Shortcut {
    private final StringProperty action;
    private final StringProperty key;

    public Shortcut(String action, String key) {
        this.action = new SimpleStringProperty(action);
        this.key = new SimpleStringProperty(key);
    }
    //Getters
    public String getAction() {return action.get();}
    public String getKey() {return key.get();}
    //Property getters
    public StringProperty getActionProperty() {return action;}
    public StringProperty getKeyProperty() {return key;}
}
