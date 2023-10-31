package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDbStore implements TicketStore {

    private static final Logger LOG = LogManager.getLogger(TicketDbStore.class.getName());
    private final BasicDataSource pool;
    private final UserStore userStore;
    private final SessionStore sessionStore;

    public TicketDbStore(BasicDataSource pool, UserStore userStore, SessionStore sessionStore) {
        this.pool = pool;
        this.userStore = userStore;
        this.sessionStore = sessionStore;
    }

    @Override
    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT into ticket (session_id, seat_row, cell, user_id) values (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
                rsl = Optional.of(ticket);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean update(Ticket ticket) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "UPDATE ticket set session_id = ?, seat_row = ?, cell = ?, user_id =? where id = ?"
        )) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.setInt(5, ticket.getId());
            rsl = ps.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean delete(Ticket ticket) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "Delete ticket where id = ?"
             )) {
            ps.setInt(1, ticket.getId());
            rsl = ps.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from ticket"
        )) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(createTicketFromDB(it));
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public Optional<Ticket> findById(int id) {
        Optional<Ticket> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from ticket where id = ?"
        )) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(createTicketFromDB(it));
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public void deleteAll() {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "DELETE from ticket"
        )) {
            ps.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private Ticket createTicketFromDB(ResultSet it) throws SQLException {
        return new Ticket(
                it.getInt("id"),
                sessionStore.findById(it.getInt("session_id")).orElse(new Session()),
                it.getInt("seat_row"),
                it.getInt("cell"),
                userStore.findById(it.getInt("user_id")).orElse(new User())
        );
    }
}
