package org.flasshka.finapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationDAO {
    private final JdbcTemplate jdbc;

    @Autowired
    public AuthorizationDAO(JdbcTemplate jdbcTemplate) {
        jdbc = jdbcTemplate;
    }

    public void registration(String login, String password) {
        jdbc.update("INSERT INTO \"Authorization\" VALUES(?, ?)", login, password);
    }

    public boolean logIn(String login, String password) {
        List<String> query = jdbc.query(
                "SELECT login FROM \"Authorization\" WHERE login=? AND password=?",
                new Object[]{login, password},
                new BeanPropertyRowMapper<>(String.class)
        );

        return query.stream()
                .findAny()
                .isPresent();
    }

    public boolean haveLogin(String login) {
        String query = jdbc.query(
                        "SELECT login FROM \"Authorization\" WHERE login=?",
                        new Object[]{login},
                        new BeanPropertyRowMapper<String>()
                ).stream()
                .findAny()
                .orElse(null);

        return query == null;
    }
}
