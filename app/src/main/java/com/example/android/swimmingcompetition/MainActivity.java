package com.example.android.swimmingcompetition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.Math;

public class MainActivity extends AppCompatActivity {
    static final String state_distance = "swimmer_distance";
    static final String time_state = "swimmer_time";
    static final String stroke_swimmer_state = "swimmer_stroke";
    static final String score_state = "team_score";
    static final String speed_state = "team_speed";
    static final String stroke_team_state = "team_stroke_rate";
    static final String nrKms_state = "nr_of_km_team";
    static final String nrHours_state = "nr_of_hours_team";
    static final String firstSwimmer_state = "firstSwimmer";
    static final String T1 = "T: ";
    static final String Kmh = " km/h";
    static final String stk = " stk/\'";
    static final String stk2 = " stk";
    static final String mt = " mt";
    static final String ap1 = "\' ";
    static final String ap2 = "\"";
    /**
     * Initialization of arrays distance, time, stroke (for each swimmer, length 4), score, speed, nrKms, nrHours (for each team, length 2)
     * used 'short' instead of 'int' to save memory; Min / max value -32,768 --- +32,768
     * used 'float' instead of 'double' to save memory (32bit vs 64bit)
     * please see explanation for the matrix firstSwimmer in the method ScoreKeeper()
     */
    private short[] distance = new short[4];
    private short[] time = new short[4];
    private short[] stroke = new short[4];
    private short[] score = new short[2];
    private short[] speed = new short[2];
    private short[] stroke_rate = new short[2];
    private float[] nrKms = new float[2];
    private float[] nrHours = new float[2];
    private byte[][] firstSwimmer = new byte[][]{{5, 5, 5, 5}, {5, 5, 5, 5}, {5, 5, 5, 5}, {5, 5, 5, 5}};

    @Override
    protected void onSaveInstanceState(Bundle dataToSave) {
        super.onSaveInstanceState(dataToSave);
        dataToSave.putShortArray(state_distance, distance);
        dataToSave.putShortArray(time_state, time);
        dataToSave.putShortArray(stroke_swimmer_state, stroke);
        dataToSave.putShortArray(score_state, score);
        dataToSave.putShortArray(speed_state, speed);
        dataToSave.putShortArray(stroke_team_state, stroke_rate);
        dataToSave.putFloatArray(nrKms_state, nrKms);
        dataToSave.putFloatArray(nrHours_state, nrHours);
        dataToSave.putSerializable(firstSwimmer_state, firstSwimmer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** if (savedInstanceState != null) {
         score = savedInstanceState.getShortArray("scoreKey");
         speed = savedInstanceState.getShortArray("speedKey");
         displayDataTeam(score[0] + "", R.id.score_team_A);
         displayDataTeam(score[1] + "", R.id.score_team_B);
         displayDataTeam(speed[0] + " km/h", R.id.speedTeamA);
         displayDataTeam(speed[1] + " km/h", R.id.speedTeamB);
         } */
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onRestoreInstanceState(Bundle dataToRetrieve) {
        super.onRestoreInstanceState(dataToRetrieve);
        distance = dataToRetrieve.getShortArray(state_distance);
        time = dataToRetrieve.getShortArray(time_state);
        stroke = dataToRetrieve.getShortArray(stroke_swimmer_state);
        score = dataToRetrieve.getShortArray(score_state);
        speed = dataToRetrieve.getShortArray(speed_state);
        stroke_rate = dataToRetrieve.getShortArray(stroke_team_state);
        nrKms = dataToRetrieve.getFloatArray(nrKms_state);
        nrHours = dataToRetrieve.getFloatArray(nrHours_state);
        firstSwimmer = (byte[][]) (dataToRetrieve.getSerializable(firstSwimmer_state));
        displayDataTeam(score[0] + "", R.id.score_team_A);
        displayDataTeam(score[1] + "", R.id.score_team_B);
        displayDataTeam(speed[0] + Kmh, R.id.speedTeamA);
        displayDataTeam(speed[1] + Kmh, R.id.speedTeamB);
        displayDataTeam(stroke_rate[0] + stk, R.id.strokeTeamA);
        displayDataTeam(stroke_rate[1] + stk, R.id.strokeTeamB);
        displayDataTeam(T1 + distance[0] + mt, R.id.totalDistanceSwimmer1);
        displayDataTeam(T1 + distance[1] + mt, R.id.totalDistanceSwimmer2);
        displayDataTeam(T1 + distance[2] + mt, R.id.totalDistanceSwimmer3);
        displayDataTeam(T1 + distance[3] + mt, R.id.totalDistanceSwimmer4);
        displayDataTeam(T1 + time[0] / 60 + ap1 + time[0] % 60 + ap2, R.id.totalTimeSwimmer1);
        displayDataTeam(T1 + time[1] / 60 + ap1 + time[1] % 60 + ap2, R.id.totalTimeSwimmer2);
        displayDataTeam(T1 + time[2] / 60 + ap1 + time[2] % 60 + ap2, R.id.totalTimeSwimmer3);
        displayDataTeam(T1 + time[3] / 60 + ap1 + time[3] % 60 + ap2, R.id.totalTimeSwimmer4);
        displayDataTeam(T1 + stroke[0] + stk2, R.id.totalStrokesSwimmer1);
        displayDataTeam(T1 + stroke[1] + stk2, R.id.totalStrokesSwimmer2);
        displayDataTeam(T1 + stroke[2] + stk2, R.id.totalStrokesSwimmer3);
        displayDataTeam(T1 + stroke[3] + stk2, R.id.totalStrokesSwimmer4);
    }

    public void displayDataTeam(String textToDisplay, int resourceId) {
        TextView text = (TextView) findViewById(resourceId); /** findViewById() expects to pass it an integer that represents a resource identifier */
        text.setText(String.valueOf(textToDisplay));
    }

    public void swimmerButtons(View v) {
        int resourceId = v.getId();
        int swNo = 0, team = 0, viewId1 = 0, viewId2 = 0, viewId3 = 0;/** swNo - swimmer number; */

        switch (resourceId) {
            /** DISTANCE */
            case (R.id.swimmer1Add10mt): {
                swNo = 0;
                team = 0;
                viewId1 = R.id.totalDistanceSwimmer1;
                viewId2 = R.id.speedTeamA;
                break;
            }
            case (R.id.swimmer2Add10mt): {
                swNo = 1;
                team = 0;
                viewId1 = R.id.totalDistanceSwimmer2;
                viewId2 = R.id.speedTeamA;
                break;
            }
            case (R.id.swimmer3Add10mt): {
                swNo = 2;
                team = 1;
                viewId1 = R.id.totalDistanceSwimmer3;
                viewId2 = R.id.speedTeamB;
                break;
            }
            case (R.id.swimmer4Add10mt): {
                swNo = 3;
                team = 1;
                viewId1 = R.id.totalDistanceSwimmer4;
                viewId2 = R.id.speedTeamB;
                break;
            }

            /** TIME */
            case (R.id.swimmer1Add30sec): {
                swNo = 0;
                team = 0;
                viewId1 = R.id.totalTimeSwimmer1;
                viewId2 = R.id.speedTeamA;
                viewId3 = R.id.strokeTeamA;
                break;
            }
            case (R.id.swimmer2Add30sec): {
                swNo = 1;
                team = 0;
                viewId1 = R.id.totalTimeSwimmer2;
                viewId2 = R.id.speedTeamA;
                viewId3 = R.id.strokeTeamA;
                break;
            }
            case (R.id.swimmer3Add30sec): {
                swNo = 2;
                team = 1;
                viewId1 = R.id.totalTimeSwimmer3;
                viewId2 = R.id.speedTeamB;
                viewId3 = R.id.strokeTeamB;
                break;
            }
            case (R.id.swimmer4Add30sec): {
                swNo = 3;
                team = 1;
                viewId1 = R.id.totalTimeSwimmer4;
                viewId2 = R.id.speedTeamB;
                viewId3 = R.id.strokeTeamB;
                break;
            }

            /** STROKE */
            case (R.id.swimmer1Add10stk): {
                swNo = 0;
                team = 0;
                viewId1 = R.id.totalStrokesSwimmer1;
                viewId2 = R.id.strokeTeamA;
                break;
            }
            case (R.id.swimmer2Add10stk): {
                swNo = 1;
                team = 0;
                viewId1 = R.id.totalStrokesSwimmer2;
                viewId2 = R.id.strokeTeamA;
                break;
            }
            case (R.id.swimmer3Add10stk): {
                swNo = 2;
                team = 1;
                viewId1 = R.id.totalStrokesSwimmer3;
                viewId2 = R.id.strokeTeamB;
                break;
            }
            case (R.id.swimmer4Add10stk): {
                swNo = 3;
                team = 1;
                viewId1 = R.id.totalStrokesSwimmer4;
                viewId2 = R.id.strokeTeamB;
                break;
            }
        }

        switch (resourceId) {
            /** DISTANCE */
            case (R.id.swimmer1Add10mt):
            case (R.id.swimmer2Add10mt):
            case (R.id.swimmer3Add10mt):
            case (R.id.swimmer4Add10mt): {
                distance[swNo] = (short) (distance[swNo] + 10);                     /** casting to 'short', because of transformation to int inside operations */
                if (team == 0) {
                    nrKms[0] = ((float) (distance[0] + distance[1])) / 1000;        /** need of casting to 'float' before dividing, since the operation inside is 'int' */
                } else nrKms[1] = ((float) (distance[2] + distance[3])) / 1000;
                if (nrHours[team] != 0) {                                           /** if swimming time [the denominator of the fraction] is 0, then speed is 0; avoided compiling errors of division by 0 */
                    speed[team] = (short) Math.round(nrKms[team] / nrHours[team]);  /** rounded 6.7 km/h to 7km/h or 6.4km/h to 6 km/h */
                } else speed[team] = 0;
                String distance_swimmer = T1 + distance[swNo] + mt;
                String speed_team = speed[team] + Kmh;
                displayDataTeam(distance_swimmer, viewId1);
                displayDataTeam(speed_team, viewId2);
                ScoreKeeper();
                break;
            }

            /** TIME */
            case (R.id.swimmer1Add30sec):
            case (R.id.swimmer2Add30sec):
            case (R.id.swimmer3Add30sec):
            case (R.id.swimmer4Add30sec): {
                time[swNo] = (short) (time[swNo] + 30);
                if (team == 0) {
                    nrHours[0] = ((float) (time[0] + time[1])) / 3600;
                    stroke_rate[0] = (short) Math.round(((float) (stroke[0] + stroke[1])) / (nrHours[0] * 60));
                } else {
                    nrHours[1] = ((float) (time[2] + time[3])) / 3600;
                    stroke_rate[1] = (short) Math.round(((float) (stroke[2] + stroke[3])) / (nrHours[1] * 60));
                }
                speed[team] = (short) Math.round(nrKms[team] / nrHours[team]);
                short minutes = (short) (time[swNo] / 60);
                byte seconds = (byte) (time[swNo] % 60);
                String total_time = T1 + minutes + ap1 + seconds + ap2;
                String speed_team = speed[team] + Kmh;
                String total_strokes_team = stroke_rate[team] + stk;
                displayDataTeam(total_time, viewId1);
                displayDataTeam(speed_team, viewId2);
                displayDataTeam(total_strokes_team, viewId3);
                break;
            }

            /** STROKE */
            case (R.id.swimmer1Add10stk):
            case (R.id.swimmer2Add10stk):
            case (R.id.swimmer3Add10stk):
            case (R.id.swimmer4Add10stk): {
                stroke[swNo] = (short) (stroke[swNo] + 10);
                if (nrHours[team] != 0) {
                    if (team == 0)
                        stroke_rate[0] = (short) Math.round(((float) (stroke[0] + stroke[1])) / (nrHours[0] * 60));
                    else
                        stroke_rate[1] = (short) Math.round(((float) (stroke[2] + stroke[3])) / (nrHours[1] * 60));
                } else stroke_rate[team] = 0;
                String total_strokes = T1 + stroke[swNo] + stk2;
                String total_strokes_team = stroke_rate[team] + stk;
                displayDataTeam(total_strokes, viewId1);
                displayDataTeam(total_strokes_team, viewId2);
                break;
            }
        }
    }

    public void ScoreKeeper() {
        /** the Open Water Swimming ScoreKeeper rules are the following:
         * 1) this competition ends at 400 meters; if the swimmers swim more, they receive no other points;
         * 2) swimmers are awarded with points when they arrive at milestones 100, 200, 300 and 400 meters;
         * 3) the first who arrives at 100 meters has 40 points, the second 30 points, the third 20 points and the last 10 points; same happens for 200, 300 and 400 milestones;
         * 4) in this version of the app there is no link between time (in minutes, seconds, hours) and scoring; the order of the swimmers arrival is given by who reaches the milestone first;
         * 5) the optimal stroke rate is between 50 and 100 strokes / minute; if the swimmer is outside this range, then 5 points are subtracted at milestone arrival;
         */
        short mileStone[] = new short[]{100, 200, 300, 400};
        byte vLine, hLine;                                                  /** vLine = each vertical Line from firstSwimmer matrix represents a milestone (100, 200, 300 or 400 meters) */
        search:
        /** hLine = each horizontal Line from firstSwimmer matrix represents the order of swimmers arrival at each milestone */
        for (vLine = 0; vLine <= 3; vLine++) {                              /** in this double loop we run the whole firstSwimmer matrix: from top to bottom (100,200,300,400 meters) and from left to right (to know the order of swimmers arrival) */
            for (hLine = 0; hLine <= 3; hLine++) {
                if (distance[hLine] == mileStone[vLine]) {                  /** this "if" checks if the distance of the swimmer reached the milestone (100,200,300,400 meters) */
                    for (byte counter = 0; counter <= 3; counter++) {       /** this "for" loop helps to check the order of arrival of swimmers within the horizontal line */
                        if (firstSwimmer[vLine][counter] == hLine)
                            break;   /** this "if" checks if the swimmer is already marked in the horizontal line of the matrix, in order not to mark it again */
                        else if (firstSwimmer[vLine][counter] == 5) {       /** the matrix was initialized to "5" value in order not to be confused with any of the swimmer values (0,1,2 or 3) */
                            firstSwimmer[vLine][counter] = hLine;           /** the writing of the swimmer in the matrix */
                            short swimmer_stroke_rate = (short) Math.round(((float) stroke[hLine]) / ((float) time[hLine] / 60)); /** calculates swimmer stroke */
                            if (hLine == 0 || hLine == 1) {                 /** if swimmer is part of team A (swimmer 1 [position 0] or swimmer 2 [position 1]) or team B (swimmer 3/4) */
                                switch (counter) {
                                    case 0:
                                        score[0] = (short) (score[0] + 40);
                                        break;
                                    case 1:
                                        score[0] = (short) (score[0] + 30);
                                        break;
                                    case 2:
                                        score[0] = (short) (score[0] + 20);
                                        break;
                                    case 3:
                                        score[0] = (short) (score[0] + 10);
                                        break;
                                }
                                if (swimmer_stroke_rate < 50 || swimmer_stroke_rate > 100)  /** if swimmer stroke rate is outside ideal range 50-100, then 5 points are subtracted */
                                    score[0] = (short) (score[0] - 5);
                                displayDataTeam(score[0] + "", R.id.score_team_A);          /** added "" to score[0] because the input has to be String, as per declared in the method */
                            } else if (hLine == 2 || hLine == 3) {
                                switch (counter) {
                                    case 0:
                                        score[1] = (short) (score[1] + 40);
                                        break;
                                    case 1:
                                        score[1] = (short) (score[1] + 30);
                                        break;
                                    case 2:
                                        score[1] = (short) (score[1] + 20);
                                        break;
                                    case 3:
                                        score[1] = (short) (score[1] + 10);
                                        break;
                                }
                                if (swimmer_stroke_rate < 50 || swimmer_stroke_rate > 100)
                                    score[1] = (short) (score[1] - 5);
                                displayDataTeam(score[1] + "", R.id.score_team_B);
                            }
                            break search;
                        }
                    }
                }
            }
        }
    }

    public void ResetButton(View v) {
        for (byte counter = 0; counter < 4; counter++) {
            distance[counter] = 0;
            time[counter] = 0;
            stroke[counter] = 0;
            for (byte counter_2 = 0; counter_2 < 4; counter_2++)
                firstSwimmer[counter][counter_2] = 5;
            if (counter < 2) {
                score[counter] = 0;
                speed[counter] = 0;
                stroke_rate[counter] = 0;
                nrKms[counter] = 0;
                nrHours[counter] = 0;
            }
        }
        displayDataTeam(score[0] + "", R.id.score_team_A);
        displayDataTeam(score[1] + "", R.id.score_team_B);
        displayDataTeam(speed[0] + Kmh, R.id.speedTeamA);
        displayDataTeam(speed[1] + Kmh, R.id.speedTeamB);
        displayDataTeam(stroke_rate[0] + stk, R.id.strokeTeamA);
        displayDataTeam(stroke_rate[1] + stk, R.id.strokeTeamB);
        displayDataTeam(T1 + distance[0] + mt, R.id.totalDistanceSwimmer1);
        displayDataTeam(T1 + distance[1] + mt, R.id.totalDistanceSwimmer2);
        displayDataTeam(T1 + distance[2] + mt, R.id.totalDistanceSwimmer3);
        displayDataTeam(T1 + distance[3] + mt, R.id.totalDistanceSwimmer4);
        displayDataTeam(T1 + time[0] / 60 + ap1 + time[0] % 60 + ap2, R.id.totalTimeSwimmer1);
        displayDataTeam(T1 + time[1] / 60 + ap1 + time[1] % 60 + ap2, R.id.totalTimeSwimmer2);
        displayDataTeam(T1 + time[2] / 60 + ap1 + time[2] % 60 + ap2, R.id.totalTimeSwimmer3);
        displayDataTeam(T1 + time[3] / 60 + ap1 + time[3] % 60 + ap2, R.id.totalTimeSwimmer4);
        displayDataTeam(T1 + stroke[0] + stk2, R.id.totalStrokesSwimmer1);
        displayDataTeam(T1 + stroke[1] + stk2, R.id.totalStrokesSwimmer2);
        displayDataTeam(T1 + stroke[2] + stk2, R.id.totalStrokesSwimmer3);
        displayDataTeam(T1 + stroke[3] + stk2, R.id.totalStrokesSwimmer4);
    }

}

