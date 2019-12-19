/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import World.PhysicsFactories.iPhysicsFactory;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
class CheckPointFactory implements iPhysicsFactory {

    public CheckPointFactory() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        /*Vec2 position = (Vec2)_parameters.get("position");
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << BodyCategories.eCheckPoint.ordinal());
        fixture.filter.maskBits = (1 << BodyCategories.ePlayer.ordinal());
        fixture.shape = shape;
        //fixture.isSensor = true;
        BodyDef def = new BodyDef();
        //def.userData = _entity;
        def.position = new Vec2((position.x),(position.y));
        
        Body body = _world.createBody(def);
        body.createFixture(fixture);
        return body;  */
        return null;
    }
}
