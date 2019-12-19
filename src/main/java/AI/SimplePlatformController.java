/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Level.sLevel;
import Level.sLevel.PathInfo;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class SimplePlatformController extends iPlatformController
{
    private boolean mToggle = false;
    private boolean mDirection = false;
    private float mSpeed;
    
    public SimplePlatformController(float _speed, String _movement)
    {
        mSpeed = _speed;
        if(_movement.equals("Horizontal"))
        {
            mDirection = false;
        }
        else if(_movement.equals("Vertical"))
        {
            mDirection = true;
        }
    }
    @Override
    public void update() 
    {
        Vec2 pos = mEntity.getBody().getPosition();
        Vec2 dimes = new Vec2(mEntity.getWidth()-1, mEntity.getHeight()-1);
        if(!mDirection)
        {
            if(mToggle == false)
            {   
                for(int i = 0; i < dimes.y+1; i++)
                {
                    if(sLevel.getPathInfo((int)(pos.x), (int)(pos.y+i)) == PathInfo.eNotPassable)
                    {
                        mToggle = true;
                        break;
                    }
                }
            }
            else
            {
                for(int i = 0; i < dimes.y+1; i++)
                {
                    if(sLevel.getPathInfo((int)(pos.x+dimes.x+1), (int)(pos.y+i)) == PathInfo.eNotPassable)
                    {
                        mToggle = false;
                        break;
                    }
                }
            }
            if(mToggle)
            {
                mEntity.getBody().setLinearVelocity(new Vec2(mSpeed, 0)); /// changed from mPlatform.getMoveSpeed()
            }
            else
            {
                mEntity.getBody().setLinearVelocity(new Vec2(-mSpeed, 0));
            }
        }
        else
        {
            if(mToggle == false)
            {
                for(int i = 0; i < dimes.x+1; i++)
                {
                    if(sLevel.getPathInfo((int)(pos.x+i), (int)(pos.y)) == PathInfo.eNotPassable)
                    {
                        mToggle = true;
                        break;
                    }
                }
            }
            else
            {
                for(int i = 0; i < dimes.x+1; i++)
                {
                    if(sLevel.getPathInfo((int)(pos.x+i), (int)(pos.y+dimes.y+1)) == PathInfo.eNotPassable)
                    {
                        mToggle = false;
                        break;
                    }
                }
            }
            if(mToggle)
            {
                mEntity.getBody().setLinearVelocity(new Vec2(0, mSpeed)); /// changed from mPlatform.getMoveSpeed()
            }
            else
            {
                mEntity.getBody().setLinearVelocity(new Vec2(0, -mSpeed));
            }
        }
    }
    
}
