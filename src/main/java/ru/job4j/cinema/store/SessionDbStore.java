package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class SessionDbStore implements SessionStore {

    private static final Logger LOG = LogManager.getLogger(SessionDbStore.class.getName());
    private final BasicDataSource pool;

    public SessionDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    @Override
    public Optional<Session> add(Session session) {
        Optional<Session> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT into sessions (name, numberOfRows, numberOfCells) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getNumberOfRows());
            ps.setInt(3, session.getNumberOfCells());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    session.setId(id.getInt(1));
                }
                rsl = Optional.of(session);
            }
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    @Override
    public boolean update(Session session) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "UPDATE sessions set name = ?, numberOfRows = ?, numberOfCells = ? where id = ?"
        )) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getNumberOfRows());
            ps.setInt(3, session.getNumberOfCells());
            ps.setInt(4, session.getId());
            rsl = ps.execute();
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    @Override
    public boolean delete(Session session) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "Delete sessions where id = ?"
             )) {
            ps.setInt(1, session.getId());
            rsl = ps.execute();
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    @Override
    public List<Session> findAll() {
        List<Session> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from sessions"
        )) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(createSessionFromDB(it));
                }
            }
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    @Override
    public Optional<Session> findById(int id) {
        Optional<Session> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from sessions where id = ?"
        )) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(createSessionFromDB(it));
                }
            }
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    @Override
    public void deleteAll() {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "DELETE from sessions"
        )) {
            ps.execute();
        } catch (SQLException e) {
            LOG.error(e);
        }
    }

    @Override
    public Map<Integer, Integer> getOccupiedRow(Session session) {
        Map<Integer, Integer> rsl = new HashMap<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT seat_row, count(cell) from ticket where session_id = ? group by seat_row"
             )) {
            ps.setInt(1, session.getId());
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.put(it.getInt(1), it.getInt(2));
                }
            }
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    @Override
    public List<Integer> getOccupiedCell(Session session, int row) {
        List<Integer> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT cell from ticket where session_id = ? AND seat_row = ?"
             )) {
            ps.setInt(1, session.getId());
            ps.setInt(2, row);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(it.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error(e);
        }
        return rsl;
    }

    private Session createSessionFromDB(ResultSet it) throws SQLException {
        return new Session(
                it.getInt("id"),
                it.getString("name"),
                it.getInt("numberOfRows"),
                it.getInt("numberOfCells")
        );
    }
}
