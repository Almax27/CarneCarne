/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import Graphics.sGraphicsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
public class IntroCamera extends iCamera
{
    public IntroCamera(Rectangle _viewPort)
    {
        super(_viewPort);
    }

    @Override
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        return _physicsSpace.mul(64.0f).sub(new Vec2(mViewPort.getX(),mViewPort.getY()));
    }

    @Override
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        return _worldSpace.mul(1.0f/64.0f).add(new Vec2(mViewPort.getX(),mViewPort.getY()));
    }

    @Override
    public Vec2 getPixelTranslation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(Graphics _graphics)
    {
        sGraphicsManager.beginTransform();
            sGraphicsManager.translate(mViewPort.getX(),mViewPort.getY());
            //sGraphicsManager.setClip(mViewPort);
            sWorld.render();
        sGraphicsManager.endTransform();
    }

    @Override
    public void update(Graphics _graphics)
    {
    }
}
