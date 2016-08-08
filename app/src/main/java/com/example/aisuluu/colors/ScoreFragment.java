package com.example.aisuluu.colors;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScoreFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    Button buttonSaveScore;
    TextView blankScoreText;
    EditText textUserName;

    private OnFragmentInteractionListener mListener;

    public ScoreFragment() {
        // Required empty public constructor
    }

    public static ScoreFragment newInstance(String param1) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_score, container, false);
        textUserName = (EditText) view.findViewById(R.id.textUserName);

        blankScoreText = (TextView) view.findViewById(R.id.blankScoreText);
        blankScoreText.setText("Your score is: " + mParam1);

        buttonSaveScore = (Button) view.findViewById(R.id.buttonSaveScore);
        // When Save Score button is clicked, return to MainActivity with score and username input from edit text field
        buttonSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.loadScore(mParam1, textUserName.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void loadScore(String score, String name);
    }
}
