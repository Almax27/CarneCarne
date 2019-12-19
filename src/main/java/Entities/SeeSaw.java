/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;

/**
 *
 * @author alasdair
 */
public class SeeSaw extends Entity
{
    RevoluteJoint mJoint;
    public SeeSaw(iSkin _skin)
    {
        super(_skin);
    }
    @Override
    public void update()
    {
        float angleError = mJoint.getJointAngle() - 0.0f; /// 0 is the angle target
        float gain = 1.0f;
        mJoint.setMotorSpeed(-gain * angleError);
    }
    public void setJoint(RevoluteJoint _joint)
    {
        mJoint = _joint;
    }
    public void render()
    {
        //Vec2 axis = sWorld.translateToWorld(new Vec2(mBody.getPosition().x,mBody.getPosition().y+));
        //sGraphicsManager.rotate(mBody.getAngle()*180.0f/(float)Math.PI);
        Vec2 physPos = getBody().getPosition().clone();
        physPos.x -= 1.0f; //FIXME: offset to compensate 0.5f bug (assumes width = 3)
        Vec2 pos = sWorld.translateToWorld(physPos);
        mSkin.render(pos.x,pos.y); 
        mSkin.setRotation(getBody().getAngle()*180.0f/(float)Math.PI);
    }
}
