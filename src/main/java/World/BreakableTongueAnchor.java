/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.AIEntity;
import Graphics.Particles.sParticleManager;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class BreakableTongueAnchor extends TongueAnchor
{
    AIEntity mAnchor;
    public BreakableTongueAnchor(AIEntity _anchor)
    {
        mAnchor = _anchor;
        mAnchor.addAnchor(this);
    }
    @Override
    public Vec2 getPosition()
    {
        return mAnchor.getBody().getPosition();
    }

    @Override
    public TongueAnchor stun()
    {
        mAnchor.removeAnchor(this);
        return null;
    }
    @Override
    public boolean hasContact()
    {
        try
        {
            if (mAnchor.getBody().getFixtureList() == null)
                return false;
        }
        catch (NullPointerException e)
        {
            return false;
        }
        return mAnchor != null;
    }
    
    public void breakContact()
    {
        sParticleManager.createSystem("Snap", mAnchor.getBody().getPosition().mul(64).add(new Vec2(32,32)), 1);
        mAnchor = null;
    }
    @Override
    public void release()
    {
        if (mAnchor != null)
            mAnchor.removeAnchor(this);
    }
    
}
