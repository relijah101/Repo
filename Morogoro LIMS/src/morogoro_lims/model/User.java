package morogoro_lims.model;

public class User {
    private final String username;
    private final String regNumber;
    private final String role;
    public User(String username, String regNumber, String role) {
        this.username = username;
        this.regNumber = regNumber;
        this.role = role;
    }

    public String getUsername() {return username;}
    public String getRegNumber() {return regNumber;}    
    public String getRole() {return role;} 
}
