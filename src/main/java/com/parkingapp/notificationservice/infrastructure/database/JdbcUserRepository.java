package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.domain.user.UserRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcUserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return namedParameterJdbcTemplate.query(
                """
                        SELECT * FROM users
                        WHERE user_id = :userId
                        """,
                Map.of("userId", userId),
                new UserRowMapper()
        )
                .stream().findFirst();
    }

    @Override
    public boolean saveUser(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", user.getId())
                .addValue("email", user.getEmail());


        return namedParameterJdbcTemplate.update(
                """
                INSERT INTO users(user_id, email)
                VALUES (:userId, :email)
                ON CONFLICT (user_id) DO UPDATE SET email = :email
                """,
                params
        ) > 0;
    }

    private static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    UUID.fromString(rs.getString("user_id")),
                    rs.getString("email")
            );
        }
    }
}
