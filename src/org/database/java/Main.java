package org.database.java;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        String url = "jdbc:mysql://localhost:3307/nome-db";
//        String user = "username";
//        String password = "password";

        //Modificare il programma precedente per fare in modo che un utente possa inserire una ricerca e filtrare i risultati:
        //- chiedere all’utente di inserire una stringa di ricerca da terminale
        //- usare quella stringa come parametro aggiuntivo della query in
        //modo che i risultati vengano filtrati con un contains (ad esempio se l’utente cerca per “ita”, il risultato della query conterrà sia Italy che Mauritania.

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        Scanner scan = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connessione riuscita!");

            String query = """
                              SELECT countries.name as countries_name, countries.country_id , regions.name as region_name, continents.name as continents_name
                              FROM countries
                              JOIN regions ON regions.region_id = countries.region_id
                              JOIN continents ON continents.continent_id = regions.continent_id
                              ORDER BY countries.name;
                    """;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int country_id = resultSet.getInt("country_id");
                        String country_name = resultSet.getNString("countries_name");
                        String region_name = resultSet.getNString("region_name");
                        String continents_name = resultSet.getNString("continents_name");

                        System.out.println(country_id + " " + country_name + " " + region_name + " " + continents_name);
                    }
                }
            }


            String fillterQuery = """
                      SELECT countries.name as countries_name, countries.country_id , regions.name as region_name, continents.name as continents_name
                      FROM countries
                      JOIN regions ON regions.region_id = countries.region_id
                      JOIN continents ON continents.continent_id = regions.continent_id
                      WHERE countries.name LIKE ?
                      ORDER BY countries.name;
                    """;
            //PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
            try (PreparedStatement preparedStatement = connection.prepareStatement(fillterQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

                System.out.println("Filtra ricerca per nome: ");
                String filter = "%" + scan.nextLine() + "%";

                preparedStatement.setString(1, filter);

                try (ResultSet resultSetFilter = preparedStatement.executeQuery()) {
                    while (resultSetFilter.next()) {
                        int country_id = resultSetFilter.getInt("country_id");
                        String country_name = resultSetFilter.getNString("countries_name");
                        String region_name = resultSetFilter.getNString("region_name");
                        String continents_name = resultSetFilter.getNString("continents_name");

                        System.out.println(country_id + " " + country_name + " " + region_name + " " + continents_name);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
