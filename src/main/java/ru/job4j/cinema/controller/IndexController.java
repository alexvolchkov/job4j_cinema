package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    private final SessionService sessionService;

    public IndexController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    private User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserName("Гость");
        }
        return user;
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("user", getUser(session));
        return "index";
    }
}
