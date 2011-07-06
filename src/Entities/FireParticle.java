/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.ParticleSys;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class FireParticle extends Entity
{
    Vec2 mVelocity;
    ParticleSys mParticles1, mParticles2;
    int mTimer;
    public FireParticle(iSkin _skin, Vec2 _velocity)
    {
        super(_skin);
        mVelocity = _velocity;
        mTimer = 10000;
        mParticles1 = sParticleManager.createSystem("Fire1", new Vec2(0, 0),-1);
        mParticles2 = sParticleManager.createSystem("Fire2", new Vec2(0, 0),-1);
        mTimer = 100;
    }
    @Override
    public void update()
    {
        mTimer--;
        if (mTimer == 0)
        {
            kill();
        }
        mBody.setLinearVelocity(mVelocity);
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition().add(new Vec2(0.0f,0.0f))); /// FIXME
        Vec2 particlePosition = mBody.getPosition().add(new Vec2(0.5f,0.5f)).mul(64f);
        mParticles1.moveEmittersTo(particlePosition.x, particlePosition.y);
        mParticles2.moveEmittersTo(particlePosition.x, particlePosition.y);
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(mBody.getAngle()*(180/(float)Math.PI));
    }
    @Override
    public void kill()
    {
        sWorld.destroyBody(mBody);
        mParticles1.kill();
        mParticles2.kill();
    }
}