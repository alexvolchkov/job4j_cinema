package ru.job4j.cinema.store;

import org.junit.After;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SessionDbStoreTest {

    @After
    public void CleanDB() {
        SessionStore store = new SessionDbStore(new Main().loadPool());
        store.deleteAll();
    }

    @Test
    public void whenCreateSession() {
        SessionStore store = new SessionDbStore(new Main().loadPool());
        Session session = new Session(0, "Session", 3, 3);
        store.add(session);
        Session sessionInDb = store.findById(session.getId()).orElse(new Session());
        assertThat(sessionInDb.getName(), is(session.getName()));
    }

    @Test
    public void update() {
        SessionStore store = new SessionDbStore(new Main().loadPool());
        Session session = new Session(0, "Session", 3, 3);
        store.add(session);
        session.setName("New name");
        store.update(session);
        Session sessionInDb = store.findById(session.getId()).orElse(new Session());
        assertThat(sessionInDb.getName(), is(session.getName()));
    }

    @Test
    public void delete() {
        SessionStore store = new SessionDbStore(new Main().loadPool());
        Session session = new Session(0, "Session", 3, 3);
        store.add(session);
        store.delete(session);
        assertTrue(store.findById(session.getId()).isEmpty());
    }

    @Test
    public void findAll() {
        SessionStore store = new SessionDbStore(new Main().loadPool());
        Session session1 = new Session(0, "Session1", 3, 3);
        Session session2 = new Session(0, "Session2", 3, 3);
        store.add(session1);
        store.add(session2);
        List<Session> sessions = store.findAll();
        assertThat(sessions.size(), is(2));
        assertTrue(sessions.containsAll(List.of(session1, session2)));
    }

    @Test
    public void findById() {
        SessionStore store = new SessionDbStore(new Main().loadPool());
        Session session1 = new Session(0, "Session1", 3, 3);
        Session session2 = new Session(0, "Session2", 3, 3);
        store.add(session1);
        store.add(session2);
        Session sessionInDb1 = store.findById(session1.getId()).orElse(new Session());
        Session sessionInDb2 = store.findById(session2.getId()).orElse(new Session());
        assertThat(sessionInDb1.getName(), is(session1.getName()));
        assertThat(sessionInDb2.getName(), is(session2.getName()));
    }
}