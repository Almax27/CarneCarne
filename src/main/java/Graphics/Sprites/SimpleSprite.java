/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Sprites;

import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;

/**
 *
 * @author a203945
 */
public class SimpleSprite extends iSprite
{
    SimpleSprite(Image _image, Vec2 _pos)
    {
        HashMap params = new HashMap();
        params.put("img", _image);
        mSkin = sSkinFactory.create("static", params, false);
    }
    SimpleSprite(String _ref, Vec2 _pos)
    {
        HashMap params = new HashMap();
        params.put("ref", _ref);
        mSkin = sSkinFactory.create("static", params, false);
    }
}
