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
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class FootballStartingState extends FootballState
{
    Vec2 mSpawnPosition;
    iSkin mStateRender;
    int mTimer;
    public FootballStartingState(FootballMode _mode)
    {
        super(_mode, false);
        HashMap params = new HashMap();
        params.put("ref", "KickOff");
        mStateRender = sSkinFactory.create("static", params);
        mTimer = 0;
    }
    @Override
    void render(int _score1, int _score2)
    {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
        Vec2 dims = new Vec2(1148, 471).mul(0.5f);
        s = s.sub(dims);
        mStateRender.render(s.x, s.y);
    }

    @Override
    void spawnFootball(Football _football)
    {
        mSpawnPosition = _football.getBody().getPosition();
        sWorld.destroyBody(_football.getBody());
    }
    @Override
    FootballState update()
    {
        mTimer++;
        if (mTimer == 120)
        {
            HashMap parameters = new HashMap();
            parameters.put("position",mSpawnPosition);
            Football football = (Football)sEntityFactory.create("Football",parameters);
            football.setGameMode(mMode);
            FootballState state = new FootballNormalState(mMode, mSpawnPosition, -1);
            state.spawnFootball(football);
            return state;
        }
        return this;
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    FootballState footballDied(Football _football)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
