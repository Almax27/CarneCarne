
package World.PhysicsFactories;

import Entities.AIEntity;
import World.PhysicsFactories.iPhysicsFactory;
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
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

/**
 *
 * @author graham
 */
public class CircleCharFactory  implements iPhysicsFactory
{
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        AIEntity entity = (AIEntity)_parameters.get("aIEntity");
        BodyCategories category = (BodyCategories)_parameters.get("category");
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        //wheelShape.
        circleFixture.density = 1;
        circleFixture.friction = 1;
        circleFixture.filter.categoryBits = (1 << category.ordinal());
        circleFixture.filter.maskBits = Integer.MAX_VALUE;
        circleFixture.shape = wheelShape;
        if (_parameters.containsKey("restitution"))
            circleFixture.restitution = (Float)_parameters.get("restitution"); 
        //circleFixture.
        
        BodyDef def = new BodyDef();
        //def.
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.position = position;
        
        Body body = _world.createBody(def);
        body.createFixture(circleFixture);
        
        def.fixedRotation = true;
        def.userData = null;
        //Body axelBody = _world.createBody(def);
        //body.createFixture(axelFixture);
        
        /*RevoluteJointDef wheelJoint = new RevoluteJointDef();
        wheelJoint.collideConnected = false;
        wheelJoint.maxMotorTorque = 5000.0f;
        wheelJoint.enableMotor = true;
        wheelJoint.bodyA = body;
        wheelJoint.bodyB = axelBody;
        wheelJoint.collideConnected = false;
        RevoluteJoint joint = (RevoluteJoint)_world.createJoint(wheelJoint);
        entity.mJoint = joint;*/
        return body;
    }
    
}
