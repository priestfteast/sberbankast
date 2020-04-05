package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.repositories.OutgoingRepository;
import com.balakin.sberbankast.services.DailyStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
public class DailyStatsController {


    private final DailyStatsRepository dailyStatsRepository;
    private final OperatorRepository operatorRepository;
    private final DailyStatsService dailyStatsService;
    private final OutgoingRepository outgoingRepository;

    private List<DailyStats> dailyStats = new ArrayList<>();
    private DailyStats dstats = new DailyStats();
    private List<String> request = new ArrayList<>();

    public DailyStatsController(DailyStatsRepository dailyStatsRepository, OperatorRepository operatorRepository, DailyStatsService dailyStatsService, OutgoingRepository outgoingRepository) {
        this.dailyStatsRepository = dailyStatsRepository;
        this.operatorRepository = operatorRepository;
        this.dailyStatsService = dailyStatsService;
        this.outgoingRepository = outgoingRepository;
    }


    @GetMapping("dailystats/view")
    public String showDailyStats(Model model) {
//        if(dailyStats.size()==0){
//            DailyStats ds = dailyStatsRepository.findTopByOrderByIdDesc();
//            dailyStats=dailyStatsRepository.getAllByDate(ds.getDate());
//        }
        if(request.size()==0){
            System.out.println(LocalDate.now().toString());
            request.add(LocalDate.now().toString().substring(0,8)+"01");
            request.add(LocalDate.now().toString());
            request.add("all");
        }
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
        model.addAttribute("request", request);
        model.addAttribute("outgoinglist",outgoingRepository.findAll());
        model.addAttribute("outgoingstring",outgoingRepository.findAll().toString());

        return "dailystats/view";
    }


    @PostMapping("viewstats")
    public String viewStats(@RequestParam MultiValueMap<String, String> formData) throws Exception {

        dailyStats = new ArrayList<>();
        dstats = new DailyStats();

        String startdate = formData.getFirst("startdate"); request.set(0,startdate);
        String enddate = formData.getFirst("enddate");request.set(1,enddate);
        String operator = formData.getFirst("operator");request.set(2,operator);


        if (operator.equals("all")) {
            dailyStats = dailyStatsService.getAllStats(Date.valueOf(startdate), Date.valueOf(enddate));
            dstats = dailyStatsService.getTotalStats(dailyStats);
        } else {
            dailyStats = dailyStatsService.getOperatorStats(Date.valueOf(startdate), Date.valueOf(enddate), operator);
            dstats = dailyStatsService.getTotalOperatorStats(dailyStats);
        }



        return "redirect:/dailystats/view";
    }

}
