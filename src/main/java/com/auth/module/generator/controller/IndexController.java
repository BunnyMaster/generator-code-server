package com.auth.module.generator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "web-pages/index";
    }

    @GetMapping("/database-parser")
    public String databaseParser() {
        return "web-pages/database-parser";
    }

    @GetMapping("sql-parser")
    public String sqlParser() {
        return "web-pages/sql-parser";
    }
}