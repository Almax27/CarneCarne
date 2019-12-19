/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Events.AreaEvents.AreaEvent;
import Events.AreaEvents.GoalZone;
import Events.FootballSpawnEvent;
import Events.GoalSpawnEvent;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import Level.sLevel;
import States.Game.StateGame;
import States.Game.iGameMode;
import Utils.sFontLoader;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author alasdair
 */
public class FootballMode implements iGameMode, iEventListener
{
    boolean mShowTips = true;
    static final int tipsDelay = 2000;
    static final int tipsCount = 6;
    Image mTipImage = null;
    int mCurrentTip = 2;
    int mTipsTimer = 0;
    
    int mTimer;
    Vec2 ballSpawnPosition;
    ArrayList<PlayerEntity> players = new ArrayList<PlayerEntity>();
    ArrayList<AreaEvent> goals = new ArrayList<AreaEvent>();
    ArrayList<Integer> scores = new ArrayList<Integer>();
    Football mFootball;
    FootballState mState;
    Font mFont;
    iSkin mBackdrop;
    public FootballMode(String _level)
    {
        mTimer = 5 * 60 * 60;
        ballSpawnPosition = null;
        mState = new FootballStartingState(this);
        scores.add(0);
        scores.add(0);
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("FootballSpawnEvent", this);
        sEvents.subscribeToEvent("GoalSpawnEvent", this);
        sLevel.newLevel(_level);
        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).mSkin.activateSubSkin("Player" + players.get(i).getPlayerNumber(), false, 0);
        }
        mFont = sFontLoader.createFont("score");
        HashMap params = new HashMap();
        params.put("ref", "timerBackground");
        mBackdrop = sSkinFactory.create("static", params);
        
        try {mTipImage = new Image("ui/footballTips/"+mCurrentTip+".png");
        } catch (SlickException ex) {Logger.getLogger(FootballMode.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    public iGameMode update(Graphics _graphics, float _time)
    {
        if(mShowTips)
        {
            mTipsTimer += _time;
            if(mTipsTimer > tipsDelay)
            {
                if(++mCurrentTip <= tipsCount) 
                {
                    mTipsTimer = 0;
                    try {mTipImage = new Image("ui/footballTips/"+mCurrentTip+".png");
                    } catch (SlickException ex) {Logger.getLogger(FootballMode.class.getName()).log(Level.SEVERE, null, ex);}
                    StateGame.reEnterSelf();
                }
                else if(mTipsTimer > 3000)
                {
                    mTipsTimer = 0;
                    mShowTips = false;
                }
            }
        }
        else
        {
            mTimer--;
            if (mTimer == 0)
            {
                mState = new FootballWonState(this, scores.get(0), scores.get(1));
            }
            else
            {
                mState = mState.update();
            }
            sLevel.update();
            sWorld.update(_graphics, _time);
            sEvents.processEvents();
            sWorld.queryForKicks();
        }
        return this;
    }

    public void render(Graphics _graphics)
    {
        if(mShowTips)
        {
            float sx = sGraphicsManager.getTrueScreenDimensions().x / 1680;
            float sy = sGraphicsManager.getTrueScreenDimensions().y / 1050;
            _graphics.scale(sx,sy);
                mTipImage.draw(0, 0);
            _graphics.scale(1/sx,1/sy);
        }
        else
        {
            sWorld.getCamera().render(_graphics);
            mState.render(scores.get(0), scores.get(1));
            Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
            mBackdrop.render(s.x- 50, s.y+50);
            mFont.drawString(s.x-25, s.y+50, getTimeString(mTimer));
        }
    }
    private String getTimeString(int _timer)
    {
        int seconds = _timer / 60;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        String secondsString = String.valueOf(seconds);
        if (secondsString.length() < 2)
        {
            secondsString = "0" + secondsString;
        }
        return minutes + ":" + secondsString;
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("PlayerCreatedEvent"))
        {
            PlayerCreatedEvent event = (PlayerCreatedEvent)_event;
            event.getPlayer().setTeam(players.size() % 2);
            players.add(event.getPlayer());
            /*if (((FootballStartingState)mState).mFootball != null)
            {
                event.getPlayer().setFootball(((FootballStartingState)mState).mFootball);
            }*/
        }
        else if (_event.getName().equals("FootballSpawnEvent"))
        {
            FootballSpawnEvent event = (FootballSpawnEvent)_event;
            mState.spawnFootball(event.getFootball());
        }
        else if (_event.getName().equals("GoalSpawnEvent"))
        {
            GoalSpawnEvent event = (GoalSpawnEvent)_event;
            GoalZone zone = event.getGoalZone();
            goals.add(zone);
            zone.setGameMode(this);
        }
        return true;
    }

    public void score(int _team, Football _football)
    {
        mState = mState.score(_team, _football, players);
        sWorld.queryForKicks();
    }
    private void multiBall()
    {
        /*mFootball = null;
        ArrayList<Football> multiBall = new ArrayList<Football>();
        for (int i = 0; i < 3; i++)
        {
            footballDied(null);
            mMultiBall.add(mFootball);
        }
        mMultiBall = multiBall;
        mFootball = null;
        for (PlayerEntity player: players)
        {
            player.setFootball(null);
        }*/
    }
    public void footballDied(Football _football)
    {
        mState = mState.footballDied(_football);
    }
    
    public void cleanup() 
    {
        for(PlayerEntity player : players)
        {
            player.destroy();
            sWorld.destroyBody(player.getBody());
        }
        sEvents.unsubscribeToEvent("PlayerCreatedEvent", this);
        sEvents.unsubscribeToEvent("FootballSpawnEvent", this);
        sEvents.unsubscribeToEvent("GoalSpawnEvent", this);
    }
}
