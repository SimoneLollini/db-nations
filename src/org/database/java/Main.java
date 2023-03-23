package org.database.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        }catch (SQLException e ){
            e.printStackTrace();
        }

    }
}
