/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class PlayerEndedTutorialEvent extends iEvent
{
    int mPlayerNumber;
    public PlayerEndedTutorialEvent(int _playerNumber)
    {
        mPlayerNumber = _playerNumber;
    }
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "PlayerEndedTutorialEvent";
    }

    public int getPlayer()
    {
        return mPlayerNumber;
    }
    
}
