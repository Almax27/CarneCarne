/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iPlatformController;
import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import Level.Tile;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class CaveIn extends Entity
{
    iPlatformController mPlatformController;
    int mWidth, mHeight;
    CaveIn(iSkin _skin, int _width, int _height)
    {
        super(_skin);
        mPlatformController = null;
        mWidth = _width;
        mHeight = _height;
    }
    
    public void setPlatformController (iPlatformController _controller)
    {
        mPlatformController = _controller;
    }
    @Override
    public void update()
    {
        if (mWaterTiles != 0)
            buoyancy();
        if (mPlatformController != null)
        {
            mPlatformController.update();
        }
        ((Tile)getBody().getFixtureList().getUserData()).updateSystems();
    }
    @Override
    public void render()
    { 
        sGraphicsManager.beginTransform();
            Vec2 axis = sWorld.translateToWorld(new Vec2(getBody().getPosition().x+0.5f,getBody().getPosition().y+0.5f));
            sGraphicsManager.rotate(axis.x, axis.y, getBody().getAngle()*180.0f/(float)Math.PI);
            mSkin.render(getBody().getPosition().x,getBody().getPosition().y);
        sGraphicsManager.endTransform();
    }
    
    public int getWidth()
    {
        return mWidth;
    }
    public int getHeight()
    {
        return mHeight;
    }
    
    @Override
    public void kill(CauseOfDeath _cause, Object _killer)
    {
    }
    
}
