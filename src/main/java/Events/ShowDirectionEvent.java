/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.PlayerEntity;
import Entities.sEntityFactory;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
public class ShowDirectionEvent extends iEvent
{
    PlayerEntity mEntity;
    public ShowDirectionEvent(PlayerEntity _entity)
    {
        mEntity = _entity;
    }

    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "ShowDirectionEvent";
    }
    
    @Override
    public boolean process()
    {
        HashMap parameters = new HashMap();
        parameters.put("position",mEntity.getBody().getPosition());
        sEntityFactory.create("Carrot",parameters);
        return true;
    }
}
