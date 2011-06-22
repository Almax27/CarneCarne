/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Level.sLevel;
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
class SpatBlockBodyFactory implements iPhysicsFactory {

    public SpatBlockBodyFactory() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Vec2 velocity = (Vec2)_parameters.get("velocity");
        Entity entity = (Entity)_parameters.get("entity");
        sLevel.TileType tileType = (sLevel.TileType)_parameters.get("tileType");
        CircleShape shape = new CircleShape();
        shape.m_radius = 0.2f;
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.filter.groupIndex = tileType.ordinal();
        fixture.filter.categoryBits = (1 << BodyCategories.eSpatTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE ^ 
                ((1 << BodyCategories.ePlayer.ordinal()));
        if (tileType.equals(sLevel.TileType.eGum))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eGum.ordinal());
            fixture.filter.maskBits = Integer.MAX_VALUE ^ 
                ((1 << BodyCategories.ePlayer.ordinal()));
        }
        fixture.density = 1;
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.fixedRotation = false;
        def.position = new Vec2((position.x),(position.y));
        def.linearVelocity = velocity;
        
        Body body = _world.createBody(def);
        body.createFixture(fixture);
        return body;  
    }
}