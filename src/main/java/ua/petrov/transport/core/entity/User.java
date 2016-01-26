
package ua.petrov.transport.core.entity;

import ua.petrov.transport.core.constants.CoreConsts.ErrorMsg;
import ua.petrov.transport.core.constants.CoreConsts.Pattern;
import ua.petrov.transport.core.validator.annotation.MatchPattern;

public class User extends Entity implements ViewBean {

    @MatchPattern(pattern = Pattern.STRING_VAL, message = ErrorMsg.LOGIN)
    private String login;

    @MatchPattern(pattern = Pattern.PASS, message = ErrorMsg.PASSWORD)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
