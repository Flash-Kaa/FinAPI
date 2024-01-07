package org.flasshka.finapi.dao;

import org.flasshka.finapi.dao.model.LimitModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class LimitsDAO {
    private final JdbcTemplate jdbc;

    @Autowired
    public LimitsDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addLimit(String login, int limit, Date start, Date end) {
        jdbc.update("INSERT INTO \"Limits\"(login, lim, startlim, endlim) VALUES (?, ?, ?, ?)", login, limit, start, end);
    }

    public List<LimitModel> getLimits(String login, Date maxDate) {
        return jdbc.query(
                "SELECT * FROM \"Limits\" WHERE login=? AND startlim < ?",
                new Object[]{login, maxDate},
                new BeanPropertyRowMapper<>(LimitModel.class)
        );
    }
}
