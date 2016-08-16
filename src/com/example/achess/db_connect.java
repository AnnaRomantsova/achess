package com.example.achess;

//package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Romantsova
 * Класс подключение к БД SQLite
 */
public class db_connect  implements Cloneable{
   public  db_connect() {	 
   }
   
   public static Connection getConnection() {
		 
			   try {
			     	Class.forName("org.sqlite.JDBC"); 
			     	Connection connect = DriverManager.getConnection("jdbc:sqlite:chess.db"); 
			     	return connect;
		       } catch (ClassNotFoundException e) {
				    e.printStackTrace();
				    return null;
			   } catch (SQLException e) {
				// TODO Auto-generated catch block
				    e.printStackTrace();
				    return null;
			}	
	}
   
   /*
    @Override
    public ResultSet clone() throws CloneNotSupportedException{
   	    ResultSet obj = (ResultSet)super.clone();
        return obj;
    }
  
   ResultSet rs;
   ResultSet rs1;*/
   
   public  Connection connect;
}


/*

*/
