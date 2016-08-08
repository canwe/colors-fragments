package com.example.aisuluu.colors;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements GameFragment.GameFragmentListener, ScoreFragment.OnFragmentInteractionListener, ScoresTableFragment.OnFragmentInteractionListener {

    protected FragmentManager fm;
    protected FragmentTransaction ft;
    String scoresRecord;
    GameFragment gameFragment = new GameFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting app to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        // Load Game Fragment
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, gameFragment);
        ft.commit();
    }

    // Receive score from Game Fragment and pass it to Score Fragment
    @Override
    public void getScore(int score) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, ScoreFragment.newInstance(String.valueOf(score)));
        ft.commit();
    }

    // Get score and username from Score Fragment, add record to SharedPreferences, pass String with all scores to ScoreTable Fragment
    @Override
    public void loadScore(String score, String userName){

        SharedPreferences preferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        scoresRecord = preferences.getString("SCORES", "");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SCORES", scoresRecord + "\n" + score + " " + userName + "\n");
        editor.apply();

        String scoresTable = preferences.getString("SCORES", "");

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, ScoresTableFragment.newInstance(scoresTable));
        ft.commit();
    }

    // After the scores are saved, load new game
    @Override
    public void loadNewGame(){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new GameFragment());
        ft.commit();
    }
}