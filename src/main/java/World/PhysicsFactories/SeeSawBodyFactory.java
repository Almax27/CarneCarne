/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import Entities.SeeSaw;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

/**
 *
 * @author alasdair
 */
public class SeeSawBodyFactory implements iPhysicsFactory {

    public SeeSawBodyFactory()
    {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Body body;
        SeeSaw userData = (SeeSaw)_parameters.get("userData");
        Vec2 position = (Vec2)_parameters.get("position");
        Vec2 dimensions = (Vec2)_parameters.get("dimensions");
        {
            BodyDef def = new BodyDef();
            def.type = BodyType.DYNAMIC;
            def.userData = userData;
            def.position = position;
            body = _world.createBody(def);
            FixtureDef fixture = new FixtureDef();
            fixture.density = 1.0f;
            fixture.filter.categoryBits = (1 << sWorld.BodyCategories.eSpatTiles.ordinal());
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(dimensions.x*0.5f, dimensions.y*0.5f);
            fixture.shape = shape;       
            body.createFixture(fixture);
        }
        {
            BodyDef def = new BodyDef();
            def.position = position;
            Body axel = _world.createBody(def);
            FixtureDef fixture = new FixtureDef();
            fixture.isSensor = true;
            fixture.filter.categoryBits = (1 << sWorld.BodyCategories.eSpatTiles.ordinal());
            CircleShape shape = new CircleShape();
            shape.m_radius = 0.1f;
            fixture.shape = shape;
            body.createFixture(fixture);
            
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.initialize(body, axel, position);
            jointDef.maxMotorTorque = 10.0f;
            jointDef.enableMotor = true;
            jointDef.enableLimit = true;
            jointDef.upperAngle = 0.6f;
            jointDef.lowerAngle = -0.6f;
            userData.setJoint((RevoluteJoint)_world.createJoint(jointDef));
        }
        return body;
    }
    
}
