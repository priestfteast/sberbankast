package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.BonusCommand;
import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.services.FineService;
import com.balakin.sberbankast.services.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class FineController {

    private final String OPERATOR_FINE_FORM_URL = "operator/fines/fineform";

    private final OperatorService operatorService;
    private final FineService fineService;

    public FineController(OperatorService operatorService, FineService fineService) {
        this.operatorService = operatorService;
        this.fineService = fineService;
    }

    @GetMapping({"operator/{operatorId}/fines"})
    public String listFines(@PathVariable String operatorId, Model model){
        log.debug("listing fines for operator id: "+operatorId);

        model.addAttribute("operator",operatorService.findCommandById(Long.valueOf(operatorId)));

        return "operator/fines/list";
    }

    @GetMapping({"operator/{operatorId}/fines/{id}/show"})
    public String showOperatorFine(@PathVariable String operatorId, @PathVariable String id, Model model){
        model.addAttribute("fine",fineService.findByOperatorIdAndFineId(Long.valueOf(operatorId),Long.valueOf(id)) );
        return "operator/fines/show";
    }

    @GetMapping({"operator/{operatorId}/fine/new"})
    public String newOperatorFine(@PathVariable String operatorId,Model model){

        //make sure we have a good id value
        OperatorCommand operatorCommand = operatorService.findCommandById(Long.valueOf(operatorId));
        //todo raise exception if null

        //need to return back parent id for hidden form property
        FineCommand fineCommand = new FineCommand();
        fineCommand.setOperatorId(Long.valueOf(operatorId));
        model.addAttribute("fine", fineCommand);



        return "operator/fines/fineform";
    }

    @GetMapping("operator/{operatorId}/fines/{id}/update")
    public String updateOperatorFine(@PathVariable String operatorId,
                                      @PathVariable String id, Model model){
        model.addAttribute("fine", fineService.findByOperatorIdAndFineId(Long.valueOf(operatorId), Long.valueOf(id)));

        return "operator/fines/fineform";
    }

    @PostMapping("operator/{operatorId}/fine")
    public String saveOrUpdate(@Valid @ModelAttribute("fine") FineCommand command, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                System.out.println(objectError.toString());
                log.debug(objectError.toString());

            });

            return OPERATOR_FINE_FORM_URL;
        }
        FineCommand savedCommand = fineService.saveFineCommand(command);

        log.debug("saved operator id:" + savedCommand.getOperatorId());
        log.debug("saved fine id:" + savedCommand.getId());

        return "redirect:/operator/" + savedCommand.getOperatorId() + "/show/";
    }

    @GetMapping("operator/{operatorId}/fines/{id}/delete")
    public String deleteById(@PathVariable String operatorId, @PathVariable String id){
        log.debug("Deleting fine id: "+id);


        fineService.deleteById(Long.valueOf(operatorId), Long.valueOf(id));
        return "redirect:/operator/" + operatorId + "/show/";
    }

}
