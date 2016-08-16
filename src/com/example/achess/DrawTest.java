package com.example.achess;


import java.io.IOException;
import java.sql.SQLException;


import android.view.View;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.graphics.Point;


public class DrawTest extends View {        
    Paint p;
    Desc desc1;
    Display disp;
    
    public DrawTest(Context context, AttributeSet attributeSet) throws SQLException {
   
      super(context, attributeSet);
      //if(!isInEditMode())   init(context);
      //Display display = this.getDisplay();
      p = new Paint();
      DBhelper dbHelper;
	  try {
			/*disp = display;
			//Определяем размер крадратика доски исходя из ширины экрана
			//Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			disp.getSize(size);
			int width =  (int)Math.ceil(size.x/8);*/
		    DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
			int width = (int)Math.ceil(displaymetrics.widthPixels/8);
            //для работы с БД		  
			dbHelper = new DBhelper(context);	
			desc1 = new Desc(width,width,dbHelper,context); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
      
    }
    
   
    public void Refresh_Desc(Context context){
 	    desc1 = null;
 	   
 	    //Определяем размер крадратика доски исходя из ширины экрана
		//Display display = getWindowManager().getDefaultDisplay();
		//Point size = new Point();
		//disp.getSize(size);
		//int width =  (int)Math.ceil(size.x/8);
	    DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
		int width = (int)Math.ceil(displaymetrics.widthPixels/8);
		//int width = 60;
		
		DBhelper dbHelper;
		try {
			dbHelper = new DBhelper(context);
			desc1 = new Desc(width,width,dbHelper,context); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
 	    // repaint();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	desc1.Draw_desc(p, canvas);
    	desc1.Draw_Place(p, canvas); 
    	desc1.Draw_Figures(p, canvas);
    }
    

   public void clear_desc() {
    	desc1.clear_desc();
    }
   
}
