/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import World.sWorld;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author alasdair
 */
class WaterTile extends RootTile {

    public WaterTile(int _rootId, TileType _tileType, String _animationsNames[])
    {
        super(TileShape.eBlock, _rootId, _tileType, _animationsNames, false, true, false, -1);
    }

    Body createPhysicsBody(int _xTile, int _yTile, HashMap _parameters)
    {
        _parameters.put("position", new Vec2(_xTile,_yTile));
        return sWorld.useFactory("WaterTileFactory",_parameters);
    }
    @Override
    Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f, new Vec2((_xTile),(_yTile)), 0);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        fixture.userData = _tile;
        fixture.filter.categoryBits = (1 << BodyCategories.eWater.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        if (mTileType.equals(TileType.eAcid))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eAcid.ordinal());
        }
        return _body.createFixture(fixture);
    }

    void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }

    
}
