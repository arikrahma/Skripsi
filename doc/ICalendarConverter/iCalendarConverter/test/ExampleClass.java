/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
//        Iterator< Row> rowIterator = sheet1.iterator();
        int colIndex = 0;
        int ex = 0;
        int lastCol = sheet1.getLastRowNum();
        int i = 0;
        int idx = 0;
        CellRangeAddress add;

//        while (rowIterator.hasNext()) {
//            row = (XSSFRow) rowIterator.next();
//            Iterator< Cell> cellIterator = row.cellIterator();
//            //System.out.println("i = "+i+", ex:"+ex);
//
//            if (row.getRowNum() > 53) {
//                break;
//            }
////            if(lastCol-(ex+1) == i) break;
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();

//                for (int f = 0; f < sheet1.getNumMergedRegions(); f++) {
//                    add = sheet1.getMergedRegion(f);
//                    
//                    int col = add.getFirstColumn();
//                    int rowNum = add.getFirstRow();
//                    if (rowNum != 0 && rowNum == cell.getRowIndex() && colIndex == cell.getColumnIndex()) {
//                        System.out.println("col:"+col+" "+",row :"+rowNum);
//                        String b = String.valueOf(sheet1.getRow(rowNum).getCell(col));
//                        System.out.println(b);     
//                        
//                    }
//                    
//                }
//               switch (cell.getCellType()) 
//               {
//                  case Cell.CELL_TYPE_FORMULA:
//                      ex++;
//                       switch (cell.getCachedFormulaResultType()) 
//                       {
//                           case Cell.CELL_TYPE_NUMERIC:
//                           i = (int)cell.getNumericCellValue();
//                           System.out.print( 
//                           (int)cell.getNumericCellValue() + " \t\t " );
//                             
//                                 
//                           break;
//                       }
//                   break;
//                  case Cell.CELL_TYPE_NUMERIC:
//                    if (cell.getColumnIndex() >= 6)
//                    {
//                        System.out.print( 
//                        (int)cell.getNumericCellValue() + " \t\t " );
//                    }
//                    break;
//                  case Cell.CELL_TYPE_STRING:
//                   add = sheet1.getMergedRegion(cell.getRowIndex());
//              
//                   if (cell.getStringCellValue().contentEquals("No."))
//                    {
//                       colIndex = cell.getColumnIndex();
//                    }
//                   if (cell.getColumnIndex() == 1)
//                   {
//                        System.out.print(
//                        cell.getStringCellValue() + " \t\t " );
//                   }              
//                   break;
//                  
//               }
//            }
//
//            System.out.println();
//        }
             
        for (int j = 0; j < sheet1.getLastRowNum(); j++) {
           
            row = sheet1.getRow(j);
            for (int k = 0; k < row.getLastCellNum(); k++) {
                
                Cell cell = row.getCell(k);
//                if (cell.getColumnIndex() == 1)
//                {
//                    System.out.println(cell.getStringCellValue());
//                }
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                if (cell.getColumnIndex() == 0 && j > 3 && evaluator.evaluate(cell).getCellType() != Cell.CELL_TYPE_NUMERIC)
                {
                    System.exit(k);
                }    
                if (cell.getColumnIndex() >= 6 && cell.getColumnIndex() <= 11)
                {
                    if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                    {
                        System.out.print((int)cell.getNumericCellValue()+" ");
                    }
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                    {
                        if (cell.getStringCellValue().contains(":"))                            
                        {
                            String[] splt = cell.getStringCellValue().split(":");
                            String[] splt2 = splt[1].split(",");
                            for (int l = 0; l < splt2.length; l++) {
                                System.out.println(splt2[l]+"= lab");
                            }                        
                        }
                      
                        else
                        {
                            CellReference cr = new CellReference(1, cell.getColumnIndex());
                            Row row2 = sheet1.getRow(cr.getRow());
                            Cell c = row2.getCell(cr.getCol());
                            System.out.print(cell.getStringCellValue() + " Ruang ="+(int)c.getNumericCellValue()+" ");
                        }
           
                        
                    }    
                    if(cell.getCellType() == Cell.CELL_TYPE_BLANK  && cell.getRowIndex() > 2)
                    {
                        CellReference cr = new CellReference(cell.getRowIndex()-1,cell.getColumnIndex());
                        Row row2 = sheet1.getRow(cr.getRow());
                        Cell c = row2.getCell(cr.getCol());
                        CellReference cr2 = new CellReference(1,cell.getColumnIndex());
                        Row row3 = sheet1.getRow(cr2.getRow());
                        Cell c2 = row3.getCell(cr2.getCol());
                        if (c.getStringCellValue().contains(":"))                            
                        {
                            String[] splt = c.getStringCellValue().split(":");
                            String[] splt2 = splt[1].split(",");
                            for (int l = 0; l < splt2.length; l++) {
                                System.out.println(splt2[l]+"= lab");
                            }                        
                        }
                      
                        else
                        {
                            System.out.print(c.getStringCellValue()+" Ruang = "+(int)c2.getNumericCellValue()+" ");
                        }
                    }
                }
                
            }  
            System.out.println("");
        }
        System.out.println(colIndex);
        
        System.out.println(idx);

        fis.close();
    }

    public static int getNbOfMergedRegions(Sheet sheet, int row) {
        int count = 0;
        for (int i = 0; i < sheet.getNumMergedRegions(); ++i) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if (range.getFirstRow() <= row && range.getLastRow() >= row) {
                ++count;
            }
        }
        return count;
    }

//    public static void main(String[] args) {
//        File file = new File("C:\\\\Users\\\\Ariq\\\\Documents\\\\NetBeansProjects\\\\Skripsi-Jadwal-Mengawas-Ujian\\\\Contoh File\\\\Jadwal_Pengawas_ Ujian_Pak_ Pascal.xlsx");
//        try 
//        {
//            Workbook wb = WorkbookFactory.create(file);  // Line 409 for ref to the exception stack trace
//            Sheet sheet = wb.getSheetAt(0);
//            for(int row = 0; row < 20; ++row)
//            {
//                int n = getNbOfMergedRegions(sheet, row);
//                System.out.println("row [" + row + "] -> " + n + " merged regions");
//            }
//            System.out.println(wb);
//        }
//        catch (Throwable e) 
//        {
//            e.printStackTrace();
//        }
//    }
}
