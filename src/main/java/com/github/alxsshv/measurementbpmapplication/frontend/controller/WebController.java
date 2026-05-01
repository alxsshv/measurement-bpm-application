package com.github.alxsshv.measurementbpmapplication.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping("/")
    public String getFirstView(){
        return "index";
    }

    // Работа с поверителями
    @GetMapping("/employees/form")
    public String getEmployeeFormView(){
        return "employee/form";
    }

    @GetMapping("/employees")
    public String getEmployeeListView(){
        return "employee/list";
    }

    @GetMapping("/employees/{id}")
    public String getEmployeeView(@RequestParam("id") String id, Model model){
        model.addAttribute("id",id);
        return "employee/card";
    }

    @GetMapping("/employees/form/{id}")
    public String getEditEmployeeForm(@RequestParam("id") String id, Model model){
        model.addAttribute("id",id);
        return "employee/edit";
    }

    @GetMapping("/tasks/fsa")
    public String getFsaReportTasksListView(){
        return "tasks/fsa/list";
    }

    @GetMapping("/tasks/fsa/form")
    public String getFsaReportTasksFormView(){
        return "tasks/fsa/form";
    }

    @GetMapping("/fsa-converters")
    public String getFsaConvertForm(){
        return "fsa/form";
    }

    @GetMapping("/arshin-converters")
    public String getArshinConvertForm(){
        return "arshin/form";
    }

}
