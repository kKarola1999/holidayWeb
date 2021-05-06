package com.example.holidayWeb.DBUtill;

import com.example.holidayWeb.Employee;
import com.example.holidayWeb.Holiday;

import java.sql.PreparedStatement;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmployeUtill extends DBEmployee {
    private DataSource dataSource;
    private String URL;
    private String email;
    private String password;
    public EmployeUtill (String URL){this.URL = URL; }


    @Override
    public List<Employee> getEmployes() throws Exception{
        List<Employee> employes  =  new ArrayList<>();
        Connection connection =  null;
        Statement statement =  null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL,email,password);
            String sql =  "SELECT * FROM pracownicy";
            statement =  connection.createStatement();
            resultSet =  statement.executeQuery(sql);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("imie_nazwisko");
                int staz = resultSet.getInt("staz");
                int extraDays = resultSet.getInt("dodatkowe_dni");
                String jobTime =  resultSet.getString("etat");
                String  password = resultSet.getString("password");
                String email = resultSet.getString("email");

                employes.add(new Employee(id, name,staz,jobTime,extraDays,email, password));
            }
        }finally {
            close(connection, statement,resultSet);
        } return employes;
    }
    public int getId(String email, String pass) throws Exception{
        int id;
        Connection connection = null;
        PreparedStatement statement =null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL,this.email,password);
            String sql = "Select id from pracownicy where email=? and pass =?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, pass);
            resultSet = statement.executeQuery(sql);
            if (resultSet.first()) {
                id = resultSet.getInt("id");
            } else {
                throw new Exception("There is not such user!");
            } return id;
        } finally {
            close(connection,statement,resultSet);
        }

    }

    public String getPassword(String emailE) throws Exception{
        Connection connection =null;
        PreparedStatement statement =null;
        ResultSet resultSet =null;
        String DBpass ;
        try{
            connection=DriverManager.getConnection(URL, email, password);
            String sql = "select password from pracownicy where email =?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, emailE);
            if (resultSet.first()){
                DBpass = resultSet.getString("password");
            } else {
                throw new Exception("Wrong email or password.");
            }
            return DBpass;
        }finally {
            close(connection,statement,resultSet);
        }
    }
    public int usedDays(String email){
        int days =0;
        try {
            List<Holiday> holidays =  getUserHolidays(email);
            for (int i = 0; i<holidays.size();i++ ){
                days = (int) ChronoUnit.DAYS.between(holidays.get(i).getStartUrlopu(), holidays.get(i).getKoniecUrlopu())+1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    public List<Holiday> getUserHolidays (String emailE) throws Exception{

        List <Holiday> holiday = new ArrayList<>();

        Connection connection =  null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{


            connection = DriverManager.getConnection(URL, email, password);

            String sql = "SELECT * FROM urlopy where email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,emailE);
            resultSet =  statement.executeQuery();

            if (resultSet.next()){
                int idUrlopy =  resultSet.getInt("idUrlopy");
                LocalDate start =  resultSet.getDate("start_urlopu").toLocalDate();
                LocalDate end =  resultSet.getDate("end_urlopu").toLocalDate();
                boolean akceptacja =  resultSet.getBoolean("akceptacja");
                int idPracownika = resultSet.getInt("Pracownicy_id");
                String name = resultSet.getString("email");

                holiday.add( new Holiday(idUrlopy,start,end,akceptacja,idPracownika, name));
            }else{
                throw new Exception("You dont have any planned leaves");
            }
            return holiday;
        }finally {
            close(connection, statement,resultSet);
        }

    }

    public void addHoliday(Holiday holiday)throws Exception{
        Connection connection =null;
        PreparedStatement statement=null;
        try {
            connection = DriverManager.getConnection(URL,email, password);
            String sql = "Insert into holiday(start_urlopu, end_urlopu, akceptacja, Pracownicy_id ,email)"+ //todo Pracownicy_id?
                    "VALUES(?, ?, ?, ?, ?)";
            statement= connection.prepareStatement(sql);

            statement.setDate(1, Date.valueOf(holiday.getStartUrlopu()));
            statement.setDate(2, Date.valueOf(holiday.getKoniecUrlopu()));
            statement.setBoolean(3, holiday.isAkceptacja());
            statement.setInt(4,holiday.getPracownikId());
            statement.setString(5, holiday.getEmail());
        }finally {
            close(connection,statement,null);
        }
    }

    public Holiday getHoliday(String id) throws Exception {
        Connection conn =null;
        PreparedStatement statement=null;
        ResultSet resultSet = null;

        Holiday holiday= null;



        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = DriverManager.getConnection(URL, email, password);

            // zapytanie SELECT
            String sql = "SELECT * FROM urlopy WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

            // wykonanie zapytania
            resultSet = statement.executeQuery();

            // przetworzenie wyniku zapytania

            if (resultSet.next()) {

//
                    LocalDate startUrlopu = resultSet.getDate("start_urlopu").toLocalDate();
                    LocalDate koniecUrlopu = resultSet.getDate("end_urlopu").toLocalDate();
                    boolean akceptacja = resultSet.getBoolean("akceptacja");
                    int pracownikID = resultSet.getInt("Pracownicy_ID");
                    String email = resultSet.getString("email");

                    // utworzenie obiektu
                    holiday = new Holiday(holidayID, startUrlopu, koniecUrlopu,akceptacja,pracownikID,email);


            } else {
                throw new Exception("Nie można znaleźć id " + holidayID);
            }

            return holiday;

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, resultSet);

        }

    }

    public void updateHoliday(Holiday holiday) throws Exception {

        Connection conn = null;
        PreparedStatement statement = null;

        try {

            // polaczenie z BD
            conn = DriverManager.getConnection(URL, email, password);

            // zapytanie UPDATE
            String sql = "UPDATE urlopy SET start_urlopu=?, end_urlopu=?,akceptacja=? "+
                    "WHERE id =?";

            statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(holiday.getStartUrlopu()));
            statement.setDate(2, Date.valueOf(holiday.getKoniecUrlopu()));
            statement.setBoolean(3, holiday.isAkceptacja());
            statement.setInt(4,holiday.getId());


            // wykonanie zapytania
            statement.execute();

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, null);

        }

    }

    public void deleteHoliday(String id) throws Exception {

        Connection conn = null;
        PreparedStatement statement = null;

        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = DriverManager.getConnection(URL, email, password);

            // zapytanie DELETE
            String sql = "DELETE FROM urlopy WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

            // wykonanie zapytania
            statement.execute();

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, null);

        }

    }

    public int getStaz(String emailE)throws Exception{
        Connection connection =null;
        PreparedStatement statement =null;
        ResultSet resultSet =null;
        int staz;
        try{
            connection=DriverManager.getConnection(URL, email, password);
            String sql = "select staz from pracownicy where email =?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, emailE);
            if (resultSet.first()){
                staz = resultSet.getInt("staz");
            } else {
                throw new Exception("smoething went wrong");
            }
            return staz;
        }finally {
            close(connection,statement,resultSet);
        }
    }

    public void setEmail(String email){this.email = email;}
    public void setPassword(String password){this.password = password;}
}