/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
abstract public class AreaEvent
{
    int x,y,x2,y2;
    public AreaEvent(int _x, int _y, int _x2, int _y2)
    {
        x = _x;
        y = _y;
        x2 = _x2;
        y2 = _y2;
        sAreaEvents.addEvent(this);
    }
    
    abstract public void enter(AIEntity _entity);
    abstract public void leave(AIEntity _entity);
    public void update()
    {
        
    }

    boolean testPoint(Vec2 _point)
    {
        return x < _point.x && _point.x < x2 && y < _point.y && _point.y < y2;
    }
}
