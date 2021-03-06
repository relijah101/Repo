package morogoro_lims.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Secondary extends Member{
    private final StringProperty grade;
    private final StringProperty school;
    private final StringProperty schoolAddr;

    public Secondary(Long id, String reg, String fName, String mName, String lName, String postal, String phone1, String phone2, 
            String idType, String idNumber, String street, String region, String status, String grade, String school, 
            String schoolAddr, String receipt, String startDate, String endDate, byte[] photo) {
        super(id, reg, fName, mName, lName, postal, phone1, phone2, idType, idNumber, street, region, status, receipt, startDate,
                endDate, photo);
        this.grade = new SimpleStringProperty(grade);
        this.school = new SimpleStringProperty(school);
        this.schoolAddr = new SimpleStringProperty(schoolAddr);
    }
    public Secondary(Long id, String fName, String mName, String lName, String postal, String phone1, String phone2, 
            String idType, String idNumber, String street, String region, String grade, String school, String schoolAddr) {
        super(id, fName, mName, lName, postal, phone1, phone2, idType, idNumber, street, region);
        this.grade = new SimpleStringProperty(grade);
        this.school = new SimpleStringProperty(school);
        this.schoolAddr = new SimpleStringProperty(schoolAddr);
    }
    //Getters
    public String getGrade() {return grade.get();}
    public String getSchool() {return school.get();}
    public String getSchoolAddr() {return schoolAddr.get();}
    //Property Getters
    public StringProperty getGradeProperty() {return grade;}
    public StringProperty getSchoolProperty() {return school;}
    public StringProperty getSchoolAddrProperty() {return schoolAddr;}  
    
    
}
