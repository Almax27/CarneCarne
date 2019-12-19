/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.SimplePlatformController;
import AI.StupidPlatformController;
import AI.iPlatformController;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
class MovingPlatformFactory implements iEntityFactory
{

    public MovingPlatformFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        /*iSkin skin = sSkinFactory.create("static", _parameters);
        //Integer width =  (Integer)_parameters.get("Width");
        Vec2 dimensions = (Vec2)_parameters.get("dimensions");
        
        skin.setDimentions(dimensions.x*64, dimensions.y*64);
        
        iPlatformController controller = null;
        String platformType = (String)_parameters.get("Type");
        if (platformType.equals("StupidPlatform"))
        {
            controller = new StupidPlatformController();
        }
        else if(platformType.equals("SimplePlatform"))
        {
            controller = new SimplePlatformController();
        }
        MovingPlatform entity = new MovingPlatform(skin, controller, dimensions);
        _parameters.put("userData", entity);
        Body body = sWorld.useFactory("MovingPlatformBodyFactory", _parameters);
        entity.mBody = body;
        return entity;*/
        assert(false);
        return null;
    }
    
}
