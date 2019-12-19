/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import World.PhysicsFactories.iPhysicsFactory;
import Entities.Entity;
import World.sWorld;
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
public class FireParticleBody implements iPhysicsFactory
{

    public FireParticleBody() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Vec2 velocity = (Vec2)_parameters.get("velocity");
        Entity entity = (Entity)_parameters.get("userData");
        FixtureDef fixture = new FixtureDef();
        fixture.isSensor = true;
        CircleShape shape = new CircleShape();
        shape.m_radius = 0.25f;
        fixture.shape = shape;
        fixture.filter.categoryBits = (1 << sWorld.BodyCategories.eFire.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        fixture.isSensor = true;
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.linearVelocity = velocity;
        def.position = position;
        Body body = _world.createBody(def);
        try{
        body.createFixture(fixture);
        } catch (NullPointerException e) /// FIXME figure out why this happens
        {
            e = null;
        }
        return body;
    }

}
