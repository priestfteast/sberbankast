package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.SberbankastApplication;
import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.services.ParseXlsService;
import com.balakin.sberbankast.services.UploadXlsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class XlsController {

    private final UploadXlsService uploadXlsService;
    private final OperatorRepository operatorRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ParseXlsService parseXlsService;
    private final String PATH = "C:\\java\\sber-ast\\";

    private List<DailyStats> dailyStats=new ArrayList<>();
    private String error = null;

    public XlsController(UploadXlsService uploadXlsService, OperatorRepository operatorRepository, DailyStatsRepository dailyStatsRepository, ParseXlsService parseXlsService) {
        this.uploadXlsService = uploadXlsService;
        this.operatorRepository = operatorRepository;
        this.dailyStatsRepository = dailyStatsRepository;
        this.parseXlsService = parseXlsService;
    }

    @GetMapping("upload/dailystats")
    public String showUploadForm( Model model){
        for (File myFile : new File(PATH).listFiles())
            if (myFile.isFile()) myFile.delete();

        List<DailyStats> nullStats = new ArrayList<>();
        if(dailyStats.size()>0) {
            for (DailyStats ds : dailyStats
            ) {
                if (ds.getOperator()==null)
                    nullStats.add(ds);
            }
        }

        model.addAttribute("stats",dailyStats);
        model.addAttribute("nullStats", nullStats);
        model.addAttribute("error", error);
        error=null;
        return "upload/dailystats";
    }

    @GetMapping("/save")
    public String saveStats()  {
        for (File myFile : new File(PATH).listFiles())
            if (myFile.isFile()) myFile.delete();
        int size = dailyStats.size();
        Date date = dailyStats.get(0).getDate();
        if(dailyStatsRepository.getFirstByDate(date)!=null) {
          error="В базе уже сохранены данные " +
                    "по статистике за " + date +".\n"+
                    " Удалите старые данные перед их перезагрузкой" +".\n";
            return "redirect:/upload/dailystats";
        }
        dailyStatsRepository.saveAll(dailyStats);
       log.debug("saved "+size+" daily stats for "+date);
        System.out.println("saved "+size+" daily stats for "+date);
        dailyStats=new ArrayList<>();
        return "redirect:/upload/dailystats";
    }

    @PostMapping("static/dailystats")
    public String UploadStats( @RequestParam("xlsstats") MultipartFile file)  {

        try {
            error=null;
            dailyStats = new ArrayList<>();

            uploadXlsService.uploadXls(file);


            dailyStats = parseXlsService.parseStatsXml(PATH + file.getOriginalFilename(), operatorRepository, dailyStatsRepository);
            List<DailyStats> nullStats = new ArrayList<>();
            if (dailyStats.size() > 0) {
                for (DailyStats ds : dailyStats
                ) {
                    if (ds.getOperator() == null)
                        nullStats.add(ds);
                }
            }
            if (nullStats.size() > 0) {
                Files.deleteIfExists(Paths.get(PATH + file.getOriginalFilename()));
                System.out.println("File deleted");
            } else {
//            dailyStatsRepository.saveAll(dailyStats);
                System.out.println("Daily stats saved");
            }

            return "redirect:/upload/dailystats";
        }
        catch (Exception e){
            error=e.toString()+"\n";
            for (StackTraceElement el: e.getStackTrace()
            ) {
                error+=el+"\n";
                System.out.println(el);
            }

            return "redirect:/upload/dailystats";
        }
    }

    @PostMapping("lost406")
    public String uploadLost( @RequestParam("xlslost") MultipartFile file) throws Exception {

      if(dailyStats.size()<1) {
          error = "You should upload dailyStats first!!!";
          return "redirect:/upload/dailystats";
      }

        uploadXlsService.uploadXls(file);
        dailyStats = parseXlsService.parseLostXml(PATH+file.getOriginalFilename(),dailyStats);
        return "redirect:/upload/dailystats";
    }

    @Transactional
    @PostMapping("deletestats")
    public String removeStatistics(Date date) {
        if(dailyStatsRepository.getFirstByDate(date)==null) {
           error="В базе нет данных " +
                    "по статистике за " + date +".\n"+
                    " Выберите другую дату" +".\n";
            return "redirect:/upload/dailystats";
        }
        dailyStatsRepository.deleteAllByDate(date);
        log.debug("Deleted statistics for "+date);
        System.out.println("Deleted statistics for "+date);
        return "redirect:/upload/dailystats";
    }

}
