/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import java.util.Random;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class Firework extends MovingParticleSys
{
    Vec2 mPosition, mVelocity;
    String mColour;
    float mOffsetTimer = 0.0f;
    Random rand = new Random();
    Firework(ParticleSysBase _particles, Vec2 _position, Vec2 _velocity, String _colour)
    {
        super(_particles);
        mPosition = _position;
        mVelocity = _velocity;
        mColour = _colour;
    }
    public boolean update(int _delta)
    {
        //Vec2 offset = new Vec2(mVelocity.y,-mVelocity.x);
        //offset = offset.mul((float)Math.cos(mOffsetTimer));
        //mPosition.add(offset.mul(rand.nextFloat()*3));
        if(rand.nextBoolean())
            mVelocity = mVelocity.add(new Vec2(rand.nextFloat()*0.5f,rand.nextFloat()*0.2f));
        else
            mVelocity = mVelocity.sub(new Vec2(rand.nextFloat()*0.5f,rand.nextFloat()*0.2f));
        mPosition = mPosition.add(mVelocity);
        mParticles.moveEmittersTo(mPosition.x, mPosition.y);
        boolean ret = mParticles.update(_delta);
        if (!ret)
        {
            sParticleManager.createSystem("Firework" + mColour, mPosition, 0.5f);            
        }
        mOffsetTimer += rand.nextFloat()*0.8f;
        return ret;
    }

    public Vec2 getPosition()
    {
        return mPosition;
    }
    
    @Override
    public void recycle()
    {
        super.recycle();
    }    
}
