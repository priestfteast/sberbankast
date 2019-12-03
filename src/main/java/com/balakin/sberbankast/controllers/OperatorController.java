package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.services.OperatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @RequestMapping({"/operator/{id}/show"})
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("operator", operatorService.findById(Long.valueOf(id)));
        return "operator/show";
    }

    @RequestMapping({"/operator/new"})
    public String newOperator(Model model){
        model.addAttribute("operator", new OperatorCommand());
        return "/operator/operatorform";
    }

    @RequestMapping({"/operator/{id}/update"})
    public String updateOperator(@PathVariable String id, Model model){

        model.addAttribute("operator", operatorService.findCommandById(Long.valueOf(id)));
        return "operator/operatorform";
    }

    @PostMapping("operator")
    public String saveOrUpdate (@ModelAttribute OperatorCommand operatorCommand, Model model){
       OperatorCommand savedOperatorCommand = operatorService.saveOperatorCommand(operatorCommand);

        return "redirect:/operator/"+savedOperatorCommand.getId()+"/show";
    }
}
