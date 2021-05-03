package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.OutgoingCommand;
import com.balakin.sberbankast.services.OutgoingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class OutgoingController {
    private final String OUTGOING_FORM_URL = "dailystats/outgoinglist";

    private final OutgoingService outgoingService;

    public OutgoingController(OutgoingService outgoingService) {
        this.outgoingService = outgoingService;
    }


    @GetMapping({"dailystats/outgoinglist"})
    public String listOutgoing(Model model){
        log.debug("listing outgoing numbers excluded from total statistics");
//need to return back parent id for hidden form property

        OutgoingCommand outgoingCommand = new OutgoingCommand();

        model.addAttribute("outgoing", outgoingCommand);
        model.addAttribute("outgoingList",outgoingService.findAllOutgoing());

        return OUTGOING_FORM_URL;
    }



   @PostMapping({"add"})
   public String addOutgoing(@Valid @ModelAttribute("outgoing")OutgoingCommand command, BindingResult bindingResult, Model model){
       if(bindingResult.hasErrors()){
           bindingResult.getAllErrors().forEach(objectError -> {
               System.out.println(objectError.toString());
               log.debug(objectError.toString());

           });

           return OUTGOING_FORM_URL;
       }

       OutgoingCommand savedOutgoingCommand = outgoingService.saveOutgoingCommand(command);

       log.debug("saved outgoing id:" + savedOutgoingCommand.getId());

       return "redirect:/" + OUTGOING_FORM_URL;
   }


    @GetMapping("dailystats/outgoinglist/{id}/delete")
    public String deleteById(@PathVariable String id){
        System.out.println("!!!!!!");
        log.debug("Deleting outgoing id: "+id);


        outgoingService.deleteById(Long.valueOf(id));
        return "redirect:/"+OUTGOING_FORM_URL;
    }

}
