/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.HUD;

import Graphics.Skins.iSkin;
import org.jbox2d.common.Vec2;

/**
 *
 * @author A203946
 */
class HudElement
{
    iSkin mSkin;
    Vec2 mPosition;
    int mTimer;
    public HudElement(iSkin _skin, Vec2 _position, int _timer)
    {
        mSkin = _skin;
        mPosition = _position;
        mTimer = _timer;
    }
    
    public boolean render()
    {
        mTimer--;
        if (mTimer == 0)
        {
            return false;
        }
        mSkin.render(mPosition.x, mPosition.y);
        return true;
    }
}
