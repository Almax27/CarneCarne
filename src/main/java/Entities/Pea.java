/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;

/**
 *
 * @author G203947
 */
public class Pea extends AIEntity
{
    Pea(iSkin _skin)
    {
        super(_skin);
    }
    @Override
    public void render()
    {
        mSkin.setRotation(getBody().getAngle()*(180/(float)Math.PI));
        super.render();
    }
    
}
