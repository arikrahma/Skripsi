/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icalendarconverter;

import java.io.File;
import java.io.FileInputStream;
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
public class ReadExcel {
    static XSSFRow row;
    public static void main(String[] args) throws Exception {
		File src = new File("C:\\Users\\Ariq\\Documents\\NetBeansProjects\\Skripsi-Jadwal-Mengawas-Ujian\\Contoh File\\Jadwal_Pengawas_ Ujian_Pak_ Pascal.xlsx");
		//File src = new File("D:\\\\Skripsi\\\\Data Baru\\\\Daftar Dosen.xlsx");
		FileInputStream fis = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		
		XSSFSheet sheet1 = wb.getSheetAt(0);
		Iterator < Row > rowIterator = sheet1.iterator();
                
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

                // suppose your formula is in A3
                CellReference cellReference = new CellReference("A3"); 
              

                
                
                while (rowIterator.hasNext()) 
                {
                   row = (XSSFRow) rowIterator.next();
                   Iterator < Cell > cellIterator = row.cellIterator();
                   while ( cellIterator.hasNext()) 
                   {
                      Cell cell = cellIterator.next();
                      CellValue cellValue = evaluator.evaluate(cell);
                      switch (evaluator.evaluateInCell(cell).getCellType()) 
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
                   System.out.println();
                }
                fis.close();
	}
}
