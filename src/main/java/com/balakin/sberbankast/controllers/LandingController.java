package com.balakin.sberbankast.controllers;



import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Position;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.services.DailyStatsService;
import com.balakin.sberbankast.services.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@Slf4j
public class LandingController {
    private final DailyStatsService dailyStatsService;
    private final OperatorRepository operatorService;
    private final PositionService positionService;

    public LandingController(DailyStatsService dailyStatsService, OperatorRepository operatorService, PositionService positionService) {
        this.dailyStatsService = dailyStatsService;
        this.operatorService = operatorService;
        this.positionService = positionService;
    }


    @GetMapping("/landing")
    public String showDailyStats(Model model) {

        int allOperators = operatorService.countAllByIdNotNull();
        int incomingOperators = operatorService.countAllByIncomingTrue();
        int outgoingOperators = operatorService.countAllByOutgoingTrue();
        int stakeOperators = operatorService.countAllByStakeTrue();
        String date = LocalDate.now().toString();
        String startDate = date.substring(0,date.length()-2)+"01";
        System.out.println(startDate+"!!!");
        DailyStats totalStats = dailyStatsService.getTotalStats(dailyStatsService.getAllStats(Date.valueOf(startDate),Date.valueOf(date)));

        Long positions = totalStats.getTotalWorkTime();
        double pos = Double.valueOf(positions);
        Long labourDays = positionService.getLabourdays(LocalDate.parse(startDate),LocalDate.parse(date));
        pos=pos/3600/7.75/labourDays;
        String formattedPos = String.format("%.2f", pos);


        model.addAttribute("date", LocalDate.now());
        model.addAttribute("operators", allOperators);
        model.addAttribute("incomingoperators", incomingOperators);
        model.addAttribute("outgoingoperators", outgoingOperators);
        model.addAttribute("stakeoperators", stakeOperators);
        model.addAttribute("stats",totalStats);
        model.addAttribute("positions",formattedPos);
        model.addAttribute("labourdays",labourDays);



        return "/landing";
    }


}
