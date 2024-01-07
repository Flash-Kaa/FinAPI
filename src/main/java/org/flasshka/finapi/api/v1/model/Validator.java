package org.flasshka.finapi.api.v1.model;

public class Validator {
    private final static int MIN_LOGIN_LEN = 10;
    private final static int MAX_LOGIN_LEN = 30;
    private final static int MIN_PASSWORD_LEN = 8;
    private final static int MAX_PASSWORD_LEN = 16;

    private Validator() {
    }

    public static boolean isValidLogin(String login) {
        return login.length() >= MIN_LOGIN_LEN
                && login.length() <= MAX_LOGIN_LEN
                && checkSymbolsCorrect(login);
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LEN
                && password.length() <= MAX_PASSWORD_LEN
                && checkSymbolsCorrect(password);
    }

    private static boolean checkSymbolsCorrect(String login) {
        for (int i = 0; i < login.length(); i++) {
            char symbol = login.charAt(i);

            if (!Character.isDigit(symbol) && !Character.isLetter(symbol)) {
                return false;
            }
        }

        return true;
    }
}
