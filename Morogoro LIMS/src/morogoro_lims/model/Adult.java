package morogoro_lims.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Adult extends Member{
    private final StringProperty houseNumber;
    private StringProperty office;
    private StringProperty sponsor;
    private StringProperty title;
    private StringProperty reference;

    public Adult(Long id, String reg, String fName, String mName, String lName, String postal, String houseNumber, String phone1, 
            String phone2, String idType, String idNumber, String street, String region, String status, 
            String receipt, String startDate, String endDate, byte[] photo, String office, String sponsor, String title, String reference) {
        super(id, reg, fName, mName, lName, postal, phone1, phone2, idType, idNumber, street, region, status, receipt, 
                startDate, endDate, photo);
        this.houseNumber = new SimpleStringProperty(houseNumber);
        this.office = new SimpleStringProperty(office);
        this.sponsor = new SimpleStringProperty(sponsor);
        this.title = new SimpleStringProperty(title);
        this.reference = new SimpleStringProperty(reference);
    }
    public Adult(Long id, String fName, String mName, String lName, String postal, String houseNumber, String phone1, 
            String phone2, String idType, String idNumber, String street, String region) {
        super(id, fName, mName, lName, postal, phone1, phone2, idType, idNumber, street, region);
        this.houseNumber = new SimpleStringProperty(houseNumber);
    }
    //Getter
    public String getHouseNumber() {return houseNumber.get();}
    public String getOffice() {return office.get();}
    public String getSponsor() {return sponsor.get();}
    public String getTitle() {return title.get();}
    public String getReference() {return reference.get();}
    //Property getter
    public StringProperty getHouseNumberProperty() {return houseNumber;}  
    public StringProperty getOfficeProperty() {return office;}
    public StringProperty getSponsorProperty() {return sponsor;}
    public StringProperty getTitleProperty() {return title;}
    public StringProperty getReferenceProperty() {return reference;}
}
