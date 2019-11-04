/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.HUD;

import GUI.Components.GraphicalComponent;
import Graphics.sGraphicsManager;
import Level.sLevel.TileType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public class Revolver extends GraphicalComponent
{
    static final int framesPerAmmo = 2;
    static final int AmmoCount = 6;
    static final int fps = 18;
    static final int defaultAmmo = 5 * framesPerAmmo;
    static int timer = 0;
    
    public Revolver(String _ref, Vector2f _position) {
        super(sGraphicsManager.getGUIContext(), _position, new Vector2f());
        init(_ref);
    }
    public Revolver(String _ref)  {
        super(sGraphicsManager.getGUIContext());
        init(_ref);
    }
    private void init(String _ref)
    {
        setMaintainRatio(true);
        try 
        {
            mSpriteSheet = new SpriteSheet(_ref,206,200);
        }
        catch (SlickException ex) 
        {
            Logger.getLogger(Revolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        mSpriteSheet.setFilter(Image.FILTER_LINEAR); //for improved scaling
        mImage = mSpriteSheet.getSprite(0,0);
        setDimentionsToImage();
    }
    
    public Vector2f getOffset()
    {
        float s = 0.75f;
        return new Vector2f(mSpriteSheet.getSprite(0,0).getWidth()*s,mSpriteSheet.getSprite(0,0).getHeight()*s);
    }
    int mTargetFrame = defaultAmmo;
    int mCurrentFrame = defaultAmmo;
    SpriteSheet mSpriteSheet = null;
    
    public void setAmmo(TileType _type)
    {
        switch(_type)
        {
            case eEdible:
                mTargetFrame = 0 * framesPerAmmo;
                break;
            case eMelonFlesh:
                mTargetFrame = 2 * framesPerAmmo;
                break;
            case eGum:
                mTargetFrame = 1 * framesPerAmmo;            
                break;
            case eChilli:
                mTargetFrame = 4 * framesPerAmmo;
                break;
            default:
            case eTileTypesMax:
                mTargetFrame = defaultAmmo;
                break;            
        }
    }

    @Override
    protected boolean updateSelf(int _delta) 
    {
        //rotate the sprite relative to current position etc
        if(mCurrentFrame != mTargetFrame)
        {
            timer += _delta;
            if(timer > 1000/fps)
            {
                timer = 0;
                mCurrentFrame++;
                if(mCurrentFrame == framesPerAmmo*AmmoCount)
                    mCurrentFrame = 0;
            }
        }
        mImage = mSpriteSheet.getSprite(mCurrentFrame%mSpriteSheet.getHorizontalCount(), (int)Math.floor((float)mCurrentFrame/(float)mSpriteSheet.getHorizontalCount())); 
        return super.updateSelf(_delta);
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException 
    {
        super.renderSelf(guic, grphcs, _globalPos);
    }
    
    
    
    
    
}
