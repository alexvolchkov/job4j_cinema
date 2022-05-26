package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface IntTicketService {
    Optional<Ticket> add(Ticket ticket);

    boolean update(Ticket ticket);

    boolean delete(Ticket ticket);

    List<Ticket> findAll();

    Optional<Ticket> findById(int id);

    void deleteAll();
}
