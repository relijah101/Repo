package morogoro_lims.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Primary extends Member{
    private final StringProperty parentFirstName;
    private final StringProperty parentMiddleName;
    private final StringProperty parentLastName;
    private final StringProperty schoolName;
    private final StringProperty schoolAddr;
    private final StringProperty grade;
    private byte[] parentPhoto;

    public Primary(Long id, String reg, String fName, String mName, String lName, String postal, String phone1, String phone2, 
            String idType, String idNumber, String street, String region, String status,String parentFirstName, 
            String parentMiddleName, String parentLastName, String schoolName, String schoolAddr, String grade, 
            String receipt, String startDate, String endDate,byte[] parentPhoto, byte[] photo) {
        super(id, reg, fName, mName, lName, postal, phone1, phone2, idType, idNumber, street, region, status, receipt,
                startDate, endDate, photo);
        this.parentFirstName = new SimpleStringProperty(parentFirstName);
        this.parentMiddleName = new SimpleStringProperty(parentMiddleName);
        this.parentLastName = new SimpleStringProperty(parentLastName);
        this.schoolName = new SimpleStringProperty(schoolName);
        this.schoolAddr = new SimpleStringProperty(schoolAddr);
        this.grade = new SimpleStringProperty(grade);
        this.parentPhoto = parentPhoto;
    }
    public Primary(Long id, String fName, String mName, String lName, String postal, String phone1, String phone2, 
            String idType, String idNumber, String street, String region, String parentFirstName, 
            String parentMiddleName, String parentLastName, String schoolName, String schoolAddr, String grade) {
        super(id, fName, mName, lName, postal, phone1, phone2, idType, idNumber, street, region);
        this.parentFirstName = new SimpleStringProperty(parentFirstName);
        this.parentMiddleName = new SimpleStringProperty(parentMiddleName);
        this.parentLastName = new SimpleStringProperty(parentLastName);
        this.schoolName = new SimpleStringProperty(schoolName);
        this.schoolAddr = new SimpleStringProperty(schoolAddr);
        this.grade = new SimpleStringProperty(grade);
    }
    //Getters
    public String getParentFirstName() {return parentFirstName.get();}
    public String getParentMiddleName() {return parentMiddleName.get();}
    public String getParentLastName() {return parentLastName.get();}
    public String getSchoolName() {return schoolName.get();}
    public String getSchoolAddr() {return schoolAddr.get();}
    public String getGrade() {return grade.get();}
    public byte[] getParentPhoto() {return parentPhoto;}    
    //Property getters
    public StringProperty getParentFirstNameProperty() {return parentFirstName;}
    public StringProperty getParentMiddleNameProperty() {return parentMiddleName;}
    public StringProperty getParentLastNameProperty() {return parentLastName;}
    public StringProperty getSchoolNameProperty() {return schoolName;}
    public StringProperty getSchoolAddrProperty() {return schoolAddr;}
    public StringProperty getGradeProperty() {return grade;}
}
