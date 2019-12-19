/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import Entities.AIEntity;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
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
public class PlayerFactory implements iPhysicsFactory {

    public PlayerFactory()
    {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        AIEntity entity = (AIEntity)_parameters.get("aIEntity");
        BodyCategories category = (BodyCategories)_parameters.get("category");
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        circleFixture.density = 4f;
        circleFixture.friction = 5;
        //circleFixture.restitution = 0.01f;
        circleFixture.filter.categoryBits = (1 << category.ordinal());
        circleFixture.filter.maskBits = Integer.MAX_VALUE;
        circleFixture.shape = wheelShape;
        circleFixture.friction = 5;
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.position = position;
        
        Body body = _world.createBody(def);
        body.createFixture(circleFixture);
        body.setBullet(true);
        def.fixedRotation = true;
        def.userData = null;
        
        return body;
    }
    
}
