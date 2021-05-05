package com.example.holidayWeb;

public class Employee {
    private int id;
    private String name;
    private double seniority;
    private double jobPossition;
    private String extraDays;
    private String email;
    private String password;

    public Employee(int id, String name, double seniority, double jobPossition, String extraDays, String email, String password) {
        this.id = id;
        this.name = name;
        this.seniority = seniority;
        this.jobPossition = jobPossition;
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

    public double getSeniority() {
        return seniority;
    }

    public void setSeniority(double seniority) {
        this.seniority = seniority;
    }

    public double getJobPossition() {
        return jobPossition;
    }

    public void setJobPossition(double jobPossition) {
        this.jobPossition = jobPossition;
    }

    public String getExtraDays() {
        return extraDays;
    }

    public void setExtraDays(String extraDays) {
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
