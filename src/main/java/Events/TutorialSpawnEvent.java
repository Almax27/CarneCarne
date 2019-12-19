/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class TutorialSpawnEvent extends iEvent
{
    int x, y;
    int mPlayerNumber;
    public TutorialSpawnEvent(int _x, int _y, int _playerNumber)
    {
        x = _x;
        y = _y;
        mPlayerNumber = _playerNumber;
    }
    @Override
    public String getName()
    {
        return getType();
    }
    
    public Vec2 getPosition()
    {
        return new Vec2(x,y);
    }

    @Override
    public String getType()
    {
        return "TutorialSpawnEvent";
    }

    public int getPlayerNumber()
    {
        return mPlayerNumber;
    }
    
}
