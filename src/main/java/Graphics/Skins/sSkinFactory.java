/*
 * Factory class that encapsulates the recycling of entities
 * deals in interfaces, internally handles entities.
 * Factory params:
 * static:      "ref" (data path e.g "char") - prefixes with "assets", appends with .png
 *              "img" (an image for the sprite to use) //ignores "ref" if present
 *              --
 * animated:    "ref" (data path e.g "char") - prefixes with "assets", appends with .png 
 *              "x1" - starting xPos (Integer)
 *              "y1" - starting yPos (Integer)
 *              "x2" - starting xPos (Integer)
 *              "y2" - starting yPos (Integer)
 *              "hScan" - horizontal animation? (boolean)
 *              "duration" - frame duration in ms (Integer)
 * character:   "subSkins" - a list of CharacterSubSkin instances
 */

package Graphics.Skins;

import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author aaron
 */

public class sSkinFactory {
    
    private static HashMap<String,iSkinFactory> mFactories;
    private sSkinFactory()
    {
    }
    public static void init()
    {
        mFactories = new HashMap();
        mFactories.put("static", new StaticSkinFactory());
        mFactories.put("animated", new AnimatedSkinFactory());
        mFactories.put("character", new CharacterSkinFactory());
        mFactories.put("tiled", new TiledSkinFactory());
    }
    public static iSkin create(String _factoryName, HashMap _params)
    {
        return create(_factoryName, _params, false);
    }
    public static iSkin create(String _factoryName, HashMap _params, boolean _alwaysOnTop)
    {         
        try
        {
            iSkinFactory factory = mFactories.get(_factoryName);
            iSkin skin = factory.useFactory(_params);
            skin.setAlwaysOnTop(_alwaysOnTop);
                
            return skin;
        }
        catch(SlickException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
