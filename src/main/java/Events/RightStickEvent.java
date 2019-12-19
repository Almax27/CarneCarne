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
public class RightStickEvent extends iEvent {

    Vec2 mDirection;
    private int mPlayer;
    public RightStickEvent(Vec2 _direction, int _player)
    {
        mDirection = _direction;
        mPlayer = _player;
    }
    
    public Vec2 getDirection()
    {
        return mDirection;
    }

    @Override
    public String getName()
    {
        return getType() + mPlayer;
    }

    @Override
    public String getType()
    {
        return "RightStickEvent";
    }
    
}
