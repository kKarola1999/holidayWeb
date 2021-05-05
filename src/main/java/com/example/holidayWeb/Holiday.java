package com.example.holidayWeb;

import java.time.LocalDate;

public class Holiday {
    private int id;
    private LocalDate startUrlopu;
    private LocalDate koniecUrlopu;
    private String akceptacja;
    private int pracownikId;

    public Holiday(int id, LocalDate startUrlopu, LocalDate koniecUrlopu, String akceptacja, int pracownikId) {
        this.id = id;
        this.startUrlopu = startUrlopu;
        this.koniecUrlopu = koniecUrlopu;
        this.akceptacja = akceptacja;
        this.pracownikId = pracownikId;
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

    public String getAkceptacja() {
        return akceptacja;
    }

    public void setAkceptacja(String akceptacja) {
        this.akceptacja = akceptacja;
    }

    public int getPracownikId() {
        return pracownikId;
    }

    public void setPracownikId(int pracownikId) {
        this.pracownikId = pracownikId;
    }
}
