package org.flasshka.finapi.dao;

import org.flasshka.finapi.dao.model.ExpenditureModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ExpenditureDAO {
    private final JdbcTemplate jdbc;

    public ExpenditureDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addExpenditures(String login, int sum, Date date) {
        jdbc.update("INSERT INTO \"Expenditure\" (login, date, sum) VALUES (?, ?, ?)", login, date, sum);
    }

    public List<ExpenditureModel> getExpenditures(String login, Date maxDate) {
        return jdbc.query("SELECT * FROM \"Expenditure\" WHERE login=? AND date < ?",
                new Object[]{login, maxDate},
                new BeanPropertyRowMapper<>(ExpenditureModel.class)
        );
    }
}
