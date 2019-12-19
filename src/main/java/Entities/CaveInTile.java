/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class CaveInTile extends Entity
{
    
    public CaveInTile(iSkin _skin)
    {
        super(_skin);
    }
    @Override
    public void update()
    {
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(getBody().getAngle()*(180/(float)Math.PI));
    }
    
}
