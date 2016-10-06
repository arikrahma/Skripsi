/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icalendarconverter;

import static icalendarconverter.ExampleClass.row;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ariq
 */
public class ExcelConverter {
    
    private File pathFile;
    static XSSFRow row;
    
    public ExcelConverter(File pathFile)
    {
        this.pathFile = pathFile;
    }
    
    public List<ScheduleClass> Converter() throws FileNotFoundException, IOException
    {
        ArrayList<ScheduleClass> scheduleList = new ArrayList<>();

        FileInputStream fis = new FileInputStream(pathFile);
        
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator < Row > rowIterator = sheet.iterator();
        Iterator < Cell > cellIterator = row.cellIterator();
        row = (XSSFRow) rowIterator.next();
        Cell cell = cellIterator.next();
        while (rowIterator.hasNext()) 
        {
            while ( cellIterator.hasNext()) 
            {
                switch(cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                    System.out.print( 
                    (int)cell.getNumericCellValue() +" \t\t " );
                    break;
                    case Cell.CELL_TYPE_STRING:
                    System.out.print(
                    cell.getStringCellValue() +" \t\t " );
                    break;
                }

            }
        }
        return scheduleList;
        
    }
}
