package com.example.aisuluu.colors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ScoresTableFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    Button buttonNewGame;
    TextView scoresTable;

    private OnFragmentInteractionListener mListener;

    public ScoresTableFragment() {
        // Required empty public constructor
    }

    public static ScoresTableFragment newInstance(String param1) {
        ScoresTableFragment fragment = new ScoresTableFragment();
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

        View view = inflater.inflate(R.layout.fragment_scores_table, container, false);

        scoresTable = (TextView) view.findViewById(R.id.scoresTable);

        // Set score record to scoreTable String
        scoresTable.setText(mParam1);

        buttonNewGame = (Button) view.findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.loadNewGame();
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
        void loadNewGame();
    }
}
