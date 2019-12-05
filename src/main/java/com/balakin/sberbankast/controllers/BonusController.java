package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.services.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class BonusController {
    private final OperatorService operatorService;

    public BonusController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping
    @RequestMapping({"operator/{operatorId}/bonuses"})
    public String listBonuses(@PathVariable String operatorId, Model model){
        log.debug("listing bonuses for operator id: "+operatorId);

        model.addAttribute("operator",operatorService.findCommandById(Long.valueOf(operatorId)));

        return "operator/bonuses/list";
    }
}
