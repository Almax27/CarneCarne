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
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
public class ExplosionBody implements iPhysicsFactory
{

    public Body useFactory(HashMap _parameters, World _world)
    {
        BodyDef def = new BodyDef();
        def.position = (Vec2)_parameters.get("position");
        def.userData = _parameters.get("userData");
        def.type = BodyType.DYNAMIC;
        
        FixtureDef fixture = new FixtureDef();
        fixture.isSensor = true;
        fixture.filter.categoryBits = (1 << BodyCategories.eFire.ordinal());
        
        CircleShape shape = new CircleShape();
        shape.m_radius = 0.75f;
        
        Body body = _world.createBody(def);
        fixture.shape = shape;
        body.createFixture(fixture);
        return body;
    }
    
}
