package com.balakin.sberbankast.repositories;


import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface DailyStatsRepository extends CrudRepository<DailyStats,Long> {

    DailyStats getFirstByDate(Date date);
    void deleteAllByDate(Date date);
    List<DailyStats> getAllByDateBetween(Date startDate, Date endDate);
    List<DailyStats> getAllByDateBetweenAndNumber(Date startDate, Date endDate, String number);
    List<DailyStats> getAllByDate(Date date);
    DailyStats getTopByDateBefore(Date date);
    DailyStats findTopByOrderByIdDesc();
    List<DailyStats> getAllByDateBetweenAndOperator(Date startDate, Date endDate, Operator operator);


}
