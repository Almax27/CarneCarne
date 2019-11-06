/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.RaceMode;

import Entities.PlayerEntity;
import Events.NewHighScoreEvent;
import Events.PlayerCreatedEvent;
import Events.RaceResetEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import Level.sLevel;
import States.Game.iGameMode;
import World.sWorld;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;

/**
 *
 * @author alasdair
 */
public class RaceMode implements iGameMode, iEventListener
{
    RaceStateMachine mRaceState;
    Collection<PlayerEntity> players = new LinkedList<PlayerEntity>();
    int mTimer;
    int mBestTime;
    iSkin mRaceRender = null;
    boolean mIsRaceActive = false;
    private boolean mIsCompleted = false;
    Queue<String> mNextLevelProgression;
    String mThisLevel;
    public RaceMode(Queue<String> _nextLevelProgression)
    {
        mTimer = mBestTime = 0;
        mRaceState = new RaceStateMachine();
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("RaceResetEvent", this);
        sEvents.subscribeToEvent("BarrierOpenEvent" + "StartGate", this);
        sEvents.subscribeToEvent("NewHighScoreEvent", this);
        mNextLevelProgression = new ArrayDeque<String>(_nextLevelProgression);
        mThisLevel = mNextLevelProgression.remove();
        sLevel.newLevel(mThisLevel);
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        if (mIsCompleted)
        {
            cleanup();
            if (mNextLevelProgression.isEmpty() || mNextLevelProgression.peek().equals(mThisLevel))
            {
                return null;
            }
            return new RaceMode(mNextLevelProgression);
        }
        else
        {
            mTimer++;
            mRaceState.update();
            sLevel.update();
            sWorld.update(_graphics, _time);
            sEvents.processEvents();
            return this;
        }
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render(_graphics);
        mRaceState.renderState();
        if (mRaceRender != null)
        {
            Vec2 dimensions = new Vec2(272, 269);
            mRaceRender.setDimentions(dimensions.x, dimensions.y);
            Vec2 s = sGraphicsManager.getTrueScreenDimensions();
            mRaceRender.render(0.5f*(s.x-dimensions.x), 0.5f*(s.y-dimensions.y));
            if (mTimer >= 120)
            {
                mRaceRender = null;
            }
        }
        if (mBestTime != 0)
        {
            sGraphicsManager.drawString("Best time: " + mBestTime, 0, 0.1f);
        }
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("PlayerCreatedEvent"))
        {
            PlayerCreatedEvent event = (PlayerCreatedEvent)_event;
            players.add(event.getPlayer());
        }
        else if (_event.getName().equals("RaceResetEvent"))
        {
            if (mNextLevelProgression.isEmpty())
            {
                RaceResetEvent event = (RaceResetEvent)_event;
                for (PlayerEntity entity: players)
                {
                    entity.resetRace();
                }
            }
            else
            {
                mIsCompleted = true;
            }
        }
        else if (_event.getName().equals("BarrierOpenEvent" + "StartGate"))
        {
            mIsRaceActive = true;
            HashMap map = new HashMap();
            map.put("ref", "CountdownGo");
            mRaceRender = sSkinFactory.create("static", map);
            mTimer = 0;
        }
        else if (_event.getType().equals("NewHighScoreEvent"))
        {
            NewHighScoreEvent event = (NewHighScoreEvent)_event;
            int time = event.getTime();
            if (mBestTime == 0)
            {
                mBestTime = time;
            }
            else if (time < mBestTime)
            {
                mBestTime = time;
            }
        }
        return true;
    }
    
    public void cleanup() 
    {
        for(PlayerEntity player : players)
        {
            player.destroy();
            sWorld.destroyBody(player.getBody());
        }
        players = null;
        sEvents.unsubscribeToEvent("PlayerCreatedEvent", this);
        sEvents.unsubscribeToEvent("RaceResetEvent", this);
        sEvents.unsubscribeToEvent("BarrierOpenEvent" + "StartGate", this);
        sEvents.unsubscribeToEvent("NewHighScoreEvent", this);
    }
}