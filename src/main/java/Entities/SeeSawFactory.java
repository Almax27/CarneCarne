/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class SeeSawFactory implements iEntityFactory
{

    public Entity useFactory(HashMap _parameters)
    {
        iSkin skin = sSkinFactory.create("static", _parameters);
        SeeSaw entity = new SeeSaw(skin);
        _parameters.put("userData", entity);
        Body body = sWorld.useFactory("SeeSawBodyFactory", _parameters);
        entity.setBody(body);
        return entity;
    }
    
}
