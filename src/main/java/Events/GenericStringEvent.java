/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class GenericStringEvent extends GenericEvent
{
    String mNameAddition;
    public GenericStringEvent(String _type, String _nameAddition)
    {
        super(_type);
        mNameAddition = _nameAddition;
    }
    @Override
    public String getName()
    {
        return getType() + mNameAddition;
    }
}
