/*
 * This class provides 
 */
package GUI.HUD;

import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Aaron herp-derp
 */
public class sHud
{
    private static Vector<HashMap<String, HudElement>> mHudElements = new Vector<HashMap<String, HudElement>>();
    public static void init()
    {
        for (int i = 0; i < 4; i++)
        {
            mHudElements.add(new HashMap<String, HudElement>());
        }
    }
    /// _checkForExistance you probably want to be true unless you are a penis head
    public static void addHudElement(int _player, String _skin, Vec2 _position, int _timer, boolean _checkForExistance)
    {
        HashMap params = new HashMap();
        params.put("ref", _skin);
        if (!mHudElements.get(_player).containsKey(_skin) || !_checkForExistance)
        {
            mHudElements.get(_player).put(_skin, new HudElement(sSkinFactory.create("static", params), _position, _timer));
        }
        else mHudElements.get(_player).get(_skin).mTimer = _timer;
    }
    public static void render(int _player)
    {
        HashMap<String, HudElement> elements = mHudElements.get(_player);
        Iterator<HudElement> iter = elements.values().iterator();
        while (iter.hasNext())
        {
            HudElement element = iter.next();
            if (!element.render())
            {
                iter.remove();
            }
        }
    }
}
