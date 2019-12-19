/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Sprites;

import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author a203945
 */
public interface iSpriteFactory {
    public iSprite useFactory(HashMap _params) throws SlickException;
    
}
