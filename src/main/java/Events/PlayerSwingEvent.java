/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class PlayerSwingEvent extends iEvent
{
    int mPlayer;
    public PlayerSwingEvent(int _player)
    {
        mPlayer = _player;
    }
    @Override
    public String getName()
    {
        return getType() + mPlayer;
    }

    @Override
    public String getType()
    {
        return "PlayerSwingEvent";
    }
    
}
