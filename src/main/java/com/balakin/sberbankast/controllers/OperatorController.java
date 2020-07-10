package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.exceptions.NotFoundException;
import com.balakin.sberbankast.services.OperatorService;
import com.balakin.sberbankast.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class OperatorController {

    private final String OPERATOR_OPERATOR_FORM_URL = "operator/operatorform";
    private final String OPERATOR_OPERATORS = "operator/operators";

    private final OperatorService operatorService;
    private final SpecialtyService specialtyService;

    public OperatorController(OperatorService operatorService, SpecialtyService specialtyService) {
        this.operatorService = operatorService;
        this.specialtyService = specialtyService;
    }

    private String data ="sort by=[name] experience=[all] callout=[every";

    @RequestMapping({"operator/operators","operator/operators.html"})
    public String getIndexPage(Model model){

        log.debug("getting operators page");
        String requestData = data;
        String initialData ="sort by=[name] experience=[all] callout=[every";
        data = initialData;
        model.addAttribute("data",requestData);
        model.addAttribute("specialtylist", specialtyService.listAllSpecialties());
        model.addAttribute("operators",operatorService.getOperators(data));
        return OPERATOR_OPERATORS;
    }


    @PostMapping("sort")
    public String sortBy (@RequestBody MultiValueMap<String, String> formData){
        data =formData.toString();
        System.out.println(formData);

        return "redirect:/"+OPERATOR_OPERATORS;
    }

    @GetMapping({"/operator/{id}/show"})
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("operator", operatorService.findById(Long.valueOf(id)));
        return "operator/show";
    }

    @GetMapping({"/operator/new"})
    public String newOperator(Model model){
        model.addAttribute("operator", new OperatorCommand());
        model.addAttribute("specialtylist", specialtyService.listAllSpecialties());
        return "operator/operatorform";
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

    @GetMapping("operator/{id}/fire")
    public String fireById(@PathVariable String id){
        log.debug("Firing operator id: "+id);
        operatorService.fireById(Long.valueOf(id));
        return "redirect:/"+OPERATOR_OPERATORS;
    }

    @PostMapping("operator")
    public String saveOrUpdate (@Valid @ModelAttribute("operator") OperatorCommand operatorCommand, BindingResult bindingResult,Model model){

       if(bindingResult.hasErrors()){
          bindingResult.getAllErrors().forEach(objectError -> {
              System.out.println(objectError.toString());
              log.debug(objectError.toString());

          });
           model.addAttribute("specialtylist", specialtyService.listAllSpecialties());
          return OPERATOR_OPERATOR_FORM_URL;
       }

        OperatorCommand savedOperatorCommand = operatorService.saveOperatorCommand(operatorCommand);
        return "redirect:/operator/"+savedOperatorCommand.getId()+"/show";
    }

    @ExceptionHandler(NotFoundException.class)
    ModelAndView handleNotFoundException(NotFoundException ex){
        log.error("Handling not found exception");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception",ex.getMessage());
        modelAndView.setViewName("404Error");
        return modelAndView;
    }
}
