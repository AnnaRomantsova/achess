package com.example.achess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.database.Cursor;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * @author Romantsova
 * ����� ��� ������ � �������
 */
public class Figure {
    /**
     * ����������� �� ����������� �� �����
     * @param x1 
     * @param y1
     */
	public Figure (int x1, int y1, DBhelper dbHelper) {			
		x = x1;
		y = y1;		

	    try {
	    	dbHelp= dbHelper;
	    	dbHelp.opendatabase();
           	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from chess_desc where x = "+x+" and y="+y, null);
			if (rs.moveToNext()) { 	    	
	  	          	    
	  	    	id = rs.getInt(rs.getColumnIndex("id_figure"));
	  	    	figure_on_desc_id = rs.getInt(rs.getColumnIndex("id"));  	    
				rs =  dbHelp.myDataBase.rawQuery("Select * from figure where id = "+id,null);			
		  	    while (rs.moveToNext()) {	  	      
		  	       patch = rs.getString(rs.getColumnIndex("patch"));  
		  	       Color = rs.getInt(rs.getColumnIndex("color")); 
		  	    }	  	   
	  	    }	
	  	   rs.close(); 	    
	  	  
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }		
	}
	/**
	 * ����������� �� id ������ �� �����
	 * @param id_fig
	 */
	public Figure (int id_fig, DBhelper dbHelper) {
		
		  figure_on_desc_id =id_fig;
	      try {
	    	dbHelp= dbHelper; 
	  		dbHelp.opendatabase();
	    	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from chess_desc where id = "+id_fig,null);			
		  	if (rs.moveToNext()) {
		  	   	id = rs.getInt(rs.getColumnIndex("id_figure"));
			    rs =  dbHelp.myDataBase.rawQuery("Select * from figure where id = "+id,null);			
		  	    while (rs.moveToNext()) {	  	      
		  	       patch =  rs.getString(rs.getColumnIndex("patch"));  
		  	       Color = rs.getInt(rs.getColumnIndex("color"));
		  	    }	
		  	   		  	    
		  	}
		  	rs.close();		  	
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }		
	}	
	
	/**
	 * �������� ������ � �����	
	 */
	private void delete_figure_from_desc() {
		
	     try {
	    	 dbHelp.myDataBase.execSQL("delete from chess_desc where id = "+figure_on_desc_id);			
	    	
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		 }		
	}	
	/**
	 * ����������� ������ �� ����� ����� � ������������ �������� ����� ������
	 * @param x - ����� ����� �� �����
	 * @param y - ����� ����� �� �����
	 * @param hod - ������� ���
	 */
	public void Put_to_new_place(int x, int y,int hod)  {
	    try {
	    	 //��������� �� ������ �� �� ���� ����� ��������� ������	
	    	
	    	 Figure f = new Figure(x,y,dbHelp);
	    	 if (f.Color > -1 && f.Color != hod) f.delete_figure_from_desc();	 
	    	 
	    	 dbHelp.myDataBase.execSQL("update chess_desc set x="+x+", y = "+y+" where id = "+ figure_on_desc_id);	
	    	 
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }		
	}

	
//	private Connection conn;
//	private Statement statement;
	DBhelper dbHelp;
	public int x;
	public int y;
	/**
	 * id ������ �� ���� Figure
	 */
	public int id = -1;
	/**
	 * id ������ �� �����
	 */
	public int figure_on_desc_id;
	/**
	 * ���� ������
	 */
	public int Color = -1; //1 -white, 0-black
	/**
	 * ���� � �������� ������
	 */
	public String patch;
	
	
}
