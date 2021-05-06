package com.example.holidayWeb.DBUtill;

import com.example.holidayWeb.Holiday;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayUtill extends DBUtil {
    private String URL;
    private String name;
    private String password;

    public HolidayUtill(String URL) {
        this.URL = URL;
    }

    @Override
    public List<Holiday> getHolidays() throws  Exception{
        List<Holiday> urlopy = new ArrayList<>();
        Connection conn =  null;
        Statement statement =  null;
        ResultSet resultSet =  null;

        try{
            conn = DriverManager.getConnection(URL, name, password);
            String sql =  "select * from Urlopy";
            statement =  conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                int id = resultSet.getInt("idUrlopy");
                LocalDate start_urlopu = resultSet.getDate("start_urlopu").toLocalDate();
                LocalDate end_urlopu = resultSet.getDate("end_urlopu").toLocalDate();
                boolean akceptacja = resultSet.getBoolean("akceptacja");
                int idPracownika = resultSet.getInt("Pracownicy_id");
                String email = resultSet.getString("email");

                urlopy.add(new Holiday(id,start_urlopu,end_urlopu,akceptacja,idPracownika,email));
            }
        } finally {
            close(conn, statement, resultSet);
        }
        return urlopy;
    }

    public Holiday getHoliday(String id) throws Exception {

        Holiday holiday = null;

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = DriverManager.getConnection(URL, name, password);

            // zapytanie SELECT
            String sql = "SELECT * FROM urlopy WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

            // wykonanie zapytania
            resultSet = statement.executeQuery();

            // przetworzenie wyniku zapytania

            if (resultSet.next()) {

//                int pid = resultSet.getInt("id");
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

    public List <Holiday> getHolidayToDelete () throws  Exception{
        List <Holiday> toDelete =  new ArrayList<>();
        Connection connection = null;
        Statement statement= null;
        ResultSet resultSet= null;

        try{
            connection =  DriverManager.getConnection(URL,name,password);
            String sql =  "Select * From urlopy where to_delete = 1";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                int id = resultSet.getInt("idUrlopy");
                LocalDate start_urlopu = resultSet.getDate("start_urlopu").toLocalDate();
                LocalDate end_urlopu = resultSet.getDate("end_urlopu").toLocalDate();
                boolean akceptacja = resultSet.getBoolean("akceptacja");
                int idPracownika = resultSet.getInt("Pracownicy_id");
                String email = resultSet.getString("email");

                toDelete.add(new Holiday(id,start_urlopu,end_urlopu,akceptacja,idPracownika,email));
            }

        }finally {
            close(connection,statement,resultSet);
        }
        return toDelete;
    }

    public Boolean getStatus(String id) throws Exception {

        boolean akceptacja = false;

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = DriverManager.getConnection(URL, name, password);

            // zapytanie SELECT
            String sql = "SELECT akceptacja FROM urlopy WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

            // wykonanie zapytania
            resultSet = statement.executeQuery();

            // przetworzenie wyniku zapytania

            if (resultSet.next()) {

//                int pid = resultSet.getInt("id");

               akceptacja = resultSet.getBoolean("akceptacja");


                // utworzenie obiektu


            } else {
                throw new Exception("Nie można znaleźć id " + holidayID);
            }

            return akceptacja;

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, resultSet);

        }

    }

    public void updateHoliday(Boolean akceptacja, int id) throws Exception {

        Connection conn = null;
        PreparedStatement statement = null;
        Holiday holiday=getHoliday(String.valueOf(id));

        try {

            // polaczenie z BD
            conn = DriverManager.getConnection(URL, name, password);

            // zapytanie UPDATE
            String sql = "UPDATE urlopy SET akceptacja=? "+
                    "WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setBoolean(1, akceptacja);
            statement.setInt(2, holiday.getId());

            holiday.setAkceptacja(akceptacja);


            // wykonanie zapytania
            statement.execute();

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, null);

        }

    }
    public void adminDeleteHoliday (String id) throws Exception{
        Connection  connection = null;
        PreparedStatement statement = null;

        try{
            int idUrlopy =  Integer.parseInt(id);

            connection = DriverManager.getConnection(URL, name, password);

            String sql = ("DELETE FROM urlopy WHERE idUrlopy = ?");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idUrlopy);

            statement.execute();

        }   finally {
            close(connection,statement,null);
        }

    }





    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}