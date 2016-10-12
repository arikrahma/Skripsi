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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
    private int rowNoIdx;
    private LocalDate lc;
    private SimpleDateFormat sp;
    private Date date;
    private DateTimeFormatter indoFormatter;
    private LocalTime lt;
    private String subject;
    public ExcelConverter(File pathFile)
    {
        this.pathFile = pathFile;
        this.rowNoIdx = 0;
    }
    
    public List<ScheduleClass> Converter() throws FileNotFoundException, IOException
    {
        ArrayList<ScheduleClass> scheduleList = new ArrayList<>();
        
        FileInputStream fis = new FileInputStream(pathFile);
        
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator < Row > rowIterator = sheet.iterator();   
        Cell cell;
        CellRangeAddress add;
        int colNoIdx = 0;
        ArrayList<String> dosen = new ArrayList<>();
        ArrayList<Integer> idxDosen = new ArrayList<>();
        ArrayList<Integer> colDosen = new ArrayList<>();
        int location = 0;
        int idxNumber = 0;
        int locationIdx = 0;
        while (rowIterator.hasNext()) 
        {
            row = (XSSFRow) rowIterator.next();
            Iterator < Cell > cellIterator = row.cellIterator();
            if (row.getRowNum() > 53) {
                break;
            }
            for (int j = 0; j < sheet.getLastRowNum(); j++) {
                for (int f = 0; f < row.getPhysicalNumberOfCells(); f++) {
                    if (String.valueOf(sheet.getRow(j).getCell(f)).contentEquals("No.")) {
                        rowNoIdx = sheet.getRow(j).getCell(f).getRowIndex();
                        colNoIdx = sheet.getRow(j).getCell(f).getColumnIndex();
                        break;
                    }
                }

            }
   
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                    add = sheet.getMergedRegion(i);
                    int col = add.getFirstColumn();
                    int rowNum = add.getFirstRow();
                    if (rowNum != 0 && rowNum == cell.getRowIndex() && col == cell.getColumnIndex()) {
                        String b = String.valueOf(sheet.getRow(rowNum).getCell(col));
                        dosen.add(b);
                        idxDosen.add(sheet.getRow(rowNum).getCell(col).getRowIndex());
                        colDosen.add(sheet.getRow(rowNum).getCell(col).getColumnIndex());
                    }
                }
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_FORMULA:
                        switch (cell.getCachedFormulaResultType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                idxNumber = (int) cell.getNumericCellValue();
                                break;
                        }
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (cell.getColumnIndex() > colNoIdx+5)
                        {
                            location = (int)cell.getNumericCellValue();
                            locationIdx = cell.getColumnIndex();
                            
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        if (!cell.getStringCellValue().equalsIgnoreCase("LIBUR"))
                        {
                            if (cell.getRowIndex() > 1 && cell.getColumnIndex() == (colNoIdx+1))
                            {   
                                String delims = "[,. ]";
                                String[] sumary = cell.getStringCellValue().split(delims);
                                for (int i = 0; i < sumary.length; i++) {
                                    if (sumary[i].equalsIgnoreCase("Mrt")) {
                                        sumary[i] = "3";
                                    }
                                }

                                lc = LocalDate.of(Integer.parseInt(sumary[5]), Integer.parseInt(sumary[3]), Integer.parseInt(sumary[2]));

                                sp = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                                String b = sumary[3] + "/" + sumary[2] + "/" + sumary[5];
                                date = new Date(b);
                                //System.out.println(sp.format(date));
                            }
                            if (cell.getRowIndex() > 1 && cell.getColumnIndex() == (colNoIdx+2))
                            {
                                String delimsJam = "[-]";
                                String[] arrJam = cell.getStringCellValue().split(delimsJam);
                                for (int i = 0; i < arrJam.length; i++) {
                                    arrJam[i] = arrJam[i].replace('.', ':');
                                }
                                indoFormatter = DateTimeFormatter
                                        .ofLocalizedTime(FormatStyle.SHORT)
                                        .withLocale(Locale.getDefault());
                                lt = LocalTime.parse(arrJam[0], indoFormatter);
                                //System.out.println(lt+"-"+lt.plusHours(2));                         
                            }
                            if (cell.getRowIndex() > 1 && cell.getColumnIndex() == (colNoIdx+5))
                                subject = cell.getStringCellValue();
                        }
                        break;
                }
                for (int i = 0; i < colDosen.size(); i++) {
                    System.out.println("locationidx:"+locationIdx);
                    System.out.println("colDosen:"+colDosen.get(i));
                    if (locationIdx == colDosen.get(i))
                    {
                        System.out.println(locationIdx);
                        scheduleList.add(new ScheduleClass(lc, lt, lt.plusHours(2), subject, dosen.get(i), String.valueOf(location)));
                    }
                }
            }
        }
        return scheduleList;
        
    }
}
