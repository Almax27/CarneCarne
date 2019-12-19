/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Aaron
 */
//class public to graphics package only
class AnimatedSkinFactory implements iSkinFactory {
    public iSkin useFactory(HashMap _params) throws SlickException
    {
        return new AnimatedSkin(_params.containsKey("ref") ? "assets/" + (String)_params.get("ref") + ".png" : null,
                                _params.containsKey("duration") ? (Integer)_params.get("duration") : 41,
                                _params.containsKey("width") ? (Integer)_params.get("width") : 64,
                                _params.containsKey("height") ? (Integer)_params.get("height") : 64); //~24fps
    }
}
