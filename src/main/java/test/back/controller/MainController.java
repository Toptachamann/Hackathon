package test.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index_slash(){
        return "paradigm";
    }


//    @GetMapping("/chat")
//    public String index() {
//        return "chat";
//    }

    @GetMapping("/paradigm")
    public String login() {
        return "redirect:/";
    }
}
