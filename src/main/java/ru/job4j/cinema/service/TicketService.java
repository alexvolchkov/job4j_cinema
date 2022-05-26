package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.TicketStore;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements IntTicketService {
    private final TicketStore store;

    public TicketService(TicketStore store) {
        this.store = store;
    }

    @Override
    public Optional<Ticket> add(Ticket ticket) {
        return store.add(ticket);
    }

    @Override
    public boolean update(Ticket ticket) {
        return store.update(ticket);
    }

    @Override
    public boolean delete(Ticket ticket) {
        return store.delete(ticket);
    }

    @Override
    public List<Ticket> findAll() {
        return store.findAll();
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return store.findById(id);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }
}
