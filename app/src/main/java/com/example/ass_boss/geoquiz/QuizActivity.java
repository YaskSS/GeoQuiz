package com.example.ass_boss.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity ";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "correct_answer";
    private static final String KEY_CHEATER = "correct_answer";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private TextView questionTextView;

    private Question[] questionsBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int currentIndex = 0;
    private int countAnswerCorrect = 0;
    private boolean isCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            countAnswerCorrect = savedInstanceState.getInt(KEY_ANSWER, 0);
            isCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
        }

        questionTextView = (TextView) findViewById(R.id.question_text_view);
        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questionsBank.length;

                updateQuestion();
            }
        });

        nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trueButton.isClickable() && !falseButton.isClickable()) {
                currentIndex = (currentIndex + 1) % questionsBank.length;


                disableAnswerButtons(true);
                isCheater = false;


                    updateQuestion();
                }

                if (currentIndex == questionsBank.length - 1) {
                    Toast toast = Toast.makeText(QuizActivity.this,
                            "res = " + countAnswerCorrect + " of " + questionsBank.length,
                            Toast.LENGTH_LONG);

                    toast.show();
                }
            }
        });

        previousButton = (ImageButton) findViewById(R.id.prev_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trueButton.isClickable() && !falseButton.isClickable()) {
                    currentIndex = currentIndex == 0 ? questionsBank.length - 1 : currentIndex - 1;

                    updateQuestion();
                }
            }
        });

        trueButton = (Button) findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAnswerButtons(false);
                checkAnswer(true);
            }
        });
        falseButton = (Button) findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disableAnswerButtons(false);
                checkAnswer(false);
            }
        });

        cheatButton = (Button) findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = questionsBank[currentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState called");
        outState.putInt(KEY_INDEX, currentIndex);
        outState.putInt(KEY_ANSWER, countAnswerCorrect);
        outState.putBoolean(KEY_CHEATER, isCheater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    private void updateQuestion() {
        int question = questionsBank[currentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answerIsTrue = questionsBank[currentIndex].isAnswerTrue();

        int messageResId = 0;

        if (isCheater) {
            messageResId = R.string.judment_toast;
        } else if (userAnswer == answerIsTrue) {
            messageResId = R.string.correct_toast;
            countAnswerCorrect++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast toast = Toast.makeText(QuizActivity.this,
                messageResId,
                Toast.LENGTH_SHORT);

        toast.show();
    }

    private void disableAnswerButtons(boolean isClickable) {
        trueButton.setClickable(isClickable);
        falseButton.setClickable(isClickable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_CHEAT:
                if (data == null) {
                    return;
                }
                isCheater = CheatActivity.wasAnswerShown(data);
                break;
        }
    }
}
