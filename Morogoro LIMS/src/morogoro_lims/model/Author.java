package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * @author Elia James
 * 
 * A model class for author object
 */
public class Author {
    private LongProperty id;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty lastName;
    private final String fullName;
    
    public Author(Long id, String firstName, String middleName, String lastName) {
        this.id = new SimpleLongProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.middleName = new SimpleStringProperty(middleName);
        this.lastName = new SimpleStringProperty(lastName);
        if(middleName.isEmpty() && lastName.isEmpty()){
            fullName = firstName;
        }else if(lastName.isEmpty()){
            fullName = firstName + " " + middleName;
        }else if(middleName.isEmpty()){
            fullName = firstName + " " + lastName;
        }else{
            fullName = firstName + " " + middleName+ " " + lastName;
        }
    }
    public Author(String firstName, String middleName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.middleName = new SimpleStringProperty(middleName);
        this.lastName = new SimpleStringProperty(lastName);
        if(middleName.isEmpty() && lastName.isEmpty()){
            fullName = firstName;
        }else if(lastName.isEmpty()){
            fullName = firstName + " " + middleName;
        }else if(middleName.isEmpty()){
            fullName = firstName + " " + lastName;
        }else{
            fullName = firstName + " " + middleName+ " " + lastName;
        }
    }
    //Getters
    public Long getId() {return id.get();}
    public String getFirstName() {return firstName.get();}
    public String getMiddleName() {return middleName.get();}
    public String getLastName() {return lastName.get();}
    public String getFullName() {return fullName;}
    //Property Getters
    public LongProperty getIdProperty(){return id;}
    public StringProperty getFirstNameProperty() {return firstName;}
    public StringProperty getMiddleNameProperty() {return middleName;}
    public StringProperty getLastNameProperty() {return lastName;}
    //to string
    @Override
    public String toString(){
        return id.get()+" / " + fullName;
    }
}
