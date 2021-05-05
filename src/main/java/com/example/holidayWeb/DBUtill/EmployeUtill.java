package com.example.holidayWeb.DBUtill;

import com.example.holidayWeb.Employee;
import com.example.holidayWeb.Holiday;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeUtill extends DBEmployee {
    private DataSource dataSource;
    private String URL;
    private String email;
    private String password;
    public EmployeUtill (String URL){this.dataSource = dataSource; }


    @Override
    public List<Employee> getEmployes() throws Exception{
        List<Employee> employes  =  new ArrayList<>();
        Connection connection =  null;
        Statement statement =  null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
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

    public List<Holiday> getUserHolidays (String email) throws Exception{

       List <Holiday> holiday = null;

        Connection connection =  null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{


            connection = DriverManager.getConnection(URL, this.email, password);

            String sql = "SELECT * FROM urlopy where email =?";
            statement = connection.prepareStatement(sql);
//            ((PreparedStatement) statement).setString(5,idEmploye); // todo że 5 kolumna
            resultSet =  statement.executeQuery(sql);

            if (resultSet.next()){
                int idUrlopy =  resultSet.getInt("idUrlopy");
                LocalDate start =  resultSet.getDate("start_urlopu").toLocalDate();
                LocalDate end =  resultSet.getDate("end_urlopu").toLocalDate();
                boolean akceptacja =  resultSet.getBoolean("akceptacja");
                int idPracownika = resultSet.getInt("Praconicy_id");
                String name = resultSet.getString("imie_nazwisko");

                holiday.add( new Holiday(idUrlopy,start,end,akceptacja,idPracownika, name));
            }else{
                throw new Exception("You dont have any planned leaves");
            }
            return holiday;
        }finally {
            close(connection, statement,resultSet);
        }

    }


    public void setEmail(String email){this.email = email;}
    public void setPassword(String password){this.password = password;}
}
