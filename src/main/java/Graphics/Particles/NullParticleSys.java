/*
 * Empty class for when particle are turned off
 */
package Graphics.Particles;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author Aaron
 */
public class NullParticleSys implements ParticleSysBase 
{
    static ParticleSystem sys = null;
    
    NullParticleSys()
    {
        try 
        {
            if(sys == null)
                sys = new ParticleSystem(new Image(1, 1));
        } 
        catch (SlickException ex) {
            Logger.getLogger(NullParticleSys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean isDead() {
        return true;
    }
    
    public boolean isPersistant() {
        return false;
    }

    public void kill() {
        
    }

    public void recycle() {
        
    }

    public void moveEmittersTo(float _x, float _y) {
        
    }

    public void moveEmittersBy(float _dx, float _dy) {
        
    }

    public void setWind(float _windFactor) {
        
    }

    public void setGravity(float _gravityFactor) {
        
    }

    public void setAngularOffset(float _degrees) {
        
    }

    public boolean update(int _delta) {
        return false;
    }

    public void render(float _x, float _y) {
        
    }

    public Vec2 getPosition() {
        return new Vec2();
    }

    public ParticleSystem getSystem() {
        return sys;
    }
    public void setPosition(Vec2 mul) {
    }
    public void setScale(float _s){
    }
    
}
