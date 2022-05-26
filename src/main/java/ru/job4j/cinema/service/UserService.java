package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.store.UserStore;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IntUserService {
    private final UserStore store;

    public UserService(UserStore store) {
        this.store = store;
    }

    @Override
    public Optional<User> add(User user) {
        return store.add(user);
    }

    @Override
    public boolean update(User user) {
        return store.update(user);
    }

    @Override
    public boolean delete(User user) {
        return store.delete(user);
    }

    @Override
    public List<User> findAll() {
        return store.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return store.findById(id);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public boolean save(User user) {
        boolean rsl = false;
        if (user.getId() == 0) {
            rsl = add(user).isPresent();
        } else {
            rsl = update(user);
        }
        return rsl;
    }

    @Override
    public Optional<User> findUserByEmailAndPhone(String email, String phone) {
        return store.findUserByEmailAndPhone(email, phone);
    }
}
