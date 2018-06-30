package morogoro_lims.controller;

public class PatternMatch {
    private static final String NUMBER = "[\\d]+";
    private static final String TEXT = "[\\w ]+";
    private static final String TITLE = "[\\w\\W\\d ]+";
    private static final String PHONE_1 = "[+][\\d ]{12,14}";
    private static final String PHONE_2 = "[0][\\d ]{10,12}";
    private static final String PWD = "(.){8,}";
    private static final String EMAIL_1 = "[a-z0-9]+@[a-z]+[.][a-z]{2,3}";
    private static final String EMAIL_2 = "[a-z0-9]+@[a-z]+[.][a-z]{2}[.][a-z]{2}";
    private static final String WEB = "[\\w\\d]+[.][a-z]{2,3}[.][a-z]{2,3}";
    
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
        return (value.matches(PatternMatch.PHONE_1) || value.matches(PatternMatch.PHONE_2));
    }
    public static boolean pwd(String value){
        return value.matches(PatternMatch.PWD);
    }
    public static boolean email(String value){
        return (value.matches(PatternMatch.EMAIL_1) || value.matches(PatternMatch.EMAIL_2));
    }
    public static boolean web(String value){
        return value.matches(PatternMatch.WEB);
    }
}
