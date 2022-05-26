package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserName("Гость");
        }
        return user;
    }

    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUser", getUser(session));
        return "users";
    }

    @GetMapping("/formUpdateUser/{userId}")
    public String formUpdateUser(Model model, HttpSession session, @PathVariable("userId") int id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("currentUser", getUser(session));
        return "saveUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        if (userService.save(user)) {
            return "redirect:/users";
        } else {
            session.setAttribute("message", "Пользователь с такой почтой или телефоном уже существует");
            session.setAttribute("user", user);
            return "redirect:/usersFail";
        }
    }

    @GetMapping("/usersFail")
    public String usersFail(Model model,
                            HttpSession session) {
        String message = (String) session.getAttribute("message");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        model.addAttribute("currentUser", getUser(session));
        return "saveUser";
    }

    @GetMapping("/formAddUser")
    public String formAddUser(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        model.addAttribute("currentUser", getUser(session));
        return "saveUser";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest request) {
        Optional<User> userDb = userService.findUserByEmailAndPhone(
                user.getEmail(), user.getPhone()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
