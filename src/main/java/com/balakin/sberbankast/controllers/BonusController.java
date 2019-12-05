package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.services.BonusService;
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
    private final BonusService bonusService;

    public BonusController(OperatorService operatorService, BonusService bonusService) {
        this.operatorService = operatorService;
        this.bonusService = bonusService;
    }

    @GetMapping
    @RequestMapping({"operator/{operatorId}/bonuses"})
    public String listBonuses(@PathVariable String operatorId, Model model){
        log.debug("listing bonuses for operator id: "+operatorId);

        model.addAttribute("operator",operatorService.findCommandById(Long.valueOf(operatorId)));

        return "operator/bonuses/list";
    }

    @GetMapping
    @RequestMapping({"operator/{operatorId}/bonuses/{id}/show"})
    public String showOperatorBonus(@PathVariable String operatorId, @PathVariable String id, Model model){
        model.addAttribute("bonus",bonusService.findByOperatorIdAndBonusId(Long.valueOf(operatorId),Long.valueOf(id)) );
        return "operator/bonuses/show";
    }
}
