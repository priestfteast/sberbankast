package com.balakin.sberbankast.repositories;


import com.balakin.sberbankast.domain.DailyStats;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface DailyStatsRepository extends CrudRepository<DailyStats,Long> {

    DailyStats getFirstByDate(Date date);
    void deleteAllByDate(Date date);
}
