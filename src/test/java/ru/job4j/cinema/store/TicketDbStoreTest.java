package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class TicketDbStoreTest {

    @After
    public void destroy() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        SessionStore sessionStore = new SessionDbStore(pool);
        new TicketDbStore(pool, userStore, sessionStore).deleteAll();
        userStore.deleteAll();
        sessionStore.deleteAll();
    }

    @Test
    public void whenCreateTicket() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        userStore.add(user);
        SessionStore sessionStore = new SessionDbStore(pool);
        Session session = new Session(0, "Session", 3, 3);
        sessionStore.add(session);
        TicketStore store = new TicketDbStore(pool, userStore, sessionStore);
        Ticket ticket = new Ticket(1, session, 1, 1, user);
        store.add(ticket);
        Ticket ticketInDb = store.findById(ticket.getId()).orElse(new Ticket());
        assertThat(ticketInDb, is(ticket));
    }

    @Test
    public void whenNotCreateTicketThenNotUniqueSeat() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        userStore.add(user);
        SessionStore sessionStore = new SessionDbStore(pool);
        Session session = new Session(0, "Session", 3, 3);
        sessionStore.add(session);
        TicketStore store = new TicketDbStore(pool, userStore, sessionStore);
        Ticket ticket1 = new Ticket(1, session, 1, 1, user);
        Ticket ticket2 = new Ticket(2, session, 1, 1, user);
        store.add(ticket1);
        assertTrue(store.add(ticket2).isEmpty());
    }

    @Test
    public void update() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        userStore.add(user);
        SessionStore sessionStore = new SessionDbStore(pool);
        Session session = new Session(0, "Session", 3, 3);
        sessionStore.add(session);
        TicketStore store = new TicketDbStore(pool, userStore, sessionStore);
        Ticket ticket = new Ticket(1, session, 1, 1, user);
        store.add(ticket);
        ticket.setCell(2);
        store.update(ticket);
        Ticket ticketInDb = store.findById(ticket.getId()).orElse(new Ticket());
        assertThat(ticketInDb, is(ticket));
    }

    @Test
    public void delete() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        userStore.add(user);
        SessionStore sessionStore = new SessionDbStore(pool);
        Session session = new Session(0, "Session", 3, 3);
        sessionStore.add(session);
        TicketStore store = new TicketDbStore(pool, userStore, sessionStore);
        Ticket ticket = new Ticket(1, session, 1, 1, user);
        store.add(ticket);
        store.delete(ticket);
        assertTrue(store.findById(ticket.getId()).isEmpty());
    }

    @Test
    public void findAll() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        userStore.add(user);
        SessionStore sessionStore = new SessionDbStore(pool);
        Session session = new Session(0, "Session", 3, 3);
        sessionStore.add(session);
        TicketStore store = new TicketDbStore(pool, userStore, sessionStore);
        Ticket ticket1 = new Ticket(1, session, 1, 1, user);
        Ticket ticket2 = new Ticket(2, session, 1, 2, user);
        store.add(ticket1);
        store.add(ticket2);
        List<Ticket> tickets = store.findAll();
        assertThat(tickets.size(), is(2));
        assertTrue(tickets.containsAll(List.of(ticket1, ticket2)));
    }

    @Test
    public void findById() {
        BasicDataSource pool = new Main().loadPool();
        UserStore userStore = new UserDbStore(pool);
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        userStore.add(user);
        SessionStore sessionStore = new SessionDbStore(pool);
        Session session = new Session(0, "Session", 3, 3);
        sessionStore.add(session);
        TicketStore store = new TicketDbStore(pool, userStore, sessionStore);
        Ticket ticket1 = new Ticket(1, session, 1, 1, user);
        Ticket ticket2 = new Ticket(2, session, 1, 2, user);
        store.add(ticket1);
        store.add(ticket2);
        Ticket ticketInDb1 = store.findById(ticket1.getId()).orElse(new Ticket());
        Ticket ticketInDb2 = store.findById(ticket2.getId()).orElse(new Ticket());
        assertThat(ticketInDb1, is(ticket1));
        assertThat(ticketInDb2, is(ticket2));
    }
}