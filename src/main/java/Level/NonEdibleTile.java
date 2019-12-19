/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Stack;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class NonEdibleTile extends BlockTile
{
    private static String[] getAnimationsNames()
    {
        String[] strings = new String[AnimationType.eAnimationsMax.ordinal()];
        strings[AnimationType.eFireHit.ordinal()] = "Default";
        strings[AnimationType.eDamage.ordinal()] = null;
        strings[AnimationType.eSpawn.ordinal()] = null;
        strings[AnimationType.eSpit.ordinal()] = "Default";
        strings[AnimationType.eJump.ordinal()] = "Hard";
        return strings;
    }
    final static String mConstAnimations[] = getAnimationsNames();
    public NonEdibleTile(int _id, sLevel.TileType _tileType, boolean _anchor)
    {
        super(_id, _tileType, mConstAnimations, false, _anchor, false, 1, TileShape.eBlock);
    }
    @Override
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
    }
    @Override
    protected Shape createShape(int _xTile, int _yTile)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f, new Vec2(_xTile,_yTile), 0);
        return shape;
    }
    @Override
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }
}
