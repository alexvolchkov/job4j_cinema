package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStore implements UserStore {
    private final BasicDataSource pool;

    public UserDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    @Override
    public Optional<User> add(User user) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT into users (username, email, phone) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
                rsl = Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public boolean update(User user) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "UPDATE users set username = ?, email = ?, phone = ? where id = ?"
        )) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getId());
            rsl = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public boolean delete(User user) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "Delete users where id = ?"
             )) {
            ps.setInt(1, user.getId());
            rsl = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public List<User> findAll() {
        List<User> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from users"
        )) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(createUserFromDB(it));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from users where id = ?"
        )) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(createUserFromDB(it));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public void deleteAll() {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "DELETE from users"
        )) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPhone(String email, String phone) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "SELECT * from users where email = ? and phone = ?"
        )) {
            ps.setString(1, email);
            ps.setString(2, phone);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(createUserFromDB(it));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    private User createUserFromDB(ResultSet it) throws SQLException {
        return new User(
                it.getInt("id"),
                it.getString("username"),
                it.getString("email"),
                it.getString("phone")
        );
    }
}
