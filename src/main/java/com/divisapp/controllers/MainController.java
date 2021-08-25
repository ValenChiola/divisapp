package com.divisapp.controllers;

import com.divisapp.entities.User;
import static com.divisapp.utils.Texts.*;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    
    @GetMapping
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("usersession");
        return user != null ? "redirect:/user/profile" : INDEX_LABEL;
    }
    
    @GetMapping("/singin")
    public String login() {
        return LOGIN_LABEL;
    }
    
}
