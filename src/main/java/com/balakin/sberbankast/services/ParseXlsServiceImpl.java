package com.balakin.sberbankast.services;

import com.balakin.sberbankast.SberbankastApplication;
import com.balakin.sberbankast.domain.*;
import com.balakin.sberbankast.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ParseXlsServiceImpl implements ParseXlsService {

    private final OperatorService operatorService;
    private final BonusRepository bonusRepository;
    private final FineRepository fineRepository;
    private final String PATH = "C:\\java\\sber-ast\\";

    public ParseXlsServiceImpl(OperatorService operatorService, BonusRepository bonusRepository, FineRepository fineRepository) {
        this.operatorService = operatorService;
        this.bonusRepository = bonusRepository;
        this.fineRepository = fineRepository;
    }

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


            String firstRowCell = dataFormatter.formatCellValue( row.getCell(0));
            if(firstRowCell.equals("Период")){
                String dateCell = dataFormatter.formatCellValue( row.getCell(1));
                date=dateCell.substring(0,dateCell.indexOf(" "));


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
    public File saveStatsToXLS(List<String> request, List<DailyStats> stats, DailyStats totalStats, PositionService positionService) throws Exception {
        for (File myFile : new File(PATH).listFiles())
            if (myFile.isFile()) myFile.delete();

        stats.add(totalStats);

        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("statistics");
        CellStyle style1 = workBook.createCellStyle();
        CellStyle style2 = workBook.createCellStyle();
        Font font1 = workBook.createFont();
        Font font2 = workBook.createFont();
        font1.setBold(true);
        font2.setBold(false);
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setFont(font1);
        style1.setWrapText(true);
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
        if(request.get(2).equals("all")){
            XSSFSheet sheet2 = workBook.createSheet("salary");
            getSalary(request,stats,sheet2, positionService);
        }


        File file = new File(PATH+request.get(0)+" - "+request.get(1)+".xlsx");

        FileOutputStream out = new FileOutputStream(file);
        workBook.write(out);
        out.close();
        System.out.println("Excel written successfully..");
        workBook.close();


        return file;
    }

    public void getSalary(List<String> request, List<DailyStats> stats,  XSSFSheet sheet, PositionService positionService){

        CellStyle style1 = sheet.getWorkbook().createCellStyle();
        CellStyle style2 = sheet.getWorkbook().createCellStyle();
        CellStyle style3 = sheet.getWorkbook().createCellStyle();
        Font font1 = sheet.getWorkbook().createFont();
        Font font2 = sheet.getWorkbook().createFont();
        font1.setBold(true);
        font2.setBold(false);
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setFont(font1);
        style3.setFont(font1);
        style3.setAlignment(HorizontalAlignment.CENTER);
        style2.setAlignment(HorizontalAlignment.JUSTIFY);
        style2.setFont(font2);
        style1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style1.setBorderRight(BorderStyle.THIN);
        style1.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style1.setBorderTop(BorderStyle.THIN);
        style1.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style2.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style2.setBorderRight(BorderStyle.THIN);
        style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style2.setBorderTop(BorderStyle.THIN);
        style2.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style3.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style3.setBorderRight(BorderStyle.THIN);
        style3.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style3.setBorderTop(BorderStyle.THIN);
        style3.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style1.setWrapText(true);
        style2.setWrapText(true);


        int rownum = 0;
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,3));
        String[] list = {"Ф.И.О.","Общие сведения"};
        List<LocalDate> days = getDays(request);
        int cellnum = 0;
        Row row = sheet.createRow(rownum);
        for (String s : list) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellStyle(style1);
            cell.setCellValue(s);
        }
        cellnum=4;
        for (LocalDate date : days) {
            sheet.addMergedRegion(new CellRangeAddress(0,0,cellnum,cellnum+7));
            Cell cell = row.createCell(cellnum);
            cell.setCellStyle(style1);
            cellnum+=8;
            String day= String.valueOf(date.getDayOfMonth()).length()>1?String.valueOf(date.getDayOfMonth()):"0"+String.valueOf(date.getDayOfMonth());
            String month= String.valueOf(date.getMonthValue()).length()>1?String.valueOf(date.getMonthValue()):"0"+String.valueOf(date.getMonthValue());

            cell.setCellValue(day+"-"+month+"-"+date.getYear());
        }


        Row row2 = sheet.createRow(++rownum);
        row2.setHeightInPoints((3*sheet.getDefaultRowHeightInPoints()));

        String[] list2 = {"Ставка", "Общее время\n по выгрузке",	"Общее время\n за месяц",	"Время",	"Расчет без\n премии",	"Премия по\n категориям",
                "Премия\n за стаж",	"Обучение,\n благодарности",	"Штрафы",	"Итого:",	"Комментарий"};

        for (String s : list2) {
            Cell cell = row2.createCell(cellnum++);
            cell.setCellStyle(style2);
            cell.setCellValue(s);
        }
        int lastCellNum = cellnum;


        String[] list4 = {"Наличие карты",	"Компания",	"Тип договора"};
        cellnum=1;
        for (String s : list4) {
            Cell cell = row2.createCell(cellnum++);
            cell.setCellStyle(style1);
            cell.setCellValue(s);
        }

        String[] list3 = {"Общ.вр",	"Готов",	"Общ. вр.\nв  часах",	"Готов\n в часах",	"Оплачивае-\nмое время\n в часах",
                "Потеряное\n время в\n часах",	 "Дневные\n часы",	"Ночные\n часы"};
        cellnum=4;
        for (int i = 0; i < days.size(); i++) {
            for (String s : list3) {
                Cell cell = row2.createCell(cellnum++);
                cell.setCellStyle(style1);
                cell.setCellValue(s);
            }
        }

        Row row3 = sheet.createRow(++rownum);
        for (int i = 0; i < row2.getLastCellNum(); i++) {
            Cell cell = row3.createCell(i);
            if(i==0) {
                cell.setCellValue("АСТ");
            }
            cell.setCellStyle(style3);

        }

        List<DailyStats> finalStats = sortStats(stats);
        finalStats = addZeroOperators(finalStats);
        for (int i = 3; i < finalStats.size()+3; i++) {
            Row rowN = sheet.createRow(i);


            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = rowN.createCell(j);
                DailyStats stat = finalStats.get(i-3);


                switch (j) {
                    case 0:
                        cell.setCellValue(stat.getOperator().getLastName()+" "+stat.getOperator().getFirstName());
                        break;
                    case 1:
                        if (stat.getOperator().isCard()) {
                            cell.setCellValue("есть карта");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 2:
                        cell.setCellValue(stat.getOperator().getCompany().toString());
                        break;
                    case 3:
                        cell.setCellValue(stat.getOperator().getContractType().toString());
                        break;
                }
                if(j==lastCellNum-11) {
                    if (stat.getOperator().isStake()) {
                        cell.setCellValue("ставка");
                    }
                }
                if(j==lastCellNum-10) {
                    cell.setCellValue(stat.getTime(stat.getTotalWorkTime()));
                }
                if(j==lastCellNum-8) {
                    String timeCell = rowN.getCell(lastCellNum-9).getAddress().formatAsString();
                    cell.setCellFormula(timeCell+"*24");

                }
                if(j==lastCellNum-7) {
                    String timeCellFormated = rowN.getCell(lastCellNum-8).getAddress().formatAsString();
                    if(stat.getOperator().isStake()) {
                        if (stat.getOperator().getLastName().contains("Бортунова")) {
                            System.out.println("Бортунова");
                            cell.setCellFormula("("+timeCellFormated + "-" +
                                    Integer.valueOf((int) (positionService.getLabourdays(LocalDate.parse(request.get(0)),
                                            LocalDate.parse(request.get(1))) * 9)).toString() + ")*90+23000");
                        } else {
                            cell.setCellFormula(timeCellFormated + "*22000/" +
                                    Integer.valueOf((int) (positionService.getLabourdays(LocalDate.parse(request.get(0)), LocalDate.parse(request.get(1))) * 9)).toString());
                        }
                    }
                    else {
                        cell.setCellFormula(timeCellFormated+"*90");
                    }
                }
                if(j==lastCellNum-6) {
                    int categoryBonus =getCategoryBonus(stat.getOperator());
                    cell.setCellValue(categoryBonus);
                }
                if(j==lastCellNum-5) {
                    int experienceBonus =getExperienceBonus(stat.getOperator());
                    cell.setCellValue(experienceBonus);
                }
                if(j==lastCellNum-4) {
                    int bonuses = getBonusesBonus(stat.getOperator(),request);
                    cell.setCellValue(bonuses);
                }
                if(j==lastCellNum-3) {
                    int fines = getFines(stat.getOperator(),request);
                    cell.setCellValue(fines);
                }
                if(j==lastCellNum-2) {
                    if(rowN.getCell(lastCellNum-10).getStringCellValue().equals("0:00:00")){
                        cell.setCellValue(0);
                    }
                    else {
                        String hoursPayement = rowN.getCell(lastCellNum - 7).getAddress().formatAsString();
                        String categoryBonus = rowN.getCell(lastCellNum - 6).getAddress().formatAsString();
                        String experienceBonus = rowN.getCell(lastCellNum - 5).getAddress().formatAsString();
                        String bonuses = rowN.getCell(lastCellNum - 4).getAddress().formatAsString();
                        String fines = rowN.getCell(lastCellNum - 3).getAddress().formatAsString();
                        cell.setCellFormula(hoursPayement + "+" + categoryBonus + "+" + experienceBonus + "+" + bonuses + "-" + fines);
                    }
                }
                if(j==lastCellNum-1) {

                    String category =getCategory(stat.getOperator());
                    String bonuses = getBonusesString(stat.getOperator(),request);
                    String fines = getFinesString(stat.getOperator(),request);
                    cell.setCellValue(category+bonuses+fines);
                }


            }

        }

        for (int i = 0; i < row2.getLastCellNum(); i++) {
            sheet.autoSizeColumn(i,true);
        }

    }

    public List<LocalDate> getDays(List<String> request){
        LocalDate startDate = LocalDate.parse(request.get(0));
        LocalDate endDate = LocalDate.parse(request.get(1));
        long numofDays = ChronoUnit.DAYS.between(startDate, endDate)+1;
        List<LocalDate> daysRange = Stream.iterate(startDate, date -> date.plusDays(1)).limit(numofDays).collect(Collectors.toList());
        return daysRange;
    }

    public List<DailyStats> sortStats(List<DailyStats> stats){
//        uniting incoming & outgoing entries
        List<DailyStats> tempoStats = new ArrayList<>(stats);
        List<DailyStats> finalStats = new ArrayList<>();

        for (DailyStats ds : tempoStats
        ) {
            for (DailyStats ds2:stats
            ) {
                if(ds2.getOperator().equals(ds.getOperator())&& ds.getDate().equals(ds2.getDate()) && !(ds2.getNumber().equals(ds.getNumber()))) {
                    boolean contains = false;
                    for (DailyStats ds3: finalStats
                    ) {
                        if(ds3.getOperator().equals(ds.getOperator()))
                            contains=true;
                    }
                    if(!contains) {
                        ds.setTotalWorkTime(ds.getTotalWorkTime() + ds2.getTotalWorkTime());
                        finalStats.add(ds);
                    }
                }
            }
        }

        for (DailyStats ds : stats
        ) {
            boolean contains = false;
            for (DailyStats fs:finalStats
            ) {
                if(ds.getOperator().equals(fs.getOperator()))
                    contains=true;
            }
            if(!contains) {
                finalStats.add(ds);
                contains=false;
            }
        }

        return finalStats;
    }

    public int getCategoryBonus(Operator op){
        int bonus = 0;
        if(op.isOutgoing()&& (!op.isIncoming())){
            return 3000;
        }
        if(op.isStake()){
            return 0;
        }
        int specsSize = operatorService.getSpecialties(op.getId()).size();
        switch (specsSize){
            case 1:
            case 2:
                bonus = 1000;
                break;
            case 3:
                bonus = 2000;
                break;
            case 4:
                bonus = 3000;
                break;
            case 5:
                bonus = 4000;
                break;
        }
        return bonus;
    }
    public String getCategory(Operator op){
        String category="";
        if(op.isOutgoing()&& (!op.isIncoming())){
            return "Категория 4 (Исход)";
        }
        if(op.isStake()){
            return "Ставка";
        }
        int specsSize = operatorService.getSpecialties(op.getId()).size();
        switch (specsSize){
            case 1:
                category="Категория 1 ";
                break;
            case 2:
                category="Категория 2 ";
                break;
            case 3:
                category="Категория 3 ";
                break;
            case 4:
                category="Категория 4 ";
                break;
            case 5:
                category="Категория 5 ";
                break;
        }
        return category;
    }

    public int getExperienceBonus(Operator op){
        int years = op.getYears();
        int bonus = 0;

        switch (years){
            case 1:
                bonus = 500;
                break;
            case 2:
                bonus = 1000;
                break;
            case 3:
                bonus = 1250;
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                bonus = 1500;
                break;
        }
        return bonus;
    }

    public int getBonusesBonus(Operator op, List<String> request){
        Set<Bonus> bonuses =  bonusRepository.findAllByOperatorId(op.getId());
        int bonus = 0;

        for (Bonus b: bonuses
        ) {
            if(b.getDate().isBefore(LocalDate.parse(request.get(1))) && b.getDate().isAfter(LocalDate.parse(request.get(0))))
                bonus+=b.getSize();
        }
        return bonus;
    }
    public String getBonusesString(Operator op, List<String> request){
        Set<Bonus> bonuses =  bonusRepository.findAllByOperatorId(op.getId());
        StringBuilder sb = new StringBuilder();
        sb.append(", ");

        for (Bonus b: bonuses
        ) {
            if(b.getDate().isBefore(LocalDate.parse(request.get(1))) && b.getDate().isAfter(LocalDate.parse(request.get(0))))
                sb.append(b.getSize()+"(бонус) - "+b.getDescription()+", ");
        }
        String result = sb.toString().length()>2?sb.toString().substring(0,sb.toString().lastIndexOf(",")):"";
        return result;
    }

    public int getFines(Operator op, List<String> request){
        Set<Fine> fines =  fineRepository.findAllByOperatorId(op.getId());
        int fine = 0;

        for (Fine f: fines
        ) {
            if(f.getDate().isBefore(LocalDate.parse(request.get(1))) && f.getDate().isAfter(LocalDate.parse(request.get(0))))
                fine+=f.getSize();
        }
        return fine;
    }

    public String getFinesString(Operator op, List<String> request){
        Set<Fine> fines =  fineRepository.findAllByOperatorId(op.getId());
        StringBuilder sb = new StringBuilder();
        sb.append(", ");

        for (Fine f: fines
        ) {
            if(f.getDate().isBefore(LocalDate.parse(request.get(1))) && f.getDate().isAfter(LocalDate.parse(request.get(0))))
                sb.append(f.getSize()+"(штраф) - "+f.getDescription()+", ");
        }
        String result = sb.toString().length()>2?sb.toString().substring(0,sb.toString().lastIndexOf(",")):"";
        return result;
    }

    public List<DailyStats> addZeroOperators(List<DailyStats>stats){
        List<Operator> operators = operatorService.getOperators("name");
        List<DailyStats> tempoStats = new ArrayList<>(stats);
        List<DailyStats> finalStats = new ArrayList<>();

        for (Operator op:operators
        ) {
            boolean contains = false;
            for (DailyStats ds: tempoStats
            ) {
                String name = ds.getOperator().getLastName()+ds.getOperator().getFirstName();
                if(name.equals(op.getLastName()+op.getFirstName()))
                    contains=true;
            }
            if(!contains){
                DailyStats ds2 = new DailyStats();
                ds2.setOperator(op);
                ds2.setTotalWorkTime(0L);
                finalStats.add(ds2);
                contains=false;
            }
        }
        finalStats.addAll(stats);
        finalStats.sort(Comparator.comparing(o -> o.getOperator().getLastName()));
        return finalStats;
    }

}