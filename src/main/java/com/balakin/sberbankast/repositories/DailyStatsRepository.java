package com.balakin.sberbankast.repositories;


import com.balakin.sberbankast.domain.DailyStats;
import org.springframework.data.repository.CrudRepository;

public interface DailyStatsRepository extends CrudRepository<DailyStats,Long> {
}
