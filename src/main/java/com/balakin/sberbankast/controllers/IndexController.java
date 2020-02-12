package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.services.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;



@Slf4j
@Controller
public class IndexController {

    private final OperatorService operatorService;

    public IndexController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    private String data ="name";

    @RequestMapping({"/","/index","/index.html"})
    public String getIndexPage(Model model){

        log.debug("getting index page");
        model.addAttribute("data",data);
        if(data.contains("name"))
            model.addAttribute("operators",operatorService.getOperatorsByName());
        if(data.contains("specialties"))
            model.addAttribute("operators",operatorService.getOperatorsBySpecialties());
        if(data.contains("date of employment"))
            model.addAttribute("operators",operatorService.getOperatorsByEmployementDate());
        return "index";
    }



    @PostMapping("sort")
    public String sortBy (@RequestBody MultiValueMap<String, String> formData){
        data =formData.toString();
        System.out.println(formData);

            return "redirect:/index/";


        }
}
