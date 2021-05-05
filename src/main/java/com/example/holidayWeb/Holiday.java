package com.example.holidayWeb;

import java.time.LocalDate;

public class Holiday {
    private int id;
    private LocalDate startUrlopu;
    private LocalDate koniecUrlopu;
    private boolean akceptacja;
    private int pracownikId;
    private String imie_nazwisko;


    public Holiday(int id, LocalDate startUrlopu, LocalDate koniecUrlopu, boolean akceptacja, int pracownikId, String imie_nazwisko) {
        this.id = id;
        this.startUrlopu = startUrlopu;
        this.koniecUrlopu = koniecUrlopu;
        this.akceptacja = akceptacja;
        this.pracownikId = pracownikId;
        this.imie_nazwisko = imie_nazwisko;
    }

    public Holiday(LocalDate startUrlopu, LocalDate koniecUrlopu, boolean akceptacja, int pracownikId, String imie_nazwisko) {
        this.startUrlopu = startUrlopu;
        this.koniecUrlopu = koniecUrlopu;
        this.akceptacja = akceptacja;
        this.pracownikId = pracownikId;
        this.imie_nazwisko = imie_nazwisko;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", startUrlopu=" + startUrlopu +
                ", koniecUrlopu=" + koniecUrlopu +
                ", akceptacja=" + akceptacja +
                ", pracownikId=" + pracownikId +
                ", imie_nazwisko='" + imie_nazwisko + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartUrlopu() {
        return startUrlopu;
    }

    public void setStartUrlopu(LocalDate startUrlopu) {
        this.startUrlopu = startUrlopu;
    }

    public LocalDate getKoniecUrlopu() {
        return koniecUrlopu;
    }

    public void setKoniecUrlopu(LocalDate koniecUrlopu) {
        this.koniecUrlopu = koniecUrlopu;
    }

    public boolean isAkceptacja() {
        return akceptacja;
    }

    public void setAkceptacja(boolean akceptacja) {
        this.akceptacja = akceptacja;
    }

    public int getPracownikId() {
        return pracownikId;
    }

    public void setPracownikId(int pracownikId) {
        this.pracownikId = pracownikId;
    }

    public String getImie_nazwisko() {
        return imie_nazwisko;
    }

    public void setImie_nazwisko(String imie_nazwisko) {
        this.imie_nazwisko = imie_nazwisko;
    }
}