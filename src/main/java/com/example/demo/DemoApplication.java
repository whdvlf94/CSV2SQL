package com.example.demo;

import com.example.demo.repository.DataRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {

    private static String fileName = "examples.csv";


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, URISyntaxException{



        //table name
        String tableName = fileName.substring(0, fileName.length() - 4);


        //show csv file to JSON
//        CSV2JSON csv2json = new CSV2JSON(fileName);


        //Creating database
        DataRepository db = new DataRepository();

        //CSV Reader
        Scanner inputReader = new Scanner(new File(fileName), "utf-8");


        //Getting column names from first line
        String columns = (inputReader.nextLine()).replace(" ",",");


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
