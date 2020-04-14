package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.OutgoingCommand;
import com.balakin.sberbankast.commands.PositionCommand;
import com.balakin.sberbankast.services.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class PositionController  {
    private final String POSITION_FORM_URL = "dailystats/labourdays";
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping({"dailystats/labourdays/{year}"})
    public String showLabourDays(@PathVariable String year, Model model){
        log.debug("showing labour days in "+year);


        PositionCommand positionCommand = positionService.findPositionByYear(year);

        model.addAttribute("position", positionCommand);


        return POSITION_FORM_URL;
    }

    @PostMapping({"change"})
    public String alterPosition(@Valid @ModelAttribute("position")PositionCommand command, BindingResult bindingResult, Model model){
        System.out.println(command.getYear());
        System.out.println(command.getJanuary());
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                System.out.println(objectError.toString());
                log.debug(objectError.toString());

            });
            model.addAttribute("position", command);
            return  POSITION_FORM_URL;
        }

        PositionCommand savedPositionCommand = positionService.savePositionCommand(command);

        log.debug("saved position id:" + savedPositionCommand.getId());

        return "redirect:/" + POSITION_FORM_URL+"/"+savedPositionCommand.getYear();
    }

    @PostMapping({"changeyear"})
    public String changeYear(@RequestParam MultiValueMap<String, String> formData){

        String year = formData.getFirst("year");

        return "redirect:/" + POSITION_FORM_URL+"/"+year;
    }
}
