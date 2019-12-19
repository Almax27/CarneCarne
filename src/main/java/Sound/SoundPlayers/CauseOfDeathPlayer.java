/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound.SoundPlayers;

import Entities.Entity.CauseOfDeath;
import Sound.iSoundAnchor;
import Sound.iSoundPlayer;

/**
 *
 * @author alasdair
 */
public class CauseOfDeathPlayer implements iSoundPlayer
{
    iSoundPlayer mPlayers[];
    public CauseOfDeathPlayer()
    {
        mPlayers = new iSoundPlayer[CauseOfDeath.eCauseOfDeathMax.ordinal()];
        iSoundPlayer nullPlayer = new NullPlayer();
        for (int i = 0; i < CauseOfDeath.eCauseOfDeathMax.ordinal(); i++)
        {
            mPlayers[i] = nullPlayer;
        }
    }
    public void createBlockingPlayer(CauseOfDeath _tileType, iSoundPlayer _player)
    {
        mPlayers[_tileType.ordinal()] = new SinglePlayBlocker(_player);
    }
    public void createNonBlockingPlayer(CauseOfDeath _tileType, iSoundPlayer _player)
    {
        mPlayers[_tileType.ordinal()] = _player;
    }
    public void play(Object _parameter)
    {
        CauseOfDeath tileType = (CauseOfDeath)_parameter;
        //mPlayers[tileType.ordinal()].play(_parameter);
    }

    public boolean isPlaying()
    {
        throw new UnsupportedOperationException("Need to know the cause of death");
    }

    public void stop(Object _parameter)
    {
        CauseOfDeath tileType = (CauseOfDeath)_parameter;
        mPlayers[tileType.ordinal()].play(_parameter);
    }

    public void playPositional(iSoundAnchor _position, Object _parameter)
    {
        CauseOfDeath tileType = (CauseOfDeath)_parameter;
        mPlayers[tileType.ordinal()].playPositional(_position, _parameter);
    }
    
}
