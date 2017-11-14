package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> userRepository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UsersUtil.USERS.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return userRepository.keySet().remove(id);
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        userRepository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return userRepository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return (List<User>) userRepository.values();
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
/*
        // Without stream
        User output = null;
        for (Integer id : userRepository.keySet()) {
            User user = this.get(id);
            String e_mail = user.getEmail();
            if (e_mail.equals(email)) {
                output = this.get(id);
            }
        }
        */
/*
        // stream with entrySet
        return userRepository.entrySet().stream()
                .filter(user -> user.getValue().getEmail().equals(email))
                .findFirst()
                .get()                  // can return error if nothing found NPE
                .getValue();
*/
        return userRepository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null);
    }
}
