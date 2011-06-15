/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;

/**
 *
 * @author A203946
 */
public class TileDestroyedEvent implements iEvent {
    
    int xTile, yTile;
    public TileDestroyedEvent(int _xTile, int _yTile)
    {
        xTile = _xTile;
        yTile = _yTile;
    }
    public String getName()
    {
        return getType();
    }
    public String getType()
    {
        return "TileDestroyedEvent";
    }
}
