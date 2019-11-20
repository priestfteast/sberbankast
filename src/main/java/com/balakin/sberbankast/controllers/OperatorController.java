package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.services.OperatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @RequestMapping({"/operator/show/{id}"})
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("operator", operatorService.findById(Long.valueOf(id)));
        return "operator/show";
    }
}
