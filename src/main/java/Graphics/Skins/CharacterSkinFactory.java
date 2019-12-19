/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import Graphics.Skins.CharacterSkin.CharacterSubSkin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Aaron
 */
public class CharacterSkinFactory implements iSkinFactory {
    CharacterSkinFactory()
    {
        
    }
    public iSkin useFactory(HashMap _params)
    {
        try
        {
            return new CharacterSkin(_params.containsKey("subSkins") ? (List<CharacterSubSkin>)_params.get("subSkins") : new ArrayList());
        }
        catch (SlickException e)
        {
            assert (false);
            return null;
        }
    }
}
