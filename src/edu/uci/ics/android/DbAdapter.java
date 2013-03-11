package edu.uci.ics.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Random;

public class DbAdapter extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "mydb";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "fruits";
	private static final String FRUIT_ID = "_id";
	private static final String FRUIT_NAME = "name";
	private static final String FILE_NAME = "fruits.txt";
	private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "("+FRUIT_ID+" integer primary key autoincrement, "+FRUIT_NAME+" text not null);";
	private SQLiteDatabase mDb;
	private Context mContext;
	private Random rand = new Random();
	
	public DbAdapter(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = ctx;
		this.mDb = getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String movieTable = "CREATE TABLE movies (ID integer primary key autoincrement, TITLE text not null, YEAR text not null, DIRECTOR text not null);";
		String starTable = "CREATE TABLE stars (ID integer primary key autoincrement, FIRST_NAME text not null, LAST_NAME text not null, DOB numeric not null, PHOTO_URL text);";
		String starsInMoviesTable = "CREATE TABLE stars_in_movies (STAR_ID integer not null, MOVIE_ID integer not null);";
		//create a statistics table. quiz_number is number of quizzes taken. CORRECT is a 0 if incorrect, 1 if correct answer. TIME_SPENT is time spent on the question";
		String statsTable = "CREATE TABLE stats (CORRECT integer, INCORRECT integer, TOTAL_TIME real);";
		db.execSQL(movieTable);
		db.execSQL(starTable);
		db.execSQL(starsInMoviesTable);
		db.execSQL(statsTable);
		// populate database
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(mContext.getAssets().open("movies.csv")));
			String line;
			System.out.println("getting movies");
			while((line=in.readLine())!=null) {
				System.out.println("line");
				String arr[] = line.split("\\s*,\\s*");
				ContentValues values = new ContentValues();
				System.out.println(arr[0]);
				values.put("ID", arr[0]);
				values.put("TITLE", arr[1]);
				values.put("YEAR", arr[2]);
				values.put("DIRECTOR", arr[3]);
				db.insert("movies", null, values); 
			}
			in = new BufferedReader(new InputStreamReader(mContext.getAssets().open("stars.csv")));
			System.out.println("getting stars");
			while((line=in.readLine())!=null) {
				System.out.println("line2");
				String arr[] = line.split("\\s*,\\s*");
				ContentValues values = new ContentValues();
				System.out.println(arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3]);
				values.put("ID", arr[0]);
				values.put("FIRST_NAME", arr[1]);
				values.put("LAST_NAME", arr[2]);
				values.put("DOB", arr[3]);
				db.insert("stars", null, values); 
			}
			in = new BufferedReader(new InputStreamReader(mContext.getAssets().open("stars_in_movies.csv")));
			System.out.println("getting stars_in_movies");
			while((line=in.readLine())!=null) {
				System.out.println("line2");
				String arr[] = line.split("\\s*,\\s*");
				ContentValues values = new ContentValues();
				System.out.println(arr[0] + " " + arr[1]);
				values.put("MOVIE_ID", arr[1]);
				values.put("STAR_ID", arr[0]);
				db.insert("stars_in_movies", null, values); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS movies");
		db.execSQL("DROP TABLE IF EXISTS stars");
		db.execSQL("DROP TABLE IF EXISTS stars_in_movies");
		db.execSQL("DROP TABLE IF EXISTS stats");
		onCreate(db);
	}
	
	public Cursor fetchAll() {
		//return mDb.query("movies", new String[] {"title"}, null, null, null, null, null);
		return mDb.query("movies", new String[] {"title,director"}, null, null, null, null, null);
	}
	
	public Cursor getActors(String movie)
	{
		Cursor cur;
		cur =  mDb.rawQuery("select stars.first_name, stars.last_name " +
						"from movies join stars_in_movies on movies.id = stars_in_movies.movie_id " +
						"join stars on stars_in_movies.star_id = stars.id " +
						"WHERE movies.title ='" + movie + "'", null);
		return cur;
	}
	
	public Cursor moviesWithActors(String actor1, String actor2)
	
	{	Cursor cur;
		cur =  mDb.rawQuery("select count(movies.title), movies.title, stars.first_name, stars.last_name " +
			"from movies join stars_in_movies on movies.id = stars_in_movies.movie_id " +
			"join stars on stars_in_movies.star_id = stars.id " +
			"group by movies.title", null);
		return cur;
	}
	
	public Cursor pickQuestion(int pick)
	{
		Cursor cur;
		int i; //used to see number of results
		int index;//used to pick one result (get right answer) and 3 others
		cur = mDb.query("movies", new String[] {"*"}, null, null, null, null, null);
		switch(pick)
		{
			case 0:
				cur = mDb.query("movies", new String[] {"title,director"}, null, null, null, null, null);
				break;
			case 1:
				cur = mDb.query("movies", new String[] {"title,year"}, null, null, null, null, null);
				break;
			case 2:
				cur = mDb.rawQuery("select count(movies.title), movies.title, stars.first_name, stars.last_name " +
						"from movies join stars_in_movies on movies.id = stars_in_movies.movie_id " +
						"join stars on stars_in_movies.star_id = stars.id " +
						"group by movies.title", null);
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
				
		}
		return cur;
	}

	public void insertNewStats(int newCorrect, int newIncorrect, double newTimePerQuestion){
		ContentValues values = new ContentValues();
		values.put("CORRECT", newCorrect);
		values.put("INCORRECT", newIncorrect);
		values.put("TOTAL_TIME", newTimePerQuestion);
		mDb.insert("stats", null, values); 
	}
	
	public Cursor getStats(){
		Cursor cur;
		cur = mDb.rawQuery("select * from stats", null);
		//System.err.println(cur.moveToFirst());
		//cur.moveToFirst();
//		while (!cur.isAfterLast())
//		{
//			//System.out.println(cur.getString(0));
//			cur.moveToNext();
//		}
		return cur;
	}
	
	public int getTotalNumberOfQuizzes(){
		Cursor cur;
		cur = mDb.rawQuery("select count(*) from stats", null);
		cur.moveToFirst();
		int value = cur.getInt(0);
		//System.out.println(value);
		return value;
	}
	
	public void clearScores(){
		mDb.delete("stats", null, null);
	}
	
	public int getTotalNumberOfCorrect(){
		Cursor cur;
		cur = mDb.rawQuery("select stats.correct from stats", null);
		cur.moveToFirst();
		int value = 0;
		while(!cur.isAfterLast()){
			//System.out.println("correct" + cur.getInt(0));
			value += cur.getInt(0);
			cur.moveToNext();
		}
		return value;
	}
	
	public int getTotalNumberOfIncorrect(){
		Cursor cur;
		cur = mDb.rawQuery("select stats.incorrect from stats", null);
		cur.moveToFirst();
		int value = 0;
		while(!cur.isAfterLast()){
			//System.out.println("incorrect" + cur.getInt(0));
			value += cur.getInt(0);
			cur.moveToNext();
		}
		return value;
	}
	
	public double getTotalTime(){
		Cursor cur;
		cur = mDb.rawQuery("select stats.total_time from stats", null);
		cur.moveToFirst();
		double value = 0;
		while(!cur.isAfterLast()){
			value += cur.getDouble(0);
			cur.moveToNext();
		}
		//System.out.println(value);
		return value;
	}
}
