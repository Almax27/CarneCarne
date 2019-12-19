/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author alasdar
 */
public interface ParticleSysBase
{
    
    //accessor for mIsDead
    public boolean isDead();
    public boolean isPersistant();
    
    public void kill();
    
    void recycle();
    
    //move emitters only
    public void moveEmittersTo(float _x, float _y);
    
    //move emitters only
    public void moveEmittersBy(float _dx, float _dy);
    
    public void setWind(float _windFactor);
    
    public void setGravity(float _gravityFactor);
    
    public void setAngularOffset(float _degrees);
    
    public ParticleSystem getSystem();

    /*
     * Assumes time step in milliseconds
     * Returns false if life < 0
     */
    boolean update(int _delta);

    void render(float _x, float _y);
    
    public Vec2 getPosition();

    public void setPosition(Vec2 mul);
    public void setScale(float _s);
}
