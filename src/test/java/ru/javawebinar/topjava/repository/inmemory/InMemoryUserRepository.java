package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.javawebinar.topjava.UserTestData.*;


@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    public void init() {
        map.clear();
        put(user);
        put(admin);
        put(guest);
        counter.getAndSet(GUEST_ID + 1);
    }

    @Override
    public List<User> getAll() {
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .toList();
    }

    @Override
    public boolean enable(int id, boolean enabled) {
        return Optional.ofNullable(get(id))
                .map(user -> {
                    user.setEnabled(enabled);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public User getByEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");
        return getCollection().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }


}