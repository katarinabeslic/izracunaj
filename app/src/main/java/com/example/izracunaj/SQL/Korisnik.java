package com.example.izracunaj.SQL;

public class Korisnik {

    int id;
    String imePrezime;
    String email;
    String username;
    String password;

    public Korisnik() {
    }

    public Korisnik(int id, String imePrezime, String email, String username, String password) {
        this.id = id;
        this.imePrezime = imePrezime;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", imePrezime='" + imePrezime + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
