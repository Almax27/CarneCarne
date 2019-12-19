/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.PlayerEntity;
import Events.GenericEvent;
import Events.RaceResetEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.sGraphicsManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alasdair
 */
public class RaceStartZone extends CheckPointZone implements iEventListener
{
    Map<PlayerEntity, CheckPointZone> mPlayers;
    int mNumPlayers;
    boolean mRaceHasStarted;
    public RaceStartZone(int _x, int _y, int _x2, int _y2, CheckPointZone _nextCheckPoint, int _numPlayers)
    {
        super(_x, _y, _x2, _y2, 0, _nextCheckPoint);
        mPlayers = new HashMap<PlayerEntity, CheckPointZone>();
        mNumPlayers = _numPlayers;
        mRaceHasStarted = false;
        sEvents.subscribeToEvent("RaceResetEvent", this);
        sEvents.subscribeToEvent("BarrierOpenEvent" + "StartGate", this);
    }
    @Override
    public void enter(AIEntity _entity) 
    {
        /*if (_entity.getCheckPoint() == this)
        {
            throw new UnsupportedOperationException("Dicks");
        }*/
        mPlayers.put((PlayerEntity)_entity, ((PlayerEntity)_entity).getCheckPoint());
        _entity.placeCheckPoint(this);
        if (!mRaceHasStarted && mPlayers.size() == mNumPlayers)
        {
            sEvents.triggerEvent(new GenericEvent("RaceCountdownStartEvent"));
        }
    }
    @Override
    public void leave(AIEntity _entity) 
    {
        //_entity.placeCheckPoint(mPlayers.get(_entity));
        if (!mRaceHasStarted)
        {
            ((PlayerEntity)_entity).getToStartingZone();
        }
        if (!mRaceHasStarted && mPlayers.size() == mNumPlayers)
        {
            sEvents.triggerEvent(new GenericEvent("RaceCountdownInterruptEvent"));
        }
        mPlayers.remove((PlayerEntity)_entity);
    }
    @Override
    public boolean incrementRaceTimer()
    {
        if (mRaceHasStarted)
        {
            return true;
        }
        /*mRaceCountDown = mPlayers.size() == mNumPlayers;
        if (mRaceCountDown)
        {
            mRaceCountDownTimer++;
            if (mRaceCountDownTimer == 180)
            {
                //sEvents.triggerEvent(new RaceStartEvent());
                mRaceHasStarted = true;
            }
        }
        else
        {
            mRaceCountDownTimer = -1;
        }*/
        return mRaceHasStarted;
    }
    
    /// Properties for the countdown animation
    static final int phase1CompleteTime = 20;
    static final float phase1EndSize = 0.8f; /// Size of the sprite at the end of phase 1
    @Override
    public void renderRaceState(int _raceTimer)
    {
        if (mRaceHasStarted)
        {
            super.renderRaceState(_raceTimer);
        }
        /*else if (mRaceCountDown)
        {
        }*/
        else
        {
           // sGraphicsManager.drawString("Waiting for all players to be ready.", 0f, 0);
        }
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("BarrierOpenEvent" + "StartGate"))
        {
            mRaceHasStarted = true;
        }
        else
        {
            RaceResetEvent event = (RaceResetEvent)_event;
            mRaceHasStarted = false;
        }
        return true;
    }
}
