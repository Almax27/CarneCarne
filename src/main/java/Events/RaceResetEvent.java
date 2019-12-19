/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class RaceResetEvent extends iEvent
{
    
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType() 
    {
        return "RaceResetEvent";
    }
    
}
