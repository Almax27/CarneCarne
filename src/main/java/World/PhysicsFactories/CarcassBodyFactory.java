/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import Entities.Entity;
import Graphics.Particles.sParticleManager;
import Level.Tile;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;

/**
 *
 * @author alasdair
 */
public class CarcassBodyFactory implements iPhysicsFactory
{
    Vec2 mSpikeDirections[];

    public CarcassBodyFactory()
    {
        mSpikeDirections = new Vec2[4];
        mSpikeDirections[0] = new Vec2(0,-1);
        mSpikeDirections[1] = new Vec2(1,0);
        mSpikeDirections[2] = new Vec2(0,1);
        mSpikeDirections[3] = new Vec2(-1,0);
    }
    
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Object userData = _parameters.get("userData");
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        circleFixture.density = 1;
        circleFixture.friction = 1;
        circleFixture.restitution = 0.1f;
        circleFixture.filter.categoryBits = (1 << BodyCategories.eCarcass.ordinal());
        circleFixture.filter.maskBits = Integer.MAX_VALUE;
        circleFixture.shape = wheelShape;
        
        BodyDef def = new BodyDef();
        def.angle = (Float)_parameters.get("rotation");
        Entity.CauseOfDeath cause = (Entity.CauseOfDeath)_parameters.get("causeOfDeath");
        switch (cause)
        {
            case eAcid:
            case eFire:
            {
                def.type = BodyType.DYNAMIC;
                break;
            }
            case eSpikes:
            {
                def.type = BodyType.DYNAMIC;
                Tile tile = (Tile)((Fixture)_parameters.get("attachment")).getUserData();
                def.angle = -tile.getRootTile().getSlopeType()*90.0f/(180.0f/(float)Math.PI);
                break;
            }
        }
        def.userData = userData;
        def.position = position;
        def.fixedRotation = false;
        def.linearVelocity = (Vec2)_parameters.get("linearVelocity");
        def.angularVelocity = (Float)_parameters.get("angularVelocity");
        def.inertiaScale = 1.0f;
        
        Body body = _world.createBody(def);
        body.createFixture(circleFixture);
        
        if (cause.equals(Entity.CauseOfDeath.eFire))
        {
            sParticleManager.createMovingSystem("Burnt", 1, body, new Vec2(0.0f,0.0f), new Vec2(0.5f,0.5f));
        }

        if (cause.equals(Entity.CauseOfDeath.eSpikes))
        {
            PrismaticJointDef jointDef = new PrismaticJointDef();
            Fixture attachment = (Fixture)_parameters.get("attachment");
            Tile tile = (Tile)attachment.getUserData();
            int direction = tile.getRootTile().getSlopeType();
            Vec2 anchor = tile.getWorldPosition();
            jointDef.initialize(body, attachment.getBody(), anchor/*.sub(mSpikeDirections[direction].mul(0.5f))*/, mSpikeDirections[direction]);
            jointDef.lowerTranslation = 0.0f;
            jointDef.upperTranslation = 0.5f;
            if (direction == 0)
            {
                //jointDef.lowerTranslation = -0.5f;
                //jointDef.upperTranslation = 0.5f;
            }
            jointDef.enableLimit = true;
            
            jointDef.maxMotorForce = 10.0f;
            jointDef.motorSpeed = 0.0f;
            jointDef.enableMotor = false;
            
            PrismaticJoint joint = (PrismaticJoint)_world.createJoint(jointDef);
            //joint.setMaxMotorForce((joint.getJointTranslation())*10.0f);
            
            float angle = (float)Math.atan2(-def.linearVelocity.x, def.linearVelocity.y);
            sParticleManager.createMovingSystem("SpikeDeath", 1.0f, body, new Vec2(0,0), new Vec2(0.5f,0.5f)).setAngularOffset(angle*180.0f/(float)Math.PI);
        }
        return body;
    }
    
}
