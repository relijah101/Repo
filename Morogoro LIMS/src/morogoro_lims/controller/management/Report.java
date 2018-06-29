package morogoro_lims.controller.management;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.model.Adult;
import morogoro_lims.model.Author;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.Librarian;
import morogoro_lims.model.Primary;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.Secondary;
import morogoro_lims.model.query.Query;

public class Report implements Initializable{
    //Librarian report
    @FXML private VBox libReportBox;
    @FXML public Text libDateText;
    @FXML public TextField libSearchFld;
    @FXML public TableView<Librarian> librarianTable;
    @FXML public TableColumn<Librarian, String> libRegCol, libNameCol, libDepCol, libPhoneCol, libAddressCol, libStreetCol;
    private final Query<Librarian> libQuery = new Query<>();
    private ObservableList<Librarian> libList = FXCollections.observableArrayList();
    
    //Adult report
    @FXML VBox adultReportBox;
    @FXML Text adultDateText;
    @FXML TextField adultSearchFld;
    @FXML TableView<Adult> adultTable;
    @FXML TableColumn<Adult, String> adultNumberCol, adultNameCol, adultPhoneCol, adultPostalCol, adultHouseCol, adultStreetCol;
    @FXML TableColumn<Adult, String> adultIdTypeCol, adultIdNumberCol;
    private final Query<Adult> adultQuery = new Query<>();
    private ObservableList<Adult> adultList = FXCollections.observableArrayList();
    
    //Primary report
    @FXML VBox primaryReportBox;
    @FXML Text primaryDateText;
    @FXML TextField primarySearchFld;
    @FXML TableView<Primary> primaryTable;
    @FXML TableColumn<Primary, String> primaryNumberCol, primaryNameCol, primaryPhoneCol, primaryPostalCol, primaryParentCol;
    @FXML TableColumn<Primary, String>  primaryStreetCol;
    @FXML TableColumn<Primary, String> primarySchoolPostalCol, primarySchoolCol, primaryClassCol, primaryIdTypeCol, primaryIdNumberCol;
    private final Query<Primary> primaryQuery = new Query<>();
    private ObservableList<Primary> primaryList = FXCollections.observableArrayList();
    
    //Secondary report
    @FXML VBox secondaryReportBox;
    @FXML Text secondaryDateText;
    @FXML TextField secondarySearchFld;
    @FXML TableView<Secondary> secondaryTable;
    @FXML TableColumn<Secondary, String> secondaryNumberCol, secondaryNameCol, secondaryPhoneCol, secondaryPostalCol, secondarySchoolCol;
    @FXML TableColumn<Secondary, String> secondaryGradeCol, secondaryIdTypeCol, secondaryIdNumberCol;
    private final Query<Secondary> secQuery = new Query<>();
    private ObservableList<Secondary> secList = FXCollections.observableArrayList();
    
    //Book report
    @FXML private VBox bookReportBox;
    @FXML public Text bookDateText;
    @FXML public TextField bookSearchFld;
    @FXML public TableView<Book> bookTable;
    @FXML public TableColumn<Book, String> numberCol, titleCol, categoryCol, publisherCol, isbnCol;
    @FXML public TableColumn<Book, Integer> editionCol, copiesCol;    
    private final Query<Book> bookQuery = new Query<>();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    
    //Category report
    @FXML private VBox categoryReportBox;
    @FXML public Text categoryDateText;
    @FXML public TextField categorySearchFld;
    @FXML public TableView<Category> categoryTable;
    @FXML public TableColumn<Category, String> categoryNumberCol, categoryNameCol, categoryDescriptionCol;
    private final Query<Category> catQuery = new Query<>();
    private ObservableList<Category> catList = FXCollections.observableArrayList();
    
    //Author report
    @FXML private VBox authorReportBox;
    @FXML public Text authorDateText;
    @FXML public TextField authorSearchFld;
    @FXML public TableView<Author> authorTable;
    @FXML public TableColumn<Author, String> authorNumberCol, authorNameCol;
    private final Query<Author> authorQuery = new Query<>();
    private ObservableList<Author> authorList = FXCollections.observableArrayList();
    
    //Publisher report
    @FXML private VBox publisherReportBox;
    @FXML public Text publisherDateText;
    @FXML public TextField publisherSearchFld;
    @FXML public TableView<Publisher> publisherTable;
    @FXML public TableColumn<Publisher, String> publisherNumberCol, publisherNameCol;
    private final Query<Publisher> pubQuery = new Query<>();
    private ObservableList<Publisher> pubList = FXCollections.observableArrayList();
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {  
        //Librarian
        initLibTable();
        initLibTableCols();
        libDateText.setText(Misc.today2());
        
        //Book
        initBookTable();
        initBookTableCols();
        bookDateText.setText(Misc.today2());
        
        //Adult
        initAdultTable();
        initAdultTableCols();
        adultDateText.setText(Misc.today2());
       
//        //Primary
        initPrimaryTable();
        initPrimaryTableCols();
        primaryDateText.setText(Misc.today2());
       
        //Secondary
        initSecondaryTable();
        initSecondaryTableCols();
        secondaryDateText.setText(Misc.today2());
        
        //Author
        initAuthorTable();
        initAuthorTableCols();
        authorDateText.setText(Misc.today2());
        
        //Category
        initCategoryTable();
        initCategoryTableCols();
        categoryDateText.setText(Misc.today2());
        
        //Publisher
        initPublisherTable();
        initPublisherTableCols();
        publisherDateText.setText(Misc.today2());
        
        //Author
        initPublisherTable();
        initPublisherTableCols();
        publisherDateText.setText(Misc.today2());
    }
    
    //Library reports
    public void initLibTable(){
        libList.clear();
        libList = libQuery.select(Query.LIBRARIAN_TABLE, 1);
        if(!libList.isEmpty()){
            librarianTable.setItems(libList);
        }else{
            librarianTable.setPlaceholder(new Text("Hakuna mkutubi aliyerekodiwa."));
        }      
    }
    public void initLibTableCols(){
        libRegCol.setCellValueFactory(new PropertyValueFactory<>("reg"));
        libNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        libDepCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        libPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        libAddressCol.setCellValueFactory(new PropertyValueFactory<>("postalAddr"));
        libStreetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
    }
    @FXML
    public void onLibSearch(){
        FilteredList<Librarian> filteredList = new FilteredList<>(libList, p->true);
        libSearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(lib->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(lib.getReg().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(lib.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(lib.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(lib.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(lib.getDepartment().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(lib.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(lib.getPhone1().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Librarian> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(librarianTable.comparatorProperty());
        librarianTable.setItems(sortedList);
    }
    @FXML
    public void onLibPrint(){
        Misc.printPageSetup(libReportBox);
    }
    
    //Book reports
    public void initBookTable(){
        bookList.clear();
        bookList = bookQuery.select(Query.BOOK_TABLE, 1);
        if(!bookList.isEmpty()){
            bookTable.setItems(bookList);
        }else{
            bookTable.setPlaceholder(new Text("Hakuna kitabu kilichorekodiwa."));
        }
    }
    public void initBookTableCols(){
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("classNumber"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("copies"));
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
    }
    @FXML
    public void onBookSearch(){
        FilteredList<Book> filteredList = new FilteredList<>(bookList, p->true);
        bookSearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(book->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(book.getClassNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(book.getTitle().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(book.getCategory().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(book.getPublisher().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(book.getIsbn().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Book> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedList);
    }
    @FXML
    public void onBookPrint(){
        Misc.printPageSetup(bookReportBox);
    }
    @FXML
    public void onBookReport(){
        Stage stage = new Stage();
        Misc.setIcon(stage);
        stage.setTitle("Tengeneza ripoti ya vitabu");
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/morogoro_lims/view/management/BookReport.fxml"));
        try{
            loader.load();
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
        
        stage.setScene(new Scene(loader.getRoot()));
        stage.showAndWait();
    }
    
    //Adult
    public void initAdultTable(){
        adultList.clear();
        adultList = adultQuery.getMembersFull(Query.ADULT_TABLE, "1");
        if(!libList.isEmpty()){
            adultTable.setItems(adultList);
        }else{
            adultTable.setPlaceholder(new Text("Hakuna mwanachama aliyerekodiwa."));
        }
       
    }
    public void initAdultTableCols(){
        adultNumberCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        adultNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        adultPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        adultPostalCol.setCellValueFactory(new PropertyValueFactory<>("postal")); 
        adultHouseCol.setCellValueFactory(new PropertyValueFactory<>("houseNumber"));
        adultStreetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
        adultIdTypeCol.setCellValueFactory(new PropertyValueFactory<>("idType"));
        adultIdNumberCol.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    }
    @FXML
    public void onAdultSearch(){
        FilteredList<Adult> filteredList = new FilteredList<>(adultList, p->true);
        adultSearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(ad->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(ad.getRegNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getStreet().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getIdType().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getStartDate().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getEndDate().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(ad.getLibName().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Adult> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(adultTable.comparatorProperty());
        adultTable.setItems(sortedList);
    }
    @FXML
    public void onAdultPrint(){
        Misc.printPageSetup(adultReportBox);
    }
    @FXML
    public void onAdultReport(){
        
    }
    
    //Secondary
    public void initSecondaryTable(){
        secList.clear();
        secList = secQuery.getMembersFull(Query.SECONDARY_TABLE, "1");
        if(!secList.isEmpty()){
            secondaryTable.setItems(secList);
        }else{
            secondaryTable.setPlaceholder(new Text("Hakuna mwanachama aliyerekodiwa."));
        }
       
    }
    public void initSecondaryTableCols(){
        secondaryNumberCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        secondaryNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        secondaryPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        secondaryPostalCol.setCellValueFactory(new PropertyValueFactory<>("schoolAddr"));
        secondarySchoolCol.setCellValueFactory(new PropertyValueFactory<>("school"));
        secondaryGradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        secondaryIdTypeCol.setCellValueFactory(new PropertyValueFactory<>("idType"));
        secondaryIdNumberCol.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    }
    @FXML
    public void onSecondarySearch(){
        FilteredList<Secondary> filteredList = new FilteredList<>(secList, p->true);
        secondarySearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(sec->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(sec.getRegNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getSchool().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getGrade().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getStartDate().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getEndDate().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(sec.getLibName().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Secondary> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(secondaryTable.comparatorProperty());
        secondaryTable.setItems(sortedList);
    }
    @FXML
    public void onSecondaryPrint(){
        Misc.printPageSetup(secondaryReportBox);
    }
    @FXML
    public void onSecondaryReport(){
        
    }
    
    //Primary
    public void initPrimaryTable(){
        primaryList.clear();
        primaryList = primaryQuery.getMembersFull(Query.PRIMARY_TABLE, "1");
        if(!primaryList.isEmpty()){
            primaryTable.setItems(primaryList);
        }else{
            primaryTable.setPlaceholder(new Text("Hakuna mwanachama aliyerekodiwa."));
        }
    }
    public void initPrimaryTableCols(){
        primaryNumberCol.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        primaryNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        primaryPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone1"));
        primaryPostalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        primaryParentCol.setCellValueFactory(new PropertyValueFactory<>("parentFullName"));
        primaryStreetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
        primarySchoolPostalCol.setCellValueFactory(new PropertyValueFactory<>("schoolAddr"));
        primarySchoolCol.setCellValueFactory(new PropertyValueFactory<>("schoolName"));
        primaryClassCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        primaryIdTypeCol.setCellValueFactory(new PropertyValueFactory<>("idType"));
        primaryIdNumberCol.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    }
    @FXML
    public void onPrimarySearch(){
        FilteredList<Primary> filteredList = new FilteredList<>(primaryList, p->true);
        primarySearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(pri->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(pri.getRegNumber().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getSchoolName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getIdType().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getGrade().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getStartDate().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getEndDate().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(pri.getLibName().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Primary> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(primaryTable.comparatorProperty());
        primaryTable.setItems(sortedList);
    }
    @FXML
    public void onPrimaryPrint(){
        Misc.printPageSetup(primaryReportBox);
    }
    @FXML
    public void onPrimaryReport(){
        
    }
    
    //Category
    public void initCategoryTable(){
        catList.clear();
        catList = catQuery.select(Query.CATEGORY_TABLE, 1);
        if(!catList.isEmpty()){
            categoryTable.setItems(catList);
        }else{
            categoryTable.setPlaceholder(new Text("Hakuna kategori iliyorekodiwa."));
        }
    }
    public void initCategoryTableCols(){
        categoryNumberCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        categoryNameCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }
    @FXML
    public void onCategorySearch(){
        FilteredList<Category> filteredList = new FilteredList<>(catList, p->true);
        categorySearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(cat->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(cat.getCategory().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Category> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        categoryTable.setItems(sortedList);
    }
    @FXML
    public void onCategoryPrint(){
        Misc.printPageSetup(categoryReportBox);
    }
    @FXML
    public void onCategoryReport(){
        Stage stage = new Stage();
        Misc.setIcon(stage);
        stage.setTitle("Tengeneza ripoti ya vitabu");
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/morogoro_lims/view/management/CategoryReport.fxml"));
        try{
            loader.load();
        }catch(IOException ioe){
            Misc.display(ioe.getLocalizedMessage(), 2);
        }
        
        stage.setScene(new Scene(loader.getRoot()));
        stage.showAndWait();
    }
    
    //Author
    public void initAuthorTable(){
        authorList.clear();
        authorList = authorQuery.select(Query.AUTHOR_TABLE, 1);
        if(!authorList.isEmpty()){
            authorTable.setItems(authorList);
        }else{
            authorTable.setPlaceholder(new Text("Hakuna mwandishi aliyerekodiwa."));
        }
    }
    public void initAuthorTableCols(){
        authorNumberCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }
    
    @FXML
    public void onAuthorSearch(){
        FilteredList<Author> filteredList = new FilteredList<>(authorList, p->true);
        authorSearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(auth->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(auth.getFirstName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(auth.getMiddleName().toLowerCase().contains(newValue.toLowerCase())) return true;
                if(auth.getLastName().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Author> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(authorTable.comparatorProperty());
        authorTable.setItems(sortedList);
    }
    @FXML
    public void onAuthorPrint(){
        Misc.printPageSetup(authorReportBox);
    }
    @FXML
    public void onAuthorReport(){
        
    }
    
    //Publisher
    public void initPublisherTable(){
        pubList.clear();
        pubList = pubQuery.select(Query.PUBLISHER_TABLE, 1);
        if(!pubList.isEmpty()){
            publisherTable.setItems(pubList);
        }else{
            publisherTable.setPlaceholder(new Text("Hakuna mchapishi aliyerekodiwa."));
        }
    }
    public void initPublisherTableCols(){
        publisherNumberCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        publisherNameCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    }
    @FXML
    public void onPublisherSearch(){
        FilteredList<Publisher> filteredList = new FilteredList<>(pubList, p->true);
        publisherSearchFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(pub->{
                if(newValue == null || newValue.isEmpty()) return true;
                if(pub.getPublisher().toLowerCase().contains(newValue.toLowerCase())) return true;
                return false;
            });
        });
        SortedList<Publisher> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(publisherTable.comparatorProperty());
        publisherTable.setItems(sortedList);
    }
    @FXML
    public void onPublisherPrint(){
        Misc.printPageSetup(publisherReportBox);
    }
    @FXML
    public void onPublisherReport(){
        
    }
}
