package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
    private Button loginButton;
    private TextView newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newUser = (TextView) findViewById(R.id.redirectSignUp);
        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent redirectToSignUp = new Intent(MainActivity.this,Main2Activity.class);
                redirectToSignUp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Log.v(TAG, FILENAME + ": Create new user!");
                startActivity(redirectToSignUp);
                return false;
            }
        });
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = findViewById(R.id.RegisterUsername);
                EditText etPassword  = findViewById(R.id.RegisterPassword);
                Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
                if (isValidUser(etUsername.getText().toString(),etPassword.getText().toString()))
                {
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                    intent.putExtra("Username", etUsername.getText().toString());
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"Valid",Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.v(TAG, FILENAME + ": Invalid user!");
                    Toast.makeText(MainActivity.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                }

            }
        });





        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */


    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password)
    {
        UserData dbData = dbHandler.findUser(userName);
        if (dbData == null)
        {
            return false;
        }
        Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
        return dbData.getMyUserName().equals(userName) && dbData.getMyPassword().equals(password);



        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */

    }

}
