package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;

@Controller
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/formAddSession")
    public String formAddSession(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserName("Гость");
        }
        model.addAttribute("newSession", new Session());
        model.addAttribute("user", user);
        return "addSession";
    }

    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session session) {
        sessionService.add(session);
        return "redirect:/index";
    }
}
