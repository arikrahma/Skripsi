/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icalendarconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javax.swing.JFileChooser;
import net.fortuna.ical4j.model.ValidationException;
/**
 *
 * @author Ariq
 */
public class FXMLDocumentController implements Initializable {
    private File selectedFile;
     
    @FXML
    private Label label;
    @FXML
    private TextField txtFile;
    @FXML
    private TextField filterTxt;
    @FXML
    private TableView<ScheduleClass> jadwalTable;
    
    ObservableList<ScheduleClass> jadwalList;
    ObservableList<ScheduleClass> filteredData = FXCollections.observableArrayList();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {

          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          
          //Set extension filter
          FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
          FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
          fileChooser.getExtensionFilters().add(extFilter);
          fileChooser.getExtensionFilters().add(extFilter2);
          selectedFile = fileChooser.showOpenDialog(null);
         
          if (selectedFile != null)
          {
                 txtFile.setText(selectedFile.getAbsolutePath());
          }
          else 
          {
             
          }
    }
    
    @FXML
    private void handleConvertAction(ActionEvent event) throws FileNotFoundException, IOException
    {
       ExcelConverter con = new ExcelConverter(selectedFile);
        jadwalList = FXCollections.observableArrayList(con.Converter());
        
        
        jadwalTable.setItems(jadwalList);
        jadwalTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("Date"));
        jadwalTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("timeAwal"));
        jadwalTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("timeAkhir"));
        jadwalTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("Subject"));
        jadwalTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("Dosen"));
        jadwalTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("Location"));
        
        filteredData.addAll(jadwalList);
        
        jadwalList.addListener( new ListChangeListener<ScheduleClass>()
        {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ScheduleClass> change)
            {
                updateFilteredData();
            }
        });
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        
    } 
    
    public void convertClicked() throws FileNotFoundException, 
            IOException, SocketException, ValidationException
    {
        ScheduleClass selected = jadwalTable.getSelectionModel().getSelectedItem();
        //System.out.println("selected = "+selected);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save iCal File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("iCalendar files (*.ics)", "*.ics");
        fileChooser.getExtensionFilters().add(extFilter);
        File save = fileChooser.showSaveDialog(null);
        
        int idx = jadwalTable.getSelectionModel().getSelectedIndex();
        //System.out.println("idx = "+idx);
        String path;
        if(save != null)
        {
            path = save.getAbsolutePath();
            CalendarConverter cc = new CalendarConverter();
            cc.calConverter(path , selected);
        }
        else
        {
            System.out.println("Canceled !");
        }
    }
    @FXML
    private void filterConvertion()
    {
        jadwalTable.setItems(filteredData);
        filterTxt.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue)
            {
                updateFilteredData();
            }
        });   
    }
    
    private void updateFilteredData()
    {
        filteredData.clear();;       
        for (ScheduleClass sc : jadwalList)
        {
            if (matchesFilter(sc))
            {
                filteredData.add(sc);
            }
        }       
        reapplyTableSortOrder();
    }
    
    private boolean matchesFilter(ScheduleClass sc)
    {
        String filterString = filterTxt.getText();
        
        if (filterString == null || filterString.isEmpty())
        {
            return true;
        }     
        String lowerCaseFilterString = filterString.toLowerCase();
        
        if (sc.getDosen().toLowerCase().indexOf(lowerCaseFilterString) != -1)
        {
            return true;
        }
        return false;
    }
    
    private void reapplyTableSortOrder()
    {
        ArrayList<TableColumn<ScheduleClass, ? >> sortOrder = new ArrayList<>(jadwalTable.getSortOrder());
        jadwalTable.getSortOrder().clear();
        jadwalTable.getSortOrder().addAll(sortOrder);
    }   
}
