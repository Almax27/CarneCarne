/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import Entities.AIEntity;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author g203947
 */

public class BoxCharFactory implements iPhysicsFactory
{
     public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        AIEntity entity = (AIEntity)_parameters.get("aIEntity");
        BodyCategories category = (BodyCategories)_parameters.get("category");
        
        BodyDef def = new BodyDef();
        if (!_parameters.containsKey("isDead"))
        {
            def.type = BodyType.DYNAMIC;
        }
        def.userData = entity;
        def.position = position;
        def.fixedRotation = true;
        Body body = _world.createBody(def);
        FixtureDef fixture = new FixtureDef();
        fixture.density = 1.0f;
        fixture.filter.categoryBits = (1 << category.ordinal());
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        fixture.shape = box;       
        body.createFixture(fixture); 
        return body;
    }
}
