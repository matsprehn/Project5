package edu.uci.ics.android;

import edu.uci.ics.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Demo_3 extends Activity {
	
	private TextView text;
	private Button button1;
	private Button button2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbAdapter db = new DbAdapter(this);
        setContentView(R.layout.main);
        
        // Change the content of the text view
        this.text = (TextView)this.findViewById(R.id.text);
        this.text.setText("Project 5 database");
        
        // Retrieve the button, change its value and add an event listener
        this.button1 = (Button)this.findViewById(R.id.button1);
        //this.button1.setText("Change the color");
        this.button2 = (Button)this.findViewById(R.id.button2);
        
        this.button2.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//Intent intent = new Intent(Demo_3.this, Statistics.class);
        		//startActivity(intent);
        	}
        });
        this.button1.setOnClickListener(new OnClickListener() {		
        	public void onClick(View v) {
			// Change the button image
			//button1.setBackgroundResource(R.drawable.btn_default_normal_green);	
			Intent intent = new Intent(Demo_3.this, NextActivity.class);
			intent.putExtra("pauseTime", 0);
			intent.putExtra("numCorrect", 0);
			intent.putExtra("totalQuestions", 0);
			//TODO: need to add timePerQuestion too
			startActivity(intent);
		}
        });

    }
}