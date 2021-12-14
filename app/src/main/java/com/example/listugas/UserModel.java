package com.example.listugas;

public class UserModel {
    private String username, email;
    private boolean isPremium;

    public void UserModel(){}

    public UserModel(String username, String email, boolean isPremium) {
        this.username = username;
        this.email = email;
        this.isPremium = isPremium;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
