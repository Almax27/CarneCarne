/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.FootballSpawnEvent;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import Utils.sFontLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.UnicodeFont;

/**
 *
 * @author alasdair
 */
public class FootballNormalState extends FootballState
{
    int mLastRandomEventSpawn = 0;
    Football mFootball;
    Random mRand;
    private Vec2 ballSpawnPosition;
    private static final int mColonWidth = 25;
    private static final int mLetterHeight = 50;
    private static final int mLetterWidth = 50;
    private static Font mFont = null;
    iSkin mGoalScoreRenders[];
    iSkin mGoalScoreRender;
    iSkin mRandomEventRender;
    private int mRandomEventTimer;
    private int mGoalScoreRenderTimer;
    private int mShowTeamTimer;
    private iSkin mRedTeam, mBlueTeam;
    public FootballNormalState(FootballMode _mode, Vec2 _ballSpawnPosition, int _justScored)
    {
        super(_mode, true);
        ballSpawnPosition = _ballSpawnPosition;
        HashMap params = new HashMap();
        
        mRand = new Random(System.currentTimeMillis());
        
        mGoalScoreRenders = new iSkin[2];
        params.put("ref", "Goal0");
        mGoalScoreRenders[0] = sSkinFactory.create("static", params);
        params.put("ref", "Goal1");
        mGoalScoreRenders[1] = sSkinFactory.create("static", params);
        mGoalScoreRender = null;
        params.put("ref", "EnemiesHaveSpawned");
        mRandomEventRender = sSkinFactory.create("static", params);
        mRandomEventTimer = 2000;
        
        if (mFont == null)
        {
            mFont = sFontLoader.scaleFont(sFontLoader.getDefaultFont(), 2.0f);
        }
        if (_justScored == -1)
        {
            mShowTeamTimer = 0;
            params.put("ref", "BlueTeam");
            mBlueTeam = sSkinFactory.create("static", params);
            params.put("ref", "RedTeam");
            mRedTeam = sSkinFactory.create("static", params);
        }
        else
        {
            mGoalScoreRender = mGoalScoreRenders[_justScored];
            mGoalScoreRenderTimer = 120;
            mShowTeamTimer = 300;
        }
    }
    
    @Override
    void render(int _score1, int _score2)
    {
        if (mShowTeamTimer != 300)
        {
            mShowTeamTimer++;
            Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
            mRedTeam.render(0, 0);
            mRedTeam.render(0, s.y);
            
            mBlueTeam.render(s.x, 0);
            mBlueTeam.render(s.x, s.y);
        }
        if (mRandomEventTimer != 2000)
        {
            mRandomEventTimer++;
            mRandomEventRender.render(mRandomEventTimer, 0);
        }
        Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
        s.y -= mFont.getHeight(_score2 + ":" + _score1)*0.65f ;
        s.x -= mFont.getWidth(_score2 + " ") + (mFont.getWidth(":")*0.5f);
        mFont.drawString(s.x, s.y, _score2 + " ", new Color(255,0,0));
        s.x += mFont.getWidth(_score2 + " ");
        mFont.drawString(s.x, s.y,":");
        s.x += mFont.getWidth(":") + mFont.getWidth(" ");
        mFont.drawString(s.x, s.y, _score1 + " ", new Color(0,0,255));
        if (mGoalScoreRender != null)
        {
            s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
            Vec2 dims = new Vec2(1148, 471);
            s = s.sub(dims.mul(0.5f));
            mGoalScoreRender.render(s.x, s.y);
            mGoalScoreRenderTimer--;
            if (mGoalScoreRenderTimer == 0)
            {
                mGoalScoreRender = null;
            }
        }
    }

    @Override
    void spawnFootball(Football _football)
    {
        ballSpawnPosition = _football.getBody().getPosition().clone();
        _football.setGameMode(mMode);
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(_football);
        }
        mFootball = _football;
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        if (_football == mFootball)
        {
            mGoalScoreRender = mGoalScoreRenders[_team];
            mGoalScoreRenderTimer = 120;
            mMode.scores.set(_team, mMode.scores.get(_team)+1);
            for (int i = 0; i < mMode.players.size(); i++)
            {
                if (i % mMode.goals.size() == _team)
                {
                    mMode.players.get(i).mScoreTracker.scoreGoal();
                }
            }
            mFootball.doom();
            mFootball = null;
            for (PlayerEntity player: _players)
            {
                player.resetRace();
            }
            mLastRandomEventSpawn++;
            if (mLastRandomEventSpawn == 3)
            {
                mLastRandomEventSpawn = 0;
                return randomEvent();
            }
        }
        return this;
    }
    private FootballState randomEvent()
    {
        int event = mRand.nextInt()%2;
        switch (event)
        {
            case 0:
            {
                return new FootballMultiballState(mMode, ballSpawnPosition, mFont);
            }
            case 1:
            {
                HashMap params = new HashMap();
                params.put("position", ballSpawnPosition);
                sEntityFactory.create("Broccoli", params);
                sEntityFactory.create("Carrot", params);
                sEntityFactory.create("Carrot", params);
                mRandomEventTimer = 0;
                break;
            }
        }
        return this;
    }
    @Override
    FootballState footballDied(Football _football)
    {
        HashMap parameters = new HashMap();
        parameters.put("position",ballSpawnPosition);
        mFootball = (Football)sEntityFactory.create("Football",parameters);
        sEvents.triggerEvent(new FootballSpawnEvent(mFootball));
        return this;
    }
    
}
