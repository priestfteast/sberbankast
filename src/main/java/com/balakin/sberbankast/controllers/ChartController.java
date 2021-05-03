package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.repositories.OutgoingRepository;
import com.balakin.sberbankast.services.DailyStatsService;
import com.balakin.sberbankast.services.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
@Slf4j
public class ChartController {
    private final DailyStatsService dailyStatsService;
    private final OperatorRepository operatorRepository;
    private final PositionService positionService;

    private List<DailyStats> dailyStats = new ArrayList<>();
    private List<String> request = new ArrayList<>();
    private String error = null;

    public ChartController(DailyStatsService dailyStatsService, OutgoingRepository outgoingRepository, OperatorRepository operatorRepository, PositionService positionService) {
        this.dailyStatsService = dailyStatsService;
        this.operatorRepository = operatorRepository;
        this.positionService = positionService;
    }


    @GetMapping("dailystats/charts")
    public String showDailyStats(Model model) {
        List<String> initialRequest = new ArrayList<>();
        initialRequest.add(LocalDate.now().toString().substring(0, 8) + "01");
        initialRequest.add(LocalDate.now().toString());
        initialRequest.add("all");
        initialRequest.add("Total loged in time");
        initialRequest.add("null");

        try {
            if (request.size() == 0) {
                request = new ArrayList(initialRequest);
            }

            ArrayList<Operator> operators = (ArrayList<Operator>) operatorRepository.findAll();
            Collections.sort(operators, new Comparator<Operator>() {
                @Override
                public int compare(Operator o1, Operator o2) {
                    return o1.getLastName().compareTo(o2.getLastName());
                }
            });


            model.addAttribute("operators", operators);
            model.addAttribute("request", request);

            String[] days = dailyStatsService.getChartDays(request.get(0), request.get(1), request.get(2));
            List<Long[]> chartsData = dailyStatsService.getChartsData(request.get(0), request.get(1), request.get(2));
            Long[] totalWorkTime = chartsData.get(2);
            Long[] totalIncoming = chartsData.get(0);
            Long[] outgoingExternal = chartsData.get(1);
            Long[] talkTimeAvg = chartsData.get(3);
            Long[] holdTimeAvg = chartsData.get(4);
            Long[] afterCallAvg = chartsData.get(5);
            Long[] lost406 = chartsData.get(6);
            Long[] opers = chartsData.get(7);
            Long[] operatorsquantity = chartsData.get(8);
            Long[] totalTime = chartsData.get(9);
            Long[] calls = chartsData.get(10);
            Long[] callsAvg = chartsData.get(11);

            Long positions = 0L;

            for (Long l : totalWorkTime
            ) {
                positions += l;
            }
            double pos = Double.valueOf(positions);
            Long labourDays = positionService.getLabourdays(LocalDate.parse(request.get(0)), LocalDate.parse(request.get(1)));
//            System.out.println(positions);
//            System.out.println(labourDays);
            pos = pos / 3600 / 7.75 / labourDays;
            String formattedPos = String.format("%.2f", pos);


            model.addAttribute("days", days);
            model.addAttribute("totalworktime", totalWorkTime);
            model.addAttribute("incoming", totalIncoming);
            model.addAttribute("outgoingexternal", outgoingExternal);
            model.addAttribute("lost406", lost406);
            model.addAttribute("talktimeavg", talkTimeAvg);
            model.addAttribute("holdtimeavg", holdTimeAvg);
            model.addAttribute("aftercallavg", afterCallAvg);
            model.addAttribute("opers", opers);
            model.addAttribute("opersquantity", operatorsquantity[0]);
            model.addAttribute("totaltime", new DailyStats().getTime(totalTime[0]));
            model.addAttribute("positions", formattedPos);
            model.addAttribute("labourdays", labourDays);
            model.addAttribute("calls", calls);
            model.addAttribute("callsavg", callsAvg);
            request = new ArrayList<>(initialRequest);



            return "dailystats/charts";
        }
        catch (Exception e){
            error=e.toString()+"\n";
            for (StackTraceElement el: e.getStackTrace()
            ) {
                error+=el+"\n";
                System.out.println(el);
            }
            model.addAttribute("error",error);
            error=null;

            return "templates/dailystats/dailystats/charts";
        }
    }


    @PostMapping("charts")
    public String viewRatings(@RequestParam MultiValueMap<String, String> formData) throws Exception {

        dailyStats = new ArrayList<>();

        String startdate = formData.getFirst("startdate"); request.set(0,startdate);
        String enddate = formData.getFirst("enddate");request.set(1,enddate);
        String operator = formData.getFirst("operator");request.set(2,operator);
        String criterion = formData.getFirst("criterion");request.set(3,criterion);
        request.set(4,"not null");

        return "redirect:/dailystats/charts";
    }

}
