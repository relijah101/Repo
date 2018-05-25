package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Librarian{
    Department dep;
    private final LongProperty id;
    private final StringProperty reg;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty lastName;
    private final StringProperty department;
    private final StringProperty postalAddr;
    private final StringProperty phone1;
    private final StringProperty phone2;
    private final StringProperty password;
    private final StringProperty street;
    private final StringProperty region;
    private final StringProperty status;
    private final byte[] photo;
    

    public Librarian(Long id, String reg, String fName, String mName, String lName, Department dep, String postal, String phone1, String phone2, String password, String street, String region, String status, byte[] photo) {
        this.id = new SimpleLongProperty(id);
        this.reg = new SimpleStringProperty(reg);
        this.firstName = new SimpleStringProperty(fName);
        this.middleName = new SimpleStringProperty(mName);
        this.lastName = new SimpleStringProperty(lName);
        this.department = new SimpleStringProperty(dep.getName());
        this.postalAddr = new SimpleStringProperty(postal);
        this.phone1 = new SimpleStringProperty(phone1);
        this.phone2 = new SimpleStringProperty(phone2);
        this.password = new SimpleStringProperty(password);
        this.street = new SimpleStringProperty(street);
        this.region = new SimpleStringProperty(region);
        this.status = new SimpleStringProperty(status);
        this.photo = photo;
        this.dep = dep;
    }
    //Getters
    public Long getId() {return id.get();}
    public String getReg() {return reg.get();}
    public String getFirstName() {return firstName.get();}
    public String getMiddleName() {return middleName.get();}
    public String getLastName() {return lastName.get();}
    public Long getDepId(){return dep.getId();}
    public String getDepartment() {return department.get();}
    public String getPostalAddr() {return postalAddr.get();}
    public String getPhone1() {return phone1.get();}
    public String getPhone2() {return phone2.get();}
    public String getPassword() {return password.get();}
    public String getStreet() {return street.get();}
    public String getRegion() {return region.get();}
    public String getStatus() {return status.get();}
    public byte[] getPhoto() {return photo;}
    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getFirstNameProperty() {return reg;}
    public StringProperty getMiddleNameProperty() {return middleName;}
    public StringProperty getLastNameProperty() {return lastName;}
    public StringProperty getDepartmentProperty() {return department;}
    public StringProperty getPostalAddrProperty() {return postalAddr;}
    public StringProperty getPhone1Property() {return phone1;}
    public StringProperty getPhone2Property() {return phone2;}
    public StringProperty getPasswordProperty() {return password;}
    public StringProperty getStreetProperty() {return street;}
    public StringProperty getRegionProperty() {return region;}
    public StringProperty getStatusProperty() {return status;}    
}
