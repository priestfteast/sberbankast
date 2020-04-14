package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface DailyStatsService {

    DailyStats getTotalStats(List<DailyStats> stats);

    DailyStats getTotalOperatorStats(List<DailyStats> stats);

    List<DailyStats> getAllStats(Date start, Date end);

    List<DailyStats> getOperatorStats(Date start, Date end, String number);

    List<DailyStats> filter(List<DailyStats> stats,String filter);

    List<Map.Entry<DailyStats, Integer[]>> getRating(List<DailyStats> stats);

    List<Long[]> getChartsData(String start, String end, String operator);

    String[] getChartDays(String start, String end,String operator);

}
