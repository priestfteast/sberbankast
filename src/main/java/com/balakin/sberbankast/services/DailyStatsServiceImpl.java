package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Outgoing;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OutgoingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalUnit;
import java.util.*;

@Slf4j
@Service
public class DailyStatsServiceImpl implements DailyStatsService {

    private final DailyStatsRepository dailyStatsRepository;
    private final OutgoingRepository outgoingRepository;



    public DailyStatsServiceImpl(DailyStatsRepository dailyStatsRepository, OutgoingRepository outgoingRepository) {
        this.dailyStatsRepository = dailyStatsRepository;


        this.outgoingRepository = outgoingRepository;
    }

    @Override
    public DailyStats getTotalOperatorStats(List<DailyStats> stats) throws Exception{
       try {
           DailyStats dstats = new DailyStats(null, "", 0L, 0L, 0L, 0L, 0L, 0L, 0L,
                   0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
                   0L, 0L, 0L, null);


           List<String> days = new ArrayList<>();
           List<Operator> operators = new ArrayList<>();


           for (DailyStats ds : stats
           ) {
               dstats.setDate(ds.getDate());
               dstats.setOperator(ds.getOperator());

               dstats.setNumber(ds.getNumber());

               dstats.setIncoming(ds.getIncoming() + dstats.getIncoming());
               dstats.setTotalWorkTime(ds.getTotalWorkTime() + dstats.getTotalWorkTime());
               dstats.setTotalAfterCallTime(ds.getTotalAfterCallTime() + dstats.getTotalAfterCallTime());
               dstats.setLost406(ds.getLost406() + dstats.getLost406());


               dstats.setOutgoingExternal(ds.getOutgoingExternal() + dstats.getOutgoingExternal());


               dstats.setTotalNotReadyTime(ds.getTotalNotReadyTime() + dstats.getTotalNotReadyTime());
               dstats.setHolded(ds.getHolded() + dstats.getHolded());
               dstats.setTotalHoldTime(ds.getTotalHoldTime() + dstats.getTotalHoldTime());
               dstats.setIncomingAvrg(ds.getIncomingAvrg() + dstats.getIncomingAvrg());


               if (!(operators.contains(ds.getOperator())))
                   operators.add(ds.getOperator());

               if (!(days.contains(ds.getDate().toString())))
                   days.add(ds.getDate().toString());

           }
           if (dstats.getIncomingAvrg() > 0L) {
               dstats.setIncomingAvrg(dstats.getIncomingAvrg() / stats.size());
           }

           if (dstats.getTotalAfterCallTime() > 0L  &&(dstats.getIncoming() + dstats.getOutgoingExternal())>0L) {
               dstats.setAfterCallTimeAvrg(dstats.getTotalAfterCallTime() / (dstats.getIncoming() + dstats.getOutgoingExternal()));
           }
           dstats.setLost((long) operators.size());
           if (dstats.getTotalHoldTime() > 0L) {
               dstats.setHoldTimeAvrg(dstats.getTotalHoldTime() / dstats.getHolded());
           }
           dstats.setOutgoingInternal((long) days.size());


           return dstats;
       }
       catch (Exception e){
           throw e;
       }
    }

    @Override
    public DailyStats getTotalStats(List<DailyStats> stats) throws Exception {
        DailyStats dstats = new DailyStats(null,"",0L,0L,0L,0L,0L,0L,0L,
                0L,0L,0L,0L,0L,0L,0L,0L,
                0L,0L,0L,null);
         List<String> outgoingList = new ArrayList<>();

        for (Outgoing out: outgoingRepository.findAll()
             ) {
            outgoingList.add(out.getNumber());
        }


            List<String> days = new ArrayList<>();
            List<Operator> operators = new ArrayList<>();

            int incOperatorCounter=0;

            for (DailyStats ds: stats
            ) {
                dstats.setDate(ds.getDate());
                dstats.setOperator(ds.getOperator());

                    dstats.setNumber(ds.getNumber());

                dstats.setIncoming(ds.getIncoming() + dstats.getIncoming());
                dstats.setTotalWorkTime(ds.getTotalWorkTime() + dstats.getTotalWorkTime());
                dstats.setTotalAfterCallTime(ds.getTotalAfterCallTime()+dstats.getTotalAfterCallTime());
                if(ds.getIncomingAvrg()==null||ds.getOperator()==null)
                    System.out.println(ds.getNumber()+"!!!!");
                if (ds.getIncomingAvrg()>0&&ds.getOperator().isIncoming())
                    incOperatorCounter++;
                if(ds.getOperator().isOutgoing()&& checkNumber(ds.getNumber(),outgoingList)) {
                    dstats.setOutgoingExternal(ds.getOutgoingExternal()+dstats.getOutgoingExternal());
                }

                if(!outgoingList.contains(ds.getNumber())) {
                    dstats.setTotalNotReadyTime(ds.getTotalNotReadyTime() + dstats.getTotalNotReadyTime());
                    dstats.setHolded(ds.getHolded()+dstats.getHolded());
                    dstats.setTotalHoldTime(ds.getTotalHoldTime()+dstats.getTotalHoldTime());
                    dstats.setIncomingAvrg(ds.getIncomingAvrg()+dstats.getIncomingAvrg());
                    dstats.setLost406(ds.getLost406() + dstats.getLost406());
                }

                if (!(operators.contains(ds.getOperator())))
                    operators.add(ds.getOperator());

                if (!(days.contains(ds.getDate().toString())))
                    days.add(ds.getDate().toString());

            }
        if(incOperatorCounter>0L) {
            dstats.setIncomingAvrg(dstats.getIncomingAvrg() / incOperatorCounter);
        }
        if(dstats.getTotalAfterCallTime()>0L) {
            if(dstats.getIncoming()+dstats.getOutgoingTotal()!=0l)
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
    public List<DailyStats> getAllStats(Date start, Date end) throws Exception{

        try {
            List<DailyStats> resultList = new ArrayList<>();

            List<DailyStats> dailyStats = dailyStatsRepository.getAllByDateBetween(start, end);

            List<String> allOperators = getAllOperators(dailyStats);


            for (String number : allOperators
            ) {

                List<DailyStats> operatorStats = dailyStatsRepository.getAllByDateBetweenAndNumber(start, end, number);

                resultList.add(getTotalOperatorStats(operatorStats));

            }
            return resultList;
        }
        catch (Exception e){
            throw e;
        }
        }






    @Override
    public List<DailyStats> getOperatorStats(Date start, Date end, String number) {
       return dailyStatsRepository.getAllByDateBetweenAndNumber(start,end,number);
    }


     public List<String> getAllOperators(List<DailyStats> stats){
        List<String> operators = new ArrayList<>();

        for (DailyStats ds:stats
             ) {
            if(!operators.contains( ds.getNumber()))
                operators.add(ds.getNumber());
        }
        return operators;
    }

    public boolean checkNumber(String number,List<String> outgoingList){
        for (String ds:outgoingList
             ) {
            if(number.contains(ds))
                return true;
        }
        return false;
    }

    public List<DailyStats> filter(List<DailyStats> stats,String filter){

        switch (filter) {
            case("Overall"):
                break;
            case("Total loged in time"):
                rateByTotalWorkTime(stats);
                break;
            case("Incoming calls quantity"):
               rateByCallsQuantity(stats);
                break;
            case("Average call duration"):
                stats=removeOutgoingoperators(stats);
                rateByCallDuration(stats);
                break;
            case("Average after call time"):
                stats=removeOutgoingoperators(stats);
                rateByAfterCallTime(stats);
                break;
            case("Average hold time"):
                stats=removeOutgoingoperators(stats);
                rateByAverageHoldTime(stats);
                break;
            case("Lost calls (406)"):
                stats=removeOutgoingoperators(stats);
                rateByLostCalls(stats);
                break;
            case("Outgoing calls quantity"):
                rateByOutgoingQuantity(stats);
                break;

        }

        return stats;
    }

    public List<DailyStats> removeOutgoingoperators(List<DailyStats> stats){
        List<String> outgoingList = new ArrayList<>();
        List<DailyStats> result = new ArrayList<>();

        for (Outgoing out: outgoingRepository.findAll()
        ) {
            outgoingList.add(out.getNumber());
        }
        for (DailyStats ds:stats
             ) {
            if(!outgoingList.contains(ds.getNumber()))
                result.add(ds);
        }
        return result;
    }

    public List<Map.Entry<DailyStats, Integer[]>> getRating(List<DailyStats> stats){
        stats = removeOutgoingoperators(stats);
        Map<DailyStats,Integer[]> rating = new HashMap<>();
        for (DailyStats ds: stats
             ) {
            rating.put(ds,new Integer[]{0,0,0,0,0,0,0});
        }

        List<List<DailyStats>> lists = new ArrayList<>();

        List<DailyStats> totalWorkTime = rateByTotalWorkTime(stats);  lists.add(totalWorkTime);

        List<DailyStats> callsQuantity = rateByCallsQuantity(stats);  lists.add(callsQuantity);

        List<DailyStats> callDuration = rateByCallDuration(stats); lists.add(callDuration);

        List<DailyStats> afterCallTime = rateByAfterCallTime(stats); lists.add(afterCallTime);

        List<DailyStats> holdTime = rateByAverageHoldTime(stats); lists.add(holdTime);

        List<DailyStats> lostCalls = rateByLostCalls(stats); lists.add(lostCalls);

        for (Map.Entry<DailyStats,Integer[]> entry:rating.entrySet()
             ) {
           rating.put(entry.getKey(),getValue(entry.getKey(),lists));
        }

        List<Map.Entry<DailyStats, Integer[]>> ratingList = new ArrayList<>(rating.entrySet());
//        for (Map.Entry<DailyStats, Integer[]> entry: ratingList
//        ) {
//            System.out.println(entry.getKey().getOperator().getLastName()+" : "+Arrays.toString(entry.getValue()));
//        }
//        System.out.println();
       Collections.sort(ratingList, new Comparator<Map.Entry<DailyStats, Integer[]>>() {
           @Override
           public int compare(Map.Entry<DailyStats, Integer[]> o1, Map.Entry<DailyStats, Integer[]> o2) {
               if (o1.getValue()[6] > o2.getValue()[6])
                   return 1;
               else if (o1.getValue()[6] < o2.getValue()[6])
                   return -1;
               else return 0;
           }
       });
        List<Map.Entry<DailyStats, Integer[]>> result = new ArrayList<>();
        result.addAll(0, ratingList);

//        for (Map.Entry<DailyStats, Integer[]> entry: result
//             ) {
//            System.out.println(entry.getKey().getOperator().getLastName()+" : "+Arrays.toString(entry.getValue()));
//        }

       return result;




    }

    public List<DailyStats> rateByOutgoingQuantity(List<DailyStats> stats){

        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getOutgoingExternal() > o2.getOutgoingExternal())
                    return -1;
                else if (o1.getOutgoingExternal() < o2.getOutgoingExternal())
                    return 1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public List<DailyStats> rateByLostCalls(List<DailyStats> stats){

        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getLost406()*100.0/o1.getIncoming() > o2.getLost406()*100.0/o2.getIncoming())
                    return -1;
                else if (o1.getLost406()*100.0/o1.getIncoming() < o2.getLost406()*100.0/o1.getIncoming())
                    return 1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public List<DailyStats> rateByAverageHoldTime(List<DailyStats> stats){

        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getHoldTimeAvrg() > o2.getHoldTimeAvrg())
                    return 1;
                else if (o1.getHoldTimeAvrg() < o2.getHoldTimeAvrg())
                    return -1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public List<DailyStats> rateByAfterCallTime(List<DailyStats> stats){
        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getAfterCallTimeAvrg() > o2.getAfterCallTimeAvrg())
                    return 1;
                else if (o1.getAfterCallTimeAvrg() < o2.getAfterCallTimeAvrg())
                    return -1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public List<DailyStats> rateByCallDuration(List<DailyStats> stats){
        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getIncomingAvrg() > o2.getIncomingAvrg())
                    return 1;
                else if (o1.getIncomingAvrg() < o2.getIncomingAvrg())
                    return -1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public List<DailyStats> rateByCallsQuantity(List<DailyStats> stats){
        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getIncoming() > o2.getIncoming())
                    return -1;
                else if (o1.getIncoming() < o2.getIncoming())
                    return 1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public List<DailyStats> rateByTotalWorkTime(List<DailyStats> stats){
        Collections.sort(stats, new Comparator<DailyStats>() {
            @Override
            public int compare(DailyStats o1, DailyStats o2) {
                if (o1.getTotalWorkTime() > o2.getTotalWorkTime())
                    return -1;
                else if (o1.getTotalWorkTime() < o2.getTotalWorkTime())
                    return 1;
                else return 0;
            }
        });
        ArrayList<DailyStats> result = new ArrayList<>();
        result.addAll(0, stats);
        return result;
    }

    public Integer[] getValue(DailyStats dStats, List<List<DailyStats>> lists){
        Integer[] value = new Integer[]{0,0,0,0,0,0,0};

        for (int i = 0; i < lists.size(); i++) {

            for (int j = 0; j < lists.get(i).size(); j++) {

                if (dStats.equals(lists.get(i).get(j))) {

                    value[i] = j + 1;
                    value[6] += j + 1;
                }
            }

        }

        return value;
    }

    public String[] getChartDays(String start, String end,String operator) throws Exception {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<LocalDate> totalDates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            totalDates.add(startDate);
            startDate = startDate.plusDays(1);
        }

        List<String> dates = new ArrayList<>();

        if(operator.equals("all")) {
            for (LocalDate date : totalDates
            ) {
                if (dailyStatsRepository.getFirstByDate(Date.valueOf(date)) != null)
                    dates.add(date.toString());
            }
        }
        else {
            for (LocalDate date : totalDates
            ) {
                if (dailyStatsRepository.getFirstByDateAndNumber(Date.valueOf(date),operator) != null)
                    dates.add(date.toString());
            }
        }

        String[] days = new String[dates.size()];

       days =dates.toArray(days);

        return days;
    }

    public List <Long[]> getChartsData(String start, String end,String operator) throws Exception {

//        Creating list for all data necessary for charts
        List<Long[]> result = new ArrayList<>();

//        Creating of all dates from request
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<LocalDate> totalDates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            totalDates.add(startDate);
            startDate = startDate.plusDays(1);
        }

//        getting all stats necessary for charts
        List<DailyStats> stats = new ArrayList<>();
        List<Long> operatorsPerDay=new ArrayList<>();
        List<DailyStats> allStats = new ArrayList<>();


        if(operator.equals("all")){
            allStats = getAllStats(Date.valueOf(start),Date.valueOf(end));
            for (LocalDate date: totalDates
                 ) {
                List<DailyStats> dayStats = dailyStatsRepository.getAllByDate(Date.valueOf(date));
                if(dayStats.size()>0){
                    operatorsPerDay.add(countOperators(dayStats));
                    stats.add(getTotalStats(dayStats));
                }
            }
        }
        else {
            allStats = getOperatorStats(Date.valueOf(start),Date.valueOf(end),operator);
            for (LocalDate date: totalDates
            ) {
                List<DailyStats> dayStats = dailyStatsRepository.getAllByDateBetweenAndNumber(Date.valueOf(date),Date.valueOf(date),operator);
                if(dayStats.size()>0){
                    operatorsPerDay.add(countOperators(dayStats));
                    stats.add(getTotalStats(dayStats));
                }
            }
        }
        DailyStats totalStats =new DailyStats();
        if(operator.equals("all")) {
            totalStats = getTotalStats(allStats);
        }
        else
            totalStats =getTotalOperatorStats(allStats);

        //        Creating data for total work time chart (0)

        Long[] totalWorkTime = new Long[stats.size()];
        Long[] totalIncoming = new Long[stats.size()];
        Long[] outgoingExternal = new Long[stats.size()];
        Long[] talkTimeAvg = new Long[stats.size()];
        Long[] holdTimeAvg = new Long[stats.size()];
        Long[] afterCallAvg = new Long[stats.size()];
        Long[] lost406 = new Long[stats.size()];
        for (int i = 0; i < stats.size(); i++) {
            totalIncoming[i]= stats.get(i).getIncoming();
            outgoingExternal[i]= stats.get(i).getOutgoingExternal();
            totalWorkTime[i]= stats.get(i).getTotalWorkTime();
            talkTimeAvg[i]= stats.get(i).getIncomingAvrg();
            holdTimeAvg[i]= stats.get(i).getHoldTimeAvrg();
            afterCallAvg[i]= stats.get(i).getAfterCallTimeAvrg();
            lost406[i]= stats.get(i).getLost406();
        }

        List<DailyStats> stats1 = (List<DailyStats>) dailyStatsRepository.findAll();
        Long time = 0L;
        for (DailyStats ds:stats1
             ) {
            time+=ds.getTotalWorkTime();
        }

        result.add(totalIncoming);
        result.add(outgoingExternal);
        result.add(totalWorkTime);
        result.add(talkTimeAvg);
        result.add(holdTimeAvg);
        result.add(afterCallAvg);
        result.add(lost406);
        result.add(operatorsPerDay.toArray(new Long[operatorsPerDay.size()]));
        Long[] operatorsQuantity = {countOperators(allStats)};
        result.add(operatorsQuantity);
        Long[] totalTime = {totalStats.getTotalWorkTime(),time};
        result.add(totalTime);
        Long[]calls ={totalStats.getIncoming(),totalStats.getOutgoingExternal(),totalStats.getLost406()};
        result.add(calls);
        Long[]callsAvg ={totalStats.getIncomingAvrg(),totalStats.getHoldTimeAvrg(),totalStats.getAfterCallTimeAvrg()};
        result.add(callsAvg);
        return result;
    }

    public Long countOperators(List<DailyStats> dayStats ){
        System.out.println(dayStats.size()+"!!");
        List<Operator> operators = new ArrayList<>();

        for (DailyStats ds: dayStats
             ) {
            if(!operators.contains(ds.getOperator()))
                operators.add(ds.getOperator());

        }
        return Long.valueOf(operators.size());
    }




}
