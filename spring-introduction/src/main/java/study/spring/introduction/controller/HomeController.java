package study.spring.introduction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Spring Container에 Spring Bean 등록(@Component)
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
