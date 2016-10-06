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
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
       
       jadwalList = FXCollections.observableArrayList(
                new ScheduleClass(LocalDate.now(ZoneId.systemDefault()), LocalTime.now(ZoneId.systemDefault()),"Kalkulus 2", "Mariskha", "9120")
        );
        
        jadwalTable.setItems(jadwalList);
        jadwalTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("Date"));
        jadwalTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("Time"));
        jadwalTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("Subject"));
        jadwalTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("Dosen"));
        jadwalTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("Location"));
 
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    
    
}
