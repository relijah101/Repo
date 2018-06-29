package morogoro_lims.controller.lending;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import morogoro_lims.model.Lending;
import morogoro_lims.model.Returning;
import morogoro_lims.model.query.Query;

public class History implements Initializable{
    @FXML private TextField searchLendingFld;
    @FXML private TableView<Lending> lendingHistoryTable;
    @FXML private TableColumn<Lending, String> lMemberRegCol, lMemberNameCol, lBookNumberCol, lBookTitleCol, lLendDateCol, lReturnDateCol;
    @FXML private TableColumn<Lending, Long> lendIdCol;   
    
    @FXML private TextField searchReturningFld;
    @FXML private TableColumn<Returning, String> rMemberRegCol, rMemberNameCol, rBookNumberCol, rBookTitleCol, rReturnDateCol;
    @FXML private TableColumn<Returning, Long> rLendIdCol;
    @FXML private TableView<Returning> returningHistoryTable;
    
    private final Query<Lending> lendQuery = new Query();
    private final Query<Returning> returnQuery = new Query();  
    private ObservableList<Lending> lendList = FXCollections.observableArrayList();
    private ObservableList<Returning> returnList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLendTable();
        initLendTableCols();
        initReturnTable();
        initReturnTableCols();
    }
    public void initLendTable(){
        lendList.clear();
        lendList = lendQuery.select(Query.LEND_TABLE, 1);
        if(lendList.isEmpty()){
            lendingHistoryTable.setPlaceholder(new Text("Hakuna rekodi"));
        }else{
            lendingHistoryTable.setItems(lendList);
        }
    }
    public void initLendTableCols(){
        lendIdCol.setCellValueFactory(new PropertyValueFactory<>("lendId"));
        lMemberRegCol.setCellValueFactory(new PropertyValueFactory<>("memberReg"));
        lMemberNameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        lBookNumberCol.setCellValueFactory(new PropertyValueFactory<>("bookNumber"));
        lBookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        lLendDateCol.setCellValueFactory(new PropertyValueFactory<>("lendDate"));
        lReturnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }
    public void initReturnTable(){
        returnList.clear();
        returnList = returnQuery.select(Query.RETURN_TABLE, 1);
        if(lendList.isEmpty()){
            returningHistoryTable.setPlaceholder(new Text("Hakuna rekodi"));
        }else{
            returningHistoryTable.setItems(returnList);
        }
    }
    public void initReturnTableCols(){
        rLendIdCol.setCellValueFactory(new PropertyValueFactory<>("returnId"));
        rMemberRegCol.setCellValueFactory(new PropertyValueFactory<>("memberReg"));
        rMemberNameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        rBookNumberCol.setCellValueFactory(new PropertyValueFactory<>("bookNumber"));
        rBookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        rReturnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }
    @FXML
    public void onSearchLending(){
        FilteredList<Lending> filteredList = new FilteredList<>(lendList, p->true);        
        searchLendingFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(lend->{
                if(newValue == null || newValue.isEmpty()) return true;
                
                String lowerCase = newValue.toLowerCase();
                if(lend.getBookNumber().contains(lowerCase)) return true;
                if(lend.getBookTitle().contains(lowerCase)) return true;
                if(lend.getMemberReg().contains(lowerCase)) return true;
                if(lend.getMemberName().contains(lowerCase)) return true;
                if(lend.getLendDate().contains(lowerCase)) return true;
                if(lend.getReturnDate().contains(lowerCase)) return true;
                if(lend.getLibReg().contains(lowerCase)) return true;
                if(lend.getLibName().contains(lowerCase)) return true;
                
                return false;
            });
        });
        SortedList<Lending> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(lendingHistoryTable.comparatorProperty());
        lendingHistoryTable.setItems(sortedList);
    }
    @FXML
    public void onSearchReturning(){
        FilteredList<Returning> filteredList = new FilteredList<>(returnList, p->true);        
        searchLendingFld.textProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(ret->{
                if(newValue == null || newValue.isEmpty()) return true;
                
                String lowerCase = newValue.toLowerCase();
                if(ret.getBookNumber().contains(lowerCase)) return true;
                if(ret.getBookTitle().contains(lowerCase)) return true;
                if(ret.getMemberReg().contains(lowerCase)) return true;
                if(ret.getMemberName().contains(lowerCase)) return true;
                if(ret.getReturnDate().contains(lowerCase)) return true;
                if(ret.getLibReg().contains(lowerCase)) return true;
                if(ret.getLibName().contains(lowerCase)) return true;
                
                return false;
            });
        });
        SortedList<Returning> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(returningHistoryTable.comparatorProperty());
        returningHistoryTable.setItems(sortedList);
    }
}
