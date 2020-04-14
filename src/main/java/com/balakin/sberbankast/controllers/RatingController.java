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
import java.util.*;

@Controller
@Slf4j
public class RatingController {

    private final DailyStatsService dailyStatsService;
    private final OutgoingRepository outgoingRepository;

    private List<DailyStats> dailyStats = new ArrayList<>();
    private List<Map.Entry<DailyStats, Integer[]>> rating = new ArrayList<>();
    private List<String> request = new ArrayList<>();

    public RatingController(DailyStatsService dailyStatsService, OutgoingRepository outgoingRepository) {
        this.dailyStatsService = dailyStatsService;
        this.outgoingRepository = outgoingRepository;
    }

    @GetMapping("dailystats/test")
    public String showTest(Model model) {
        return "dailystats/test";
    }

    @GetMapping("dailystats/rating")
    public String showDailyStats(Model model) {

        if(request.size()==0){
            request.add(LocalDate.now().toString().substring(0,8)+"01");
            request.add(LocalDate.now().toString());
            request.add("overall");
        }

        model.addAttribute("stats", dailyStats);
        model.addAttribute("rating",rating);
        model.addAttribute("request", request);
        model.addAttribute("outgoinglist",outgoingRepository.findAll());
        model.addAttribute("outgoingstring",outgoingRepository.findAll().toString());

        return "dailystats/rating";
    }


    @PostMapping("rating")
    public String viewRatings(@RequestParam MultiValueMap<String, String> formData) throws Exception {

        dailyStats = new ArrayList<>();
        rating = new ArrayList<>();


        String startdate = formData.getFirst("startdate"); request.set(0,startdate);
        String enddate = formData.getFirst("enddate");request.set(1,enddate);
        String criterion = formData.getFirst("criterion");request.set(2,criterion);


        if(criterion.equals("Overall")){
            rating = dailyStatsService.getRating(dailyStatsService.getAllStats(Date.valueOf(startdate), Date.valueOf(enddate)));
        }
        else {
            dailyStats = dailyStatsService.filter(dailyStatsService.getAllStats(Date.valueOf(startdate), Date.valueOf(enddate)), criterion);
        }






        return "redirect:/dailystats/rating";
    }

}
