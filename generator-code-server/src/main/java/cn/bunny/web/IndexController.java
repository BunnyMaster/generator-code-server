package cn.bunny.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/main";
    }

    @GetMapping("main")
    public String databasePage() {
        return "main";
    }

    @GetMapping("/sql")
    public String sqlPage() {
        return "sql";
    }

}
