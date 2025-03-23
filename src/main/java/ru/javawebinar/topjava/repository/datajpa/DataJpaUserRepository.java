package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudUserRepository;

    private final CrudMealRepository crudMealRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository, CrudMealRepository crudMealRepository) {
        this.crudUserRepository = crudRepository;
        this.crudMealRepository = crudMealRepository;
    }

    @Override
    public User save(User user) {
        return crudUserRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return crudUserRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudUserRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudUserRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    @Transactional
    public User getWithMeals(int id) {
        return Optional.ofNullable(crudUserRepository.getWithRoles(id))
                .map(user -> {
                    user.setMeals(crudMealRepository.getAll(id));
                    return user;
                })
                .orElse(null);
    }
}
