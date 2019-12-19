/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
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
public class FootballWonState extends FootballState
{
    private iSkin mDisplay;
    private Vec2 mOffset;
    public FootballWonState(FootballMode _mode, int _score1, int _score2)
    {
        super(_mode, false);
        HashMap params = new HashMap();
        mOffset = new Vec2(792, 702);
        if (_score1 > _score2)
        {
            params.put("ref", "BlueWon");
        }
        else if (_score1 == _score2)
        {
            params.put("ref", "Draw");
            mOffset = new Vec2(1148, 471);
        }
        else
        {
            params.put("ref", "RedWon");
        }
        mOffset = mOffset.mul(0.5f);
        mDisplay = sSkinFactory.create("static", params);
    }
    
    @Override
    void render(int _score1, int _score2)
    {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
        s = s.sub(mOffset);
        mDisplay.render(s.x, s.y);
    }

    @Override
    void spawnFootball(Football _football)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(null);
        }
        return this;
    }

    @Override
    FootballState footballDied(Football _football)
    {
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(null);
        }
        return this;
    }
    
}
