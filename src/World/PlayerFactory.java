/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

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
class PlayerFactory implements iPhysicsFactory {

    public PlayerFactory() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        AIEntity entity = (AIEntity)_parameters.get("aIEntity");
        BodyCategories category = (BodyCategories)_parameters.get("category");
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        circleFixture.density = 4;
        circleFixture.friction = 5;
        circleFixture.filter.categoryBits = (1 << category.ordinal());
        circleFixture.filter.maskBits = Integer.MAX_VALUE;
        circleFixture.shape = wheelShape;
        PolygonShape axelShape = new PolygonShape();
        FixtureDef axelFixture = new FixtureDef();
        axelShape.setAsBox(0.1f, 0.1f);
        axelFixture.density = 0.0001f;
        axelFixture.filter.categoryBits = (1 << category.ordinal());
        axelFixture.filter.maskBits = Integer.MAX_VALUE;
        axelFixture.filter.groupIndex = -100;
        axelFixture.shape = axelShape;
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.position = position;
        
        Body body = _world.createBody(def);
        body.createFixture(circleFixture);
        
        def.fixedRotation = true;
        def.userData = null;
        
        return body;
    }
    
}