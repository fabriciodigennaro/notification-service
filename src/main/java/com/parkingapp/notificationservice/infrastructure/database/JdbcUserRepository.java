package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.user.UserRepository;
import org.springframework.jdbc.core.RowMapper;
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
    public Optional<String> getUserEmailAddressByUserId(UUID userId) {
        return namedParameterJdbcTemplate.query(
                """
                        SELECT * FROM users
                        WHERE id = :userId
                        """,
                Map.of("userId", userId),
                new UserRowMapper()
        )
                .stream().findFirst();
    }

    private static class UserRowMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("email");
        }
    }
}
