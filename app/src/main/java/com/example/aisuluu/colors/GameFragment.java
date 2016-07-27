package com.example.aisuluu.colors;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    GameFragmentListener gameFragmentListener;

    Button textButton;
    Button colorButton;
    ImageView answerStatusImage;
    ImageView meaningImage;
    ImageView textcolorImage;
    TextView scoresText;
    LinearLayout lifeLinearLayout;
    Button negativeButton;
    Button positiveButton;
    Handler mHandler;
    int mLifeCounter = 4;
    Random rand;
    TextView timerText;
    final int gameDuration = 30000; // in millis
    final int tickPeriod = 1000; // in millis
    int answerStatusImageDuration = 1000; // in milliseconds
    ScaleAnimation scaleAnimation;
    LinearLayout textColorLinearLayout;
    float[] xOfLifeStatus = new float[4];

    Animation answerStatusImageAnimation;
    Animation slideInMeaningAnimation;
    Animation slideInTextColorAnimation;
    Animation slideInTextAnimation;
    Animation slideInColorAnimation;
    Animation counterAnimation;

    String mTextArray[] = {"black", "blue", "yellow", "red"};
    String mTextToCheck = "";
    String mColorToCheck = "";
    Boolean mAnswerToCheck = false;
    int mScores = 0;

    AnimationSet animationSet = new AnimationSet(false);
    AnimationSet answerStatusImageScale = new AnimationSet(false);
    AnimationSet answerStatusImageTranslate = new AnimationSet(false);

    CountDownTimer timer;
    int counter = 3;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        textButton = null;
        colorButton = null;
        answerStatusImage = null;
        scoresText = null;
        negativeButton = null;
        positiveButton = null;
        mHandler = null;
        rand = null;
        timerText = null;
        timer.cancel();

        super.onDestroyView();
    }

    public interface GameFragmentListener {
        void getScore(int score);
    }

    private void sendMessage(int data){
        if (isAdded() && gameFragmentListener != null){
            gameFragmentListener.getScore(data);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            gameFragmentListener = (GameFragmentListener) context;
        }catch (ClassCastException ex){
            throw  new ClassCastException(context.toString() + " must implement GameFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gameFragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        lifeLinearLayout = (LinearLayout) view.findViewById(R.id.lifeLinearLayout);
        timerText = (TextView) view.findViewById(R.id.timerText);
        textColorLinearLayout = (LinearLayout) view.findViewById(R.id.textColorLinearLayout);
        meaningImage = (ImageView) view.findViewById(R.id.meaningImage);
        textcolorImage = (ImageView) view.findViewById(R.id.textcolorImage);

        gameFragmentListener = (GameFragmentListener) getActivity();
        textButton = (Button) view.findViewById(R.id.textButton);
        colorButton = (Button) view.findViewById(R.id.colorButton);
        answerStatusImage = (ImageView) view.findViewById(R.id.answerStatusImage);
        answerStatusImage.setVisibility(View.INVISIBLE);
        scoresText = (TextView) view.findViewById(R.id.scoresText);
        negativeButton = (Button) view.findViewById(R.id.negativeButton);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswerToCheck = false;
                checkAnswer();
            }
        });
        positiveButton = (Button) view.findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(onYesClickListener);

        answerStatusImageAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.obj_tran);
        slideInTextAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slidein_text_animation);
        slideInColorAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slidein_color_animation);
        counterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.counter_animation);

        slideInMeaningAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_meaning_animation);
        slideInTextColorAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_textcolor_animation);

        textButton.startAnimation(slideInTextAnimation);
        colorButton.startAnimation(slideInColorAnimation);

        rand = new Random();
        mHandler = new Handler(Looper.getMainLooper());

        //answerStatusImage.setVisibility(View.VISIBLE);

        answerStatusImage.setImageResource(R.drawable.counter_3);

        timer = new CountDownTimer(gameDuration, tickPeriod) {
            public void onTick(long millisUntilFinished) {
                if(mLifeCounter > 0 && timerText != null)
                    timerText.setText("Time:  " + String.valueOf(millisUntilFinished/ tickPeriod));
                else {
                    timer.cancel();
                    onFinish();
                }
            }

            //after 30 secs, replace game fragment with score fragment
            public void onFinish() {
                sendMessage(mScores);
                Log.d("MYLOG", "FINISHED");
            }
        };
        counter = 3;
        // 3, 2, 1 counter
        scaleView();
        final Resources resources = getResources();
        final String packageName = getContext().getPackageName();

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (counter > 0) {
                    String resOfImage = "drawable/counter_" + counter;
                    Log.d("MYLOG", resOfImage);
                    answerStatusImage.setImageResource(resources.getIdentifier(resOfImage, null, packageName));
                    counter--;
                    animation.start();
                }
                else
                    animation.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                }
        });

        answerStatusImage.startAnimation(animationSet);
        meaningImage.startAnimation(slideInMeaningAnimation);
        textcolorImage.startAnimation(slideInTextColorAnimation);


        positiveButton.setEnabled(false);
        negativeButton.setEnabled(false);
        // after 3, 2, 1
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //starting game timer, 30 secs
                timer.start();
                if(positiveButton != null && negativeButton != null){
                    positiveButton.setEnabled(true);
                    negativeButton.setEnabled(true);
                }


                generatePairs();
            }
        }, 5000);

        //get x coordinates only after the layout is created
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        for(int i = 0; i < xOfLifeStatus.length; i++){
                            View v = lifeLinearLayout.getChildAt(i);
                            xOfLifeStatus[i] = v.getRight();
                        }
                        // Don't forget to remove your listener when you are done with it.
                        ViewTreeObserver observer = view.getViewTreeObserver();
                        if (observer != null) observer.removeOnGlobalLayoutListener(this);
                    }
                });

        return view;
    }

    //generating random text and colors
    public void generatePairs(){
        if (rand == null) rand = new Random();
        int textArrayRandomPosition = rand.nextInt(mTextArray.length);

        mTextToCheck = mTextArray[textArrayRandomPosition];

        if(textButton != null) {
            textButton.setText(mTextToCheck);

            textArrayRandomPosition = rand.nextInt(mTextArray.length);
            colorButton.setText(mTextArray[textArrayRandomPosition]);

            textArrayRandomPosition = rand.nextInt(mTextArray.length);
            textButton.setTextColor(Color.parseColor(mTextArray[0]));

            colorButton.setTextColor(Color.parseColor(mTextArray[textArrayRandomPosition]));

            mColorToCheck = mTextArray[textArrayRandomPosition];
        }
    }

    private View.OnClickListener onYesClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            mAnswerToCheck = true;
            checkAnswer();
        }
    };

    //check if generated pairs and answer are correct
    public void checkAnswer(){

        Log.d("MYLOG", "Text: " + mTextToCheck + " Color: " + mColorToCheck + " Answer: " + mAnswerToCheck);


        if((mTextToCheck.equals(mColorToCheck)) == mAnswerToCheck){
            mScores++;
            answerStatusImage.setImageResource(R.drawable.tick);
            scoresText.setText("Score   " + String.valueOf(mScores));

            if(mLifeCounter > 0) answerStatusImageTickAnim();

            answerStatusImage.startAnimation(answerStatusImageTranslate);
            generatePairs();
        }
        else {
            //if the answer is wrong
            answerStatusImage.setImageResource(R.drawable.cross);
            mLifeCounter--;
            if(mLifeCounter > 0)answerStatusImageAnim();

            answerStatusImage.startAnimation(answerStatusImageTranslate);


            if (mLifeCounter > 0) {

                if (mAnswerToCheck) {
                    negativeButton.setTextColor(Color.BLACK);
                } else
                    positiveButton.setTextColor(Color.BLACK);

                negativeButton.setEnabled(false);
                positiveButton.setEnabled(false);

                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        negativeButton.setEnabled(true);
                        positiveButton.setEnabled(true);
                        negativeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorYesNoButton));
                        positiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorYesNoButton));

                        if (mLifeCounter == 0) {
                            Toast.makeText(getActivity(), "GAMEOVER", Toast.LENGTH_SHORT).show();
                        } else
                            generatePairs();
                    }
                }, answerStatusImageDuration);

            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View v = lifeLinearLayout.getChildAt(mLifeCounter);
                    ImageView lifeImageView = (ImageView) v;
                    lifeImageView.setImageResource(R.drawable.deadlifecircle);
                }
            }, 700);

        }

        //answerStatusImage.startAnimation(answerStatusImageAnimation);

    }


    public void scaleView() {

        if (animationSet == null) animationSet = new AnimationSet(true);

        Animation scaleZoomIn = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleZoomIn.setDuration(450);
        scaleZoomIn.setRepeatMode(Animation.REVERSE);
        scaleZoomIn.setRepeatCount(1);


        animationSet.addAnimation(scaleZoomIn);

        animationSet.setFillAfter(true);
        animationSet.setFillBefore(true);

    }
    public void answerStatusImageAnim(){

        if (answerStatusImageScale == null) answerStatusImageScale = new AnimationSet(true);
        if (answerStatusImageTranslate == null) answerStatusImageTranslate = new AnimationSet(true);

        Animation scaleZoomIn = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleZoomIn.setDuration(100);
        Animation scaleZoomOut = new ScaleAnimation(1f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleZoomOut.setDuration(80);
        scaleZoomOut.setStartOffset(50);

        answerStatusImageTranslate.addAnimation(scaleZoomIn);
        answerStatusImageTranslate.addAnimation(scaleZoomOut);

        Animation scaleFadeOut = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleFadeOut.setDuration(550);
        scaleFadeOut.setStartOffset(200);
        scaleFadeOut.setFillAfter(false);

        Animation translateFadeOut = new TranslateAnimation(0,xOfLifeStatus[mLifeCounter-1] + mLifeCounter*63, 0, -1000);
        translateFadeOut.setDuration(550);
        translateFadeOut.setStartOffset(200);
        translateFadeOut.setFillAfter(true);


        answerStatusImageTranslate.addAnimation(scaleFadeOut);
        answerStatusImageTranslate.addAnimation(translateFadeOut);

        answerStatusImageTranslate.setFillAfter(false);
        answerStatusImageTranslate.setFillBefore(true);

       // answerStatusImageTranslate.setStartOffset(100);

    }
    public void answerStatusImageTickAnim(){

        if (answerStatusImageScale == null) answerStatusImageScale = new AnimationSet(true);
        if (answerStatusImageTranslate == null) answerStatusImageTranslate = new AnimationSet(true);

        Animation scaleZoomIn = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleZoomIn.setDuration(100);
        Animation scaleZoomOut = new ScaleAnimation(1f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleZoomOut.setDuration(80);
        scaleZoomOut.setStartOffset(50);
//        scaleZoomOut.setFillAfter(false);
//        scaleZoomIn.setFillBefore(false);
//        scaleZoomOut.setFillBefore(false);
//        scaleZoomIn.setFillAfter(false);

        answerStatusImageTranslate.addAnimation(scaleZoomIn);
        answerStatusImageTranslate.addAnimation(scaleZoomOut);

        Animation scaleFadeOut = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleFadeOut.setDuration(550);
        scaleFadeOut.setStartOffset(200);
        scaleFadeOut.setFillAfter(false);

        Animation translateFadeOut = new TranslateAnimation(0,0, 0, -1000);
        translateFadeOut.setDuration(550);
        translateFadeOut.setStartOffset(200);
        translateFadeOut.setFillAfter(true);


        answerStatusImageTranslate.addAnimation(scaleFadeOut);
        answerStatusImageTranslate.addAnimation(translateFadeOut);

        answerStatusImageTranslate.setFillAfter(false);
        answerStatusImageTranslate.setFillBefore(true);

        // answerStatusImageTranslate.setStartOffset(100);






    }

}
