package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.services.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

    private final OperatorService operatorService;

    public IndexController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @RequestMapping({"/","/index","/index.html"})
    public String getIndexPage(Model model){
        log.debug("getting index page");
        model.addAttribute("operators",operatorService.getOperators());
        return "index.html";
    }
}
