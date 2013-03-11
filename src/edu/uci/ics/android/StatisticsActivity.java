package edu.uci.ics.android;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsActivity extends Activity {

	private TextView quizzesTaken;
	private TextView totalCorrect;
	private TextView totalIncorrect;
	private TextView averageTimePerQuestion;
	private Button homeButton;
	private Button clearButton;
	private DbAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		
		quizzesTaken = (TextView)this.findViewById(R.id.quizzesTaken);
		totalCorrect = (TextView)this.findViewById(R.id.totalCorrect);
		totalIncorrect = (TextView)this.findViewById(R.id.totalIncorrect);
		averageTimePerQuestion = (TextView)this.findViewById(R.id.averageTimePerQuestion);
		homeButton = (Button)this.findViewById(R.id.backButton);
		clearButton = (Button)this.findViewById(R.id.clear);
		this.homeButton.setOnClickListener(new OnClickListener() {		
        	public void onClick(View v) {
			// Change the button image
			//button1.setBackgroundResource(R.drawable.btn_default_normal_green);	
			Intent intent = new Intent(StatisticsActivity.this, Demo_3.class);
			startActivity(intent);
			finish();
		}
        });
		this.clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				db.clearScores();
				Toast toast = Toast.makeText(StatisticsActivity.this, "scores cleared", 3);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				Intent intent = new Intent(StatisticsActivity.this, Demo_3.class);
				startActivity(intent);
				finish();
			}
		});
	
		db = new DbAdapter(this);
		Cursor cur = db.getStats();
		boolean isData = cur.moveToFirst();
		
		//display information
		if(isData){
			quizzesTaken.setText("Total # of quizzes taken: " + db.getTotalNumberOfQuizzes());
			totalCorrect.setText("Total # of correct answers: " + db.getTotalNumberOfCorrect());
			totalIncorrect.setText("Total # of incorrect answers: " + db.getTotalNumberOfIncorrect());
			
			double avgTimePerQuestion = (db.getTotalTime() / db.getTotalNumberOfQuizzes());
			DecimalFormat f = new DecimalFormat();
			f.setMinimumFractionDigits(3);
			//System.out.println(f.format(avgTimePerQuestion));
			averageTimePerQuestion.setText("Avg time per question: " + f.format(avgTimePerQuestion) + " seconds");
		}
	}
}
