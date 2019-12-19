/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class GenericEvent extends iEvent
{
    private String mType;
    public GenericEvent(String _type)
    {
        mType = _type;
    }

    @Override
    public String getName()
    {
        return mType;
    }

    @Override
    public String getType()
    {
        return mType;
    }
}
