package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.BonusCommand;
import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.services.BonusService;
import com.balakin.sberbankast.services.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class BonusController {
    private final OperatorService operatorService;
    private final BonusService bonusService;

    public BonusController(OperatorService operatorService, BonusService bonusService) {
        this.operatorService = operatorService;
        this.bonusService = bonusService;
    }

    @GetMapping({"operator/{operatorId}/bonuses"})
    public String listBonuses(@PathVariable String operatorId, Model model){
        log.debug("listing bonuses for operator id: "+operatorId);

        model.addAttribute("operator",operatorService.findCommandById(Long.valueOf(operatorId)));

        return "operator/bonuses/list";
    }

    @GetMapping({"operator/{operatorId}/bonuses/{id}/show"})
    public String showOperatorBonus(@PathVariable String operatorId, @PathVariable String id, Model model){
        model.addAttribute("bonus",bonusService.findByOperatorIdAndBonusId(Long.valueOf(operatorId),Long.valueOf(id)) );
        return "operator/bonuses/show";
    }

    @GetMapping({"operator/{operatorId}/bonus/new"})
    public String newOperatorBonus(@PathVariable String operatorId,Model model){


        //make sure we have a good id value
        OperatorCommand operatorCommand = operatorService.findCommandById(Long.valueOf(operatorId));
        //todo raise exception if null

        //need to return back parent id for hidden form property
        BonusCommand bonusCommand = new BonusCommand();
        bonusCommand.setOperatorId(Long.valueOf(operatorId));
        model.addAttribute("bonus", bonusCommand);


        return "operator/bonuses/bonusform";
    }

    @GetMapping("operator/{operatorId}/bonuses/{id}/update")
    public String updateOperatorBonus(@PathVariable String operatorId,
                                         @PathVariable String id, Model model){
        model.addAttribute("bonus", bonusService.findByOperatorIdAndBonusId(Long.valueOf(operatorId), Long.valueOf(id)));

        return "operator/bonuses/bonusform";
    }

    @PostMapping("operator/{operatorId}/bonus")
    public String saveOrUpdate(@ModelAttribute BonusCommand command){
        BonusCommand savedCommand = bonusService.saveBonusCommand(command);

        log.debug("saved operator id:" + savedCommand.getOperatorId());
        log.debug("saved bonus id:" + savedCommand.getId());

        return "redirect:/operator/" + savedCommand.getOperatorId() + "/bonuses/" + savedCommand.getId() + "/show";
    }

    @GetMapping("operator/{operatorId}/bonuses/{id}/delete")
    public String deleteById(@PathVariable String operatorId, @PathVariable String id){
        log.debug("Deleting bonus id: "+id);


        bonusService.deleteById(Long.valueOf(operatorId), Long.valueOf(id));
        return "redirect:/operator/"+operatorId+"/bonuses";
    }

}
