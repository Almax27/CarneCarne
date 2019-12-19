/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import Level.Tile;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class BodyMovingParticleSys extends MovingParticleSys
{
    Body mBody;
    Vec2 mLocalPosition;
    Vec2 mOffset;
    BodyMovingParticleSys(ParticleSysBase _particles, Body _body, Vec2 _localPosition, Vec2 _offset)
    {
        super(_particles);
        mBody = _body;
        mLocalPosition = _localPosition;
        mOffset = _offset;
    }
    public boolean update(int _delta)
    {
        Vec2 position = getPosition();
        mParticles.moveEmittersTo(position.x, position.y);
        if (mBody.getFixtureList() == null)
        {
            mParticles.kill();
            return false;
        }
        return mParticles.update(_delta);
    }
    public Vec2 getPosition()
    {
        return mBody.getWorldPoint(mLocalPosition).add(mOffset).mul(64);
    }
    
    @Override
    public void setPosition(Vec2 _offset)
    {
        mOffset = _offset;
    }
}
