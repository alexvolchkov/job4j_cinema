package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.store.SessionStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SessionService implements IntSessionService {
    private final SessionStore store;

    public SessionService(SessionStore store) {
        this.store = store;
    }

    @Override
    public Optional<Session> add(Session session) {
        return store.add(session);
    }

    @Override
    public boolean update(Session session) {
        return store.update(session);
    }

    @Override
    public boolean delete(Session session) {
        return store.delete(session);
    }

    @Override
    public List<Session> findAll() {
        return store.findAll();
    }

    @Override
    public Optional<Session> findById(int id) {
        return store.findById(id);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public List<Integer> getFreeRow(Session session) {
        Map<Integer, Integer> mapOccupiedRow = store.getOccupiedRow(session);
        List<Integer> occupiedRow = new ArrayList<>();
        for (Integer row : mapOccupiedRow.keySet()) {
            if (mapOccupiedRow.get(row) >= session.getNumberOfCells()) {
                occupiedRow.add(row);
            }
        }
        List<Integer> numberRows  = IntStream.range(1, session.getNumberOfRows() + 1)
                .boxed()
                .collect(Collectors.toList());
        numberRows.removeAll(occupiedRow);
        return numberRows;
    }

    @Override
    public List<Integer> getFreeCell(Session session, int row) {
        List<Integer> occupiedCell = store.getOccupiedCell(session, row);
        List<Integer> numberCells  = IntStream.range(1, session.getNumberOfCells() + 1)
                .boxed()
                .collect(Collectors.toList());
        numberCells.removeAll(occupiedCell);
        return numberCells;
    }
}
