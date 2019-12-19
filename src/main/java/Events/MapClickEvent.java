/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasair
 */
public class MapClickEvent extends iEvent {
    
    Vec2 mPosition; /// This is the world space position
    String mButton;
    protected int mPlayer;
    public MapClickEvent(Vec2 _position, String _button, int _player) /// Pass in the screen space position
    {
        mPosition = _position;
        mButton = _button;
        mPlayer = _player;
    }
    public String getName()
    {
        return getType() + mButton + mPlayer;
    }
    public String getType()
    {
        return "MapClickEvent";
    }
    public Vec2 getPosition()
    {
        return mPosition;
    }
}
