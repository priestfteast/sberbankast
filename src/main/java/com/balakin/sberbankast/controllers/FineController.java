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
public class FineController {
    private final OperatorService operatorService;

    public FineController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping
    @RequestMapping({"operator/{operatorId}/fines"})
    public String listFines(@PathVariable String operatorId, Model model){
        log.debug("listing fines for operator id: "+operatorId);

        model.addAttribute("operator",operatorService.findCommandById(Long.valueOf(operatorId)));

        return "operator/fines/list";
    }
}
