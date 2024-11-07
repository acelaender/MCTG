package models;

import com.fasterxml.jackson.annotation.JsonAlias;

public class User {
    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;

    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //TODO: No email!
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return "User{" + "username='" + username + "'" + ", password='" + password + "'}" ;
    }
}
