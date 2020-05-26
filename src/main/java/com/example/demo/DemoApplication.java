package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {


    private static String fileName = "example.csv";
//    private static String fileName = "alg_1.csv";


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, URISyntaxException {
//        String fileName = file.getName();

        //table name
        String tableName = fileName.substring(0, fileName.length() - 4);

        //Creating database
        Database db = new Database();
//        Database db2 = new Database();

        //CSV Reader
        Scanner inputReader = new Scanner(new File(fileName));


        //Getting column names from first line
        String columns = (inputReader.nextLine()).replace(" ","_");


        //Check the table if does not exist, create a table
        db.createTable(tableName, columns);

        //create a Header Mapping table
        String headerTableName = tableName + "header";
        db.createHeaderTable(headerTableName);

        //Inserting data to database
        while (inputReader.hasNextLine()) {
            db.addData(tableName, columns, gernerateRow(inputReader.nextLine()));
        }
            db.addHeaderData(headerTableName,columns);

    }

    //Generate suitable row for entering SQL Query
    public static String gernerateRow(String row) {
        String rowForSQL = "";
        String[] cols = row.split(",");
        for (int i = 0; i < cols.length; i++) {
            rowForSQL += "'" + cols[i] + "'" + (i != (cols.length - 1) ? "," : "");
        }
        return rowForSQL;
    }


}
