package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DailyStatsServiceImpl implements DailyStatsService {

    private final DailyStatsRepository dailyStatsRepository;

    public DailyStatsServiceImpl(DailyStatsRepository dailyStatsRepository) {
        this.dailyStatsRepository = dailyStatsRepository;
    }

    @Override
    public DailyStats getTotalStats(List<DailyStats> stats) {
        DailyStats dstats = new DailyStats(null,"",0L,0L,0L,0L,0L,0L,0L,
                0L,0L,0L,0L,0L,0L,0L,0L,
                0L,0L,0L,null);


            List<String> days = new ArrayList<>();
            List<Operator> operators = new ArrayList<>();

            int incOperatorCounter=0;

            for (DailyStats ds: stats
            ) {
                dstats.setDate(ds.getDate());
                dstats.setOperator(ds.getOperator());
                if(!dstats.getNumber().contains(ds.getNumber())) {
                    dstats.setNumber(ds.getNumber() + " " + dstats.getNumber());
                }
                dstats.setIncoming(ds.getIncoming() + dstats.getIncoming());
                dstats.setTotalWorkTime(ds.getTotalWorkTime() + dstats.getTotalWorkTime());
                dstats.setTotalAfterCallTime(ds.getTotalAfterCallTime()+ds.getTotalAfterCallTime());
                dstats.setLost406(ds.getLost406() + dstats.getLost406());
                dstats.setIncomingAvrg(ds.getIncomingAvrg()+dstats.getIncomingAvrg());
                if (ds.getIncomingAvrg()>0)
                    incOperatorCounter++;
                dstats.setOutgoingExternal(ds.getOutgoingExternal()+dstats.getOutgoingExternal());
                dstats.setTotalNotReadyTime(ds.getTotalNotReadyTime()+dstats.getTotalNotReadyTime());
                dstats.setHolded(ds.getHolded()+dstats.getHolded());
                dstats.setTotalHoldTime(ds.getTotalHoldTime()+dstats.getTotalHoldTime());
                if (!(operators.contains(ds.getOperator())))
                    operators.add(ds.getOperator());

                if (!(days.contains(ds.getDate().toString())))
                    days.add(ds.getDate().toString());

            }
        if(incOperatorCounter>0L) {
            dstats.setIncomingAvrg(dstats.getIncomingAvrg() / incOperatorCounter);
        }
        if(dstats.getTotalAfterCallTime()>0L) {
            dstats.setAfterCallTimeAvrg(dstats.getTotalAfterCallTime() / (dstats.getIncoming() + dstats.getOutgoingTotal()));
        }
        dstats.setLost((long) operators.size());
        if(dstats.getTotalHoldTime()>0L) {
            dstats.setHoldTimeAvrg(dstats.getTotalHoldTime() / dstats.getHolded());
        }
        dstats.setOutgoingInternal((long) days.size());



        return dstats;
    }

    @Override
    public List<DailyStats> getAllStats(Date start, Date end) {

        List<DailyStats> resultList = new ArrayList<>();

        List<DailyStats> dailyStats = dailyStatsRepository.getAllByDateBetween(start,end);

        List<Operator> allOperators = getAllOperators(dailyStats);


        for (Operator op: allOperators
             ) {

            List<DailyStats> operatorStats = dailyStatsRepository.getAllByDateBetweenAndOperator(start,end, op);

            resultList.add(getTotalStats(operatorStats));
            System.out.println(resultList.size());

        }
        return resultList;
        }






    @Override
    public List<DailyStats> getOperatorStats(Date start, Date end, String number) {
       return dailyStatsRepository.getAllByDateBetweenAndNumber(start,end,number);
    }


     public List<Operator> getAllOperators(List<DailyStats> stats){
        List<Operator> operators = new ArrayList<>();

        for (DailyStats ds:stats
             ) {
            if(!operators.contains( ds.getOperator()))
                operators.add(ds.getOperator());
        }
        return operators;
    }


}
