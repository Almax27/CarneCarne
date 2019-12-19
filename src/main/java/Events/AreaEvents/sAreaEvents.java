/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import java.util.ArrayList;
import java.util.Collection;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class sAreaEvents
{
    static private Collection<AreaEvent> mAreaEvents = new ArrayList<AreaEvent>();
    
    static public AreaEvent collidePoint(Vec2 _point)
    {
        for (AreaEvent event: mAreaEvents)
        {
            if (event.testPoint(_point))
            {
                return event;
            }
        }
        return null;
    }
    
    static public void addEvent(AreaEvent _event)
    {
        mAreaEvents.add(_event);
    }
    
    static public void clearEvents()
    {
        mAreaEvents.clear();
    }
}
