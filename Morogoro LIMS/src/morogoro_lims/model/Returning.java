package morogoro_lims.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Returning {
    private LongProperty returnId;
    private final StringProperty bookNumber;
    private final StringProperty bookTitle;
    private final StringProperty memberReg;
    private final StringProperty memberName;
    private final StringProperty libReg;
    private final StringProperty libName;
    private final StringProperty returnDate;

    public Returning(Long returnId, String bookNumber, String bookTitle, String memberReg, String memberName, String libReg, String libName, String returnDate) {
        this.returnId = new SimpleLongProperty(returnId);
        this.bookNumber = new SimpleStringProperty(bookNumber);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.memberReg = new SimpleStringProperty(memberReg);
        this.memberName = new SimpleStringProperty(memberName);
        this.libReg = new SimpleStringProperty(libReg);
        this.libName = new SimpleStringProperty(libName);
        this.returnDate = new SimpleStringProperty(returnDate);
    }
    
    public Returning(String bookNumber, String bookTitle, String memberReg, String memberName, String libReg, String libName, String returnDate) {
        this.bookNumber = new SimpleStringProperty(bookNumber);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.memberReg = new SimpleStringProperty(memberReg);
        this.memberName = new SimpleStringProperty(memberName);
        this.libReg = new SimpleStringProperty(libReg);
        this.libName = new SimpleStringProperty(libName);
        this.returnDate = new SimpleStringProperty(returnDate);
    }

    public Long getReturnId() {return returnId.get();}
    public String getBookNumber() {return bookNumber.get();}
    public String getBookTitle() {return bookTitle.get();}
    public String getMemberReg() {return memberReg.get();}
    public String getMemberName() {return memberName.get();}
    public String getLibReg() {return libReg.get();}
    public String getLibName() {return libName.get();}
    public String getReturnDate() {return returnDate.get();}
    
    public LongProperty getReturnIdProperty() {return returnId;}
    public StringProperty getBookNumberProperty() {return bookNumber;}
    public StringProperty getBookTitleProperty() {return bookTitle;}
    public StringProperty getMemberRegProperty() {return memberReg;}
    public StringProperty getMemberNameProperty() {return memberName;}
    public StringProperty getLibRegProperty() {return libReg;}
    public StringProperty getLibNameProperty() {return libName;}
    public StringProperty getReturnDateProperty() {return returnDate;}   
    
}

