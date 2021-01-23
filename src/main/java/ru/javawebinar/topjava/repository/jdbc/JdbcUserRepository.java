package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.jdbcValidation;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<Role>> RESULT_SET_EXTRACTOR = new ResultSetExtractor<List<Role>>() {
        @Override
        public List<Role> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString("role")));
            }
            return roles;
        }
    };

    private ResultSetExtractor<Map<Integer, Set<Role>>> RESULT_SET_EXTRACTOR_ROLES = new ResultSetExtractor<Map<Integer, Set<Role>>>() {
        @Override
        public Map<Integer, Set<Role>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Integer, Set<Role>> rolesOfUser = new HashMap<>();
            Integer userId;
            Role role;

            while (resultSet.next()) {
                userId = Integer.parseInt(resultSet.getString("user_id"));
                role = Role.valueOf(resultSet.getString("role"));
                Set<Role> roleSet = rolesOfUser.get(userId);
                if (roleSet == null) {
                    roleSet = new HashSet<>();
                }
                roleSet.add(role);
                rolesOfUser.put(userId, roleSet);
            }
            return rolesOfUser;
        }
    };

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
        jdbcValidation(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }

        List<Role> roles = new ArrayList<>(user.getRoles());
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());

        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ? )",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, roles.get(i).toString());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(users, id);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(users, users.get(0).getId());
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", RESULT_SET_EXTRACTOR_ROLES);
        for (User user : users) {
            user.setRoles(roles.get(user.id()));
        }
        return users;
    }

    private User setRoles(List<User> users, int userId) {
        String sqlRoles = "SELECT role FROM user_roles WHERE user_id  = ?";
        List<Role> roles = jdbcTemplate.query(sqlRoles, RESULT_SET_EXTRACTOR, userId);
        for (User user : users) {
            user.setRoles(roles);
        }
        return DataAccessUtils.singleResult(users);
    }
}
