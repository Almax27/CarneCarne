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
public abstract class TongueAnchor
{
    abstract public Vec2 getPosition();

    public boolean hasContact()
    {
        return true;
    }

    abstract public TongueAnchor stun();
    public void release()
    {
        
    }

}
