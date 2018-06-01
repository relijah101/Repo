package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Member {
    private final LongProperty id;
    private StringProperty regNumber;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty lastName;
    private final StringProperty postal;
    private final StringProperty phone1;
    private final StringProperty phone2;
    private final StringProperty idType;
    private final StringProperty idNumber;
    private final StringProperty street;
    private final StringProperty region;
    private StringProperty status;
    private StringProperty receipt;
    private StringProperty startDate;
    private StringProperty endDate;
    private byte[] photo;

    public Member(Long id, String reg, String firstName, String middleName, String lastName, String postal, String phone1, String phone2, 
            String idType, String idNumber, String street, String region, String status, String receipt, String startDate, 
            String endDate, byte[] photo) {
        this.id = new SimpleLongProperty(id);
        this.regNumber = new SimpleStringProperty(reg);
        this.firstName = new SimpleStringProperty(firstName);
        this.middleName = new SimpleStringProperty(middleName);
        this.lastName = new SimpleStringProperty(lastName);
        this.postal = new SimpleStringProperty(postal);
        this.phone1 = new SimpleStringProperty(phone1);
        this.phone2 = new SimpleStringProperty(phone2);
        this.idType = new SimpleStringProperty(idType);
        this.idNumber = new SimpleStringProperty(idNumber);
        this.street = new SimpleStringProperty(street);
        this.region = new SimpleStringProperty(region);
        this.status = new SimpleStringProperty(status);
        this.receipt = new SimpleStringProperty(receipt);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.photo = photo;
    }
     public Member(Long id, String firstName, String middleName, String lastName, String postal, String phone1, String phone2, 
            String idType, String idNumber, String street, String region) {
        this.id = new SimpleLongProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.middleName = new SimpleStringProperty(middleName);
        this.lastName = new SimpleStringProperty(lastName);
        this.postal = new SimpleStringProperty(postal);
        this.phone1 = new SimpleStringProperty(phone1);
        this.phone2 = new SimpleStringProperty(phone2);
        this.idType = new SimpleStringProperty(idType);
        this.idNumber = new SimpleStringProperty(idNumber);
        this.street = new SimpleStringProperty(street);
        this.region = new SimpleStringProperty(region);
    }
    //Getters
    public Long getId() {return id.get();}
    public String getRegNumber() {return regNumber.get();}
    public String getFirstName() {return firstName.get();}
    public String getMiddleName() {return middleName.get();}
    public String getLastName() {return lastName.get();}
    public String getPostal() {return postal.get();}
    public String getPhone1() {return phone1.get();}
    public String getPhone2() {return phone2.get();}
    public String getIdType() {return idType.get();}
    public String getIdNumber() {return idNumber.get();}
    public String getStreet() {return street.get();}
    public String getRegion() {return region.get();}
    public String getStatus() {return status.get();}
    public String getReceipt() {return receipt.get();}
    public String getStartDate() {return startDate.get();}
    public String getEndDate() {return endDate.get();}
    public byte[] getPhoto() {return photo;}
    //Property Getters
    public LongProperty getIdProperty() {return id;}
    public StringProperty getRegNumberProperty() {return regNumber;}
    public StringProperty getFirstNameProperty() {return firstName;}
    public StringProperty getMiddleNameProperty() {return middleName;}
    public StringProperty getLastNameProperty() {return lastName;}
    public StringProperty getPostalProperty() {return postal;}
    public StringProperty getPhone1Property() {return phone1;}
    public StringProperty getPhone2Property() {return phone2;}
    public StringProperty getIdTypeProperty() {return idType;}
    public StringProperty getIdNumberProperty() {return idNumber;}
    public StringProperty getStreetProperty() {return street;}
    public StringProperty getRegionProperty() {return region;}
    public StringProperty getStatusProperty() {return status;}
    public StringProperty getReceiptProperty() {return receipt;}
    public StringProperty getStartDateProperty() {return startDate;}
    public StringProperty getEndDateProperty() {return endDate;}
}
