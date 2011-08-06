/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class FootballMultiballState extends FootballState
{
    private ArrayList<Football> mBalls;
    private iSkin mSkin;
    private float mSkinPosition;
    public FootballMultiballState(FootballMode _mode)
    {
        super(_mode, false);
        mBalls = new ArrayList<Football>();
        HashMap parameters = new HashMap();
        
        parameters.put("position",new Vec2(26,20));
        for (int i = 0; i < 3; i++)
        {
            Football football = (Football)sEntityFactory.create("Football",parameters);
            football.setGameMode(mMode);
            mBalls.add(football);
        }
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(null);
        }
        HashMap params = new HashMap();
        params.put("ref", "MultiBall");
        mSkin = sSkinFactory.create("static", params);
        mSkinPosition = 0;
    }
    
    @Override
    void render(int _score1, int _score2)
    {
        if (mSkin != null)
        {
            mSkin.render(mSkinPosition, 0);
            mSkinPosition += sGraphicsManager.getTrueScreenDimensions().x / 1000.0f;
            if (mSkinPosition > sGraphicsManager.getTrueScreenDimensions().x)
            {
                mSkin = null;
            }
        }
    }

    @Override
    void spawnFootball(Football _football)
    {
        throw new UnsupportedOperationException("Shouldn't be spawning footballs");
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        if (mBalls.contains(_football))
        {
            mBalls.remove(_football);
            mMode.scores.set(_team, mMode.scores.get(_team)+1);
            for (int i = 0; i < mMode.players.size(); i++)
            {
                if (i % mMode.goals.size() == _team)
                {
                    mMode.players.get(i).mScoreTracker.scoreGoal();
                }
            }
        }
        if (mBalls.isEmpty())
        {
            return new FootballNormalState(mMode);
        }
        return this;
    }

    @Override
    FootballState footballDied(Football _football)
    {
        mBalls.remove(_football);
        if (mBalls.isEmpty())
        {
            return new FootballNormalState(mMode);
        }
        return this;
    }
    
}