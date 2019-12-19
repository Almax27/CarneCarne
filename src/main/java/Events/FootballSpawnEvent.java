/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.AIEntity;
import Entities.Entity;
import Entities.Football;

/**
 *
 * @author alasdair
 */
public class FootballSpawnEvent extends iEvent
{
    Football mEntity;
    public FootballSpawnEvent(Entity _entity)
    {
        mEntity = (Football)_entity;
    }
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "FootballSpawnEvent";
    }
    
    public Football getFootball()
    {
        return mEntity;
    }
    
}
