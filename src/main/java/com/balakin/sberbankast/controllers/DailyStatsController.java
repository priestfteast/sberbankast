package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
public class DailyStatsController {


    private final DailyStatsRepository dailyStatsRepository;
    private final OperatorRepository operatorRepository;

    private List<DailyStats> dailyStats = new ArrayList<>();
    private DailyStats dstats = new DailyStats("", 0L, 0L, 0L);

    public DailyStatsController(DailyStatsRepository dailyStatsRepository, OperatorRepository operatorRepository) {
        this.dailyStatsRepository = dailyStatsRepository;
        this.operatorRepository = operatorRepository;
    }


    @GetMapping("dailystats/view")
    public String showDailyStats(Model model) {
//        if(dailyStats.size()==0){
//            DailyStats ds = dailyStatsRepository.findTopByOrderByIdDesc();
//            dailyStats=dailyStatsRepository.getAllByDate(ds.getDate());
//        }
        ArrayList<Operator> operators = (ArrayList<Operator>) operatorRepository.findAll();
        Collections.sort(operators, new Comparator<Operator>() {
            @Override
            public int compare(Operator o1, Operator o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });

        model.addAttribute("operators", operators);
        model.addAttribute("stats", dailyStats);
        model.addAttribute("dstats", dstats);

        return "dailystats/view";
    }


    @PostMapping("viewstats")
    public String viewStats(@RequestParam MultiValueMap<String, String> formData) throws Exception {

        dailyStats = new ArrayList<>();
        dstats = new DailyStats("", 0L, 0L, 0L);

        String startdate = formData.getFirst("startdate");
        String enddate = formData.getFirst("enddate");
        String operator = formData.getFirst("operator");

        List<Operator> operators = new ArrayList<>();
        List<String> days = new ArrayList<>();
        if (operator.equals("all")) {
            dailyStats = dailyStatsRepository.getAllByDateBetween(Date.valueOf(startdate), Date.valueOf(enddate));
        } else
            dailyStats = dailyStatsRepository.getAllByDateBetweenAndNumber(Date.valueOf(startdate), Date.valueOf(enddate), operator);

        for (DailyStats ds : dailyStats
        ) {
            dstats.setIncoming(ds.getIncoming() + dstats.getIncoming());
            dstats.setTotalWorkTime(ds.getTotalWorkTime() + dstats.getTotalWorkTime());
            dstats.setLost406(ds.getLost406() + dstats.getLost406());
            if (!(operators.contains(ds.getOperator())))
                operators.add(ds.getOperator());
            if (!(days.contains(ds.getDate().toString())))
                days.add(ds.getDate().toString());

        }
        dstats.setNumber(String.valueOf(operators.size()));
        dstats.setId((long) days.size());




        return "redirect:/dailystats/view";
    }

}
