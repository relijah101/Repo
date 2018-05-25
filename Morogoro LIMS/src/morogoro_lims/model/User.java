package morogoro_lims.model;

public class User {
    private final String username;
    private final String regNumber;

    public User(String username, String regNumber) {
        this.username = username;
        this.regNumber = regNumber;
    }

    public String getUsername() {return username;}
    public String getRegNumber() {return regNumber;}    
    
}
