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
class MelonSkinTile extends BlockTile {

    public enum SkinDirection
    {
        eUp,
        eRight,
        eDown,
        eLeft,
        eDownRight,
        eDownLeft,
        eUpLeft,
        eUpRight,
        eSkinDirectionsSize,
    }
    private static String[] getAnimationsNames()
    {
        String[] strings = new String[AnimationType.eAnimationsMax.ordinal()];
        strings[AnimationType.eFireHit.ordinal()] = "Default";
        strings[AnimationType.eDamage.ordinal()] = "MelonS";
        strings[AnimationType.eSpawn.ordinal()] = null;
        strings[AnimationType.eSpit.ordinal()] = "Default";
        strings[AnimationType.eJump.ordinal()] = "MelonS";
        return strings;
    }
    final static String mConstAnimations[] = getAnimationsNames();
    SkinDirection mDirection;
    public MelonSkinTile(int _id, TileType type, SkinDirection _direction, int _maxHealth)
    {
        super(_id, type, mConstAnimations, false, true, false, _maxHealth);
        mDirection = _direction;
    }
    
    @Override
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean flags[] = new boolean[3];
        flags[0] = true;
        flags[1] = true;
        flags[2] = true;
        switch (mDirection)
        {
            case eUp:
            {
                flags[0] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                flags[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                break;
            }
            case eRight:
            {
                flags[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                flags[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                break;
            }
            case eDown:
            {
                flags[0] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                flags[1] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                break;
            }
            case eLeft:
            {
                flags[0] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                flags[1] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                break;
            }
            case eUpRight:
            {
                flags[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                break;
            }
            case eUpLeft:
            {
                flags[1] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                break;
            }
            case eDownRight:
            {
                flags[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                break;
            }
            case eDownLeft:
            {
                flags[1] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                flags[2] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                break;
            }
        }
        int value = 0;
        for (int i = 0; i < 3; i++)
        {
            if (!flags[i])
            {
                value += (1 << i);
            }
        }
        if (value != 0)
        {
            _stack.push(_xTile);
            _stack.push(_yTile);
            _stack.push(mId+(value*16));
        }
    }
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        switch (mDirection)
        {
            case eUp:
            {
                return !_direction.equals(Direction.eFromDown);
            }
            case eRight:
            {
                return !_direction.equals(Direction.eFromLeft);
            }
            case eDown:
            {
                return !_direction.equals(Direction.eFromUp);
            }
            case eLeft:
            {
                return !_direction.equals(Direction.eFromRight);
            }
            case eUpRight:
            {
                return !(_direction.equals(Direction.eFromDown)||_direction.equals(Direction.eFromLeft));
            }
            case eUpLeft:
            {
                return !(_direction.equals(Direction.eFromDown)||_direction.equals(Direction.eFromRight));
            }
            case eDownRight:
            {
                return !(_direction.equals(Direction.eFromUp)||_direction.equals(Direction.eFromLeft));
            }
            case eDownLeft:
            {
                return !(_direction.equals(Direction.eFromUp)||_direction.equals(Direction.eFromRight));
            }
        }
        return false;
    }
}
