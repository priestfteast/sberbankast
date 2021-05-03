package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.repositories.OutgoingRepository;
import com.balakin.sberbankast.services.DailyStatsService;
import com.balakin.sberbankast.services.ParseXlsService;
import com.balakin.sberbankast.services.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private final ParseXlsService parseXlsService;
    private final PositionService positionService;

    private List<DailyStats> dailyStats = new ArrayList<>();
    private List<DailyStats> dailyStatsForXls = new ArrayList<>();
    private DailyStats dstats = new DailyStats();
    private List<String> request = new ArrayList<>();
    private List<String> requestForXLS = new ArrayList<>();
    private String error = null;
    private boolean isNewRequest =true;

    public DailyStatsController(DailyStatsRepository dailyStatsRepository, OperatorRepository operatorRepository, DailyStatsService dailyStatsService, OutgoingRepository outgoingRepository, ParseXlsService parseXlsService, PositionService positionService) {
        this.dailyStatsRepository = dailyStatsRepository;
        this.operatorRepository = operatorRepository;
        this.dailyStatsService = dailyStatsService;
        this.outgoingRepository = outgoingRepository;
        this.parseXlsService = parseXlsService;
        this.positionService = positionService;
    }


    @GetMapping("dailystats/view")
    public String showDailyStats(Model model) {
        List<String> initialRequest = new ArrayList<>();
        initialRequest.add(LocalDate.now().toString().substring(0, 8) + "01");
        initialRequest.add(LocalDate.now().toString());
        initialRequest.add("all");
        boolean isNewRequestCopy = isNewRequest;

    if (request.size() == 0) {
        request = new ArrayList(initialRequest);
        dstats=new DailyStats();
        dailyStats =new ArrayList<>();
    }
    ArrayList<Operator> operators = (ArrayList<Operator>) operatorRepository.findAll();
    Collections.sort(operators, new Comparator<Operator>() {
        @Override
        public int compare(Operator o1, Operator o2) {
            return o1.getLastName().compareTo(o2.getLastName());
        }
    });

    model.addAttribute("operators", operators);
    model.addAttribute("stats", new ArrayList(dailyStats));
    model.addAttribute("dstats", dstats);
    model.addAttribute("request", request);
    model.addAttribute("error", error);
    model.addAttribute("outgoinglist", outgoingRepository.findAll());
    model.addAttribute("outgoingstring", outgoingRepository.findAll().toString());
    model.addAttribute("isNewRequest",isNewRequestCopy);
    requestForXLS = new ArrayList<>(request);
    request = new ArrayList(initialRequest);
    dailyStatsForXls = new ArrayList<>(dailyStats);
    dailyStats = new ArrayList<>();
    isNewRequest = true;

    error = null;

    return "dailystats/view";
}


    @PostMapping("viewstats")
    public String viewStats(@RequestParam MultiValueMap<String, String> formData)  {


        dailyStats = new ArrayList<>();
        dstats = new DailyStats();


        String startdate = formData.getFirst("startdate"); request.set(0,startdate);
        String enddate = formData.getFirst("enddate");request.set(1,enddate);
        String operator = formData.getFirst("operator");request.set(2,operator);

        try {
            if (operator.equals("all")) {
                dailyStats = dailyStatsService.getAllStats(Date.valueOf(startdate), Date.valueOf(enddate));
                dstats = dailyStatsService.getTotalStats(dailyStats);
            } else {
                dailyStats = dailyStatsService.getOperatorStats(Date.valueOf(startdate), Date.valueOf(enddate), operator);
                dstats = dailyStatsService.getTotalOperatorStats(dailyStats);
            }
            isNewRequest=false;
            Collections.sort(dailyStats, new Comparator<DailyStats>() {
                @Override
                public int compare(DailyStats d1, DailyStats d2) {
                    return d1.getOperator().getLastName().compareTo(d2.getOperator().getLastName());
                }
            });
            return "redirect:/dailystats/view";
        }
        catch (Exception e){
            error=e.toString()+"\n";
            for (StackTraceElement el: e.getStackTrace()
                 ) {
                error+=el+"\n";
                System.out.println(el);
            }
            return "redirect:/dailystats/view";
        }
    }

//    @RequestMapping("/excel")
//    public String openXls() throws Exception {
//        File xls = parseXlsService.saveStatsToXLS(request,dailyStats, dstats);
//        Runtime.getRuntime().exec(new String[]
//                {"rundll32", "url.dll,FileProtocolHandler",
//                        xls.getAbsolutePath()});
//        return "redirect:/dailystats/view";
//    }
@ResponseBody
@RequestMapping(value = "/excel", produces = "application/vnd.ms-excel")
    public FileSystemResource doAction(HttpServletResponse response) throws Exception {

        File xls = parseXlsService.saveStatsToXLS(requestForXLS,dailyStatsForXls, dstats, positionService);
        String header = "attachment; filename="+xls.getName();
    response.setHeader("Content-Disposition", header);
    return new FileSystemResource(xls);
    }


    }
