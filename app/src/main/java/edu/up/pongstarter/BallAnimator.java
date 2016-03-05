package edu.up.pongstarter;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


/**
 * class that animates a ball repeatedly moving diagonally on
 * simple background
 *
 * @author Steve Vegdahl
 * @author Andrew Nuxoll
 * @author Dominic Ferrari
 * @author Briahna Santillana
 * @version February 2016
 */
public class BallAnimator  implements Animator {

    // instance variables
    Random randy = new Random();
    private int countx = randy.nextInt(30) + 5; // counts the number of logical clock ticks
    private int county = randy.nextInt(30) + 5;
    private int speed = randy.nextInt(31) + 15;

    private float compx=0;
    private boolean compdir=true;

    private int level = 1;//hardness level

    private boolean goBackwardsx = false; // whether clock is ticking backwards
    private boolean goBackwardsy = false;

    private boolean inPlay = true;//dead or alive

    private int cScore = 0;
    private int pScore = 0;

    private MainActivity mainActivity;

    private float xpad;

    private boolean gameOver = false;

    private int ballsLeft = 10;


    public BallAnimator(MainActivity main) {
        mainActivity = main;
    }

    /**
     * Interval between animation frames: .03 seconds (i.e., about 33 times
     * per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    public int interval() {
        return 30;
    }

    /**
     * The background color: a light blue.
     *
     * @return the background color onto which we will draw the image.
     */
    public int backgroundColor() {
        // create/return the background color
        return Color.rgb(0, 0, 0);
    }

    /**
     * Tells the animation whether to go backwards.
     *
     * @param b true iff animation is to go backwards.
     */
    public void goBackwardsy(boolean b) {
        // set our instance variable
        goBackwardsy = b;
    }

    public void goBackwardsx(boolean b) {
        goBackwardsx = b;
    }

    /**
     * Action to perform on clock tick
     *
     * @param g the graphics object on which to draw
     */
    public void tick(Canvas g) {
        if(!gameOver) {
            Paint greenPaint = new Paint();
            greenPaint.setColor(Color.GREEN);

            //set the walls of playing field
            RectF rightWall = new RectF(1600.0f, 0.0f, 1630.0f, 1360.0f);
            RectF leftWall = new RectF(0.0f, 0.0f, 30.0f, 1360.0f);
            g.drawRect(rightWall, greenPaint);
            g.drawRect(leftWall, greenPaint);

            if (compdir) {
                if (compx >= 1100)
                    compdir = !compdir;

                compx += 30;
            } else if (!compdir) {
                if (compx <= 0)
                    compdir = !compdir;

                compx -= 30;
            }

        /*
            External Citation: Date: February 26, 2016
                               Problem: Did  not know how to draw rectangle
                               Resource: Android Studio reference Online
                               http://developer.android.com/reference/android/graphics/RectF.html
                               Solution: used RectF Class;
         */

            RectF beginnerPaddle = new RectF(xpad, 1330.0f, xpad + 500, 1360.0f);
            RectF expertPaddle = new RectF(xpad, 1330.0f, xpad + 200, 1360.0f);

            RectF B_computerPaddle = new RectF(compx, 0.0f, compx + 500, 30.0f);
            RectF E_computerPaddle = new RectF(compx, 0.0f, compx + 200, 30.0f);

            //Draws paddle based on level
            if (level == 1) {
                g.drawRect(beginnerPaddle, greenPaint);
                g.drawRect(B_computerPaddle, greenPaint);
            } else if (level == 2) {
                g.drawRect(expertPaddle, greenPaint);
                g.drawRect(E_computerPaddle, greenPaint);
            }

            //sets ticks forward or backward
            if (goBackwardsx) {
                countx--;
            } else {
                countx++;
            }

            if (goBackwardsy) {
                county--;
            } else {
                county++;
            }

            int numx = (countx * speed);
            int numy = (county * speed);

            // bump our count either up or down by one, depending on whether
            // we are in "backwards mode".
            if (inPlay) {


                // Determine the pixel position of our ball.  Multiplying by 15
                // has the effect of moving 15 pixel per frame.  Modding by 600
                // (with the appropriate correction if the value was negative)
                // has the effect of "wrapping around" when we get to either end
                // (since our canvas size is 600 in each dimension).
                if (numx > (g.getWidth() - 90))//right wall
                {
                    goBackwardsx(!goBackwardsx);
                }
                if (numy > g.getHeight() - 130) {//bottom paddle
                    if (level == 1) {
                        if (numx < xpad + 500 && numx > xpad - 60) {
                            goBackwardsy(!goBackwardsy);
                        } else {
                            inPlay = false;
                            cScore++;
                            ballsLeft--;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.vibrate();
                                }
                            });
                        }
                    } else if (level == 2) {

                        if (numx < xpad + 200 && numx > xpad - 60) {
                            Log.i("before", "" + goBackwardsy);
                            goBackwardsy(!goBackwardsy);
                            Log.i("after", "" + goBackwardsy);
                        } else {
                            inPlay = false;
                            cScore++;
                            ballsLeft--;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.vibrate();
                                }
                            });
                        }
                    }
                }
                if (numy < 30)//opponent paddle
                {
                    if (level == 1) {
                        if (numx < compx + 500 && numx > compx - 60) {
                            goBackwardsy(!goBackwardsy);
                        } else {
                            inPlay = false;
                            pScore++;
                            ballsLeft--;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.vibrate();
                                }
                            });
                        }
                    } else if (level == 2) {
                        if (numx < compx + 200 && numx > compx - 60) {
                            goBackwardsy(!goBackwardsy);
                        } else {
                            inPlay = false;
                            pScore++;
                            ballsLeft--;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.vibrate();
                                }
                            });
                        }
                    }
                }

                if (numx < 30)//left wall
                {
                    goBackwardsx(!goBackwardsx);
                }

                // Draw the ball in the correct position.
                g.drawRect(numx, numy, numx + 60, numy + 60, greenPaint);
            }
            /*
            External Citation: Date: March 4, 2016
                               Problem: Did  not know how to update score
                               Resource: Sara Perkins
                               Solution: used runOnUiThread();
         */
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.setScore(cScore, pScore,ballsLeft);
                }
            });

            if(ballsLeft==0)
            {
                gameOver=!gameOver;
            }
        }

        if(gameOver) {
            Paint gameOver = new Paint();
            gameOver.setColor(Color.GREEN);
            gameOver.setTextSize(100f);
            g.drawText("GAME OVER", (g.getWidth()/2) - 300, g.getHeight() / 2, gameOver);

            if(cScore>pScore)
            {
                g.drawText("COMPUTER WON",g.getWidth()/2-200,g.getHeight()/2+200,gameOver);
            }
            else if(pScore>cScore)
            {
                g.drawText("YOU WON",g.getWidth()/2-200,g.getHeight()/2+200,gameOver);
            }
            else if(pScore==cScore)
            {
                g.drawText("TIE",g.getWidth()/2-200,g.getHeight()/2+200,gameOver);
            }
        }

    }

    /**
     * Tells that we never pause.
     *
     * @return indication of whether to pause
     */

    public boolean doPause() {
        return false;
    }

    /**
     * Tells that we never stop the animation.
     *
     * @return indication of whether to quit.
     */
    public boolean doQuit() {
        return false;
    }

    /**
     * reverse the ball's direction when the screen is tapped
     */
    public void onTouch(MotionEvent event) {
        xpad = event.getX();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    public void setCountx(int countx) {
        this.countx = countx;
    }

    public void setCounty(int county) {
        this.county = county;
    }

    public void setGoBackwardsx(boolean goBackwardsx) {
        this.goBackwardsx = goBackwardsx;
    }

    public void setGoBackwardsy(boolean goBackwardsy) {
        this.goBackwardsy = goBackwardsy;
    }

    public int getcScore() {
        return cScore;
    }

    public void setcScore(int cScore) {
        this.cScore = cScore;
    }

    public int getpScore() {
        return pScore;
    }

    public void setpScore(int pScore) {
        this.pScore = pScore;
    }

    public float getXpad() {
        return xpad;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getBallsLeft() {
        return ballsLeft;
    }

    public void setBallsLeft(int ballsLeft) {
        this.ballsLeft = ballsLeft;
    }

    public void setXpad(int xpad) {


        this.xpad = xpad;
    }
}//class TextAnimator
