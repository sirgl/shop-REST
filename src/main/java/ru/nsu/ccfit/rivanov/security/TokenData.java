package ru.nsu.ccfit.rivanov.security;

import java.util.Date;

public class TokenData {
    private final String login;
    private final Date date;


    public TokenData(String login, Date date) {
        this.login = login;
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenData tokenData = (TokenData) o;

        return login.equals(tokenData.login) && date.equals(tokenData.date);

    }
}
