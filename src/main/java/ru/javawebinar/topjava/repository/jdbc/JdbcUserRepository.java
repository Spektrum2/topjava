package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> USER_WITH_ROLES_EXTRACTOR = rs -> {
        Map<Integer, User> userMap = new LinkedHashMap<>();

        while (rs.next()) {
            int userId = rs.getInt("id");
            User user = userMap.get(userId);

            if (user == null) {
                user = ROW_MAPPER.mapRow(rs, rs.getRow());
                user.setRoles(new HashSet<>());
                userMap.put(userId, user);
            }

            Optional.ofNullable(rs.getString("role"))
                    .map(Role::valueOf)
                    .ifPresent(user.getRoles()::add);
        }

        return new ArrayList<>(userMap.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            deleteRoles(user);
            insertRoles(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        String sql = "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, r.role " +
                "FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE u.id = ?";
        List<User> users = jdbcTemplate.query(sql, USER_WITH_ROLES_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        String sql = "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, r.role " +
                "FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE u.email = ?";
        List<User> users = jdbcTemplate.query(sql, USER_WITH_ROLES_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, r.role " +
                "FROM users u LEFT JOIN user_role r ON u.id = r.user_id ORDER BY u.name, u.email";
        return jdbcTemplate.query(sql, USER_WITH_ROLES_EXTRACTOR);
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO user_role (user_id, role) VALUES (?, ?)",
                    roles.stream()
                            .map(role -> new Object[]{user.getId(), role.name()})
                            .toList()
            );
        }
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
    }
}
