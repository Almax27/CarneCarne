/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import World.sWorld;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class ExplosionFactory implements iEntityFactory
{

    public ExplosionFactory()
    {
    }

    public Entity useFactory(HashMap _parameters)
    {
        Entity entity = new Explosion((PlayerEntity)_parameters.get("player"));
        _parameters.put("userData", entity);
        entity.setBody(sWorld.useFactory("ExplosionBody", _parameters));
        return entity;
    }
    
}
