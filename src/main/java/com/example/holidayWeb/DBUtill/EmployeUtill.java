package com.example.holidayWeb.DBUtill;

import com.example.holidayWeb.Employee;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeUtill extends DBEmployee {
    private DataSource dataSource;

    public EmployeUtill (DataSource dataSource){this.dataSource = dataSource; }


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
}
