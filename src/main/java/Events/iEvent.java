/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.lang.String;
/**
 *
 * @author alasdair
 */
public abstract class iEvent {
    
    abstract public String getName();
    abstract public String getType(); /// This is the name of the event type, not used for hashing just to know.
    
    public boolean process() /// Returns true if it has finished processing
    {
        return true;
    }
}
