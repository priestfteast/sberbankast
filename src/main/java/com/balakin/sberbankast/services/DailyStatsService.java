package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;

import java.sql.Date;
import java.util.List;

public interface DailyStatsService {

    DailyStats getTotalStats(List<DailyStats> stats);

    List<DailyStats> getAllStats(Date start, Date end);

    List<DailyStats> getOperatorStats(Date start, Date end, String number);

}
