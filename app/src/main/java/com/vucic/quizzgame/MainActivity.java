package com.vucic.quizzgame;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vucic.quizzgame.models.Answer;
import com.vucic.quizzgame.models.Question;
import com.vucic.quizzgame.util.Tags;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int COUNT_DOWN_INTERVAL = 1000;
    public static final int TOTAL_TIME = 30000;
    public static final int EXTRA_TIME = 10000;
    private TextView timerTextView, questionTextView, pointsTextView;
    private Button answer1Button, answer2Button, answer3Button;
    private Button extraTimeButton, extraPointsButton, removeOneWrongQuestionButton;
    private ProgressBar timeProgressBar;
    private int questionIndex = 0;
    private int points = 0;
    private String playerName;
    private List<Question> allQuestions;
    private CountDownTimer countDownTimer;
    private int remainingTime;
    boolean increasePoints = false;
    private boolean removeOneWrongQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerTextView = findViewById(R.id.timerTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answer1Button = findViewById(R.id.answer1Button);
        answer2Button = findViewById(R.id.answer2Button);
        answer3Button = findViewById(R.id.answer3Button);
        extraTimeButton = findViewById(R.id.extraTimeButton);
        extraPointsButton = findViewById(R.id.extraPointsButton);
        removeOneWrongQuestionButton = findViewById(R.id.removeOneWrongAnswerButton);
        timeProgressBar = findViewById(R.id.timeProgressBar);
        pointsTextView = findViewById(R.id.pointsTextView);

        allQuestions = Question.getAllQuestions();
        displayQuestion(allQuestions.get(questionIndex));

        Intent intent = getIntent();
        playerName = intent.getStringExtra(Tags.PLAYER_NAME);

        startTimer(TOTAL_TIME);

        extraTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                int newTime = remainingTime + EXTRA_TIME;
                if (newTime >= TOTAL_TIME) {
                    newTime = TOTAL_TIME;
                }
                startTimer(newTime);
                extraTimeButton.setEnabled(false);
            }
        });

        extraPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increasePoints = true;
                extraPointsButton.setEnabled(false);
            }
        });

    }

    private void startTimer(int totalTime) {
        countDownTimer = new CountDownTimer(totalTime, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinish) {
                remainingTime = (int) millisUntilFinish;
                int progress = (int) (millisUntilFinish * 100.0 / TOTAL_TIME);
                timeProgressBar.setProgress(progress);
                timerTextView.setText(getString(R.string.time_with_placeholder, millisUntilFinish / 1000));
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        };
        countDownTimer.start();
    }

    private void displayQuestion(Question question) {
        questionTextView.setText(question.getText());
        answer1Button.setText(question.getAnswer1().getText());
        answer2Button.setText(question.getAnswer2().getText());
        answer3Button.setText(question.getAnswer3().getText());
        answer1Button.setEnabled(true);
        answer2Button.setEnabled(true);
        answer3Button.setEnabled(true);

        removeOneWrongQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOneWrongQuestion = true;
                if (removeOneWrongQuestion) {
                    if (!question.getAnswer1().isCorrect()) {
                        answer1Button.setEnabled(false);
                    } else if (!question.getAnswer2().isCorrect()) {
                        answer2Button.setEnabled(false);
                    } else if (!question.getAnswer3().isCorrect()) {
                        answer3Button.setEnabled(false);
                    }
                }
                removeOneWrongQuestion = false;
                removeOneWrongQuestionButton.setEnabled(false);
            }
        });

        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrect(question.getAnswer1(), question.getPoints());
            }
        });
        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrect(question.getAnswer2(), question.getPoints());
            }
        });
        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrect(question.getAnswer3(), question.getPoints());
            }
        });
    }

    private void checkAnswerCorrect(Answer answer, int points) {
        if (answer.isCorrect()) {
            if (increasePoints) {
                points *= 2;
                increasePoints = false;
            }
            this.points += points;
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
        updatePoints();

        questionIndex++;
        if (questionIndex == allQuestions.size()) {
            gameOver();
        } else {
            displayQuestion(allQuestions.get(questionIndex));
        }
    }

    private void updatePoints() {
        pointsTextView.setText(getString(R.string.points_with_placeholder, points));
    }

    private void gameOver() {
        Toast.makeText(this, "Game over", Toast.LENGTH_SHORT).show();
        goToEndGameScreen();
    }

    private void goToEndGameScreen() {
        countDownTimer.cancel();
        Intent intent = new Intent(this, EndGameActivity.class);
        intent.putExtra(Tags.POINTS, points);
        intent.putExtra(Tags.PLAYER_NAME, playerName);

        startActivity(intent);
        finish();
    }

    // Homework
    // 1. Add the name to the high score
    // 2. 3 top high scores
    // 3. implement some kind of power-ups (in-game helps)
    // Example: Extra time, double points for next question
    // 4. Add welcome screen
    // 5. Add achievements (High-score > 30, 50, 100)
}