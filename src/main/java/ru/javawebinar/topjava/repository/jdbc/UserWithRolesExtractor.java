package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserWithRolesExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> userMap = new LinkedHashMap<>();

        while (rs.next()) {
            int userId = rs.getInt("id");
            User user = userMap.get(userId);

            if (user == null) {
                user = new User();
                user.setId(userId);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getDate("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setRoles(new HashSet<>());
                userMap.put(userId, user);
            }

            Optional.ofNullable(rs.getString("role"))
                    .map(Role::valueOf)
                    .ifPresent(user.getRoles()::add);
        }

        return new ArrayList<>(userMap.values());
    }
}
