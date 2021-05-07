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

    public EmployeUtill(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Employee> getEmployes() throws Exception {
        List<Employee> employes = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM pracownicy";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("imie_nazwisko");
                int staz = resultSet.getInt("staz");
                int extraDays = resultSet.getInt("dodatkowe_dni");
                String jobTime = resultSet.getString("etat");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                employes.add(new Employee(id, name, staz, jobTime, extraDays, email, password));
            }
        } finally {
            close(connection, statement, resultSet);
        }
        return employes;
    }

    public int getId(String emailE, String pass) throws Exception {
        int id;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            String sql = "Select id from pracownicy where email = ? and password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, emailE);
            statement.setString(2, pass);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            } else {
                throw new Exception("There is not such user!");
            }
            return id;
        } finally {
            close(connection, statement, resultSet);
        }

    }

    public Boolean getPassword(String email1, String password1) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Boolean DBpass;
        try {
            connection = dataSource.getConnection();
            String sql = "select password,email from pracownicy where email =? and password=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email1);
            statement.setString(2, password1);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                DBpass = true;
            } else {
                DBpass = false;
            }
            return DBpass;
        } finally {
            close(connection, statement, resultSet);
        }
    }

    public int usedDays(String email, String password) {
        int days = 0;
        try {
            List<Holiday> holidays = getUserHolidays(email, password);
            for (int i = 0; i < holidays.size(); i++) {
                days = (int) ChronoUnit.DAYS.between(holidays.get(i).getStartUrlopu(), holidays.get(i).getKoniecUrlopu()) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    public List<Holiday> getUserHolidays(String email1, String password1) throws Exception {

        List<Holiday> holiday = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {


            connection = dataSource.getConnection();

            int id = getId(email1, password1);

            String sql = "SELECT * FROM urlopy where Pracownicy_id = ? ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idUrlopy = resultSet.getInt("idUrlopy");
                LocalDate start = resultSet.getDate("start_urlopu").toLocalDate();
                LocalDate end = resultSet.getDate("end_urlopu").toLocalDate();
                boolean akceptacja = resultSet.getBoolean("akceptacja");
                int idPracownika = resultSet.getInt("Pracownicy_id");
                String name = resultSet.getString("email");

                holiday.add(new Holiday(idUrlopy, start, end, akceptacja, idPracownika, name));
            }

        } finally {
            close(connection, statement, resultSet);
        }
        return holiday;

    }

    public void addHoliday(Holiday holiday) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean toDelete=false;
        try {
            connection = dataSource.getConnection();
            String sql = "Insert into urlopy (start_urlopu, end_urlopu, akceptacja, Pracownicy_id ,email,to_delete)" + //todo Pracownicy_id?
                    "VALUES(?, ?, ?, ?, ?,?)";
            statement = connection.prepareStatement(sql);

            statement.setString(1, (holiday.getStartUrlopu()).toString());
            statement.setString(2, (holiday.getKoniecUrlopu().toString()));
//            statement.setDate(2, Date.valueOf(holiday.getKoniecUrlopu()));
            statement.setBoolean(3, holiday.isAkceptacja());
            statement.setInt(4, holiday.getPracownikId());
            statement.setString(5, holiday.getEmail());
            statement.setBoolean(6,toDelete );
            statement.execute();
        } finally {
            close(connection, statement, null);
        }
    }

    public void addEmployee(Employee employee) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            String sql = "Insert into pracownicy (imie_nazwisko, staz, dodatkowe_dni, etat ,password,email)" + //todo Pracownicy_id?
                    "VALUES(?, ?, ?, ?, ?,?)";
            statement = connection.prepareStatement(sql);

            statement.setString(1, (employee.getName()));
            statement.setInt(2, (employee.getSeniority()));
            statement.setInt(3, employee.getExtraDays());
            statement.setString(4, employee.getJobTime());
            statement.setString(5, employee.getPassword());
            statement.setString(6, employee.getEmail());
            statement.execute();
        } finally {
            close(connection, statement, null);
        }
    }

    public Holiday getHoliday(String id) throws Exception {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Holiday holiday = null;


        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = dataSource.getConnection();

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
                holiday = new Holiday(holidayID, startUrlopu, koniecUrlopu, akceptacja, pracownikID, email);


            } else {
                throw new Exception("Nie można znaleźć id " + holidayID);
            }

            return holiday;

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, resultSet);

        }

    }


    public LocalDate getEndDate(String id) throws Exception {

        LocalDate endDate = LocalDate.parse("2020-01-01");

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = dataSource.getConnection();

            // zapytanie SELECT
            String sql = "SELECT end_urlopu FROM urlopy WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

            // wykonanie zapytania
            resultSet = statement.executeQuery();

            // przetworzenie wyniku zapytania

            if (resultSet.next()) {

//                int pid = resultSet.getInt("id");

                endDate = resultSet.getDate("end_urlopu").toLocalDate();


                // utworzenie obiektu


            } else {
                throw new Exception("Nie można znaleźć id " + holidayID);
            }

            return endDate;

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, resultSet);

        }

    }

    public LocalDate getStartDate(String id) throws Exception {

        LocalDate startDate = LocalDate.parse("2020-01-01");

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            // konwersja id na liczbe
            int holidayID = Integer.parseInt(id);

            // polaczenie z BD
            conn = dataSource.getConnection();

            // zapytanie SELECT
            String sql = "SELECT start_urlopu FROM urlopy WHERE idUrlopy =?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

            // wykonanie zapytania
            resultSet = statement.executeQuery();

            // przetworzenie wyniku zapytania

            if (resultSet.next()) {

//                int pid = resultSet.getInt("id");

                startDate = resultSet.getDate("start_urlopu").toLocalDate();


                // utworzenie obiektu


            } else {
                throw new Exception("Nie można znaleźć id " + holidayID);
            }

            return startDate;

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, resultSet);

        }

    }

    public void updateHoliday(LocalDate start, LocalDate end, int id) throws Exception {

        Connection conn = null;
        PreparedStatement statement = null;

        try {

            // polaczenie z BD
            conn = dataSource.getConnection();

            // zapytanie UPDATE
            String sql = "UPDATE urlopy SET start_urlopu= ?, end_urlopu= ?,akceptacja = 0 WHERE idUrlopy = ?";

            statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(start));
            statement.setDate(2, Date.valueOf(end));
            statement.setInt(3, id);


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
            conn = dataSource.getConnection();

            //String sql = "DELETE FROM urlopy WHERE idUrlopy =?";
            String sql = "UPDATE urlopy SET to_delete = 1 where idUrlopy = ?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1, holidayID);

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

            connection = dataSource.getConnection();

            String sql = ("DELETE FROM urlopy WHERE idUrlopy = ?");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idUrlopy);

            statement.execute();

        }   finally {
            close(connection,statement,null);
        }

    }


    public List <Holiday> getHolidayToDelete () throws  Exception{
            List <Holiday> toDelete =  new ArrayList<>();
            Connection connection = null;
            Statement statement= null;
            ResultSet resultSet= null;

            try{
                connection =  dataSource.getConnection();
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



    public int getStaz(String emailE,String password) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int staz;
        try {
            connection = dataSource.getConnection();
            String sql = "select staz from pracownicy where email = ? and password=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, emailE);
            statement.setString(2,password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                staz = resultSet.getInt("staz");
            } else {
                throw new Exception("smoething went wrong");
            }
            return staz;
        } finally {
            close(connection, statement, resultSet);
        }
    }
}

