package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ParseXlsServiceImpl implements ParseXlsService {






    @Override
    public List<DailyStats> parseXml(String path,OperatorRepository operatorRepository,DailyStatsRepository dailyStatsRepository) throws Exception {

        List<DailyStats> dailyStats = new ArrayList<>();

        String date = "";

        File file = new File(path);

        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook readbookXls = null;
        XSSFWorkbook readbookXlsx = null;

        switch (checkXlsOrXlsx(path)){
            case 1: readbookXls = WorkbookFactory.create(file);
            break;
            case 2:  readbookXlsx = new XSSFWorkbook(new FileInputStream(file));
            break;
            case 3: throw new Exception("Указанный файл не является таблицей Excel. Перезагрузите файл.");
        }
// Getting the Sheet at index zero
        Sheet readsheet = null;
        if (readbookXls != null)
            readsheet = readbookXls.getSheetAt(0);
        else
            readsheet = readbookXlsx.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : readsheet) {
            System.out.println();

            String firstRowCell = dataFormatter.formatCellValue( row.getCell(0));
            if(firstRowCell.equals("Период")){
                String dateCell = dataFormatter.formatCellValue( row.getCell(1));
                date=dateCell.substring(0,dateCell.indexOf(" "));

                System.out.println(date);
            }
            if(firstRowCell.matches("^Комсистемс[0-9]{3}$"))   {
                DailyStats dStats = new DailyStats();
                dStats.setDate(getDate(date));
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    String cell_data = dataFormatter.formatCellValue(row.getCell(i));

                    switch (i){
                        case 0:
                            System.out.print(cell_data+"  ");
                            dStats.setNumber(cell_data.replaceAll("Комсистемс",""));
                            dStats.setOperator(operatorRepository.findByNumber(cell_data.replaceAll("Комсистемс","")));
                            break;
                        case 1:
                            System.out.print(cell_data+"  ");
                            break;
                        case 2:
                            System.out.print(cell_data+"  ");
                            dStats.setIncoming(Long.valueOf( cell_data));
                            break;
                        case 3:
                            System.out.print(cell_data+"  ");
                            dStats.setLost(Long.valueOf( cell_data));
                            break;
                        case 4:
                            System.out.print(cell_data+"  ");
                            dStats.setOutgoingTotal(Long.valueOf( cell_data));
                            break;
                        case 5:
                            System.out.print(cell_data+"  ");
                            dStats.setOutgoingExternal(Long.valueOf( cell_data));
                            dStats.setOutgoingInternal(dStats.getOutgoingTotal()-dStats.getOutgoingExternal());
                            break;
                        case 6:
                            System.out.print(cell_data+"  ");
                            dStats.setHolded(Long.valueOf( cell_data));
                            break;
                        case 7:
                            System.out.print(cell_data+"  ");
                            dStats.setIncomingAvrg(parseTime( cell_data));
                            break;
                        case 8:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalWorkTime(parseTime( cell_data));
                            break;
                        case 9:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalNotReadyTime(parseTime( cell_data));
                            break;
                        case 10:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalAfterCallTime(parseTime( cell_data));
                            break;
                        case 11:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalTalkingTime(parseTime( cell_data));
                            break;
                        case 12:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalIncomingTime(parseTime( cell_data));
                            break;
                        case 13:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalOutGoingTime(parseTime( cell_data));
                            break;
                        case 14:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalExternalOutGoingTime(parseTime( cell_data));
                            break;
                        case 15:
                            System.out.print(cell_data+"  ");
                            dStats.setTotalHoldTime(parseTime( cell_data));
                            break;
                        case 16:
                            System.out.print(cell_data+"  ");
                            break;
                       default:
                           break;
                    }
                }
                dailyStats.add(dStats);
            }
        }
        closeReadBooks(readbookXls,readbookXlsx);

        System.out.println(dailyStats.size());
        return dailyStats;
    }

    @Override
    public long parseTime(String time) {
        if(time.equals(""))
            return 0L;
        String[] list = time.split(":");
        if(list[0].length()>2)
            list[0]= list[0].substring(0,2);
        if(list[1].length()>2)
            list[1]= list[1].substring(0,2);
        if(list[2].length()>2)
            list[2]= list[2].substring(0,2);

//        System.out.println(list[0]+" "+list[1]+" "+list[2]);

        Long seconds = 0L;
        if(list.length==3) {
            seconds += Long.parseLong(list[0]) * 60 * 60;
//            System.out.println(seconds);
            seconds += Long.parseLong(list[1]) * 60;
//            System.out.println(seconds);
            seconds += Long.parseLong(list[2]);
//            System.out.println(seconds);
        }
        if(list.length==2)
        {
            seconds += Long.parseLong(list[0]) * 60;
            seconds += Long.parseLong(list[1]);
        }

        return seconds;
    }

    @Override
    public int checkXlsOrXlsx(String fileName) {
        System.out.println(fileName);
        if(fileName.endsWith(".xls"))
            return 1;
        if(fileName.endsWith(".xlsx"))
            return 2;
        else return 3;
    }

    public void closeReadBooks(Workbook readbookXls, XSSFWorkbook readbookXlsx) throws Exception {
        if( readbookXls!=null)
            readbookXls.close();
        if( readbookXlsx!=null)
            readbookXlsx.close();

    }

    public String getTime(Long time) {

         int seconds   = Math.toIntExact(time);
        int finalHours =  (seconds/3600);
        int secondsLeft = seconds%3600;
        int minutesFinal = secondsLeft/60;
        int secondsFinal = secondsLeft%60;
        String result = String.valueOf(finalHours)+":"+String.valueOf(minutesFinal)+":"+secondsFinal;

        return result;
    }

    public Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
        java.util.Date date1 = format.parse(date);
        Date result = new Date( date1.getTime());
        return result;
    }

    public static void main(String[] args) throws Exception {
//        ParseXlsService parseXlsService = new ParseXlsServiceImpl();
////        parseXlsService.parseXml( "C:/Users/Jeremy/Desktop/projects/Report.xlsx");
////        parseXlsService.parseTime("00:04:51");
//        Date result = ((ParseXlsServiceImpl) parseXlsService).getDate("19.02.20");
//        System.out.println(result.toString());

    }

}
