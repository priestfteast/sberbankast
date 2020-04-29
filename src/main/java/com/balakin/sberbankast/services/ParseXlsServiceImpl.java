package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ParseXlsServiceImpl implements ParseXlsService {


    @Override
    public List<DailyStats> parseStatsXml(String path,OperatorRepository operatorRepository,DailyStatsRepository dailyStatsRepository) throws Exception {

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
                if(row.getLastCellNum()<10){
                    closeReadBooks(readbookXls,readbookXlsx);
                    Files.deleteIfExists(file.toPath());
                    throw new Exception("\n"+"Загруженный файл не является файлом статистики!"+"\n"+"Загрузите корректный файл"+"\n");

                }
                DailyStats dStats = new DailyStats();
                dStats.setDate(getDate(date));
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    String cell_data = dataFormatter.formatCellValue(row.getCell(i));

                    switch (i){
                        case 0:
                            System.out.print(cell_data+"  ");
                            dStats.setNumber(cell_data.replaceAll("Комсистемс",""));
                            if (operatorRepository.findByNumber(cell_data.replaceAll("Комсистемс", "")) != null) {
                                dStats.setOperator(operatorRepository.findByNumber(cell_data.replaceAll("Комсистемс", "")));
                            } else {
                                dStats.setOperator(operatorRepository.findByAdditionalNumber(cell_data.replaceAll("Комсистемс", "")));
                            }
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
                            dStats.setLost406(0L);
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
                            if(dStats.getIncoming()+dStats.getOutgoingTotal()>0) {
                                dStats.setAfterCallTimeAvrg(dStats.getTotalAfterCallTime() / (dStats.getIncoming() + dStats.getOutgoingTotal()));
                            }
                            else
                                dStats.setAfterCallTimeAvrg(0L);
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
                            if(dStats.getHolded()==0L)
                                dStats.setHoldTimeAvrg(0L);
                            else
                                dStats.setHoldTimeAvrg(dStats.getTotalHoldTime()/dStats.getHolded());
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
    public List<DailyStats> parseLostXml(String path,List<DailyStats> dailyStats) throws Exception {



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
            if(firstRowCell.equals("Начало периода")){
                String dateCell = dataFormatter.formatCellValue( row.getCell(1));
                date=dateCell.substring(0,dateCell.indexOf(" "));
                Date period = getDate(date);
                System.out.println(date);
                if(!(dailyStats.get(0).getDate().equals(period))) {
                    closeReadBooks(readbookXls,readbookXlsx);
                    Files.deleteIfExists(Paths.get(path));
                    throw new Exception("Different periods in stats file & lostCalls file");
                }
            }
            if(firstRowCell.matches("^Комсистемс[0-9]{3}$"))   {
                if(row.getLastCellNum()>5){
                    closeReadBooks(readbookXls,readbookXlsx);
                    Files.deleteIfExists(file.toPath());
                    throw new Exception("\n"+"Загруженный файл не является файлом с пропущенными!"+"\n"+"Загрузите корректный файл"+"\n");
                }
               String number = dataFormatter.formatCellValue(row.getCell(1));
                Long lost406 = Long.valueOf(dataFormatter.formatCellValue(row.getCell(3)));
                for (DailyStats stats: dailyStats
                     ) {
                    if(stats.getNumber().equals(number))
                        stats.setLost406(lost406);
                }
            }
        }
        closeReadBooks(readbookXls,readbookXlsx);


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



    public Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
        java.util.Date date1 = format.parse(date);
        Date result = new Date( date1.getTime());
        return result;
    }





    @Override
    public File saveStatsToXLS(List<String> request, List<DailyStats> stats, DailyStats totalStats) throws Exception {
        for (File myFile : new File("src\\main\\resources\\dailystats").listFiles())
            if (myFile.isFile()) myFile.delete();

            stats.add(totalStats);

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("statistics");
        CellStyle style1 = workBook.createCellStyle();
        CellStyle style2 = workBook.createCellStyle();
        Font font1 = workBook.createFont();
        Font font2 = workBook.createFont();
        font1.setBold(true);
        font2.setBold(false);
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setFont(font1);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setFont(font2);

        int rownum = 1;
        Cell cell = sheet.createRow(rownum).createCell(0);
        cell.setCellValue(request.get(0)+" - "+request.get(1));
        cell.setCellStyle(style1);
        rownum+=2;

        String[] list = {"ФИО","Смен","Доб.","Отработано","Не готов","Входящих","Ср. разговор","Ср. удержания","Ср. постобработка",
                "Исходящие", "Потеряные (406)"};
        int cellnum = 0;
        Row row = sheet.createRow(rownum++);
        for (String s : list) {
             cell = row.createCell(cellnum++);
            cell.setCellValue(s);
            cell.setCellStyle(style1);
        }


        for (DailyStats ds : stats) {
            row = sheet.createRow(rownum++);
            String lost = "0(0%)";
            if(ds.getIncoming()!=0) {
                double lostRate =  (double)ds.getLost406() * 100 / (double)ds.getIncoming();
                lost = ds.getLost406() + "(" + String.format("%.2f", lostRate) + "%)";
            }
            Object[] objArr = {ds.getOperator().getLastName()+" "+ds.getOperator().getFirstName(), ds.getOutgoingInternal(),ds.getNumber(),
                    ds.getTime(ds.getTotalWorkTime()), ds.getTime(ds.getTotalNotReadyTime()), ds.getIncoming(), ds.getTime(ds.getIncomingAvrg()),
                    ds.getTime(ds.getHoldTimeAvrg()), ds.getTime(ds.getAfterCallTimeAvrg()), ds.getOutgoingExternal(), lost};
            cellnum = 0;
            for (Object obj : objArr) {
                 cell = row.createCell(cellnum++);
                cell.setCellStyle(style2);

                if (obj instanceof Date)
                    cell.setCellValue((Date) obj);
                else if (obj instanceof Boolean)
                    cell.setCellValue((Boolean) obj);
                else if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((int) obj);
                else if (obj instanceof Long)
                    cell.setCellValue((Long) obj);
            }
        }


        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
            cell = sheet.getRow(rownum-1).getCell(i);
            if(cell.getColumnIndex()<3)
                cell.setCellValue("");
            cell.setCellStyle(style1);
        }


        File file = new File("src\\main\\resources\\dailystats\\"
                +request.get(0)+" - "+request.get(1)+".xls");

        FileOutputStream out = new FileOutputStream(file);
        workBook.write(out);
        out.close();
        System.out.println("Excel written successfully..");
        workBook.close();


        return file;
    }




}
