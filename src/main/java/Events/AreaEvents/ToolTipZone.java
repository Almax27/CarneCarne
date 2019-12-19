/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.PlayerEntity;

/**
 *
 * @author alasdair
 */
public class ToolTipZone extends AreaEvent
{
    private String mStr, mType;
    public ToolTipZone(int _x, int _y, int _x2, int _y2, String _str, String _type)
    {
        super(_x, _y, _x2, _y2);
        mStr = _str;
        mType = _type;
    }
    @Override
    public void enter(AIEntity _entity)
    {
        try
        {
            ((PlayerEntity)_entity).displayTooltip(mStr,mType);
        }
        catch (ClassCastException e)
        {
            
        }
    }

    @Override
    public void leave(AIEntity _entity)
    {
    }
    
}
