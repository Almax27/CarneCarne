/*
 * This abstract class is the root of all evil (read: graphics implementations)
 *
 */

package Graphics;

import Events.WindowResizeEvent;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Graphics.Sprites.iSprite;
import Level.Lighting.sLightsManager;
import Level.sLevel;
import States.Game.StateGame;
import Utils.sFontLoader;
import World.sWorld;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author aaron
 */
public class sGraphicsManager {

    private static Vec2 mNativeScreenDimensions = new Vec2(0,0);
    private static Vec2 mScreenDimensions = new Vec2(0,0);
    private static Rectangle mClip, mWorldClip;
    private static boolean mAllowTransform = false;
    private static boolean mIsFullScreen = false;
    private static DisplayMode mLastDisplayMode = null;
    private static boolean mAllowShaders = true;
    private static boolean mAllowParticles = true;
    private static boolean mRenderDebugInfo = false;
    
    private static ArrayList<iSprite> mManagedSprites = new ArrayList<iSprite>();
    private static ArrayList<iSkin> mAlwaysOnTopSkins = new ArrayList<iSkin>();
    private static ArrayList<Vec2> mAlwaysOnTopPos = new ArrayList<Vec2>();
    private static AppGameContainer mGameContainer;
    
    public static GUIContext getGUIContext(){return mGameContainer;}
    public static Vec2 RelativeToAbsoluteScreen(Vec2 _rel)
    {
         return new Vec2(Display.getDisplayMode().getWidth() * _rel.x, Display.getDisplayMode().getHeight() * _rel.y);
    }
    public static Vec2 AbsoluteToRelativeScreen(Vec2 _abs)
    {
        return new Vec2(_abs.x / Display.getDisplayMode().getWidth(), _abs.y / Display.getDisplayMode().getHeight());
    }
    public static Vec2 getNativeScreenDimensions()
    {
        return mNativeScreenDimensions.clone();
    }
    public static Vec2 getScreenDimensions()
    {
        return mScreenDimensions.clone();
    }
    public static Vec2 getTrueScreenDimensions()
    {
        return new Vec2(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
    }        
    public static void setScreenDimensions(Vec2 _screenDimensions)
    {
        mScreenDimensions = _screenDimensions.clone();
    }
  
    private sGraphicsManager()
    {
    }
    public static void onResize()
    {
        Vec2 dim = new Vec2(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
        sEvents.triggerEvent(new WindowResizeEvent(dim));
        mWorldClip.setBounds(0, 0, dim.x, dim.y);
    }
    static public void init(AppGameContainer _gc) throws SlickException
    {
        mGameContainer = _gc;
        mLastDisplayMode = Display.getDisplayMode();
        setScreenDimensions(new Vec2(mLastDisplayMode.getWidth(), mLastDisplayMode.getHeight()));
        mNativeScreenDimensions.x = Display.getDesktopDisplayMode().getWidth();
        mNativeScreenDimensions.y = Display.getDesktopDisplayMode().getHeight();
        sFontLoader.setDefaultFont("default");
        mWorldClip = new Rectangle(0,0,mScreenDimensions.x,mScreenDimensions.y);
    }
    public static void beginTransform()
    {
        mAllowTransform = true;
        mGameContainer.getGraphics().pushTransform();
    }
    public static void endTransform()
    {
        mAllowTransform = false;
        mGameContainer.getGraphics().popTransform();    
    }
    
    public static void toggleRenderDebugInfo() 
    {
        mRenderDebugInfo = !mRenderDebugInfo;
    }
    
    public static boolean getAllowShaders(){return mAllowShaders;}
    public static void setAllowShaders(boolean _allow){mAllowShaders = _allow;}
    
    public static boolean getAllowParticles(){return mAllowParticles;}
    public static void setAllowParticles(boolean _allow){mAllowParticles = _allow;}
    
    public static void setClip(Rectangle _rect)
    {
        mGameContainer.getGraphics().setClip(_rect);
        mClip = _rect;
    }
    public static void clearClip()
    {
        mGameContainer.getGraphics().clearClip(); 
    }
    public static Rectangle getClip()
    {
        return mClip;
    }
    public static Rectangle getWorldClip()
    {
        return mWorldClip;
    }
    public static void translate(float _x, float _y)
    {
        if(mAllowTransform)
            mGameContainer.getGraphics().translate(_x, _y);
    }
    public static void rotate(float _x, float _y, float _angle)
    {
        if(mAllowTransform)
            mGameContainer.getGraphics().rotate(_x, _y, _angle);
    }
    public static void scale(float _s)
    {
        scale(_s,_s);
    }
    public static void scale(float _x, float _y)
    {
        if(mAllowTransform)
            mGameContainer.getGraphics().scale(_x, _y);
    }
    public static void setColor(Color _color)
    {
        mGameContainer.getGraphics().setColor(_color);
    }
    public static void fill(Shape _shape)
    {
        mGameContainer.getGraphics().fill(_shape);
    }
    public static void fill(Shape _shape, ShapeFill _fill)
    {
        mGameContainer.getGraphics().fill(_shape, _fill);
    }
    public static void addSprite(iSprite _sprite)
    {
        mManagedSprites.add(_sprite);
    }
    
    public static void renderManagedSprites()
    {
        Vec2 worldTrans = sWorld.getPixelTranslation();
        iSprite sprite;
        for(int i = 0; i < mManagedSprites.size(); i++)
        {
            sprite = mManagedSprites.get(i);
            sprite.render(worldTrans.x, worldTrans.y);
            //FIXME: check is dead and remove
        }
    }
    
    public static void renderDebugInfo()
    {
        if(mRenderDebugInfo)
        {
            float pos = 30f;
            float step = 20f;
            
            List<String> lines = Arrays.asList( "Effective FPS:              " + StateGame.getEffectiveFPS(),
                                                "",
                                                "Physics Bodies:             " + sWorld.getBodyCount(),
                                                "",
                                                "Visible Graphical Entities  " + sWorld.getVisibleEntityCount(),
                                                "Visible Tiles:              " + sLevel.getVisibleTileCount(),
                                                "",
                                                "Loaded Particle Systems:    " + sParticleManager.getLoadedSystemCount(),
                                                "Pooled Particle Systems:    " + sParticleManager.getPoolCount(),
                                                "Instanced Particle Systems: " + sParticleManager.getInstancedSystemCount(),
                                                "Visible Particle Systems:   " + sParticleManager.getVisibleSystemCount(),
                                                "",
                                                "Instanced Lights:           " + sLightsManager.getInstancedLightCount(),
                                                "Visible Lights:             " + sLightsManager.getVisibleLightCount()
                                                );
            
            mGameContainer.getGraphics().setColor(new Color(0, 0, 0, 0.5f));
            mGameContainer.getGraphics().fillRect(0, 0, 300, pos+((lines.size()+1)*step));
            
            mGameContainer.getGraphics().setColor(Color.white);
            mGameContainer.setShowFPS(true);
            
            for(String str : lines)
            {
                mGameContainer.getGraphics().drawString(str, 0, pos);
                pos+=step;
            }
        }
        else
        {
            mGameContainer.setShowFPS(false);
        }
    }
    
    /*
     * Draw string in current view port
     * _x: X position in screen space 0.0f-1.0f
     * _y: Y position in screen space 0.0f-1.0f
     */
    public static void drawString(String _str, float _x, float _y)
    {
        drawString(_str, _x, _y, Color.white);
    }
    public static void drawString(String _str, float _x, float _y, Color _color)
    {
        drawString(_str, _x, _y, _color, null, false);        
    }
    public static void drawString(String _str, float _x, float _y, Color _color, Font _font, boolean _center)
    {
        if(_font == null)
            _font = mGameContainer.getGraphics().getFont();
        float xPos = (mScreenDimensions.x * _x);
        float yPos = (mScreenDimensions.y * _y);
        if(_center)
        {
            xPos -= _font.getWidth(_str) * 0.5f;
            yPos -= _font.getHeight(_str) * 0.5f;
        }
        _font.drawString(xPos, yPos,_str,_color);
    }

    public static void toggleFullscreen()
    {
        try 
        {
            mIsFullScreen = !mIsFullScreen;
            if(mIsFullScreen)
            {
                mLastDisplayMode = Display.getDisplayMode();
                mGameContainer.setDisplayMode((int)mNativeScreenDimensions.x, (int)mNativeScreenDimensions.y, true);
            }
            else
            {
                mGameContainer.setDisplayMode(mLastDisplayMode.getWidth(), mLastDisplayMode.getHeight(), false);
            }
        }
        catch(SlickException ex){Logger.getLogger(sGraphicsManager.class.getName()).log(Level.SEVERE, null, ex);}
        onResize();
    }
    
    public static void addAlwaysOnTopSkin(iSkin _skin, Vec2 _renderTrans)
    {
        mAlwaysOnTopSkins.add(_skin);
        mAlwaysOnTopPos.add(_renderTrans);
    }
    public static void renderAlwaysOnTopSkins()
    {
        int i = 0;
        for(iSkin skin : mAlwaysOnTopSkins)
        {
            skin.setAlwaysOnTop(false);
                Vec2 pos = mAlwaysOnTopPos.get(i);
                skin.render(pos.x, pos.y);
            skin.setAlwaysOnTop(true);
            i++;
        }
        mAlwaysOnTopSkins.clear();
        mAlwaysOnTopPos.clear();
    }
    
}
