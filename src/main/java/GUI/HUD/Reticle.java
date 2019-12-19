/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.HUD;

import Entities.AIEntity;
import Graphics.Sprites.iSprite;
import Graphics.Sprites.sSpriteFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 
 */
public class Reticle {
    private iSprite mCurrentReticle = null;
    private iSprite mReticleSpriteController = null, mReticleSpriteMouse = null;
    private AIEntity mPlayer = null;
    private Vec2 mCurrentDir = new Vec2(1,0);
    private Vec2 mLastPosition = new Vec2(0,0);
    private float mOffset = 200;
    private float mDistance = 200;
    private boolean mByPosition = false;
    public Reticle(AIEntity _player)
    {
        mPlayer = _player;
        HashMap params = new HashMap();
        params.put("ref", "Reticle");
        mReticleSpriteController = sSpriteFactory.create("simple", params, false);
        params.clear();
        params.put("ref", "ReticleMouse");
        mReticleSpriteMouse = sSpriteFactory.create("simple", params, false);
        mCurrentReticle = mReticleSpriteMouse;
        //mLastPosition = 
    }
    public void update()
    {
        if(mByPosition)
        {
            mCurrentReticle = mReticleSpriteMouse;
            //translate player to screen space and offset to centre
            Vec2 playerPos = mPlayer.getBody().getPosition().mul(64).add(sWorld.getPixelTranslation());
            mCurrentDir = mLastPosition.sub(playerPos);
            mDistance = mCurrentDir.normalize();
            mCurrentReticle.setPosition(mLastPosition.sub(new Vec2(8,8)));
        }
        else
        {
            mCurrentReticle = mReticleSpriteController;
            Vec2 playerPixelPosition = mPlayer.getBody().getPosition().mul(64);
            Vec2 reticlePos = playerPixelPosition.add(mCurrentDir.mul(mDistance));
            mCurrentReticle.setPosition(reticlePos.sub(new Vec2(16,16))); //20,20 offset to center sprite
            
            float angle = (float) Math.acos(Vec2.dot(mCurrentDir, new Vec2(0,-1)));
            if(mCurrentDir.x >= 0)
                mCurrentReticle.setRotation(angle * (180.0f/(float)Math.PI));
            else
                mCurrentReticle.setRotation(-angle * (180.0f/(float)Math.PI));
        }
        
    }
    public void updateDirection(Vec2 dir)
    {
        mCurrentDir = dir;
        mCurrentDir.normalize();
        mDistance = mOffset;
        mByPosition = false;
    }
    public void setWorldPosition(Vec2 _pos)
    {
        mCurrentDir = _pos.sub(sWorld.translateToWorld(mPlayer.getBody().getPosition()));
        mDistance = mCurrentDir.normalize();
        //mDistance = Math.min(mDistance,mOffset);
        mByPosition = true;
    }
    public void setScreenPosition(Vec2 _pos)
    {
        mLastPosition = _pos;
        mByPosition = true;
    }
    
    public Vec2 getPlayerDirection()
    {
        return mCurrentDir.clone();
    }
    public void render()
    {
        if(mByPosition)
        {
            mCurrentReticle.render(0, 0);
        }
        else
        {
            Vec2 pixelPosition = sWorld.getPixelTranslation();
            mCurrentReticle.render(pixelPosition.x,pixelPosition.y);
        }
    }
    
}
