/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components.Effects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author a203945
 */
public class AnimatedEffect implements iComponentEffect
{
    public AnimatedEffect(String _ref, int _tileWidth, int _tileHeight, int duration)
    {
        try 
        {
            SpriteSheet ss = new SpriteSheet(_ref, _tileWidth, _tileHeight);
            mAnim = new Animation(ss, duration);
        } 
        catch (SlickException ex) {
            Logger.getLogger(AnimatedEffect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Animation mAnim = null;

    public void render(int _x, int _y, int _w, int _h) 
    {
        if(mAnim != null)
            mAnim.draw(_x, _y, _w, _h);
    }

    public void update(int _delta) 
    {
        //do nothing
    }
    
}
