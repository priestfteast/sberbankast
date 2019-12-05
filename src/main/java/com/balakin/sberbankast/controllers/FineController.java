package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.services.FineService;
import com.balakin.sberbankast.services.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class FineController {
    private final OperatorService operatorService;
    private final FineService fineService;

    public FineController(OperatorService operatorService, FineService fineService) {
        this.operatorService = operatorService;
        this.fineService = fineService;
    }

    @GetMapping
    @RequestMapping({"operator/{operatorId}/fines"})
    public String listFines(@PathVariable String operatorId, Model model){
        log.debug("listing fines for operator id: "+operatorId);

        model.addAttribute("operator",operatorService.findCommandById(Long.valueOf(operatorId)));

        return "operator/fines/list";
    }

    @GetMapping
    @RequestMapping({"operator/{operatorId}/fines/{id}/show"})
    public String showOperatorFine(@PathVariable String operatorId, @PathVariable String id, Model model){
        model.addAttribute("fine",fineService.findByOperatorIdAndFineId(Long.valueOf(operatorId),Long.valueOf(id)) );
        return "operator/fines/show";
    }
}
