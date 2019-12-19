/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class RaceStartEvent extends iEvent
{
    protected RaceStartEvent()
    {
        
    }
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "RaceStartEvent";
    }
    
    @Override
    public boolean process()
    {
        sEvents.triggerEvent(new GenericStringEvent("BarrierOpenEvent", "StartGate"));
        return true;
    }
    
}
