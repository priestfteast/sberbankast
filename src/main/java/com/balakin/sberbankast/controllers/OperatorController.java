package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.services.OperatorService;
import com.balakin.sberbankast.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class OperatorController {

    private final OperatorService operatorService;
    private final SpecialtyService specialtyService;

    public OperatorController(OperatorService operatorService, SpecialtyService specialtyService) {
        this.operatorService = operatorService;
        this.specialtyService = specialtyService;
    }

    @GetMapping({"/operator/{id}/show"})
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("operator", operatorService.findById(Long.valueOf(id)));
        return "operator/show";
    }

    @GetMapping({"/operator/new"})
    public String newOperator(Model model){
        model.addAttribute("operator", new OperatorCommand());
        return "/operator/operatorform";
    }
    @GetMapping({"/operator/{id}/update"})
    public String updateOperator(@PathVariable String id, Model model){

        model.addAttribute("operator", operatorService.findCommandById(Long.valueOf(id)));
        model.addAttribute("specialtylist", specialtyService.listAllSpecialties());

        return "operator/operatorform";
    }

    @GetMapping("operator/{id}/delete")
    public String deleteById(@PathVariable String id){
        log.debug("Deleting operator id: "+id);
        operatorService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    @PostMapping("operator")
    public String saveOrUpdate (@ModelAttribute OperatorCommand operatorCommand){
       OperatorCommand savedOperatorCommand = operatorService.saveOperatorCommand(operatorCommand);

        return "redirect:/operator/"+savedOperatorCommand.getId()+"/show";
    }
}
