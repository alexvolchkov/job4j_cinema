package ru.job4j.cinema.store;

import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStore {

    Optional<User> add(User user);

    boolean update(User user);

    boolean delete(User user);

    List<User> findAll();

    Optional<User> findById(int id);

    void deleteAll();

    Optional<User> findUserByEmailAndPhone(String email, String phone);
}
