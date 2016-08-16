package com.example.achess;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.io.IOException;
import java.sql.SQLException;


import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Button;




import android.view.View.OnClickListener;


@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity implements OnTouchListener {
    DrawTest drawView;
    private Button mButton;
    AttributeSet attributeSet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {        
         	    setContentView(R.layout.activity_main);
     	        //drawView=(DrawTest)findViewById(R.id.DrawTest); 
     	        drawView.setOnTouchListener(this); 
     	        
		     //   mButton=(Button)findViewById(R.id.button1);		      
              
		        //нажатие на кнопку "начать сначала"
		        OnClickListener oclBtnClear = new OnClickListener() {
			         @Override
			        public void onClick(View v) {
			        	 //расставляем фигуры заново		       
			        	 drawView.clear_desc();
			        	 drawView.invalidate();
			        	 v.invalidate();
			         }
		       };		      
		       mButton.setOnClickListener(oclBtnClear);

		} catch (Exception e) {			
			e.printStackTrace();
		}   
    }   


    @Override
    public boolean onTouch(View v, MotionEvent event) { 
	      switch (event.getAction()) {
	      case MotionEvent.ACTION_DOWN: // нажатие
	        this.drawView.desc1.Play(Math.round(event.getX()), Math.round(event.getY()));
	      //  this.drawView.Refresh_Desc(this);
	        //v.refreshDrawableState();
	        v.invalidate();
	        break;	    
	      }  
	      return true;
    }
    
   
   
   


}


