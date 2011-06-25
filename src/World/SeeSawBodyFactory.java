/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

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
 * @author alasdair
 */
class SeeSawBodyFactory implements iPhysicsFactory {

    public SeeSawBodyFactory()
    {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Object userData = _parameters.get("userData");
        Vec2 position = (Vec2)_parameters.get("position");
        Vec2 dimensions = (Vec2)_parameters.get("dimensions");
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = userData;
        def.position = position;
        Body body = _world.createBody(def);
        FixtureDef fixture = new FixtureDef();
        fixture.density = 1.0f;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(dimensions.x*0.5f, dimensions.y*0.5f);
        fixture.shape = shape;
        body.createFixture(fixture);
        return body;
    }
    
}
