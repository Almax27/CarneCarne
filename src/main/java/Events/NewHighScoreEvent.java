/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.PlayerEntity;

/**
 *
 * @author alasdair
 */
public class NewHighScoreEvent extends iEvent
{
    int mTime;
    PlayerEntity mPlayer;
    public NewHighScoreEvent(int _time, PlayerEntity _player)
    {
        mTime = _time;
        mPlayer = _player;
    }

    
    public int getTime()
    {
        return mTime;
    }
    @Override
    public String getName()
    {
        return "NewHighScoreEvent";
    }

    @Override
    public String getType()
    {
        return "NewHighScoreEvent";
    }
    
}
