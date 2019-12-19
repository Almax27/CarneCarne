/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class CaveInTileFactory implements iEntityFactory
{

    public Entity useFactory(HashMap _parameters)
    {
        Body body = (Body)_parameters.get("Body");
        iSkin skin = (iSkin)sSkinFactory.create("static", _parameters);
        CaveInTile entity = new CaveInTile(skin);
        body.setUserData(entity);
        entity.setBody(body);
        return entity;
    }
    
}
