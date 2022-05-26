package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Optional;

public interface IntSessionService {

    Optional<Session> add(Session session);

    boolean update(Session session);

    boolean delete(Session session);

    List<Session> findAll();

    Optional<Session> findById(int id);

    void deleteAll();

    List<Integer> getFreeRow(Session session);

    List<Integer> getFreeCell(Session session, int row);
}
