package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.Ponto;
import com.balakin.sberbankast.services.OperatorService;
import com.balakin.sberbankast.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
public class IndexController {

    private final OperatorService operatorService;
    private final SpecialtyService specialtyService;

    public IndexController(OperatorService operatorService, SpecialtyService specialtyService) {
        this.operatorService = operatorService;
        this.specialtyService = specialtyService;
    }

    private String data ="sort by=[name] experience=[all] callout=[every";

    @RequestMapping({"/","/index","/index.html"})
    public String getIndexPage(Model model){

        log.debug("getting index page");

        List<Ponto> pontos = new ArrayList<>();

        Ponto ponto = new Ponto();
        ponto.setX(15);
        ponto.setY(15);
        pontos.add(ponto);

        ponto = new Ponto();
        ponto.setX(20);
        ponto.setY(21);
        pontos.add(ponto);

        ponto = new Ponto();
        ponto.setX(30);
        ponto.setY(8);
        pontos.add(ponto);

        model.addAttribute("pontos",pontos);

        model.addAttribute("data",data);
        model.addAttribute("specialtylist", specialtyService.listAllSpecialties());
        model.addAttribute("operators",operatorService.getOperators(data));
        return "index";
    }


    @PostMapping("sort")
    public String sortBy (@RequestBody MultiValueMap<String, String> formData){
        data =formData.toString();
        System.out.println(formData);

        return "redirect:/index/";
        }
}
