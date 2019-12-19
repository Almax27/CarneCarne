/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class GenericCountdownEvent extends iEvent
{
    int mTime;
    iEvent mEvent;
    public GenericCountdownEvent(int _time, iEvent _event)
    {
        mTime = _time;
        mEvent = _event;
    }
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean process()
    {
        if (mTime == 0)
        {
            return false;
        }
        sEvents.triggerEvent(mEvent);
        mTime--;
        return true;
    }
    
}
