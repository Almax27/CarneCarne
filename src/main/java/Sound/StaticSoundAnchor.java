/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class StaticSoundAnchor implements iSoundAnchor
{
    private Vec2 mPosition;
    public StaticSoundAnchor(Vec2 _position)
    {
        mPosition = _position;
    }
    public Vec2 getPosition()
    {
        return mPosition.clone(); /// FIxme probably not necessary
    }
    
}
