/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import AI.PlayerInputController;
import Entities.AIEntity;
import Entities.PlayerEntity;
import Events.CaveInEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Level.Lighting.sLightsManager;
import Level.sLevel;
import Utils.Shader.LightSource;
import Utils.Shader.LightingShader;
import Utils.Shader.Shader;
import World.sWorld;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.SlickCallable;

/**
 *
 * @author alasdair
 */
public class BodyCamera extends iCamera implements iEventListener
{

    private static final float directionEpsilon = 0.6f;
    private static final float maxLookOffset = 0.05f;
    private static final float offsetMoveVelocity = 0.002f;
    
    Body        mBody;
    Vec2        mPosition = new Vec2(0,0);
    Vec2        mTranslation = new Vec2(0,0);
    float       mTimer;
    
    boolean     mLookDirection;
    Vec2        mLookOffset;
    float       mXLookVelocity;
    int         mLookChangeTimer;
    
    Vec2        mShake = new Vec2(0,0);
    boolean     mTopSplit;
    boolean mFourPlayerReady;
    CaveInEvent mCaveInEvent;
    CameraGrabber mGrabber = null;
    
    Image       mOverlay = null;
    Image       mLightBlendBase = null;
    LightingShader mLightingShader = null;
    
    public BodyCamera(Body _body, Rectangle _viewPort, boolean _topSplit, boolean _fourPlayerReady)
    {
        super(_viewPort);
        mFourPlayerReady = _fourPlayerReady;
        mBody = _body;
        if(mBody != null)
            ((PlayerEntity)_body.getUserData()).setClip(_viewPort);
        mTopSplit = _topSplit;
        mTimer = 0;
        mLookChangeTimer = 0;
        mLookOffset = new Vec2(0,0);
        mXLookVelocity = 0.0f;
        mLookDirection = false;
        sEvents.subscribeToEvent("CaveInEvent", this);
        mGrabber = new CameraGrabber(new Vec2(34,11));
        try 
        {
            mOverlay = new Image("ui/overlay.png");
            if(sGraphicsManager.getAllowShaders())
            {
                mLightingShader = LightingShader.makeShader("shaders/test.vert", "shaders/test.frag");
                mLightBlendBase = new Image((int)mViewPort.getWidth(), (int)mViewPort.getHeight());
            }
        } catch (SlickException ex) {Logger.getLogger(BodyCamera.class.getName()).log(Level.SEVERE, null, ex);}    
    }
    
    private void calculateLookOffset()
    {
        if(mBody == null) return;
        
        mLookOffset.y = 0;
        float v = mBody.getLinearVelocity().x;
        v = ((PlayerInputController)((AIEntity)mBody.getUserData()).getController()).mPlayerDir.x;
        boolean oldLookDirection = mLookDirection;
        float widthScale = mViewPort.getWidth()/sGraphicsManager.getNativeScreenDimensions().x;
        if (v > 0.0f)
        {
            if (v > directionEpsilon)
            {
                mLookDirection = true;
            }
        }
        else
        {
            if (v < -directionEpsilon)
            {
                mLookDirection = false;
            }
        }
        if (oldLookDirection != mLookDirection)
        {
            mLookChangeTimer++;
            if (mLookChangeTimer < 60)
            {
                mLookDirection = !mLookDirection;
            }
        }
        else mLookChangeTimer = 0;
        if (mLookDirection)
        {
            if (mLookOffset.x > -maxLookOffset)
            {
                mXLookVelocity -= offsetMoveVelocity;
            }
            else mXLookVelocity *= 0.99f;
        }
        else
        {
            if (mLookOffset.x < maxLookOffset)
            {
                mXLookVelocity += offsetMoveVelocity;
            }
            else mXLookVelocity *= 0.99f;
        }
        mXLookVelocity *= 0.99f;
        mLookOffset.x += mXLookVelocity * widthScale;
        /*if (mGrabber != null)
        {
            mLookOffset = mGrabber.getOffset(mPosition);
            mGrabber = mGrabber.update();
        }*/
    }

    @Override
    public void resize(Rectangle _viewPort) 
    {
        super.resize(_viewPort);
        if(mBody == null) return;
        ((PlayerEntity)mBody.getUserData()).setClip(_viewPort);
        try 
        {//resize light blend base
            mLightBlendBase.getTexture().release(); //make super sure memory is released
            mLightBlendBase.destroy();
            mLightBlendBase = new Image((int)_viewPort.getWidth(), (int)_viewPort.getHeight());
        } 
        catch (SlickException ex) {Logger.getLogger(iCamera.class.getName()).log(Level.SEVERE, null, ex);}
        System.gc();
    }
    
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        Vec2 worldSpace = new Vec2(_physicsSpace.x*64.0f,_physicsSpace.y*64.0f);
        worldSpace.x -= mPosition.x*64.0f;
        worldSpace.y -= mPosition.y*64.0f;
        worldSpace.x += mTranslation.x;//64.0f;
        worldSpace.y += mTranslation.y;//64.0f;    
        return worldSpace;
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += mPosition.x;
        physicsSpace.y += mPosition.y;
        physicsSpace.x -= mTranslation.x/64.0f;
        physicsSpace.y -= mTranslation.y/64.0f;
        return physicsSpace;
    }
    public Vec2 getPixelTranslation()
    {
        return new Vec2(mTranslation.x+(mPosition.x*-64.0f),mTranslation.y+(mPosition.y*-64.0f));
    }
    public void update(Graphics _graphics)
    {
        calculatePosition();
        mTimer += 1.0f;
        /*if (mCaveInEvent != null)
        {
            float scale = mCaveInEvent.getScale(mBody.getPosition());
            if (scale != 0.0f)
            {
                mShake.x = scale*(float)Math.sin(mTimer*8.0f);
                mShake.y = scale*(float)Math.sin(mTimer*5.0f);
            }
            else
            {
                mCaveInEvent = null;
            }
        }*/
        
    }
    public void render(Graphics _graphics)
    {
        if(mBody == null) return;
        sGraphicsManager.setClip(mViewPort);
        sGraphicsManager.beginTransform();
        {
            sGraphicsManager.translate(mViewPort.getX(),mViewPort.getY());
            calculatePosition();
            
            //apply gradient to background
            ShapeFill fill = new GradientFill(new Vector2f(0,0), new Color(159,111,89), new Vector2f(mViewPort.getMaxX(),mViewPort.getMaxY()), new Color(186, 160, 149), false);
            Rectangle shape = new Rectangle(0,0, mViewPort.getWidth(),mViewPort.getHeight());
            _graphics.fill(shape, fill);

            //render world
            //sGraphicsManager.scale(0.5f);
            sLevel.renderBackground(_graphics); //FIXME: breaks on older machines due to texture size restrictions
            sWorld.render();
            sGraphicsManager.renderManagedSprites();
            sLevel.renderForeground();
            sParticleManager.render((int)getPixelTranslation().x, (int)getPixelTranslation().y, (int)mViewPort.getWidth(), (int)mViewPort.getHeight(),0); 

            //render lighting
            renderLighting(_graphics);
            
            sGraphicsManager.renderAlwaysOnTopSkins();
            
            //render HUD and overlay
            ((PlayerEntity)mBody.getUserData()).renderHUD();
            mOverlay.draw(0,0, (int)mViewPort.getWidth(), (int)mViewPort.getHeight());
            
        }
        sGraphicsManager.endTransform();  
        sGraphicsManager.clearClip();
        
    }
    
    protected void renderLighting(Graphics _graphics)
    {
        if(mLightingShader != null && mLightBlendBase != null)
        {
            //update visible source list
            mLightingShader.clearSources();
            ArrayList<LightSource> sourceList = sLightsManager.getVisible(mViewPort);
            for(LightSource source : sourceList)
            {
                mLightingShader.addLightSource(source);
            }
            //copy buffer
            SlickCallable.enterSafeBlock();
                _graphics.copyArea(mLightBlendBase, (int)mViewPort.getX(), (int)mViewPort.getY());
            SlickCallable.leaveSafeBlock();
            //render visible lights
            mLightingShader.startShader();
                mLightBlendBase.draw(0,0);
            mLightingShader.endShader();
            Shader.forceFixedShader();
        }
    }
    @Override
    public iCamera addPlayer(Body _body)
    {
        return new SplitScreenCamera(_body, mBody, mViewPort, mTopSplit, mFourPlayerReady);
    }
    protected void calculatePosition()
    {
        if(mBody == null) return;
        mPosition = mBody.getPosition();
        mPosition = mPosition.add(new Vec2(0.5f,0.5f));
        calculateLookOffset();
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        s.x = (mViewPort.getMaxX()- mViewPort.getX());
        s.y = (mViewPort.getMaxY()- mViewPort.getY());
        mTranslation = new Vec2(( (s.x/2)/64.0f), ((s.y/2)/64.0f));
        //mTranslation = mTranslation.add(mLookOffset); //FIXME: FIX CURSOR BUG
        if (mPosition.x < mTranslation.x)
        {
            mTranslation.x -= ((mTranslation.x)-mPosition.x);
        }
        if (mPosition.y < mTranslation.y)
        {
            mTranslation.y -= ((mTranslation.y)-mPosition.y);
        }        
        Vec2 dimensions = sLevel.getLevelDimensions();
        Vec2 rightBorder = dimensions.sub(s.mul(0.5f/64f));
        if (mPosition.x > rightBorder.x)
        {
            mTranslation.x += mPosition.x-rightBorder.x;
        }
        if (mPosition.y > rightBorder.y)
        {
            mTranslation.y += mPosition.y-rightBorder.y;
        }
        mTranslation = mTranslation.mul(64.0f);
        mTranslation = mTranslation.add(mShake);
        sGraphicsManager.setScreenDimensions(s);
    }
    public boolean trigger(iEvent _event)
    {
        mCaveInEvent = (CaveInEvent)_event;
        return true;
    }
}
