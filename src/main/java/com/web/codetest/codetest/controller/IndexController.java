package com.web.codetest.codetest.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/codetest")
    public String index(@RequestParam(name="name",required = false,defaultValue = "wangxu") String name, Model model){
        model.addAttribute("name",name);
        return "index";
    }

}
