package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;

import java.util.List;

public interface DailyStatsService {
    List<DailyStats> getDailyStats(String request);

    DailyStats findById(Long l);

    void deleteById(Long idToDelete);
}
