package com.example.izracunaj.SQL;

public class Rezultat {
    String korisnik;
    int rezultat;

    public Rezultat(){
    }

    public Rezultat(String korisnik, int najboljiRezultat) {
        this.korisnik = korisnik;
        this.rezultat = najboljiRezultat;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public int getRezultat() {
        return rezultat;
    }

    public void setRezultat(int rezultat) {
        this.rezultat = rezultat;
    }

    @Override
    public String toString() {
        return "Rezultat{" +
                "korisnik='" + korisnik + '\'' +
                ", rezultat=" + rezultat +
                '}';
    }
}
