package edu.up.pongstarter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Test;

import java.util.Random;

/**
 * PongMainActivity
 *
 * This is the activity for the Pong game. It attaches a PongAnimator to
 * an AnimationSurface.
 *
 * @enhancemnts: 1.Changed the paddle size from begginner to expert
 *               2. when ball leaves play dont add new ball until button is clicked; add new
 *                  ball by clicking start
 *
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @author Dominic Ferrari
 * @author Briahna Santillana
 * @version March 2016
 *
 */
public class MainActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    //protected AnimationSurface testAnimator;

    protected Button beginnerMode;
    protected Button expertMode;
    protected Button startButton;

    protected AnimationSurface mySurface;
    protected BallAnimator test;

    protected TextView playerScore;
    protected TextView oppScore;
    protected TextView balls;

    protected Vibrator vibrator;

    protected MediaPlayer music;
    /**
     * creates an AnimationSurface containing a TestAnimator.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = new BallAnimator(this);

        //disable screen rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Connect the animation surface with the animator
        mySurface = (AnimationSurface)this.findViewById(R.id.animationSurface);
        mySurface.setAnimator(test);

        beginnerMode = (Button)findViewById(R.id.Beginnerbutton);
        beginnerMode.setOnClickListener(this);

        expertMode = (Button)findViewById(R.id.Expertbutton);
        expertMode.setOnClickListener(this);

        startButton = (Button)findViewById(R.id.Startbutton);
        startButton.setOnClickListener(this);

        playerScore = (TextView)findViewById(R.id.NumScore1);
        oppScore = (TextView)findViewById(R.id.NumScore2);
        playerScore.setText(0+"");
        oppScore.setText(0+"");

        balls =(TextView)findViewById(R.id.numBallsLeft);
        balls.setText("0");

        music = MediaPlayer.create(this,R.raw.song);
        music.start();
/*
            External Citation: Date: March 4, 2016
                               Problem: Did  not know how to vibrate
                               Resource: stack overflow
                               http://stackoverflow.com/questions/5775973/how-can-i-request-the-vibrate-permission
                               Solution: gave permissions in AndroidManifest file;
         */
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //this.mainCanvas = (test)findViewById(R.id.animationSurface);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.Beginnerbutton) {
            test.setLevel(1);

        }
        if(view.getId()==R.id.Expertbutton)
        {
            test.setLevel(2);
        }
        if (view.getId() == R.id.Startbutton){
            Random random = new Random();
            int num = random.nextInt(4);

            //sets random origin
            test.setCountx(random.nextInt(30) + 5);
            test.setCounty(random.nextInt(30) + 5);
            test.setSpeed(random.nextInt(31) + 15);

            //sets random direction
            if(num==0)
            {
                test.setGoBackwardsx(true);
                test.setGoBackwardsy(true);
            }
            else if(num==1)
            {
                test.setGoBackwardsx(false);
                test.setGoBackwardsy(false);
            }
            else if(num==2)
            {
                test.setGoBackwardsx(true);
                test.setGoBackwardsy(false);
            }
            else if(num==3)
            {
                test.setGoBackwardsx(false);
                test.setGoBackwardsy(true);
            }

            test.setInPlay(true);

            if(test.isGameOver())
            {
                test.setGameOver(false);
                test.setcScore(0);
                test.setpScore(0);
                test.setBallsLeft(10);
            }

        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setScore(int cScore,int pScore,int ballsleft)
    {
        oppScore.setText(cScore+"");
        playerScore.setText(pScore+"");
        balls.setText(ballsleft+"");
    }

    public void vibrate()
    {
        vibrator.vibrate(500);
    }

    public void stopMusic()
    {
        music.stop();
    }

}

