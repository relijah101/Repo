package morogoro_lims.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Info {
    private final StringProperty name;
    private final StringProperty phone1;
    private final StringProperty phone2;
    private final StringProperty city;
    private final StringProperty address;
    private final StringProperty email;

    public Info(String name, String phone1, String phone2, String city, String address, String email) {
        this.name = new SimpleStringProperty(name);
        this.phone1 = new SimpleStringProperty(phone1);
        this.phone2 = new SimpleStringProperty(phone2);
        this.city = new SimpleStringProperty(city);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
    }
    //Getters
    public String getName() {return name.get();}
    public String getPhone1() {return phone1.get();}
    public String getPhone2() {return phone2.get();}
    public String getCity() {return city.get();}
    public String getAddress() {return address.get();}
    public String getEmail() {return email.get();}
    //Property Getters
    public StringProperty getNameProperty() {return name;}
    public StringProperty getPhone1Property() {return phone1;}
    public StringProperty getPhone2Property() {return phone2;}
    public StringProperty getCityProperty() {return city;}
    public StringProperty getAddressProperty() {return address;}
    public StringProperty getEmailProperty() {return email;}
    
    
}
