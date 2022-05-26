package ru.job4j.cinema.store;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class UserDbStoreTest {

    @After
    public void CleanDB() {
        UserStore store = new UserDbStore(new Main().loadPool());
        store.deleteAll();
    }

    @Test
    public void whenCreateUser() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        store.add(user);
        User userInDb = store.findById(user.getId()).orElse(new User());
        assertThat(userInDb.getUserName(), is(user.getUserName()));
    }

    @Test
    public void whenNotCreateUserThenNotUniqueEmail() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "User1", "aa@mail.ru", "123-45-67");
        User user2 = new User(0, "User2", "aa@mail.ru", "222-45-67");
        store.add(user1);
        assertTrue(store.add(user2).isEmpty());
    }

    @Test
    public void whenNotCreateUserThenNotUniquePhone() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "User1", "aa@mail.ru", "123-45-67");
        User user2 = new User(0, "User2", "1@mail.ru", "123-45-67");
        store.add(user1);
        assertTrue(store.add(user2).isEmpty());
    }

    @Test
    public void update() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        store.add(user);
        user.setUserName("New name");
        store.update(user);
        User userInDb = store.findById(user.getId()).orElse(new User());
        assertThat(userInDb.getUserName(), is(user.getUserName()));
    }

    @Test
    public void delete() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user = new User(0, "User", "aa@mail.ru", "123-45-67");
        store.add(user);
        store.delete(user);
        assertTrue(store.findById(user.getId()).isEmpty());
    }

    @Test
    public void findAll() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "User1", "aa@mail.ru", "123-45-67");
        User user2 = new User(0, "User2", "1@mail.ru", "222-45-67");
        store.add(user1);
        store.add(user2);
        List<User> users = store.findAll();
        assertThat(users.size(), is(2));
        assertTrue(users.containsAll(List.of(user1, user2)));
    }

    @Test
    public void findById() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "User1", "aa@mail.ru", "123-45-67");
        User user2 = new User(0, "User2", "22@mail.ru", "222-45-67");
        store.add(user1);
        store.add(user2);
        User userInDb1 = store.findById(user1.getId()).orElse(new User());
        User userInDb2 = store.findById(user2.getId()).orElse(new User());
        assertThat(userInDb1.getUserName(), is(user1.getUserName()));
        assertThat(userInDb2.getUserName(), is(user2.getUserName()));
    }

    @Test
    public void findByEmailAndPhone() {
        UserStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "User1", "aa@mail.ru", "123-45-67");
        User user2 = new User(0, "User2", "22@mail.ru", "222-45-67");
        store.add(user1);
        store.add(user2);
        User userInDb1 = store.findUserByEmailAndPhone(user1.getEmail(), user1.getPhone()).orElse(new User());
        User userInDb2 = store.findUserByEmailAndPhone(user2.getEmail(), user2.getPhone()).orElse(new User());
        assertThat(userInDb1.getUserName(), is(user1.getUserName()));
        assertThat(userInDb2.getUserName(), is(user2.getUserName()));
    }
}