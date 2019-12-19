/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Level.sLevel;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
class SpatBlockFactory implements iEntityFactory{

    public SpatBlockFactory()
    {
    }

    public Entity useFactory(HashMap _parameters)
    {
        HashMap skinDef = new HashMap();
        sLevel.TileType tileType = (sLevel.TileType)_parameters.get("tileType");
        int rootId = (Integer)_parameters.get("rootId");
        switch (tileType)
        {
            case eGum:
            {
                skinDef.put("ref", "GumSpit");
                break;
            }
            case eMelonFlesh:
            {
                skinDef.put("ref", "MelonSpit");
                break;
            }
            default:
            {
                skinDef.put("ref", "MeatSpit");
                break;
            }
        }
        iSkin skin = sSkinFactory.create("static", skinDef);
        
        SpatBlock entity = new SpatBlock(skin, rootId, 0.125f);
        
        _parameters.put("entity", entity);
        Body body = sWorld.useFactory("SpatBlockFactory", _parameters);
        
        entity.setBody(body);
        
        return entity;
    }
    
}
