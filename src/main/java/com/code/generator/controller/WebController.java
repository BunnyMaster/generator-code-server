package com.code.generator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/database";
    }

    @GetMapping("database")
    public String databasePage() {
        return "database";
    }

    @GetMapping("/sql")
    public String sqlPage() {
        return "sql";
    }

}
