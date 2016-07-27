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

public class MainActivity extends AppCompatActivity implements GameFragment.GameFragmentListener, BlankFragment.OnFragmentInteractionListener, ScoresTableFragment.OnFragmentInteractionListener{

    int mScore = 0;
    protected FragmentManager fm;
    protected FragmentTransaction ft;
    String scoresRecord;
    GameFragment gameFragment = new GameFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        fm = getSupportFragmentManager();

        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, gameFragment);
        ft.commit();

    }


    @Override
    public void getScore(int score) {
        mScore = score;
        Bundle bundle = new Bundle();
        bundle.putString("message", "Message to check" );
        bundle.putInt("score", score);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, BlankFragment.newInstance(String.valueOf(score), "0"));
        ft.commit();
    }

    @Override
    public void loadScore(String score, String userName){

        SharedPreferences preferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        scoresRecord = preferences.getString("SCORES", "");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SCORES", scoresRecord + "\n" + score + " " + userName + "\n");
        editor.apply();

        String scoresTable = preferences.getString("SCORES", "NO SCORES");

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, ScoresTableFragment.newInstance(scoresTable, ""));
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void loadNewGame(){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new GameFragment());
        ft.commit();
    }
}