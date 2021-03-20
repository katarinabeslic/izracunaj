package com.example.izracunaj;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.izracunaj.SQL.DatabaseBroker;

import java.util.ArrayList;
import java.util.Random;

public class PlayGameActivity extends AppCompatActivity {

    int x, y, correctAnswer, incorrectAnswer;
    private TextView tvTimer, tvPoints, tvSum, tvResult;
    private Button btn1, btn2, btn3, btn4;
    CountDownTimer countDownTimer;
    long millisUntilFinished;
    private int points;
    private int numberOfQuestions;
    int[] btnIds;
    int correctAnswerPosition;
    ArrayList<Integer> incorrectAnswers;
    Random random;
    String ime;
    String[] operatorArray;
    DatabaseBroker dbb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);
        getSupportActionBar().hide();
        dbb = new DatabaseBroker(this);

        ime = getIntent().getExtras().getString("ime");

        x = 0;
        y = 0;
        correctAnswer = 0;
        incorrectAnswer = 0;
        operatorArray = new String[]{"+", "-", "*", "÷"};

        tvTimer = findViewById(R.id.tvTimer);
        tvPoints = findViewById(R.id.tvPoints);
        tvSum = findViewById(R.id.tvSum);
        tvResult = findViewById(R.id.tvResult);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        millisUntilFinished = 30400;
        points = 0;
        numberOfQuestions = 0;
        btnIds = new int[]{R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
        random = new Random();
        incorrectAnswers = new ArrayList<>();
        startGame();
    }

    private void startGame() {
        tvTimer.setText("" + millisUntilFinished / 1000 + "s");
        tvPoints.setText("" + points + "/" + numberOfQuestions);
        generateQuestion();
        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" +(millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                btn1.setClickable(false);
                btn2.setClickable(false);
                btn3.setClickable(false);
                btn4.setClickable(false);
                Intent intent = new Intent(PlayGameActivity.this, GameOverActivity.class);
                intent.putExtra("rezultat", points);
                intent.putExtra("ime", ime);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void generateQuestion() {
        numberOfQuestions++;
        x = random.nextInt(10);
        y = 1 + random.nextInt(9);
        String selectedOperator = operatorArray[random.nextInt(4)];
        correctAnswer = getAnswer(selectedOperator);
        tvSum.setText(x + " " + selectedOperator +" " + y + "= ");
        correctAnswerPosition = random.nextInt(4);
        ((Button)findViewById(btnIds[correctAnswerPosition])).setText(""+correctAnswer);
        while (true){
            if (incorrectAnswers.size() > 3)
                break;
            x = random.nextInt(10);
            y = 1 + random.nextInt(9);
            selectedOperator = operatorArray[random.nextInt(4)];
            incorrectAnswer = getAnswer(selectedOperator);
            if (incorrectAnswer == correctAnswer){
                continue;
            }
            incorrectAnswers.add(incorrectAnswer);
        }
        for (int i = 0; i<3; i++){
            if (i == correctAnswerPosition)
                continue;
            ((Button)findViewById(btnIds[i])).setText("" + incorrectAnswers.get(i));
        }
        incorrectAnswers.clear();
    }

    private int getAnswer(String selectedOperator) {
        int answer = 0;
        switch (selectedOperator){
            case "+":
                answer = x + y;
                break;
            case "-":
                answer = x - y;
                break;
            case "*":
                answer = x * y;
                break;
            case "÷":
                answer = x / y;
                break;
        }
        return answer;
    }

    public void chooseAnswer(View view) {
        int answer = Integer.parseInt(((Button)view).getText().toString());
        if (answer == correctAnswer){
            points++;
            tvPoints.setText(points + "/" + numberOfQuestions);
            tvResult.setText("TAČNO!");
        } else {
            tvPoints.setText(points+"/"+numberOfQuestions);
            tvResult.setText("NETAČNO!");
        }
        generateQuestion();
    }


    public void odustani(View view) {
        Intent intent = new Intent(PlayGameActivity.this, GameOverActivity.class);
        intent.putExtra("rezultat", points);
        intent.putExtra("ime", ime);
        startActivity(intent);
        finish();
    }
}
