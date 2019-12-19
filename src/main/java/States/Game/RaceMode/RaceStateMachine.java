/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.RaceMode;

import Events.GenericStringEvent;
import Events.RaceResetEvent;
import Events.RaceWonEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import Utils.sFontLoader;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Font;

/**
 *
 * @author alasdair
 */
class RaceStateMachine implements iEventListener
{
    private enum State
    {
        eRaceNotStarted,
        eRaceCountDown,
        eRaceInProgress,
        eRaceWon,
        eRaceCompleted,
        eStatesMax,
    }
    iSkin mCountDownRender;
    State mState;
    int mTimer;
    Font mFont;
    Font mFont2;
    public RaceStateMachine()
    {
        mState = State.eRaceNotStarted;
        mTimer = 0;
        sEvents.subscribeToEvent("RaceWonEvent", this);
        sEvents.subscribeToEvent("RaceCountdownStartEvent", this);
        sEvents.subscribeToEvent("RaceCountdownInterruptEvent", this);
        sEvents.subscribeToEvent("RaceCompletedEvent", this);
        sEvents.blockEvent("RaceResetEvent");
        mFont = sFontLoader.scaleFont(sFontLoader.createFont("score"), 2.0f);
        mFont2 = sFontLoader.scaleFont(sFontLoader.createFont("score"), 4.0f);
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getType().equals("RaceWonEvent"))
        {
            RaceWonEvent event = (RaceWonEvent)_event;
            changeState(State.eRaceWon);
        }
        else if (_event.getType().equals("RaceCountdownStartEvent"))
        {
            changeState(State.eRaceCountDown);
            HashMap params = new HashMap();
            params.put("ref", "Countdown3");
            mCountDownRender = sSkinFactory.create("static", params);
            mTimer = 1;
        }
        else if (_event.getType().equals("RaceCountdownInterruptEvent"))
        {
            mCountDownRender = null;
            mState = State.eRaceNotStarted;
        }
        else if (_event.getType().equals("RaceCompletedEvent"))
        {
            if (!mState.equals(State.eRaceNotStarted))
            {
                //sEvents.blockEvent("RaceResetEvent");
                changeState(State.eRaceCompleted);
                //sEvents.unblockEvent("RaceResetEvent");
            }
        }
        return true;
    }
    
    /// Properties for the countdown animation
    static final int phase1CompleteTime = 20;
    static final float phase1EndSize = 0.8f; /// Size of the sprite at the end of phase 1
    public void update()
    {
        mTimer++;
        switch (mState)
        {
            case eRaceNotStarted:
            {
                break;
            }
            case eRaceWon:
            {
                if (mTimer == 15 * 60)
                {
                    changeState(State.eRaceNotStarted);
                }
                break;
            }
            case eRaceCompleted:
            {
                if (mTimer == 5 * 60)
                {
                    changeState(State.eRaceNotStarted);
                }
                break;
            }
            case eRaceCountDown:
            {
                if (mTimer == 180)
                {
                    changeState(State.eRaceInProgress);
                    //sEvents.triggerEvent(new RaceStartEvent());
                    sEvents.triggerEvent(new GenericStringEvent("BarrierOpenEvent", "StartGate"));
                }
                else
                {
                    int mod60 = mTimer % 60;
                    if (mod60 == 0)
                    {
                        int time = 3-(mTimer /60);
                        HashMap params = new HashMap();
                        params.put("ref", "Countdown" + time);
                        mCountDownRender = sSkinFactory.create("static", params);
                    }
                }
            }
        }
    }
    void renderState()
    {
        if (mState.equals(State.eRaceCountDown))
        {
            int mod60 = mTimer % 60;
            float scale = 1.0f;
            if (mod60 < phase1CompleteTime)
            {
                float time = (float)(mod60)/(float)(phase1CompleteTime);
                scale = phase1EndSize * time;
            }
            else
            {
                float time = (float)(mod60-phase1CompleteTime)/(float)(60-phase1CompleteTime);
                scale = ((1.0f-phase1EndSize) * time) + phase1EndSize;
            }
            if (mod60 != 0)
            {
                Vec2 dimensions = new Vec2(scale*(float)272, scale*(float)269);
                mCountDownRender.setDimentions(dimensions.x, dimensions.y);
                Vec2 s = sGraphicsManager.getTrueScreenDimensions();
                mCountDownRender.render(0.5f*(s.x-dimensions.x), 0.5f*(s.y-dimensions.y));
            }
        }
        else if (mState.equals(State.eRaceWon))
        {
            Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
            s.x -= mFont.getWidth("Timeleft: ");
            s.y -= mFont.getHeight("Timeleft: ");
            mFont.drawString(s.x, s.y, "Timeleft: " + getTimeString((15 * 60) - mTimer));
            
            if (mTimer < 240)
            {
                float x = sGraphicsManager.getTrueScreenDimensions().x;
                x *= 0.5f;
                x -= mFont2.getWidth("Race ends in 15 seconds!")*0.5f;
                float y = mTimer;
                if (mTimer % 60 >= 30)
                {
                    y += sGraphicsManager.getTrueScreenDimensions().y*0.5f;
                }
                mFont2.drawString(x, y, "Race ends in 15 seconds!");
            }
        }
    }
    private String getTimeString(int _timer)
    {
        int seconds = _timer / 60;
        //seconds -= minutes * 60;
        int milliseconds = _timer - (seconds * 60);
        float milliF = (float)milliseconds / 60.0f;
        String milliString = String.valueOf(milliF*100);
        if (milliString.length() > 2)
        {
            milliString = milliString.substring(0, 2);
        }
        while (milliString.length() < 2)
        {
            milliString = "0" +  milliString;
        }
        for (int i = 0; i < 2; i++)
        {
            if (milliString.getBytes()[i] == '.')
            {
                milliString = milliString.substring(0, i);
                break;
            }
        }
        while (milliString.length() < 2)
        {
            milliString = "0" +  milliString;
        }
        return seconds + ":" + milliString;
    }
    private void changeState(State _state)
    {
        switch (_state)
        {
            case eRaceNotStarted:
            {
                sEvents.unblockEvent("RaceResetEvent");
                sEvents.triggerEvent(new RaceResetEvent());
                sEvents.blockEvent("RaceResetEvent");
                mTimer = 0;
                break;
            }
            case eRaceCompleted:
            {
                mTimer = 0;
                break;
            }
            case eRaceWon:
            {
                mTimer = 0;
                break;
            }
        }
        mState = _state;
    }
    
}
