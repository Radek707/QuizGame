package com.vucic.quizzgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.vucic.quizzgame.util.Tags;

public class EndGameActivity extends AppCompatActivity {

    private TextView previousHighScoreTextView;
    private TextView newHighScoreTextView;
    private TextView highScore1TextView;
    private TextView highScore2TextView;
    private TextView highScore3TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Intent intent = getIntent();
        String playerName = intent.getStringExtra(Tags.PLAYER_NAME);
        int points = intent.getIntExtra(Tags.POINTS, 0);
        TextView pointsTextView = findViewById(R.id.pointsTextView);
        previousHighScoreTextView = findViewById(R.id.previousHighScoreTextView);
        newHighScoreTextView = findViewById(R.id.newHighScoreTextView);
        TextView playerNameTextView = findViewById(R.id.playerNameTextView);
        playerNameTextView.setText(playerName);
        pointsTextView.setText(getString(R.string.points_with_placeholder, points));

        highScore1TextView = findViewById(R.id.highScore1TextView);
        highScore1TextView.setText(intent.getStringExtra(Tags.HIGH_SCORE_1));
        highScore2TextView = findViewById(R.id.highScore2TextView);
        highScore2TextView.setText(intent.getStringExtra(Tags.HIGH_SCORE_2));
        highScore3TextView = findViewById(R.id.highScore3TextView);
        highScore3TextView.setText(intent.getStringExtra(Tags.HIGH_SCORE_3));

        saveHighScore(playerName, points);
    }

    private void saveHighScore(String playerName, int points) {
        // int string
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        int highScore = sharedPreferences.getInt(Tags.HIGH_SCORE, 0);

        String highScoreRecord1 = sharedPreferences.getString(Tags.HIGH_SCORE_1, "");
        highScore1TextView.setText(highScoreRecord1);

        String highScoreRecord2 = sharedPreferences.getString(Tags.HIGH_SCORE_2,"");
        highScore2TextView.setText(highScoreRecord2);

        String highScoreRecord3 = sharedPreferences.getString(Tags.HIGH_SCORE_3,"");
        highScore3TextView.setText(highScoreRecord3);

        String playerNameHighScore = sharedPreferences.getString(Tags.PLAYER_NAME, "");
        previousHighScoreTextView.setText(getString(R.string.previous_high_score_with_placeholder, playerNameHighScore, highScore));

        if (points > highScore) {
            newHighScoreTextView.setVisibility(View.VISIBLE);

            String highScoreRecord = playerName + " " + points + " points";
            highScoreRecord3 = highScoreRecord2;
            highScoreRecord2 = highScoreRecord1;
            highScoreRecord1 = highScoreRecord;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Tags.HIGH_SCORE, points);
            editor.putString(Tags.PLAYER_NAME, playerName);
            editor.putString(Tags.HIGH_SCORE_3, highScoreRecord3);
            editor.putString(Tags.HIGH_SCORE_2, highScoreRecord2);
            editor.putString(Tags.HIGH_SCORE_1, highScoreRecord1);
            editor.apply();
        }
    }
}