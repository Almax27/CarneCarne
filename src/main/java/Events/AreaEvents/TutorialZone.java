/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;

/**
 *
 * @author alasdair
 */
public class TutorialZone extends CheckPointZone
{

    public TutorialZone()
    {
        super(-1,-1,-1,-1, -1, null);
    }
    
    @Override
    public void enter(AIEntity _entity)
    {
    }

    @Override
    public void leave(AIEntity _entity)
    {
    }
    
}
