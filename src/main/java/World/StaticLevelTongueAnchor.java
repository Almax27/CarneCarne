/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class StaticLevelTongueAnchor extends TongueAnchor
{
    Vec2 mPosition;
    public StaticLevelTongueAnchor(Vec2 _position)
    {
        mPosition = _position;
    }

    @Override
    public Vec2 getPosition()
    {
        return mPosition;
    }

    @Override
    public TongueAnchor stun()
    { /// Do le nothing
        return this;
    }
}
