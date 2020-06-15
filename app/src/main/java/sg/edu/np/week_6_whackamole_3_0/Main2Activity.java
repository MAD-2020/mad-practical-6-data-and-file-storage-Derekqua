package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */



    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
    private Button cancelButton;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = findViewById(R.id.RegisterUsername);
                EditText etPassword = findViewById(R.id.RegisterPassword);

                ArrayList<Integer>levels = new ArrayList<>();
                ArrayList<Integer>scores = new ArrayList<>();
                if (dbHandler.findUser(etUsername.getText().toString()) ==null)
                {
                    UserData userData = new UserData();
                    userData.setMyUserName(etUsername.getText().toString());
                    userData.setMyPassword(etPassword.getText().toString());
                    for (int i =1; i<11; i++)
                    {

                        levels.add(i);
                        scores.add(0);

                    }
                    userData.setLevels(levels);
                    userData.setScores(scores);
                    dbHandler.addUser(userData);
                    Log.v(TAG, FILENAME + ": New user created successfully!");
                    Toast.makeText(Main2Activity.this,"User Account created successfully!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(Main2Activity.this,"User already exits during new user creation!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
