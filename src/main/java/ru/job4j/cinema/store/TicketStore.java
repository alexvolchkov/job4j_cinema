package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketStore {

    Optional<Ticket> add(Ticket ticket);

    boolean update(Ticket ticket);

    boolean delete(Ticket ticket);

    List<Ticket> findAll();

    Optional<Ticket> findById(int id);

    void deleteAll();
}
