package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class TicketController {
    private final SessionService sessionService;
    private final TicketService ticketService;

    public TicketController(SessionService sessionService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    private User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserName("Гость");
        }
        return user;
    }

    @GetMapping("/buy-ticket")
    public String buyTicket(Model model, HttpSession session) {
        User user = getUser(session);
        if (user.getId() == 0) {
            return "redirect:/loginPage";
        }
        model.addAttribute("user", user);
        model.addAttribute("sessions", sessionService.findAll());
        return "buyTicket";
    }

    @PostMapping("/buy-ticket-select-row")
    public String buyTicketSelectRow(Model model, HttpSession session, @RequestParam("session_id") int id) {
        model.addAttribute("user", getUser(session));
        Session currentSession = sessionService.findById(id).get();
        model.addAttribute("currentSession", currentSession);
        model.addAttribute("rows", sessionService.getFreeRow(currentSession));
        return "buyTicketSelectRow";
    }

    @PostMapping("/buy-ticket-select-cell")
    public String buyTicketSelectCell(Model model,
                                      HttpSession session,
                                      @RequestParam("session_id") int id,
                                      @RequestParam("row") int row) {
        model.addAttribute("user", getUser(session));
        Session currentSession = sessionService.findById(id).get();
        model.addAttribute("currentSession", currentSession);
        model.addAttribute("row", row);
        model.addAttribute("cells", sessionService.getFreeCell(currentSession, row));
        return "buyTicketSelectCell";
    }

    @PostMapping("/addTicket")
    public String addTicket(HttpSession session,
                            @RequestParam("session_id") int id,
                            @RequestParam("row") int row,
                            @RequestParam("cell") int cell) {
        User user = getUser(session);
        Session currentSession = sessionService.findById(id).get();
        Optional<Ticket> ticket = ticketService.add(new Ticket(0, currentSession, row, cell, user));
        return ticket.isPresent() ? "redirect:/formTicket/" + ticket.get().getId() : "redirect:/index";
    }

    @GetMapping("/formTicket/{ticketId}")
    public String formTicket(Model model, @PathVariable("ticketId") int id, HttpSession session) {
        model.addAttribute("ticket", ticketService.findById(id).orElse(new Ticket()));
        model.addAttribute("user", getUser(session));
        return "ticket";
    }
}
