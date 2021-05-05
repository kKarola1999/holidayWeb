package com.example.holidayWeb;

public class Employee {
    private int id;
    private String name;
    private int seniority;
    private String jobTime; // todo
    private int extraDays;
    private String email;
    private String password;

    public Employee(String name, int seniority, String jobTime, int extraDays, String email, String password) {
        this.name = name;
        this.seniority = seniority;
        this.jobTime = jobTime;
        this.extraDays = extraDays;
        this.email = email;
        this.password = password;
    }

    public Employee(int id, String name, int seniority, String jobPossition, int extraDays, String email, String password) {
        this.id = id;
        this.name = name;
        this.seniority = seniority;
        this.jobTime = jobPossition;
        this.extraDays = extraDays;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public int getExtraDays() {
        return extraDays;
    }

    public void setExtraDays(int extraDays) {
        this.extraDays = extraDays;
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
}
