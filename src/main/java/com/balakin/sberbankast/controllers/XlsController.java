package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.DailyStats;
import com.balakin.sberbankast.repositories.DailyStatsRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.services.ParseXlsService;
import com.balakin.sberbankast.services.ParseXlsServiceImpl;
import com.balakin.sberbankast.services.UploadXlsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
public class XlsController {

    private final UploadXlsService uploadXlsService;
    private final OperatorRepository operatorRepository;
    private final DailyStatsRepository dailyStatsRepository;

    public XlsController(UploadXlsService uploadXlsService, OperatorRepository operatorRepository, DailyStatsRepository dailyStatsRepository) {
        this.uploadXlsService = uploadXlsService;
        this.operatorRepository = operatorRepository;
        this.dailyStatsRepository = dailyStatsRepository;
    }

    @GetMapping("upload/dailystats")
    public String showUploadForm( Model model){


        return "upload/dailystats";
    }

    @PostMapping("dailystats")
    public String handleImagePost( @RequestParam("xlsfile") MultipartFile file) throws Exception {

       uploadXlsService.uploadXls(file);

       ParseXlsService parseXlsService = new ParseXlsServiceImpl();

       List<DailyStats> dailyStats = parseXlsService.parseXml("C:\\Users\\Jeremy\\sberbankast\\src\\main\\resources\\dailystats\\"+file.getOriginalFilename(),operatorRepository, dailyStatsRepository);

       dailyStatsRepository.saveAll(dailyStats);
       return "redirect:/upload/dailystats";
    }
}
