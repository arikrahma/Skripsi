/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icalendarconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.fortuna.ical4j.model.ValidationException;
/**
 *
 * @author Ariq
 */
public class FXMLDocumentController implements Initializable {
     public File selectedFile;
     
    @FXML
    private Label label;
    @FXML
    private TextField txtFile;
    @FXML
    public Button bt;
    @FXML
    public TableView<ScheduleClass> jadwalTable;
    
    ObservableList<ScheduleClass> jadwalList;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {

          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          selectedFile = fileChooser.showOpenDialog(null);
          if (selectedFile != null)
          {
              txtFile.setText(selectedFile.getAbsolutePath());
          }
          else
          {
              System.out.println("File not Valid !");
          }
    }
    
    @FXML
    private void handleConvertAction(ActionEvent event) throws FileNotFoundException, IOException
    {
       ExcelConverter con = new ExcelConverter(selectedFile);
//       jadwalList = FXCollections.observableArrayList(
//                new ScheduleClass(LocalDate.now(ZoneId.systemDefault()), LocalTime.now(ZoneId.systemDefault()),LocalTime.now(ZoneId.systemDefault()),"Kalkulus 2", "Mariskha", "9120")
//        );
        jadwalList = FXCollections.observableArrayList(con.Converter());
        
        jadwalTable.setItems(jadwalList);
        jadwalTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("Date"));
        jadwalTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("timeAwal"));
         jadwalTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("timeAkhir"));
        jadwalTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("Subject"));
        jadwalTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("Dosen"));
        jadwalTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("Location"));
 
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    } 
    
    public void convertClicked() throws FileNotFoundException, IOException, SocketException, ValidationException
    {
        int selected = jadwalTable.getSelectionModel().getSelectedIndex();
        //System.out.println("selected = "+selected);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save iCal File");

        File save = fileChooser.showSaveDialog(null);
        int idx = jadwalTable.getSelectionModel().getSelectedIndex();
        //System.out.println("idx = "+idx);
        String path;
        if(save != null)
        {
            path = save.getAbsolutePath();
            CalendarConverter cc = new CalendarConverter();
            cc.calConverter(path , jadwalList.get(idx));
        }
        else
        {
            System.out.println("File corrupted !");
        }
    }
    
}