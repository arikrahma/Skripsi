/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icalendarconverter;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author Ariq
 */
public class ExampleClass {
    static XSSFRow row;
    public static void main(String[] args) throws Exception {
		File src = new File("C:\\Users\\Ariq\\Documents\\NetBeansProjects\\Skripsi-Jadwal-Mengawas-Ujian\\Contoh File\\Jadwal_Pengawas_ Ujian_Pak_ Pascal.xlsx");
		//File src = new File("D:\\\\Skripsi\\\\Data Baru\\\\Daftar Dosen.xlsx");
		FileInputStream fis = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		
		XSSFSheet sheet1 = wb.getSheetAt(0);
		Iterator < Row > rowIterator = sheet1.iterator();
                
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                row = (XSSFRow) rowIterator.next();
                Iterator < Cell > cellIterator = row.cellIterator();
                Cell cell = cellIterator.next();
                while (rowIterator.hasNext()) 
                {
                   
                   if (row.getRowNum()== 0 || row.getRowNum() == 1)
                   {
                       continue;
                   }
                   
                   while ( cellIterator.hasNext()) 
                   {
                      
                      //CellValue cellValue = evaluator.evaluate(cell);
                      switch(cell.getCellType())
                      {
                         case Cell.CELL_TYPE_STRING:
                         if (cell.getStringCellValue().contentEquals("No."))
                          {
                              System.out.println("True");
                          }
                          else
                          {
                              System.out.print(
                              cell.getStringCellValue() +" \t\t " );
                          }
                          break;
                      }
                     
//                      switch (evaluator.evaluateInCell(cell).getCellType()) 
//                      { 
//                         case Cell.CELL_TYPE_NUMERIC:
//                         System.out.print( 
//                         (int)cell.getNumericCellValue() +" \t\t " );
//                         break;
//                         case Cell.CELL_TYPE_STRING:
//                         System.out.print(
//                         cell.getStringCellValue() +" \t\t " );
//                         break;
//                      }
                   }
                   System.out.println();
                }
                
                fis.close();
                System.out.println("Clock");
                System.out.println(LocalTime.now()+""+LocalDate.now());
                
	}
}
