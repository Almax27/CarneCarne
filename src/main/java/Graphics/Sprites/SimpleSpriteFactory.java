/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Sprites;

import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author a203945
 */
public class SimpleSpriteFactory implements iSpriteFactory{

    public iSprite useFactory(HashMap _params) throws SlickException {
        
        Vec2 pos = new Vec2(2,2);
        if(_params.containsKey("pos"))
            pos = (Vec2)_params.get("pos");
        
        if(_params.containsKey("img"))
        {
            return new SimpleSprite((Image)_params.get("img"),pos);
        }
        else
        {
            return new SimpleSprite((String)_params.get("ref"),pos);
        }
    }
    
}
