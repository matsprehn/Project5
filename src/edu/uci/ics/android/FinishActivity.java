package edu.uci.ics.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FinishActivity extends Activity {

	private TextView correct;
	private TextView incorrect;
	private TextView totalQuestions;
	private TextView timePerQuestion;
	private TextView totalTime;
	private Button homeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish);
		
		this.correct = (TextView)this.findViewById(R.id.correctText);
		this.incorrect = (TextView)this.findViewById(R.id.incorrectText);
		this.totalQuestions = (TextView)this.findViewById(R.id.totalQuestions);
		this.timePerQuestion = (TextView)this.findViewById(R.id.timePerQuestion);
		this.totalTime = (TextView)this.findViewById(R.id.totalTime);
		this.homeButton = (Button)this.findViewById(R.id.backButton);
		this.homeButton.setOnClickListener(new OnClickListener() {		
        	public void onClick(View v) {
			// Change the button image
			//button1.setBackgroundResource(R.drawable.btn_default_normal_green);	
			Intent intent = new Intent(FinishActivity.this, Demo_3.class);
			startActivity(intent);
			finish();
		}
        });
		
		this.correct.setText("Number correct: " + getIntent().getExtras().getInt("numCorrect"));
		this.incorrect.setText("Number incorrect: " + getIntent().getExtras().getInt("totalQuestions"));
		this.totalQuestions.setText("Total questions: " + getIntent().getExtras().getInt("totalQuestions"));
		
		Long totalSeconds = (long) 0;
		Long totalDecimalSeconds = (long) 0;
		if(getIntent().getExtras().getInt("totalQuestions") != 0){
			Long totalMilliseconds = getIntent().getExtras().getLong("totalTime");
			Long averageMilliseconds = totalMilliseconds / getIntent().getExtras().getInt("totalQuestions");
			totalSeconds = averageMilliseconds / 1000;
			totalDecimalSeconds = averageMilliseconds % 1000;
		}
		
		this.timePerQuestion.setText("Time per question: " + totalSeconds + "." + totalDecimalSeconds + " seconds");
		this.totalTime.setText("Total time: " + (getIntent().getExtras().getLong("totalTime") / 1000) + " seconds");
	}
}
