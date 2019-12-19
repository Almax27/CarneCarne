/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class FlyingAI extends AIEntity
{
    boolean mToggle;
    public FlyingAI(iSkin _skin)
    {
        super(_skin);
        mToggle = true;
    }
    
    public void fly(Vec2 _point, float _maxSpeed)
    {
        getBody().setLinearDamping(0.8f);        
        Vec2 pos = getBody().getPosition();
        Vec2 speed = new Vec2((_point.x-pos.x)/2, (_point.y-pos.y)/2);
//        mSkin.setRotation(speed.x*20);
        
        if(speed.x > _maxSpeed)
            speed.x = _maxSpeed;
        else if(speed.x < -_maxSpeed)
            speed.x = -_maxSpeed;
        
        if(speed.y > _maxSpeed)
            speed.y = _maxSpeed;
        else if(speed.y < -_maxSpeed)
            speed.y = -_maxSpeed;

        getBody().applyLinearImpulse(speed, getBody().getWorldCenter());
    }

    
}
