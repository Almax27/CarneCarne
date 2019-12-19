/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.PrismaticJoint;

/**
 *
 * @author alasdair
 */
public class Carcass extends Entity
{
    int mTimer;
    Object mKilledMe;
    Vec2 mOffset;
    int mSpawnTimer;
    public Carcass(iSkin _skin, Object _killedMe, Vec2 _offset)
    {
        super(_skin);
        mTimer = 0;
        mKilledMe = _killedMe;
        mOffset = _offset;
        mSpawnTimer = 50;
    }
    @Override
    public void update()
    {
        if (mSpawnTimer != 0)
        {
            mSpawnTimer--;
        }
        try
        {
            PrismaticJoint attachment = (PrismaticJoint)mBody.getJointList().joint;
            //attachment.setMaxMotorForce((attachment.getJointTranslation())*10.0f);
            if (attachment.getJointTranslation() < 0.1f)
            {
                mBody.getWorld().destroyJoint(attachment);
            }
            if (attachment.getJointSpeed() < 0.1f && attachment.getJointSpeed() > -0.1f)
            {
                attachment.enableMotor(true);
            }
            if (attachment.m_maxMotorForce > 4.0f)
                attachment.setMaxMotorForce(attachment.m_maxMotorForce*0.99f);
        }
        catch (NullPointerException e)
        {
            if (mWaterHeight != 0)
                buoyancy();
            mTimer++;
            if (mTimer == 120)
            {
                sWorld.destroyBody(mBody);
            }
        }
    }
    
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        pixelPosition = pixelPosition.add(mOffset);
        mSkin.setRotation(mBody.getAngle()*180.0f/(float)Math.PI);
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
    
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer)
    {
        if (mBody != null && mKilledMe != _killer && mSpawnTimer == 0)
        {
            if (_causeOfDeath.equals(CauseOfDeath.eImpact))
                sParticleManager.createSystem("CarcassImpactCarne", mBody.getPosition(), 1);
            super.kill(_causeOfDeath, _killer);
        }
    }
}
