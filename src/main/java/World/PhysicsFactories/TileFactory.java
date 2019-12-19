/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import World.PhysicsFactories.iPhysicsFactory;
import Level.Tile;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
public class TileFactory implements iPhysicsFactory {
    
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Tile tile = (Tile)_parameters.get("Tile");
        float angle = ((Float)_parameters.get("angle")).floatValue();
        
        BodyDef def = new BodyDef();
        if (_parameters.containsKey("bodyType"))
        {
            def.type = ((BodyType)_parameters.get("bodyType"));
        }
        def.angle = angle;
        def.position.x = position.x;
        def.position.y = position.y;
        //def.fixedRotation = true;
        Body body = _world.createBody(def);
        return body;        
    }
}
