/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.PlayerEntity;
import Events.GenericEvent;
import Events.RaceResetEvent;
import Events.RaceWonEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Sound.SoundScape;
import Sound.sSound;
import java.util.HashSet;

/**
 *
 * @author alasdair
 */
public class RaceEndZone extends CheckPointZone implements iEventListener
{
    private HashSet<PlayerEntity> mWonPlayers;
    public RaceEndZone(int _x, int _y, int _x2, int _y2, int _numCheckPoints)
    {
        super(_x, _y, _x2, _y2, _numCheckPoints, null);
        sEvents.subscribeToEvent("newLevel", this);
        sEvents.subscribeToEvent("BarrierOpenEvent" + "StartGate", this);
        mWonPlayers = new HashSet<PlayerEntity>();
    }
    @Override
    public void renderRaceState(int _raceTimer)
    {
       // sGraphicsManager.drawString("You won the race in " + getTimeString(_raceTimer) + " seconds.", 0f, 0);
    }
    @Override
    public void enter(AIEntity _entity)
    {
        PlayerEntity entity = (PlayerEntity)_entity;
        if (!mWonPlayers.contains(entity))
        {
            mWonPlayers.add(entity);
            if (mWonPlayers.size() == 1)
            {
                _entity.placeCheckPoint(this);
                sSound.play(SoundScape.Sound.eRaceWin, 0);
                sEvents.triggerDelayedEvent(new RaceWonEvent(entity));
            }
            else if (mWonPlayers.size() == 4)
            {
                sEvents.triggerDelayedEvent(new GenericEvent("RaceCompletedEvent"));
                sEvents.triggerDelayedEvent(new RaceResetEvent());
                _entity.placeCheckPoint(new RaceLostFakeZone(mCheckPointNumber, mWonPlayers.size()));
            }
            else
            {
                _entity.placeCheckPoint(new RaceLostFakeZone(mCheckPointNumber, mWonPlayers.size()));
            }
            entity.mScoreTracker.finish(entity.getRaceTimer(), entity, mWonPlayers.size());
        }
    }
    @Override
    public boolean incrementRaceTimer()
    {
        return false;
    }

    public boolean trigger(iEvent _event)
    {
        if(_event.getName().equals("BarrierOpenEvent" + "StartGate"))
        {
            mWonPlayers.clear();
        }
        else if(_event.getType().equals("newLevel"))
        {
            sEvents.unsubscribeToEvent("newLevel", this);
            sEvents.unsubscribeToEvent("BarrierOpenEvent" + "StartGate", this);
        }
        return true;
    }
}
