package morogoro_lims.controller;

public class PatternMatch {
    private static final String NUMBER = "[\\d]+";
    private static final String TEXT = "[\\w ]+";
    private static final String TITLE = "[\\w\\W\\d ]+";
    private static final String PHONE = "[\\d ]{10,13}";
    private static final String PWD = "(.){8,}";
    private static final String EMAIL_1 = "[a-z0-9]+@[a-z]+[.][a-z]{2,3}";
    private static final String EMAIL_2 = "[a-z0-9]+@[a-z]+[.][a-z]{2}[.][a-z]{2}";
    
    public static boolean number(String value){
        return value.matches(PatternMatch.NUMBER);
    }
    public static boolean text(String value){
        return value.matches(PatternMatch.TEXT);
    }
    public static boolean title(String value){
        return value.matches(PatternMatch.TITLE);
    }
    public static boolean phone(String value){
        return value.matches(PatternMatch.PHONE);
    }
    public static boolean pwd(String value){
        return value.matches(PatternMatch.PWD);
    }
    public static boolean email(String value){
        return (value.matches(PatternMatch.EMAIL_1) || value.matches(PatternMatch.EMAIL_2));
    }
}
