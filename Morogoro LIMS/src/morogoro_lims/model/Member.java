package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Member {
    private final LongProperty id;
    private final StringProperty fName;
    private final StringProperty mName;
    private final StringProperty lName;
    private final StringProperty postal;
    private final StringProperty phone1;
    private final StringProperty phone2;
    private final StringProperty idType;
    private final StringProperty idNumber;
    private final StringProperty street;
    private final StringProperty region;
    private final StringProperty status;
    private final byte[] photo;

    public Member(Long id, String fName, String mName, String lName, String postal, String phone1, String phone2, String idType, String idNumber, String street, String region, String status, byte[] photo) {
        this.id = new SimpleLongProperty(id);
        this.fName = new SimpleStringProperty(fName);
        this.mName = new SimpleStringProperty(mName);
        this.lName = new SimpleStringProperty(lName);
        this.postal = new SimpleStringProperty(postal);
        this.phone1 = new SimpleStringProperty(phone1);
        this.phone2 = new SimpleStringProperty(phone2);
        this.idType = new SimpleStringProperty(idType);
        this.idNumber = new SimpleStringProperty(idNumber);
        this.street = new SimpleStringProperty(street);
        this.region = new SimpleStringProperty(region);
        this.status = new SimpleStringProperty(status);
        this.photo = photo;
    }
    //Getters
    public Long getId() {return id.get();}
    public String getfName() {return fName.get();}
    public String getmName() {return mName.get();}
    public String getlName() {return lName.get();}
    public String getPostal() {return postal.get();}
    public String getPhone1() {return phone1.get();}
    public String getPhone2() {return phone2.get();}
    public String getIdType() {return idType.get();}
    public String getIdNumber() {return idNumber.get();}
    public String getStreet() {return street.get();}
    public String getRegion() {return region.get();}
    public String getStatus() {return status.get();}
    public byte[] getPhoto() {return photo;}
    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getfNameProperty() {return fName;}
    public StringProperty getmNameProperty() {return mName;}
    public StringProperty getlNameProperty() {return lName;}
    public StringProperty getPostalProperty() {return postal;}
    public StringProperty getPhone1Property() {return phone1;}
    public StringProperty getPhone2Property() {return phone2;}
    public StringProperty getIdTypeProperty() {return idType;}
    public StringProperty getIdNumberProperty() {return idNumber;}
    public StringProperty getStreetProperty() {return street;}
    public StringProperty getRegionProperty() {return region;}
    public StringProperty getStatusProperty() {return status;}
}
