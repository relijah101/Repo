package morogoro_lims.controller.technical;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import morogoro_lims.controller.Misc;
import morogoro_lims.controller.PatternMatch;
import morogoro_lims.model.Book;
import morogoro_lims.model.Category;
import morogoro_lims.model.Publisher;
import morogoro_lims.model.query.Query;

public class AddBook implements Initializable{
    private final Query<Book> query = new Query();
    private final Query<String> categoryQuery = new Query();
    private final Query<String> publisherQuery = new Query();
    @FXML TextField classNumberFld, titleFld, editionFld, copiesFld, isbnFld;
    @FXML ComboBox categoryFld, publisherFld;
    @FXML CheckBox yes, no;
    
    ObservableList<String> category = categoryQuery.select(Query.CATEGORY_TABLE, 1);
    ObservableList<String> publisher = publisherQuery.select(Query.PUBLISHER_TABLE, 1);
    boolean reference;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryFld.setItems(category);
        publisherFld.setItems(publisher);
    }
    public void reset(){
        classNumberFld.setText("");
        titleFld.setText("");
        editionFld.setText("");
        copiesFld.setText("");
        isbnFld.setText("");
        categoryFld.getSelectionModel().select(-1);
        publisherFld.getSelectionModel().select(-1);
    }
    @FXML
    public void onCancel(){
        reset();
    }
    @FXML
    public void onYesReference(){
        reference = true;
        no.setSelected(false);
    }
    @FXML
    public void onNoReference(){
        reference = false;
        yes.setSelected(false);
    }
            
    @FXML
    public void onSave(){
        if(categoryFld.getSelectionModel().getSelectedIndex() == -1){
            Misc.display("Hakikisha umekamilisha kujaza fomu", 1);
            return;
        }
        if(publisherFld.getSelectionModel().getSelectedIndex() == -1){
            Misc.display("Hakikisha umekamilisha kujaza fomu", 1);
            return;
        }
        String number = classNumberFld.getText();
        String title = titleFld.getText();
        String edition = editionFld.getText();
        String copies = copiesFld.getText();
        String isbn = isbnFld.getText();
        
        String catObj = categoryFld.getSelectionModel().getSelectedItem().toString();
        Long catId = Long.parseLong(catObj.split("/ ")[0]);
        String cat = catObj.split("/ ")[1];     
        
        String pubObj = publisherFld.getSelectionModel().getSelectedItem().toString();
        Long pubId = Long.parseLong(pubObj.split("/")[0]);
        String pub = pubObj.split("/")[1];
        
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
        if(cat.isEmpty()){
            Misc.display("Hakikisha umechagua kategori ya kitabu", 1);
            categoryFld.requestFocus();
            return;
        }
        if(pub.isEmpty()){
            Misc.display("Hakikisha umechagua mchapishaji wa kitabu", 1);
            publisherFld.requestFocus();
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
        Category cate = new Category(catId, cat, "");
        Publisher publ = new Publisher(pubId, pub);
        Book book = new Book(number, title, cate, Integer.parseInt(edition), Integer.parseInt(copies), publ, isbn, reference);
        if(query.insert(book, Query.BOOK_TABLE)) reset();
    }
}
