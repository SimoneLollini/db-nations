package org.database.java;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

//        String url = "jdbc:mysql://localhost:3307/nome-db";
//        String user = "username";
//        String password = "password";

        String url = System.getenv("DB_URL");
        String user =System.getenv("DB_USER") ;
        String password =System.getenv("DB_PASSWORD");


        try(Connection connection = DriverManager.getConnection(url,user,password)){
            System.out.println("Connessione riuscita!");

            String query = """
                              SELECT countries.name as countries_name, countries.country_id , regions.name as region_name, continents.name as continents_name
                              FROM countries\s
                              JOIN regions ON regions.region_id = countries.region_id\s
                              JOIN continents ON continents.continent_id = regions.continent_id\s
                              ORDER BY countries.name;
                    """;

            try(PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY)){
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    while(resultSet.next()){
                    int country_id = resultSet.getInt("country_id");
                    String country_name = resultSet.getNString("countries_name");
                    String region_name = resultSet.getNString("region_name");
                    String continents_name = resultSet.getNString("continents_name");

                    System.out.println(country_id + " " + country_name +" "+ region_name+" "+ continents_name);
}

                }
            }
        }catch (SQLException e ){
            e.printStackTrace();
        }

    }
}
