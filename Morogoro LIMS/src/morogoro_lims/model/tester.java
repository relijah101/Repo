package morogoro_lims.model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
public class tester {
    public static void main(String[] args) throws DocumentException{
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("Report.pdf"));
            doc.open();
            doc.add(new Paragraph("Report"));
            doc.close();
//        JFileChooser chooser = new JFileChooser();
//        chooser.showSaveDialog(null);
//        if(chooser.getSelectedFile() != null){
//            File file = chooser.getSelectedFile();
//            new tester().backup(file);
//            System.out.println(file.getAbsolutePath());
//            System.out.println(file.getName()+".sql");
//        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void backup(File file){
//        Process p = null;
//        String cmd = "mysqldump -uroot -pmysql --add-drop-database -B library_database -r" + file.getAbsolutePath() +""+ file.getName() + ".sql";
//        try{
//            Runtime runtime = Runtime.getRuntime();
//            p = runtime.exec(cmd);
//            
//            int processComplete = p.waitFor();
//            
//            if(processComplete == 0){
//                System.out.println("Backup created successfully.");
//            }else{
//                System.out.println("Backup not created successfully.");
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
    }
    
//    public tester() throws DocumentException{
//        FileOutputStream os = null;
//        try {
//            
//            //File file = new File("/morogoro_lims");
//            //os = new FileOutputStream(file);
//            //Create a PDF Document
//            PdfDocument pdfDoc = new PdfDocument();
//            //Creating a PDF Writer
//            PdfWriter writer = PdfWriter.getInstance(pdfDoc, null);
//            //pdfDoc.add(new Paragraph());
//            //pdfDoc.add();
//            //Creating a Document
//            Document document = new Document();
//            document.close();
//            //os.close();
//        } catch (Exception ex) {
//            Logger.getLogger(tester.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
