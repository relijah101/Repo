package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.query.Query;

public class EditBook implements Initializable{
    private final Query<Book> query = new Query();
    private final Query<Category> categoryQuery = new Query();
    private final Query<Publisher> publisherQuery = new Query();
    @FXML TextField classNumberFld, titleFld, editionFld, copiesFld, isbnFld;
    @FXML ComboBox<Category> categoryFld;
    @FXML ComboBox<Publisher> publisherFld;
    @FXML CheckBox yes, no;
    
    ObservableList<Category> category = categoryQuery.select(Query.CATEGORY_TABLE, 1);
    ObservableList<Publisher> publisher = publisherQuery.select(Query.PUBLISHER_TABLE, 1);
    private String reference = "0";
    private Book book;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryFld.setItems(category);
        publisherFld.setItems(publisher);
        classNumberFld.setDisable(true);
    }
    public void reset(){
        classNumberFld.setText(this.book.getClassNumber());
        titleFld.setText(this.book.getTitle());
        editionFld.setText(this.book.getEdition()+"");
        copiesFld.setText(this.book.getCopies()+"");
        isbnFld.setText(this.book.getIsbn());        
        categoryFld.getSelectionModel().select(book.getCategoryClass());
        publisherFld.getSelectionModel().select(this.book.getPublisherClass());
    }
    public void initData(Book book){
        this.book = book;
        
        classNumberFld.setText(this.book.getClassNumber());
        titleFld.setText(this.book.getTitle());
        editionFld.setText(this.book.getEdition()+"");
        copiesFld.setText(this.book.getCopies()+"");
        isbnFld.setText(this.book.getIsbn());
        categoryFld.getSelectionModel().select(this.book.getCategoryClass());
        publisherFld.getSelectionModel().select(this.book.getPublisherClass());
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onYesReference(){
        reference = "1";
        no.setSelected(false);
    }
    @FXML
    public void onNoReference(){
        reference = "0";
        yes.setSelected(false);
    }
            
    @FXML
    public void onSave(ActionEvent event){
        if(categoryFld.getSelectionModel().getSelectedItem() == null){
            Misc.display("Hakikisha umekamilisha kujaza fomu", 1);
            return;
        }
        if(publisherFld.getSelectionModel().getSelectedItem() == null){
            Misc.display("Hakikisha umekamilisha kujaza fomu", 1);
            return;
        }
        String number = classNumberFld.getText();
        String title = titleFld.getText();
        String edition = editionFld.getText();
        String copies = copiesFld.getText();
        String isbn = isbnFld.getText();
        
        if(number.isEmpty()){
            Misc.display("Hakikisha umejaza namba ya kitabu", 1);
            classNumberFld.requestFocus();
            return;
        }
        if(title.isEmpty()){
            Misc.display("Hakikisha umejaza jina la kitabu", 1);
            titleFld.requestFocus();
            return;
        }
        if(edition.isEmpty()){
            Misc.display("Hakikisha umejaza chapisho la kitabu", 1);
            editionFld.requestFocus();
            return;
        }
        if(copies.isEmpty()){
            Misc.display("Hakikisha umejaza idadi ya vitabu", 1);
            copiesFld.requestFocus();
            return;
        }
        if(isbn.isEmpty()){
            Misc.display("Hakikisha umejaza ISBN namba ya kitabu", 1);
            isbnFld.requestFocus();
            return;
        }
        if(false == PatternMatch.title(title)){
            Misc.display("Muundo wa jina la kitabu umekosewa. Tafadhali jaribu tena.", 1);
            titleFld.requestFocus();
            return;
        }
        if(false == PatternMatch.number(edition)){
            Misc.display("Muundo wa chapisho la kitabu umekosewa. Tafadhali jaribu tena.", 1);
            editionFld.requestFocus();
            return; 
        }
        if(false == PatternMatch.number(copies)){
            Misc.display("Muundo wa idadi ya kitabu umekosewa. Tafadhali jaribu tena.", 1);
            copiesFld.requestFocus();
            return; 
        }
        Category cate = categoryFld.getSelectionModel().getSelectedItem();
        Publisher publ = publisherFld.getSelectionModel().getSelectedItem();
        Book addedBook = new Book(number, title, cate, Integer.parseInt(edition), Integer.parseInt(copies), publ, isbn, reference);
        
        if(query.update(addedBook, Query.BOOK_TABLE)){
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
