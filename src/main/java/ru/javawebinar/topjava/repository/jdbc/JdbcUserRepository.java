package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
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

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;


    ResultSetExtractor<List<Role>> RESULT_SET_EXTRACTOR = new ResultSetExtractor<List<Role>>() {
        @Override
        public List<Role> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString("role")));
            }
            return roles;
        }
    };

    ResultSetExtractor<Map<Integer, Set<Role>>> RESULT_SET_EXTRACTOR_ROLES = new ResultSetExtractor<Map<Integer, Set<Role>>>() {
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
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> roles = new ArrayList<>();
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            roles.addAll(user.getRoles());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            String sqlRoles = "SELECT role FROM user_roles WHERE user_id = ?";
            roles = jdbcTemplate.query(sqlRoles, RESULT_SET_EXTRACTOR, user.id());
        }

        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());

        List<Role> finalRoles = roles;
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ? )",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, finalRoles.get(i).toString());

                    }

                    public int getBatchSize() {
                        return finalRoles.size();
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
        String sqlRoles = "SELECT role FROM user_roles WHERE user_id = ?";
        setRoles(sqlRoles, users, id);
    /*    List<Role> roles = jdbcTemplate.query(sqlRoles, RESULT_SET_EXTRACTOR, id);
        for (User user : users) {
            user.setRoles(roles);
        }*/

        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        String sqlRoles = "SELECT role FROM user_roles WHERE user_id IN (SELECT  id FROM users WHERE email = ?)";
        setRoles(sqlRoles, users, email);
       /* List<Role> roles = jdbcTemplate.query(sqlRoles, RESULT_SET_EXTRACTOR, email);
        for (User user : users) {
            user.setRoles(roles);
        }*/

        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", RESULT_SET_EXTRACTOR_ROLES);
        for (User user : users) {
            user.setRoles(roles.get(user.getId()));
        }
        return users;
    }

    private void setRoles(String sqlRoles, List<User> users, Object emailOrId) {
        List<Role> roles = jdbcTemplate.query(sqlRoles, RESULT_SET_EXTRACTOR, emailOrId);
        for (User user : users) {
            user.setRoles(roles);
        }
    }
}
