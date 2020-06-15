package sg.edu.np.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private UserData userData;
    private ArrayList<Integer>score_list;
    private ArrayList<Integer>level_list;
    private Context mCon;


    public CustomScoreAdaptor(Context con,UserData userdata){
        userData = userdata;
        level_list = userdata.getLevels();
        score_list = userdata.getScores();
        mCon = con;


        /* Hint:
        This method takes in the data and readies it for processing.
         */
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select,parent,false);
        return new CustomScoreViewHolder(item);

        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){
        Integer level = level_list.get(position);
        Integer score = score_list.get(position);
        holder.levels.setText("Level "+level);
        holder.scores.setText("Highest Score: " + score);
        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCon,Main4Activity.class);
                intent.putExtra("Level",level_list.get(position));
                intent.putExtra("Username",userData.getMyUserName());
                mCon.startActivity(intent);

            }
        });




        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
    }

    public int getItemCount(){
        return level_list.size();
        /* Hint:
        This method returns the the size of the overall data.
         */
    }
}