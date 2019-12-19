/*
 * Factory params:
 * simple:      "ref" (data path e.g "character/1.tmx") - prefixes with "data"
 *              "img" (an image for the sprite to use) //ignores "ref" if present
 *              "pos" (Vec2) initial position (defaults to (0,0)
 *              --
 */
package Graphics.Sprites;

import Graphics.sGraphicsManager;
import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author a203945
 */
public class sSpriteFactory {
    private static HashMap<String, iSpriteFactory> mFactories = new HashMap();
    private sSpriteFactory()
    {
        
    }
    public static void init()
    {
        mFactories.put("simple", new SimpleSpriteFactory());
    }
     public static iSprite create(String _factoryName, HashMap _params)
    {
        return create(_factoryName, _params, true);
    }
    public static iSprite create(String _factoryName, HashMap _params, boolean _autoRender)
    {
        try
        {
            iSpriteFactory factory = (iSpriteFactory)mFactories.get(_factoryName);
            iSprite sprite = factory.useFactory(_params);
            if(_autoRender)
                sGraphicsManager.addSprite(sprite);
            return sprite;
        }
        catch(SlickException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
