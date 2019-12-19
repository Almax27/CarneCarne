/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound.SoundPlayers;

import Level.sLevel.TileType;
import Sound.iSoundAnchor;
import Sound.iSoundPlayer;

/**
 *
 * @author alasdair
 */
public class TileTypePlayer implements iSoundPlayer
{
    iSoundPlayer mPlayers[];
    public TileTypePlayer()
    {
        mPlayers = new iSoundPlayer[TileType.eTileTypesMax.ordinal()];
        iSoundPlayer nullPlayer = new NullPlayer();
        for (int i = 0; i < TileType.eTileTypesMax.ordinal(); i++)
        {
            mPlayers[i] = nullPlayer;
        }
    }
    public void createBlockingPlayer(TileType _tileType, iSoundPlayer _player)
    {
        mPlayers[_tileType.ordinal()] = new SinglePlayBlocker(_player);
    }
    public void createNonBlockingPlayer(TileType _tileType, iSoundPlayer _player)
    {
        mPlayers[_tileType.ordinal()] = _player;
    }
    public void play(Object _parameter)
    {
        TileType tileType = (TileType)_parameter;
        if(tileType != null)
            mPlayers[tileType.ordinal()].play(_parameter);
    }

    public boolean isPlaying()
    {
        throw new UnsupportedOperationException("Need to know the tile type");
    }

    public void stop(Object _parameter)
    {
        TileType tileType = (TileType)_parameter;
        mPlayers[tileType.ordinal()].play(_parameter);
    }

    public void playPositional(iSoundAnchor _position, Object _parameter)
    {
        TileType tileType = (TileType)_parameter;
        mPlayers[tileType.ordinal()].playPositional(_position, _parameter);
    }
    
}
