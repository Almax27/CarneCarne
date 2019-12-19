/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import Events.KeyDownEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Level.sLevel;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author a203945
 */
public class FreeCamera extends iCamera implements iEventListener
{
    int xPixel, yPixel;
    
    public FreeCamera(Rectangle _viewPort)
    {
        super(_viewPort);
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xPixel = (int) (s.x/2);
        yPixel = (int) (s.y/2);
        sEvents.subscribeToEvent("KeyDownEvent" + "w" + 0, this);
        sEvents.subscribeToEvent("KeyDownEvent" + "a" + 0, this);
        sEvents.subscribeToEvent("KeyDownEvent" + "s" + 0, this);
        sEvents.subscribeToEvent("KeyDownEvent" + "d" + 0, this);
    }
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        Vec2 worldSpace = new Vec2(_physicsSpace.x-(xPixel/64.0f),_physicsSpace.y-(yPixel/64.0f));
        return new Vec2((s.x/2)+((worldSpace.x*64.0f)*64.0f), (s.y/2)+((worldSpace.y)*64.0f));
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += xPixel/64.0f;
        physicsSpace.y += yPixel/64.0f;
        physicsSpace.x -= (s.x/2)/64.0f;
        physicsSpace.y -= (s.y/2)/64.0f;
        return physicsSpace;        
    }
    public Vec2 getPixelTranslation()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        return new Vec2((s.x/2)+(-xPixel),(s.y/2)+(-yPixel));        
    }
    public void render(Graphics _graphics)
    {
        sGraphicsManager.beginTransform();
            sGraphicsManager.translate(mViewPort.getX(),mViewPort.getY());
            sGraphicsManager.setClip(mViewPort);
            ShapeFill fill = new GradientFill(new Vector2f(0,0), new Color(159,111,89), new Vector2f(mViewPort.getMaxX(),mViewPort.getMaxY()), new Color(186, 160, 149), false);
            Rectangle shape = new Rectangle(0,0, mViewPort.getWidth(),mViewPort.getHeight());
            sGraphicsManager.fill(shape, fill);
            sLevel.renderBackground(_graphics);
            sWorld.render();
            sGraphicsManager.renderManagedSprites();
            sParticleManager.render((int)getPixelTranslation().x, (int)getPixelTranslation().y, (int)mViewPort.getWidth(), (int)mViewPort.getHeight(),0);
            sLevel.renderForeground();
        sGraphicsManager.endTransform();       
    }
    
    public boolean trigger(iEvent _event)
    {
        KeyDownEvent event = (KeyDownEvent)_event;
        switch (event.getKey())
        {
            case 'w':
            {
                yPixel -= 5;
                break;
            }
            case 'a':
            {
                xPixel -= 5;
                break;
            }
            case 's':
            {
                yPixel += 5;
                break;
            }
            case 'd':
            {
                xPixel += 5;
                break;
            }
        }
        return true;
    }
    
    @Override
    public iCamera addPlayer(Body _body)
    {
        return new BodyCamera(_body, mViewPort, true, false);
    }

    public void update(Graphics _graphics) {
    }
}
