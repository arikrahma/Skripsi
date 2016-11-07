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
public class ExcelConverter {
    
   private File pathFile;
    static XSSFRow row;
    private int rowNoIdx;
    private int colMatkulIdx;
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
     
        CellRangeAddress add;
        int colNoIdx = 0;
        ArrayList<String> dosen = new ArrayList<>();
        ArrayList<Integer> idxDosen = new ArrayList<>();
        ArrayList<Integer> colDosen = new ArrayList<>();
        ArrayList<String> location = new ArrayList<>();
        int idxNumber = 0;
        ArrayList<Integer> locationIdx = new ArrayList<>();
        outerloop :
        for (int j = 0; j < sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            for (int f = 0; f < row.getLastCellNum(); f++) {
                Cell cell = row.getCell(f);
                if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().equalsIgnoreCase("No.")) {
                    rowNoIdx = j;
                    colNoIdx = cell.getColumnIndex();
                }
                else if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().equalsIgnoreCase("Nama Mata Kuliah"))
                {
                    colMatkulIdx = cell.getColumnIndex();
                    break outerloop;
                }
                     
            }
        }
        //System.out.println("col matkul = "+colMatkulIdx);
        System.out.println("sheet = "+sheet.getLastRowNum());
        outerloop2 :
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
           //System.out.println("i = "+i);
           outerloop :
            for (int j = 0; j < row.getLastCellNum(); j++) {
                row = sheet.getRow(i);
                if (row == null)
                {
                    i = sheet.getLastRowNum();
                    break outerloop2;
                }
                //System.out.println("Jey = " + j);
                Cell cell = row.getCell(j);
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                if (cell.getColumnIndex() == colNoIdx && i > rowNoIdx + 3 && cell.getCellType() != Cell.CELL_TYPE_BLANK
                        && evaluator.evaluate(cell).getCellType() != Cell.CELL_TYPE_NUMERIC) {
                    i = sheet.getLastRowNum();
                    break outerloop2;
                }
                if (cell.getColumnIndex() == colNoIdx && i > rowNoIdx+3 
                        && cell.getCellType() == Cell.CELL_TYPE_BLANK )
                {
                    i = i + 1;
                    break outerloop;

                    //System.out.println("I = "+i);               
                }
       
                  if (cell.getRowIndex() > rowNoIdx+1 && cell.getColumnIndex() == (colNoIdx + 1)) {
                    if (cell.getCellType() == Cell.CELL_TYPE_BLANK)  
                    {
                        i = i+1;
                        break outerloop;
                    }
                    String delims = "[,. ]";
                    String[] sumary = cell.getStringCellValue().split(delims);
                    for (int l = 0; l < sumary.length; l++) {
                        if (sumary[l].equalsIgnoreCase("Mrt")) {
                            sumary[l] = "3";
                        }
                        if (sumary[l].equalsIgnoreCase("Okt")) {
                            sumary[l] = "10";
                        }
                        if (sumary[l].equalsIgnoreCase("`16")) {
                            sumary[l] = "2016";
                        } 
                    }

                    lc = LocalDate.of(Integer.parseInt(sumary[5]), Integer.parseInt(sumary[3]), Integer.parseInt(sumary[2]));
                   // System.out.println("LC = "+lc);

//                        sp = new SimpleDateFormat("EEEE, MMMM d, yyyy");
//                        String b = sumary[3] + "/" + sumary[2] + "/" + sumary[5];
//                        date = new Date(b);
                    //System.out.println(sp.format(date));
                }
                if (cell.getRowIndex() > rowNoIdx+1 && cell.getColumnIndex() == (colNoIdx + 2)) {
                    if (cell.getStringCellValue().equalsIgnoreCase("LIBUR"))
                    {   
                        i = i+1;
                        break outerloop;
                    }
                    else
                    {
                        if(cell.getStringCellValue().equalsIgnoreCase("Shift 1") 
                                || cell.getStringCellValue().equalsIgnoreCase("Shift 2"))
                        {
                            CellReference cr = new CellReference(cell.getRowIndex()+1, cell.getColumnIndex());
                            Row row2 = sheet.getRow(cr.getRow());
                            Cell c = row2.getCell(cr.getCol());
                            String delimsJam = "[-]";
                            String[] arrJam = c.getStringCellValue().split(delimsJam);
                            for (int k = 0; k < arrJam.length; k++) {
                                arrJam[k] = arrJam[k].replace('.', ':');
                            }
//                                indoFormatter = DateTimeFormatter
//                                        .ofLocalizedTime(FormatStyle.SHORT)
//                                        .withLocale(Locale.getDefault());
                            //System.out.println("I3 = " + i);
                            lt = LocalTime.parse(arrJam[0]);
                            //System.out.println(lt+"-"+lt.plusHours(2)); 
                            
                        }
                        else
                        {
                            String delimsJam = "[-]";
                            String[] arrJam = cell.getStringCellValue().split(delimsJam);
                            for (int k = 0; k < arrJam.length; k++) {
                                arrJam[k] = arrJam[k].replace('.', ':');
                            }
//                                indoFormatter = DateTimeFormatter
//                                        .ofLocalizedTime(FormatStyle.SHORT)
//                                        .withLocale(Locale.getDefault());
                            //System.out.println("I3 = " + i);
                            lt = LocalTime.parse(arrJam[0]);
                            //System.out.println(lt+"-"+lt.plusHours(2)); 
                        }
                        
                    }
                                        
                }
                if (cell.getRowIndex() > rowNoIdx+1 && cell.getColumnIndex() == colMatkulIdx ) {
                    subject = cell.getStringCellValue();
                    //System.out.println("Subject = "+subject);
                }
   
                if (cell.getRowIndex() > rowNoIdx && cell.getColumnIndex() >= colMatkulIdx+1 
                        && cell.getColumnIndex() < row.getLastCellNum()) {
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                        location.add(String.valueOf((int)cell.getNumericCellValue()));
//                        locationIdx.add(cell.getColumnIndex());
                    }
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getStringCellValue().contains(":")) {
                            String[] splt = cell.getStringCellValue().split(":");
                            String[] splt2 = splt[1].split(",");
                            for (int l = 0; l < splt2.length; l++) {
                                dosen.add(splt2[l].trim());
                                location.add("Lab");
                                //System.out.println(splt2[l] + "= lab");
                            }
                        } else  {
                            CellReference cr = new CellReference(1, cell.getColumnIndex());
                            Row row2 = sheet.getRow(cr.getRow());
                            Cell c = row2.getCell(cr.getCol());
                            if (!cell.getStringCellValue().isEmpty())
                            {
                                dosen.add(cell.getStringCellValue().trim());
                                location.add(String.valueOf((int) c.getNumericCellValue()).trim());
                            }
                            
                            //System.out.print(cell.getStringCellValue() + " Ruang =" + (int) c.getNumericCellValue() + " ");
                        }

                    }
                    if (cell.getCellType() == Cell.CELL_TYPE_BLANK && cell.getRowIndex() > 2) {
                        CellReference cr = new CellReference(cell.getRowIndex() - 1, cell.getColumnIndex());
                        Row row2 = sheet.getRow(cr.getRow());
                        Cell c = row2.getCell(cr.getCol());
                        CellReference cr2 = new CellReference(1, cell.getColumnIndex());
                        Row row3 = sheet.getRow(cr2.getRow());
                        Cell c2 = row3.getCell(cr2.getCol());
                        if (c.getStringCellValue().contains(":")) {
                            String[] splt = c.getStringCellValue().split(":");
                            String[] splt2 = splt[1].split(",");
                            for (int l = 0; l < splt2.length; l++) {
                                dosen.add("".trim());
                                location.add("");
                                //System.out.println(splt2[l] + "= lab");
                            }
                        } else {
                            if (!c.getStringCellValue().isEmpty())
                            {
                               dosen.add("");
                               location.add("");
                            }
                           
                            //System.out.print(c.getStringCellValue() + " Ruang = " + (int) c2.getNumericCellValue() + " ");
                        }
                    }
 //                   scheduleList.add(new ScheduleClass(lc, lt, lt, subject, dosen.get(j), location.get(j)));
                } 
//                System.out.println("lc = "+lc+",lt = "+lt+",subject = "+subject+",dosen = "+dosen.get(i)+",location = "+location.get(i));
//                scheduleList.add(new ScheduleClass(lc, lt, lt, subject, dosen.get(j), location.get(j)));

            }

            for (int j = 0; j < dosen.size(); j++) {
                //System.out.println("lc = "+lc+",lt = "+lt+",subject = "+subject+",dosen = "+dosen.get(j)+",location = "+location.get(j));
                scheduleList.add(new ScheduleClass(lc, lt, lt.plusHours(2), subject, dosen.get(j), location.get(j)));
            }
            dosen.clear();
            location.clear();
           
        }
        
        return Mergering(scheduleList);
    }
    
    public List<ScheduleClass> Mergering (ArrayList<ScheduleClass> scheduleList)
    {
        int count = 0;
       // System.out.println("Size "+scheduleList.size());
        ArrayList<ScheduleClass> scheduleListSmt = new ArrayList<>();
        
        for (int i = 0; i < scheduleList.size(); i++) {

           
           if (scheduleList.get(i).getDosen().isEmpty() )
           {
               scheduleListSmt.add(scheduleList.get(i));
//               scheduleList.remove(i);
           }
           
//                System.out.println("dosen= "+scheduleList.get(i).getDosen()+", Location = "+scheduleList.get(i).getLocation()+", subject = "+scheduleList.get(i).getSubject()+", ltA = "+
//                   scheduleList.get(i).getTimeAwal()+", ltB = "+scheduleList.get(i).getTimeAkhir()+", lc = "+scheduleList.get(i).getDate());
            
        }
        for (int i = 0; i < scheduleListSmt.size() ; i++) {
//            if ( scheduleList.get(i).getDate().equals(scheduleListSmt.get(i).getDate()) &&
//                    scheduleList.get(i).getTimeAwal().equals(scheduleListSmt.get(i).getTimeAwal()) 
//                    && scheduleList.get(i).getTimeAkhir().equals(scheduleListSmt.get(i).getTimeAkhir())
//                    )
//            {
//                //scheduleList.get(i).setSubject(scheduleList.get(i).getSubject()+", "+scheduleListSmt.get(i).getSubject());
//                scheduleList.set(i, new ScheduleClass(scheduleList.get(i).getDate()
//                        , scheduleList.get(i).getTimeAwal(), scheduleList.get(i).getTimeAkhir()
//                        , scheduleList.get(i).getSubject()+", "+scheduleListSmt.get(i).getSubject(),
//                        scheduleList.get(i).getDosen(), scheduleList.get(i).getLocation()));
//            }
              for (int j = 0; j < scheduleList.size(); j++) {
                  if(scheduleList.get(j).equals(scheduleListSmt.get(i)))
                  {
                      scheduleList.remove(j);
                  }
              }
              
//            System.out.println("dosen= "+scheduleListSmt.get(i).getDosen()+", Location = "+scheduleListSmt.get(i).getLocation()+", subject = "+scheduleListSmt.get(i).getSubject()+", ltA = "+
//                    scheduleListSmt.get(i).getTimeAwal()+", ltB = "+scheduleListSmt.get(i).getTimeAkhir()+", lc = "+scheduleListSmt.get(i).getDate());
//               System.out.println("dosen= "+scheduleList.get(i).getDosen()+", Location = "+scheduleList.get(i).getLocation()+", subject = "+scheduleList.get(i).getSubject()+", ltA = "+
//                    scheduleList.get(i).getTimeAwal()+", ltB = "+scheduleList.get(i).getTimeAkhir()+", lc = "+scheduleList.get(i).getDate());
        }
        for (int i = 0; i < scheduleList.size(); i++) {
            outerloop :
            for (int j = 0; j < scheduleListSmt.size(); j++) {
                if (scheduleList.get(i).getDate().equals(scheduleListSmt.get(j).getDate())
                        && scheduleList.get(i).getTimeAwal().equals(scheduleListSmt.get(j).getTimeAwal()))
                {
                    String ss = scheduleList.get(i).getSubject();
                    scheduleList.get(i).setSubject(ss+", "+scheduleListSmt.get(j).getSubject());
                    j = j + 1;
                    break outerloop;
                }
            }
        }
        return scheduleList;
    }
}
