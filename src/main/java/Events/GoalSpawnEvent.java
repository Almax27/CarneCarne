/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Events.AreaEvents.GoalZone;

/**
 *
 * @author alasdair
 */
public class GoalSpawnEvent extends iEvent
{
    GoalZone mZone;
    public GoalSpawnEvent(GoalZone _zone)
    {
        mZone = _zone;
    }
    
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "GoalSpawnEvent";
    }
    
    public GoalZone getGoalZone()
    {
        return mZone;
    }
    
}
