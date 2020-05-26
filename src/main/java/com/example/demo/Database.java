package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Repository
public class Database {
    private String port = "3306";
    private String dbName = "csv2sql";
    private String userName = "root";
    private String password = "spring";
    private Connection con;

    public Database() throws SQLException, ClassNotFoundException {
        connect();
    }

    public Database(String port, String dbName, String userName, String password) throws ClassNotFoundException, SQLException {
        this.port = port;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
        connect();
    }

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + dbName + "?serverTimezone=Asia/Seoul", userName, password);
    }

    public void addData(String tableName, String columns, String values) throws SQLException {
        String query = "insert into " + tableName + "(ID," + columns + ")";
        query += "values(NULL," + values + ");";


        Statement stm = (Statement) con.createStatement();
        int executeUpdate = stm.executeUpdate(query);

        System.out.println("Eklendi!");
    }

    public void addHeaderData(String tableName, String columns) throws SQLException {
        String[] cols = columns.split(",");


//        List<String> list = new ArrayList<>();
//        String query ="";
//        for (int i = 0; i < cols.length; i++) {
//            String item = cols[i];
//            query = "INSERT INTO " + tableName + "(ID,Header)" + " VALUES (NULL," + '"'+ item + '"'+ ");";
//            list.add(query);
//        }
//
//        for (String item:list
//             ) {
//            Statement stm = (Statement) con.createStatement();
//            stm.executeUpdate(item);
//        }

        for (int i = 0; i < cols.length; i++) {
            String item = cols[i];
            String query = "INSERT INTO " + tableName + "(ID,Header)" + " VALUES (NULL," + '"'+ item + '"'+ ");";
            Statement stm = (Statement) con.createStatement();
            stm.executeUpdate(query);
        }
    }


    public void createHeaderTable(String headerTableName) throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + headerTableName + " (ID int NOT NULL AUTO_INCREMENT, Header VARCHAR(255) NOT NULL ,";
        sqlCreate += "PRIMARY KEY (ID));";
        Statement stmt = con.createStatement();
        stmt.execute(sqlCreate);

    }


    public void createTable(String tableName, String columns) throws SQLException {
        String[] cols = columns.split(",");
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + tableName + " (ID int NOT NULL AUTO_INCREMENT,";
        for (int i = 0; i < cols.length; i++) {
            sqlCreate += cols[i] + checkType(cols[i]) + ",";
        }
        sqlCreate += "PRIMARY KEY (ID))";
        Statement stmt = con.createStatement();
        stmt.execute(sqlCreate);
    }

    public String checkType(String columnName) {
        Scanner sc = new Scanner(columnName);
        if (sc.hasNextInt()) {
            return " INT(11)";
        }
        else if(sc.hasNextDouble()){return " DECIMAL(8,2)";}
        else {
            return " VARCHAR(255)";
        }
    }
}
