package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    CountDownTimer readyTimer;
    CountDownTimer placeMoleTimer;
    private Integer score, level;
    private TextView scoreText;
    private MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);;
    private String userName;
    private Button backButton;

    private void readyTimer(){
        readyTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                final Toast makeToast =  Toast.makeText(Main4Activity.this, "Get Ready in " + l/1000 + " seconds!", Toast.LENGTH_SHORT);
                makeToast.show();
                Handler time = new Handler();
                time.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeToast.cancel();
                    }
                }, 1000);
                Log.v(TAG, "Ready Countdown!" + l / 1000);


            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready Countdown Complete!");
                Toast.makeText(Main4Activity.this,"GO!",Toast.LENGTH_SHORT).show();
                placeMoleTimer(setLevel(level));


            }
        };
        readyTimer.start();
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
    }
    private void placeMoleTimer(final Integer i ){
        placeMoleTimer = new CountDownTimer(i,1000) {
            @Override
            public void onTick(long l) {
                setNewMole(level);
                Log.v(TAG,"New Mole Location!");
            }
            @Override
            public void onFinish() {
                placeMoleTimer(setLevel(level));
            }

        };
        placeMoleTimer.start();

        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
    }
    private static final int[] BUTTON_IDS = {
            R.id.topLeftBtn,
            R.id.topMidBtn,
            R.id.topRightBtn,
            R.id.midLeftBtn,
            R.id.midBtn,
            R.id.midRightBtn,
            R.id.bottomLeftBtn,
            R.id.bottomMidBtn,
            R.id.bottomRightBtn
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        scoreText = (TextView)findViewById(R.id.scoreTextView);
        score = 0;
        scoreText.setText(String.valueOf(score));
        Intent receivingEnd = getIntent();
        level = receivingEnd.getIntExtra("Level",0);
        userName = receivingEnd.getStringExtra("Username");
        backButton = (Button) findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intent = new Intent(Main4Activity.this,Main3Activity.class);
                intent.putExtra("Username",userName);
                startActivity(intent);
            }
        });




        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */


        for(final int id : BUTTON_IDS){
            final Button button = (Button) findViewById(id);
            button.setText("O");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doCheck(button);

                }
            });

            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
    }
    private void doCheck(Button checkButton)
    {
        if (checkButton.getText() == "*")
        {
            Log.v(TAG,"Hit, score added!");
            score+=1;
        }
        else{

            Log.v(TAG, "Missed, point deducted!");
            score-=1;
        }
        scoreText.setText(String.valueOf(score));

        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */

    }

    public void setNewMole(Integer i)
    {
        for (int id :BUTTON_IDS){
            Button b=findViewById(id);
            b.setText("O");
        }
        if (i<=5)
        {
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            Button selectedButton = findViewById(BUTTON_IDS[randomLocation]);
            selectedButton.setText("*");
        }
        else{
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            Button selectedButton = findViewById(BUTTON_IDS[randomLocation]);
            selectedButton.setText("*");
            int randomLocation2 = ran.nextInt(9);
            Button selectedButton2 = findViewById(BUTTON_IDS[randomLocation2]);
            selectedButton2.setText("*");
        }

    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        readyTimer.cancel();
        if (placeMoleTimer!= null)
        {
            placeMoleTimer.cancel();
        }
        UserData userData = dbHandler.findUser(userName);
        if(userData.getScores().get(level-1)<score)
        {
            userData.getScores().set(level-1,score);
            dbHandler.deleteAccount(userName);
            dbHandler.addUser(userData);

        }



    }
    private Integer setLevel(Integer level)
    {
        if (level==1)
        {
            return 10000;
        }
        else if (level==2)
        {
            return 9000;
        }
        else if (level==3)
        {
            return 8000;
        }
        else if (level==4)
        {
            return 7000;
        }
        else if (level==5)
        {
            return 6000;
        }
        else if (level==6)
        {
            return 5000;
        }
        else if (level==7)
        {
            return 4000;
        } else if (level==8)
        {
            return 3000;
        } else if (level==9)
        {
            return 2000;
        }
        else{
            return 1000;
        }

    }

}
