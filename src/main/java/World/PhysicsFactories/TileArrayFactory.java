/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import World.PhysicsFactories.iPhysicsFactory;
import Level.CaveInSearcher;
import World.sWorld;
import java.util.ArrayList;
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
public class TileArrayFactory implements iPhysicsFactory
{

    public TileArrayFactory() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        ArrayList<CaveInSearcher.TempTile> tiles = (ArrayList<CaveInSearcher.TempTile>)_parameters.get("tiles");
        Object entity = _parameters.get("entity");
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.fixedRotation = false;
        
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << sWorld.BodyCategories.eSpatTiles.ordinal());
        PolygonShape shape = new PolygonShape();
        fixture.shape = shape;
        fixture.density = 1.0f;
        Body body = _world.createBody(def);
        for (CaveInSearcher.TempTile tile: tiles)
        {
            shape.setAsBox(0.5f, 0.5f, new Vec2(tile.x, tile.y), 0);
            body.createFixture(fixture);
        }
        return body;
    }
    
}
