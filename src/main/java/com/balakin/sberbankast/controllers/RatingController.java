package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.DailyStats;
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
    private String error=null;
    private boolean isNewRequest =true;

    public RatingController(DailyStatsService dailyStatsService, OutgoingRepository outgoingRepository) {
        this.dailyStatsService = dailyStatsService;
        this.outgoingRepository = outgoingRepository;
    }


    @GetMapping("dailystats/rating")
    public String showDailyStats(Model model) {
        List<String> initialRequest = new ArrayList<>();
        initialRequest.add(LocalDate.now().toString().substring(0, 8) + "01");
        initialRequest.add(LocalDate.now().toString());
        initialRequest.add("overall");
        boolean isNewRequestCopy = isNewRequest;
        if(request.size()==0){
            request = new ArrayList(initialRequest);
        }

        model.addAttribute("stats", new ArrayList(dailyStats));
        model.addAttribute("rating",new ArrayList(rating));
        model.addAttribute("request", request);
        model.addAttribute("outgoinglist",outgoingRepository.findAll());
        model.addAttribute("outgoingstring",outgoingRepository.findAll().toString());
        model.addAttribute("isNewRequest",isNewRequestCopy);
        model.addAttribute("error",error);

        request = new ArrayList(initialRequest);
        dailyStats = new ArrayList<>();
        rating = new ArrayList<>();
        isNewRequest = true;
        error = null;

        return "dailystats/rating";
    }


    @PostMapping("rating")
    public String viewRatings(@RequestParam MultiValueMap<String, String> formData) throws Exception {

        dailyStats = new ArrayList<>();
        rating = new ArrayList<>();


        String startdate = formData.getFirst("startdate");
        request.set(0, startdate);
        String enddate = formData.getFirst("enddate");
        request.set(1, enddate);
        String criterion = formData.getFirst("criterion");
        request.set(2, criterion);

        try {
            if (criterion.equals("Overall")) {
                rating = dailyStatsService.getRating(dailyStatsService.getAllStats(Date.valueOf(startdate), Date.valueOf(enddate)));
            } else {
                dailyStats = dailyStatsService.filter(dailyStatsService.getAllStats(Date.valueOf(startdate), Date.valueOf(enddate)), criterion);
            }
           isNewRequest = false;
            return "redirect:/dailystats/rating";
        }
        catch (Exception e){
            error=e.toString()+"\n";
            for (StackTraceElement el: e.getStackTrace()
            ) {
                error+=el+"\n";
                System.out.println(el);
            }

            return "redirect:/dailystats/rating";
        }
    }


}
