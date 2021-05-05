package com.example.holidayWeb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBUtilEmployee extends DBUtil {

    private DataSource dataSource;

    public DBUtilEmployee(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Holiday> getHolidays() throws Exception {

        List<Holiday> phones = new ArrayList<>();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // polaczenie z BD
            conn = dataSource.getConnection();

            // wyrazenie SQL
            String sql = "SELECT * FROM Urlopy";
            statement = conn.createStatement();

            // wykonanie wyrazenia SQL
            resultSet = statement.executeQuery(sql);

            // przetworzenie wyniku zapytania
            while (resultSet.next()) {

                // pobranie danych z rzedu
                int id = resultSet.getInt("id");
                LocalDate startUrlopu = resultSet.getDate("start_urlopu").toLocalDate();
                LocalDate endUrlopu = resultSet.getDate("end_urlopu").toLocalDate();
                String akceptacja = resultSet.getString("akceptacja");
                int idEmployee = resultSet.getInt("Pracownicy_id");


                // dodanie do listy nowego obiektu
                phones.add(new Holiday(id,startUrlopu,endUrlopu,akceptacja,idEmployee));

            }

        } finally {

            // zamkniecie obiektow JDBC
            close(conn, statement, resultSet);
        }


        return phones;

    }
}
