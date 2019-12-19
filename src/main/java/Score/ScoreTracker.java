/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Score;

import Entities.PlayerEntity;
import Events.NewHighScoreEvent;
import Events.sEvents;
import GUI.Components.Text;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Utils.sFontLoader;
import java.util.Random;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author alasdair
 */

abstract public class ScoreTracker
{
    protected PlayerEntity mWinningPlayer = null;
    protected int mScore;
    private int mBestTime;
    private int mBestEverTime;
    private float mRenderedScore;
    private int mWinnerTimer;
    private int mFireworkTimer;
    private Text mScoreText;
    Font mScoreDisplay;
    PlayerEntity mEntity;
    private boolean mWon;

    private String getTimeString(int _timer)
    {
        int seconds = _timer / 60;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        return minutes + ":" + seconds;
    }
    public void render()
    {
        //sGraphicsManager.drawString("Time penis: " + mBestTime, 0, 0.3f);
        {
            if (mWon)
            {
                Vec2 topLeft = new Vec2(mEntity.getViewport().getX(),mEntity.getViewport().getY());
                float width = mEntity.getViewport().getWidth();
                topLeft.x += width *0.5f;
                topLeft.x /= sGraphicsManager.getTrueScreenDimensions().x;
                topLeft.y /= sGraphicsManager.getTrueScreenDimensions().y;
                mScoreDisplay.drawString(topLeft.x, topLeft.y, "Time: " + getTimeString(mBestTime));
            }
        }
    }


    public enum ScoreEvent
    {
        eKilledOpponent,
        eDied,
        eWonRace,
        eLostRace,
        eScoredGoal,
        eConceededGoal,
        eScoreEventsMax,
    }
    
    protected ScoreTracker(int _GUIManager, PlayerEntity _entity) 
    {
        mScoreText = new Text(sGraphicsManager.getGUIContext(), sFontLoader.createFont("score", 30), "SCORE", new Vector2f(30,40), false);
        mRenderedScore = mScore = 0;
        mBestTime = mBestEverTime = Integer.MAX_VALUE;
        //loadWinnerImage(1);
        mEntity = _entity;
        mWon = false;
    }

    private void loadWinnerImage(int _position)
    {
        if (mScoreDisplay == null)
        {
            mScoreDisplay = sFontLoader.scaleFont(sFontLoader.createFont("score"), 2.0f);
            mBestTime = mEntity.getRaceTimer();
        }
    }
    public void score(ScoreEvent _event)
    {
        if (_event.equals(ScoreEvent.eWonRace) || _event.equals(ScoreEvent.eScoredGoal))
        {
            throw new UnsupportedOperationException("Call winRace or scoregoal instead, not this directly");
        }
        scoreimpl(_event);
    }
    abstract public void scoreimpl(ScoreEvent _event);
    public void finish(int _time, PlayerEntity _player, int _position)
    {
        loadWinnerImage(_position);
        scoreimpl(ScoreEvent.eWonRace);
        if (_time < mBestTime)
        {
            mBestTime = _time;
            if (_time < mBestEverTime)
            {
                mBestEverTime = _time;
                sEvents.triggerEvent(new NewHighScoreEvent(_time, _player));
            }
        }
        mEntity.mSkin.activateSubSkin("finishedRaceAtPosition" + _position, false, 0);
        mWinnerTimer = 180;
        if(_position == 1)
            mWinningPlayer = _player;
    }
    public void scoreGoal()
    {
        scoreimpl(ScoreEvent.eScoredGoal);
    }
    public void raceEnded()
    {
        mWinningPlayer = null;
        //looser image here is req
    }

    public void update() 
    {
        if(mWinningPlayer != null)
        {
            mFireworkTimer--;
            if(mFireworkTimer <= 0)
            {
                Random rand = new Random();
                mFireworkTimer = 15;
                String color = "Green";
                switch(rand.nextInt(3))
                {
                    case 0:
                    {
                        color = "Red";
                        break;
                    }
                    case 1:
                    {
                        color = "Blue";
                        break;
                    }
                }
                if(rand.nextBoolean())
                    sParticleManager.createFirework(color, mWinningPlayer.getBody().getPosition().mul(64).add(new Vec2(32, 80)), new Vec2(rand.nextFloat(),-3));
                else
                    sParticleManager.createFirework(color, mWinningPlayer.getBody().getPosition().mul(64).add(new Vec2(32, 80)), new Vec2(-rand.nextFloat(),-3));
            }
        }
        Color stringColour = Color.white;
        int renderedScore = (int)mRenderedScore;
        if (mRenderedScore < mScore)
        {
            mRenderedScore += (float)(mScore - mRenderedScore)*0.1f;
            if (mRenderedScore - (float)renderedScore > 0.5f)
            {
                renderedScore++;
            }
            stringColour = Color.green;
        }
        else if (mRenderedScore > mScore)
        {
            mRenderedScore += (float)(mScore - mRenderedScore)*0.1f;
            if (mRenderedScore - (float)renderedScore < -0.5f)
            {
                renderedScore--;
            }
            stringColour = Color.red;
        }
        if (renderedScore == mScore)
            stringColour = Color.white;
        mScoreText.setColor(stringColour);
        mScoreText.setTextString(String.valueOf(renderedScore));
        
    }
}
