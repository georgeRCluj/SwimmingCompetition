package com.example.android.swimmingcompetition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /** Initialization of arrays distance, time, stroke, score. Default value 0 for int data type. */
    int[] distance = new int[4];
    int[] time = new int[4];
    int[] stroke = new int[4];
    int[] score = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Displays the given score for Team */
    public void displayTotalDistanceSwimmer_1(String distance) {
        TextView scoreView = (TextView) findViewById(R.id.totalTimeSwimmer1);
        scoreView.setText(String.valueOf(distance));
    }

    public void swimmer1_10meters(View v) {
        distance[0] = distance[0] + 10;
        String distance_swimmer = "T: " + distance[0] + " mt";
        displayTotalDistanceSwimmer_1(distance_swimmer);
    }

}
