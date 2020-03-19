package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public interface ParseXlsService {
    List<DailyStats> parseStatsXml(String path, OperatorRepository operatorRepository, DailyStatsRepository dailyStatsRepository) throws Exception;
    List<DailyStats> parseLostXml(String path, List<DailyStats> dailyStats) throws Exception;
    long parseTime(String time);
    int checkXlsOrXlsx(String fileName);
    void closeReadBooks(Workbook readbookXls, XSSFWorkbook readbookXlsx) throws Exception;

}
