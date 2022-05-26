package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SessionStore {

    Optional<Session> add(Session session);

    boolean update(Session session);

    boolean delete(Session session);

    List<Session> findAll();

    Optional<Session> findById(int id);

    void deleteAll();

    Map<Integer, Integer> getOccupiedRow(Session session);

    List<Integer> getOccupiedCell(Session session, int row);
}
