package com.example.aisuluu.colors;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    TextView scoreText;


    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        Bundle bundle = getArguments();
        String myValue = bundle.getString("message");
        int score = bundle.getInt("score");



        scoreText = (TextView) view.findViewById(R.id.finalScoreText);
        scoreText.setText(String.valueOf(score));

        // Inflate the layout for this fragment
        return view;
    }

}
