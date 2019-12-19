/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
class StaticSkinFactory implements iSkinFactory
{
    public iSkin useFactory(HashMap _params) throws SlickException
    {
        if(_params.containsKey("img"))
        {
            return new StaticSkin((Image)_params.get("img"));
        }
        else
        {
            return new StaticSkin("assets/" + (String)_params.get("ref") + ".png");
        }
    }
}
