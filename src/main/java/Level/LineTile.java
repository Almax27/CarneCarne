/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Stack;

/**
 *
 * @author alasdair
 */
public class LineTile extends BlockTile
{
    Direction mDirection;
    LineTile(int _id, sLevel.TileType _tileType, Direction _direction, String _animationsNames[], boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        super(_id, _tileType, _animationsNames, _regrows, _anchor, _isFlammable, _maxHealth, TileShape.eEdge);
        mDirection = _direction;
    }
    
    public @Override
    void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean edges[] = new boolean[2];
        edges[1] = true;
        switch (mDirection)
        {
            case eFromLeft:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                break;
            }
            case eFromRight:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                break;
            }
            case eFromDown:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                break;
            }
            case eFromUp:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                break;
            }
            default:
                assert(false);
        }
        int textureUnit = 0;
        for (int i = 0; i < 2; i++)
        {
            if (!edges[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
    @Override
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        if (_direction.equals(mDirection) || _direction.ordinal() == (mDirection.ordinal()+2)%4)
        {
            return _materialEdges.check(_tileType, mTileType);
        }
        return false;
    }
}
