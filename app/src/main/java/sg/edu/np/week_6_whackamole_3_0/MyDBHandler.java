package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME ="WhackAMole.db";
    public static String ACCOUNTS= "ACCOUNTS";
    public static String COLUMN_USERNAME = "UserName";
    public static String COLUMN_PASSWORD = "Password";
    public static String COLUMN_LEVEL =  "Level";
    public static String COLUMN_SCORE = "Score";




    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + ACCOUNTS +
                "(" + COLUMN_USERNAME + " TEXT," +COLUMN_PASSWORD + " TEXT," +
                COLUMN_LEVEL + " INTEGER," + COLUMN_SCORE + " INTEGER" + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        Log.d(TAG,FILENAME + "DB Created: " + CREATE_ACCOUNTS_TABLE );

        /* HINT:
            This is triggered on DB creation.
            Log.v(TAG, "DB Created: " + CREATE_ACCOUNTS_TABLE);
         */

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        onCreate(db);
        /* HINT:
            This is triggered if there is a new version found. ALL DATA are replaced and irreversible.
         */
    }

    public void addUser(UserData userData)
    {
        ContentValues values =new ContentValues();
        for(int i=0; i<userData.getLevels().size();++i)
        {
            values.put(COLUMN_USERNAME, userData.getMyUserName());
            values.put(COLUMN_PASSWORD, userData.getMyPassword());
            values.put(COLUMN_LEVEL, userData.getLevels().get(i));
            values.put(COLUMN_SCORE, userData.getScores().get(i));
            SQLiteDatabase db = this.getWritableDatabase();
            Log.v(TAG,FILENAME + ": Adding data for database:" +values.toString());
            db.insert(ACCOUNTS, null , values);
            db.close();
        }
            /* HINT:
                This adds the user to the database based on the information given.
                Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
             */
    }

    public UserData findUser(String username)
    {
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + "= \"" + username +"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Log.v(TAG, FILENAME +": Find user from database: " + query);
        UserData queryData = new UserData();
        ArrayList<Integer>levels = new ArrayList<>();
        ArrayList<Integer>scores = new ArrayList<>();


        if (cursor.moveToFirst())
        {
            queryData.setMyUserName(cursor.getString(0));
            queryData.setMyPassword(cursor.getString(1));
            do{

                levels.add(cursor.getInt(2));
                scores.add(cursor.getInt(3));

            } while(cursor.moveToNext());
            cursor.close();
            Log.v(TAG, FILENAME + ": QueryData " + queryData.getLevels().toString() + queryData.getScores().toString());
            queryData.setLevels(levels);
            queryData.setScores(scores);

        }
        else{
            queryData = null;
            Log.d(TAG, FILENAME + ": No data found");

        }
        db.close();
        return queryData;



        /* HINT:
            This finds the user that is specified and returns the data information if it is found.
            If not found, it will return a null.
            Log.v(TAG, FILENAME +": Find user form database: " + query);

            The following should be used in getting the query data.
            you may modify the code to suit your design.

            if(cursor.moveToFirst()){
                do{
                    ...
                    .....
                    ...
                }while(cursor.moveToNext());
                Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
            }
            else{
                Log.v(TAG, FILENAME+ ": No data found!");
            }
         */
    }

    public boolean deleteAccount(String username) {
        boolean result = false;

        String query ="SELECT * FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + "= \"" + username + "\"";
        Log.d(TAG, FILENAME + ": Database delete user: " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =db.rawQuery(query, null);
        UserData delData = new UserData();
        ArrayList<Integer>levels = new ArrayList<>();
        ArrayList<Integer>scores = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            do
            {   delData.setMyUserName(cursor.getString(0));
                db.delete(ACCOUNTS,COLUMN_USERNAME + "= ?",new String[]{String.valueOf(delData.getMyUserName())} );
            } while (cursor.moveToNext());
            cursor.close();
            result= true;
        }
        db.close();
        return result;




        /* HINT:
            This finds and delete the user data in the database.
            This is not reversible.
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
         */

    }

}
