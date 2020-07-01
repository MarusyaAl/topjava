package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        if (user.isNewUser()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            log.info("save {}", user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> result = (List<User>) repository.values();
        Collections.sort(result);
        return result;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        List<User> result = (List<User>) repository.values();
        for (User user : result) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
 /*       final Optional<User> first = result.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
        return first;*/
    }
}
