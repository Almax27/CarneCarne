/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author alasdair
 */
public class DoubleParticleSys extends ParticleSys
{
    String mRefOther;
    ParticleSystem mOther;
    public DoubleParticleSys(ParticleSystem _system1, String _ref1, ParticleSystem _system2, String _ref2, float _lifeTime)
    {
        super(_system1, _lifeTime, _ref1);
        mOther = _system2;
        mRefOther = _ref2;
    }
    private void switchSystems()
    {
        ParticleSystem other = mOther;
        mOther = mSystem;
        mSystem = other;
        
        String otherRef = mRefOther;
        mRefOther = mRef;
        mRef = otherRef;
    }

    @Override
    public void recycle() {
        super.recycle();
        switchSystems();
        super.recycle();
        switchSystems();
    }
    
    //move emitters only
    @Override
    public void moveEmittersTo(float _x, float _y)
    {
        super.moveEmittersTo(_x, _y);
        switchSystems();
        super.moveEmittersTo(_x, _y);
        switchSystems();
    }
    
    //move emitters only
    @Override
    public void moveEmittersBy(float _dx, float _dy)
    {
        super.moveEmittersBy(_dx, _dy);
        switchSystems();
        super.moveEmittersBy(_dx, _dy);
        switchSystems();
    }
    
    @Override
    public void setWind(float _windFactor)
    {
        super.setWind(_windFactor);
        switchSystems();
        super.setWind(_windFactor);
        switchSystems();
    }
    
    @Override
    public void setGravity(float _gravityFactor)
    {
        super.setGravity(_gravityFactor);
        switchSystems();
        super.setGravity(_gravityFactor);
        switchSystems();
    }
    
    @Override
    public void setAngularOffset(float _degrees)
    {
        super.setAngularOffset(_degrees);
        switchSystems();
        super.setAngularOffset(_degrees);
        switchSystems();
    }

    /*
     * Assumes time step in milliseconds
     * Returns false if life < 0
     */
    @Override
    public boolean update(int _delta)
    {
        boolean ret = super.update(_delta);
        switchSystems();
        ret = super.update(_delta) || ret;
        switchSystems();
        return ret;
    }

    @Override
    public void render(float _x, float _y)
    {
        super.render(_x, _y);
        switchSystems();
        super.render(_x, _y);
        switchSystems();
    }
}
