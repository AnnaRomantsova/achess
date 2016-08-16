package com.example.achess;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

//import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.Context;



import android.database.Cursor;

//import javax.swing.*;

/**
 Класс шахм Доска  
 @author Romantsova
*/
public class Desc {
    /**
     * @param w -ширина клетки на доске
     * @param h -длина клетки на доске
     */
	public Desc(int w, int h, DBhelper dbHelper,Context cont) {
		 
		dbHelp= dbHelper; 
		dbHelp.opendatabase();
		Get_Figures();
		hod = Get_Hod();
	    cell_width = w;
		cell_height = h;
		context = cont;
		//bitm = bitmap;
	}
	
	/**
	  загружает массив фигур из таблицы chess_desc
	*/
	private void Get_Figures() {
			 			 
  
		    	Arrays.fill(Figures, null);		    	
	           	Cursor rs = dbHelp.myDataBase.rawQuery("select * from chess_desc", null);
				int i = 0;
		  	    while (rs.moveToNext()) {
		  	      try { 	
		  	        int x = rs.getInt(rs.getColumnIndex("x"));
		  	        int y = rs.getInt(rs.getColumnIndex("y"));
		  	        Figures[i] = new Figure (x,y,dbHelp);		  	        
		  	        i ++;	
		  	      } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }	 		  
		  	          	   
		  	    }	    		  	 
		  	   rs.close();
	}    
	/**
	 * @return 0 - если ходят белые, 1 - если черные
	 */
	private int Get_Hod() {
	     try {
	    	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from hod",null);	
	    	rs.moveToNext();
			int hod=rs.getInt(rs.getColumnIndex("hod"));			
		  	rs.close(); 		  	
			return  hod; 	   
				    
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		 }  
	     
	}	
	/**
	 * рисует пустую доску	
	 */
	public  void  Draw_desc(Paint paint, Canvas canvas){
		   
		   // Graphics2D g2 = (Graphics2D) g;
		    int leftX = 0;
		    int topY = 0;
		   // Rectangle2D square; 
		    paint.setStyle(Paint.Style.FILL); 
       	    for (int i=1; i<=8; i++) {
	       	  for (int j=1; j<=8; j++) {
	       		
	       		  if (j == 1) leftX = -cell_width;
	       		  if ((i-j)%2 == 0 ) paint.setColor(Color.GRAY);
	       		     else   paint.setColor(Color.WHITE);
	       		  
	       		  leftX = leftX + cell_width;    		  
	       		 // square = new Rectangle2D.Double(leftX, topY, cell_width, cell_height);
	       		 canvas.drawRect(leftX, topY, cell_width+leftX, cell_height+topY, paint);
	       	     // g2.fill(square);
	       		
	       	  }
	       	  topY   = topY + cell_height;
	         }
       	     paint.setColor(Color.BLACK);
		     hod = Get_Hod();
		    // Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		     paint.setTextSize(40);
		    // fontPaint.setStyle(Paint.Style.STROKE);
		     if (!is_game_over()) {
		        if (hod==0) status_hod = "Ходят белые"; else status_hod = "Ходят черные";
		        canvas.drawText(status_hod, 3*cell_width, cell_height*8+50, paint);		       	    
		     } else {
		    	if (hod==1) status_hod = "Победили белые!!!"; else status_hod = "Победили черные!!!";
		    	canvas.drawText(status_hod, 3*cell_width, cell_height*8+50, paint);
		     }
		     
	        // GeneralPath graph = new GeneralPath();
	        // g2.draw(graph);	         
	}
	
	/**
	 * рисуем фигуры
	 * @param g
	 * @param j
	 */
	

	
	public void Draw_Figures(Paint paint, Canvas canvas) {
	    try {
	    	
		      int x1;
		      int y1;		     
		      Bitmap bitmap;
		      
		      BitmapFactory.Options options = new BitmapFactory.Options();
		      options.outHeight = cell_height;
		      options.outWidth = cell_width;
		      for(int i = 0; i <= 32; i++)
		      {		    	 		    	   
		    	    x1 = (int) (Figures[i].x *cell_width)-cell_width;
		    	    y1 = (int) (8*cell_height-(Figures[i].y)*cell_height);
		    	    InputStream ims = getClass().getResourceAsStream("/assets/"+Figures[i].patch);
		    	    bitmap  = BitmapFactory.decodeStream(ims, null, options);
		    	    canvas.drawBitmap(bitmap, x1, y1, paint);
		      }     
		 } catch (Exception e) {
			e.printStackTrace();
		 } 
	}
	
	/**
	 * рисует место куда можно поставить выделенную фигуру из cur_Cells
	 * @param g
	 */
	
	public void Draw_Place(Paint paint, Canvas canvas ){
		Bitmap bitmap;		
	    if (cur_Cells != null) {
	    	 paint.setColor(Color.GREEN);
		    // Rectangle2D rect;
		     for (Point p: cur_Cells) { 
		    	 paint.setStrokeWidth(3);
		    	 //paint.setStrokeWidth(2);
		        // paint.setStyle(Style.STROKE);
		    	// paint.setStyle(Paint.Style.STROKE); 
		    	 canvas.drawRect((p.x-1)*cell_width , 8*cell_height -p.y*cell_height , (p.x-1)*cell_width+ cell_width, 8*cell_height -p.y*cell_height + cell_height, paint);	
		    	// rect = new Rectangle2D.Double((p.x-1)*cell_width,8*cell_height -p.y*cell_height,cell_width, cell_height);
		     }
	    }    
	}
	
    /**
     * смена хода
     */
	private void Change_Hod() {
	     try {
	    	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from hod",null);	
		    rs.moveToNext();	
			int hod=rs.getInt(rs.getColumnIndex("hod"));
			if (hod==1)  dbHelp.myDataBase.execSQL("update hod set hod = 0"); else dbHelp.myDataBase.execSQL("update hod set hod = 1");
	  	    rs.close();	  	   
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		 }  
	}
	/**
	 * 
	 * @return true - если конец игры
	 */
   	public boolean is_game_over() {
	     try {
	    	Cursor rs = dbHelp.myDataBase.rawQuery("Select count(*) as cnt from chess_desc where id_figure in (12,6)",null);	 
	    	rs.moveToNext();	
			int cnt = rs.getInt(rs.getColumnIndex("cnt"));
			rs.close();			
			if (cnt<2) return true; else return false;  
	  	    
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		 }     
	     
	}
   	
	/**
	 * возвращает массив клеток куда можно поставить фигуру
	 * @param x -координата x клетки где стоит фигура
	 * @param y - координата y клетки где стоит фигура
	 * @param fig_id - id фигуры на доске из таблицы chess_desc
	 * @return массив нужных клеток
	 */
   	
	public  ArrayList<Point> show_move_place(int x,int y,int fig_id) {
		int move_x;
	    int move_y;
	    int sort;
	    int movement_id;
	    int id_figure;
	    cells = null;
	    Point p =null;
	   
		if (fig_id<=0) return null;
	    try { 	    
	    	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from chess_desc where id = "+fig_id,null);
	    	if  (rs.moveToNext()) {
	    		id_figure = rs.getInt(rs.getColumnIndex("id"));
	    		rs = dbHelp.myDataBase.rawQuery("Select * from move where id_figure = "+rs.getString(rs.getColumnIndex("id_figure")),null);		
		  	    while (rs.moveToNext()) {
		  	        move_x = rs.getInt(rs.getColumnIndex("move_x"));
		  	        move_y = rs.getInt(rs.getColumnIndex("move_y"));
		  	        sort = rs.getInt(rs.getColumnIndex("sort"));
		  	        movement_id = rs.getInt(rs.getColumnIndex("movement_id"));
		  	        if (is_in_desc(x+move_x, y+move_y)) {
		  	        	if (if_free_cell(x,y,x+move_x,y+move_y,sort,movement_id,id_figure)) {
			  	        	if (cells == null) cells = new ArrayList<Point>();
			  	        	p = new Point();
			  	        	p.x = x+move_x;
			  	        	p.y = y+move_y;
			  	        	cells.add(p);
		  	        	};	
		  	        }  
		  	    }	
		  	  rs.close();
	    	}
	    	rs.close();
	    	
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	    return cells;
	}
	
	
	/**
	 * проверка клетки (x1,y1) на то можно ли поставить на нее фигуру id_figure
	 * @param x - координаты фигуры откуда надо переставить
	 * @param y - координаты фигуры откуда надо переставить
	 * @param x1 - координаты фигуры куда надо переставить
	 * @param y1 - координаты фигуры куда надо переставить
	 * @param sort - сортировка внутри движения movement_id
	 * @param movement_id - id движения movement_id
	 * @param id_figure
	 * @return true если на пути до клетки(x1,y1) не было других фигур и клетка(x1,y1) пуста или на ней стоит чужая фигура
	 */
	private boolean if_free_cell(int x,int y,int x1,int y1, int sort,int movement_id,int id_figure){
	    try {
	    	int new_x;
	    	int new_y;
	    	Figure fig;
	    	Figure fig1;
	    	fig = new Figure(id_figure,dbHelp);
	    	
	    	//проверка самой клетки
	    	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from chess_desc where x = "+x1+" and y = "+y1,null);		
	  	    if (rs.moveToNext()){		  	      
	  	    	fig1 = new Figure(rs.getInt(rs.getColumnIndex("id")),dbHelp);
	  	        if (fig1.Color == fig.Color)  {
	  	        	rs.close();	  		    	  	       
	  	        	return false;		  	         
	  	        };	
	  	    }
	  	    rs.close();
	  	    //проверка клеток по пути до точки
	    	rs =  dbHelp.myDataBase.rawQuery("Select * from move where movement_id = "+movement_id+" and sort < "+sort,null);
	    	while  (rs.moveToNext()) {
	    		new_x = x+rs.getInt(rs.getColumnIndex("move_x"));
	    		new_y = y+rs.getInt(rs.getColumnIndex("move_y"));	    	
	    	    if (is_in_desc(new_x,new_y)) {
	              Cursor rs1 = dbHelp.myDataBase.rawQuery("Select * from chess_desc where x = "+new_x+" and y = "+new_y,null);		
		  	      if (rs1.moveToNext()){		  	      
		  	    	  rs.close();			    	 
		  	    	  return false;		  	         
		  	      }  
		  	      rs1.close();
		  	    }	  	    
	    	}
	    	rs.close();	    	
	    	return true; 
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		 }
	}
	
	/**
	 * по x, y ищет клетку, затем фигуру в этой клетке если она есть
	 * @param x - координаты клетки
	 * @param y - координаты клетки
	 * @return id фигуры на доске или 0 если клетка пуста
	 */
	public int get_figure_on_desc(int x, int y)
	{
		
		int id_figure = 0;
		if (x <= 8 && x>=1 && y >=1 && y <=8) {
			
		     try {
		   
		    	Cursor rs = dbHelp.myDataBase.rawQuery("Select * from chess_desc where x = "+x +" and y = "+y,null);				
		  	    if (rs.moveToNext()) {
		  	        id_figure = rs.getInt(rs.getColumnIndex("id"));
		  	        rs.close();			  	  
                    return id_figure; 		  	      
		  	    }
		  	    rs.close();		  	    
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }		     
		}
		
		return id_figure; 
	}
	/**
	  * @return true если клетка помещается на доске 
	 */
	private  boolean is_in_desc(int x,int y){		
 		if (x>8 || x<1 || y>8 ||y <1) return false; 
 		  else return true;
	}
	
    /**
     * @param x
     * @param y 
     * @param fig_list -массив клеток 
     * @return true если (x,y) находится среди fig_list
     */
	
	private boolean if_coursor_in_move_place(int x,int y, ArrayList<Point> fig_list) {
		    
		  for (Point p: fig_list) { 
	  	       if (p.x == x &&p.y ==y) return true;	  	       
	  	  }	  	    
	    return false;
	}
	
	/**
	 * очистка доски, расстановка фигур заново
	 */
	public  void clear_desc(){
	    try {
	     	    	
			//dbHelper.opendatabase();
	    	dbHelp.myDataBase.execSQL("delete from chess_desc");
	    	dbHelp.myDataBase.execSQL("insert into chess_desc select * from clear_chess_desc");
	    	dbHelp.myDataBase.execSQL("update hod set hod=0");
	    //	dbHelp.close();
	    
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	/**
	 * совершение хода
	 * @param mouse_x -коорд мыши
	 * @param mouse_y -коорд мыши
	 */
	
	public void Play(int mouse_x,int mouse_y) {
		//Connection = getConnection();
		if (is_game_over()) return;
	    int new_x =  (int)Math.ceil( (double)mouse_x /cell_width); //  Math.ceil((double)a / b).intValue();
	    int new_y = (int)Math.ceil( (double) (8*cell_height-mouse_y)/cell_height);   
	    boolean flag;
     
		new_selected_figure_id = get_figure_on_desc(new_x, new_y);		
		 
		hod = Get_Hod();	
		
		Figure cur_figure = new Figure(cur_selected_figure_id,dbHelp);
		Figure new_figure = new Figure(new_selected_figure_id,dbHelp);	
		if (new_figure.Color == hod) 
		    new_Cells = show_move_place(new_x,new_y,new_selected_figure_id);
		else new_Cells = null;
		if (cur_figure.Color == hod) {	
			
			if (cur_figure.Color != new_figure.Color && new_selected_figure_id>0) flag=true; else flag=false;  
			if (cur_selected_figure_id >0 && (new_selected_figure_id ==0 || flag)) {
				if (if_coursor_in_move_place(new_x,new_y,cur_Cells) == true ) {
				   cur_figure.Put_to_new_place(new_x,new_y,hod);
				   Change_Hod();
				   cur_Cells = null;
				};   
			};
		};	
		cur_selected_figure_id = new_selected_figure_id;	
		if (new_Cells != null)
		    cur_Cells = new ArrayList<Point>(new_Cells);	
		Get_Figures();
	   
   }
    
	/**
	 * текущий ход
	 */
	public int hod;
	//private ResultSet rs;
	//private Connection conn;
	Context context;
	//private Statement statement;
	//private Bitmap bitm;
	private Figure[] Figures = new Figure[32];
	private static ArrayList<Point> cells; 
	private int cur_selected_figure_id = 0;
	private int new_selected_figure_id = 0;   
	DBhelper dbHelp;
	private int cell_width; 
	private int cell_height;
	ArrayList<Point> cur_Cells;
	ArrayList<Point> new_Cells;	
	String status_hod;
	
}
