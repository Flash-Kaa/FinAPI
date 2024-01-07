package org.flasshka.finapi.dao.model;


import java.util.Date;

public class ExpenditureModel {
    private int id;
    private String login;
    private Date date;
    private int sum;

    public ExpenditureModel() {
    }

    public ExpenditureModel(int id, String login, Date date, int sum) {
        this.id = id;
        this.login = login;
        this.date = date;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
