package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Outgoing;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OutgoingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    public DailyStats getTotalOperatorStats(List<DailyStats> stats) {
        DailyStats dstats = new DailyStats(null,"",0L,0L,0L,0L,0L,0L,0L,
                0L,0L,0L,0L,0L,0L,0L,0L,
                0L,0L,0L,null);


        List<String> days = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();



        for (DailyStats ds: stats
        ) {
            dstats.setDate(ds.getDate());
            dstats.setOperator(ds.getOperator());

                dstats.setNumber(ds.getNumber());

            dstats.setIncoming(ds.getIncoming() + dstats.getIncoming());
            dstats.setTotalWorkTime(ds.getTotalWorkTime() + dstats.getTotalWorkTime());
            dstats.setTotalAfterCallTime(ds.getTotalAfterCallTime()+dstats.getTotalAfterCallTime());
            dstats.setLost406(ds.getLost406() + dstats.getLost406());


                dstats.setOutgoingExternal(ds.getOutgoingExternal()+dstats.getOutgoingExternal());




                dstats.setTotalNotReadyTime(ds.getTotalNotReadyTime() + dstats.getTotalNotReadyTime());
                dstats.setHolded(ds.getHolded()+dstats.getHolded());
                dstats.setTotalHoldTime(ds.getTotalHoldTime()+dstats.getTotalHoldTime());
                dstats.setIncomingAvrg(ds.getIncomingAvrg()+dstats.getIncomingAvrg());


            if (!(operators.contains(ds.getOperator())))
                operators.add(ds.getOperator());

            if (!(days.contains(ds.getDate().toString())))
                days.add(ds.getDate().toString());

        }
        if(dstats.getIncomingAvrg()>0L) {
            dstats.setIncomingAvrg(dstats.getIncomingAvrg() / stats.size());
        }
        if(dstats.getTotalAfterCallTime()>0L) {
            dstats.setAfterCallTimeAvrg(dstats.getTotalAfterCallTime() / (dstats.getIncoming() + dstats.getOutgoingExternal()));
        }
        dstats.setLost((long) operators.size());
        if(dstats.getTotalHoldTime()>0L) {
            dstats.setHoldTimeAvrg(dstats.getTotalHoldTime() / dstats.getHolded());
        }
        dstats.setOutgoingInternal((long) days.size());



        return dstats;
    }

    @Override
    public DailyStats getTotalStats(List<DailyStats> stats) {
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

        List<String> allOperators = getAllOperators(dailyStats);


        for (String number: allOperators
             ) {

            List<DailyStats> operatorStats = dailyStatsRepository.getAllByDateBetweenAndNumber(start,end, number);

            resultList.add(getTotalOperatorStats(operatorStats));

        }
        return resultList;
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
        for (Map.Entry<DailyStats, Integer[]> entry: ratingList
        ) {
            System.out.println(entry.getKey().getOperator().getLastName()+" : "+Arrays.toString(entry.getValue()));
        }
        System.out.println();
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

        for (Map.Entry<DailyStats, Integer[]> entry: result
             ) {
            System.out.println(entry.getKey().getOperator().getLastName()+" : "+Arrays.toString(entry.getValue()));
        }

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
        System.out.println(Arrays.toString(value)+"!");

        return value;
    }


}
