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
public class EdgeTile extends BlockTile
{
    Direction mDirection;
    EdgeTile(int _id, sLevel.TileType _tileType, Direction _direction, String _animationsNames[], boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        super(_id, _tileType, _animationsNames, _regrows, _anchor, _isFlammable, _maxHealth, TileShape.eEdge);
        mDirection = _direction;
    }

    public @Override
    void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean edge = true;
        switch (mDirection)
        {
            case eFromLeft:
            {
                edge = _tileGrid.boundaryFrom(_xTile+1, _yTile, mDirection, mTileType);
                break;
            }
            case eFromRight:
            {
                edge = _tileGrid.boundaryFrom(_xTile-1, _yTile, mDirection, mTileType);
                break;
            }
            case eFromDown:
            {
                edge = _tileGrid.boundaryFrom(_xTile, _yTile-1, mDirection, mTileType);
                break;
            }
            case eFromUp:
            {
                edge = _tileGrid.boundaryFrom(_xTile, _yTile+1, mDirection, mTileType);
                break;
            }
            default:
                assert(false);
        }
        int textureUnit = (edge)? 0:1;
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
    @Override
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        if (mDirection.ordinal() == (_direction.ordinal()+2)%4)
        {
            return _materialEdges.check(_tileType, mTileType);
        }
        return false;
    }
    @Override
    protected Shape createShape(int _xTile, int _yTile)
    {
        PolygonShape shape = new PolygonShape();
        switch (mDirection)
        {
            case eFromLeft:
            {
                shape.setAsBox(0.25f, 0.5f, new Vec2(_xTile+0.25f,_yTile), 0.0f);
                break;
            }
            case eFromRight:
            {
                shape.setAsBox(0.25f, 0.5f, new Vec2(_xTile-0.25f,_yTile), 0.0f);
                break;
            }
            case eFromDown:
            {
                shape.setAsBox(0.5f, 0.25f, new Vec2(_xTile,_yTile-0.25f), 0.0f);
                break;
            }
            case eFromUp:
            {
                shape.setAsBox(0.5f, 0.25f, new Vec2(_xTile,_yTile+0.25f), 0.0f);
                break;
            }
            default:
                assert(false);
        }
        return shape;
    }
}
