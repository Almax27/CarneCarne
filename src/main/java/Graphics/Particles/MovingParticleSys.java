/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author alasdair
 */
abstract public class MovingParticleSys implements ParticleSysBase
{
    ParticleSysBase mParticles;
    Vec2 mOffset;
    public MovingParticleSys(ParticleSysBase _particles)
    {
        mParticles = _particles;
    }

    public boolean isDead()
    {
        return mParticles.isDead();
    }
    
    public boolean isPersistant()
    {
        return mParticles.isPersistant(); 
    }

    public void kill() {
        mParticles.kill();
    }

    public void recycle() {
        mParticles.recycle();
    }

    public void moveEmittersTo(float _x, float _y) {
        mParticles.moveEmittersTo(_x, _y);
    }

    public void moveEmittersBy(float _dx, float _dy) {
        mParticles.moveEmittersBy(_dx, _dy);
    }

    public void setWind(float _windFactor) {
        mParticles.setWind(_windFactor);
    }

    public void setGravity(float _gravityFactor) {
        mParticles.setGravity(_gravityFactor);
    }

    public void setAngularOffset(float _degrees) {
        mParticles.setAngularOffset(_degrees);
    }


    public void render(float _x, float _y)
    {
        mParticles.render(_x, _y);
    }


    public ParticleSystem getSystem() {
        return mParticles.getSystem();
    }
    public void setPosition(Vec2 mul) {
        mParticles.setPosition(mul);
    }
    public void setScale(float _s)
    {
        mParticles.setScale(_s);
    }
}
