/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
public class BroccoliExplosionBody implements iPhysicsFactory
{

    public Body useFactory(HashMap _parameters, World _world)
    {
        BodyDef def = new BodyDef();
        def.position = (Vec2)_parameters.get("position");
        def.userData = _parameters.get("userData");
        Body body = _world.createBody(def);
        
        FixtureDef fixture = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.m_radius = (Float)_parameters.get("radius");
        fixture.shape = shape;
        //fixture.isSensor = true;
        fixture.filter.categoryBits = (1 << BodyCategories.eSelfSensor.ordinal());
        /*fixture.filter.maskBits = Integer.MAX_VALUE ^ (
                (1 << BodyCategories.ePlayer.ordinal())
                );*/
        
        body.createFixture(fixture);
        return body;
    }
    
}
