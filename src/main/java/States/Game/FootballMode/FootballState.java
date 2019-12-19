/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import java.util.ArrayList;

/**
 *
 * @author alasdair
 */
abstract public class FootballState
{
    FootballMode mMode;
    protected FootballState(FootballMode _mode, boolean _renderFootball)
    {
        mMode = _mode;
    }
    abstract void render(int _score1, int _score2);
    abstract void spawnFootball(Football _football);
    abstract FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players);

    abstract FootballState footballDied(Football _football);
    FootballState update()
    {
        return this;
    }
}
