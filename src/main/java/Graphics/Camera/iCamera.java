/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
public abstract class iCamera {
    
    protected Rectangle mViewPort;
    
    iCamera(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    abstract public Vec2 translateToWorld(Vec2 _physicsSpace);
    abstract public Vec2 translateToPhysics(Vec2 _worldSpace);
    abstract public Vec2 getPixelTranslation();
    abstract public void render(Graphics _graphics);
    abstract public void update(Graphics _graphics);
    public void resize(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    public iCamera addPlayer(Body _body)
    {
        return this;
    }

    void transposeBody(iCamera mCameraB)
    {
        throw new UnsupportedOperationException("Invalid class for this operation");
    }

    Body switchBody(Body mBody)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
