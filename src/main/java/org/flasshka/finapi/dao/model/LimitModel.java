package org.flasshka.finapi.dao.model;


import java.util.Date;

public class LimitModel {
    private int id;
    private String login;
    private int lim;
    private Date startLim;
    private Date endLim;

    public LimitModel() {
    }

    public LimitModel(int id, String login, int lim, Date startLim, Date endLim) {
        this.id = id;
        this.login = login;
        this.lim = lim;
        this.startLim = startLim;
        this.endLim = endLim;
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

    public int getLim() {
        return lim;
    }

    public void setLim(int lim) {
        this.lim = lim;
    }

    public Date getStartLim() {
        return startLim;
    }

    public void setStartLim(Date startLim) {
        this.startLim = startLim;
    }

    public Date getEndLim() {
        return endLim;
    }

    public void setEndLim(Date endLim) {
        this.endLim = endLim;
    }
}
