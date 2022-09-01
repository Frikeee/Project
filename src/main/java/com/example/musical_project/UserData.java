package com.example.musical_project;

public class UserData {
    private String name;
    private String secondName;
    private String login;
    private String password;
    private String email;

    public UserData(String name, String secondName, String login, String password, String email) {
        this.name = name;
        this.secondName = secondName;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public UserData() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
