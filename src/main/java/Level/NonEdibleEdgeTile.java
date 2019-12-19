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
class NonEdibleEdgeTile extends EdgeTile
{
    float mBodyScale;
    public NonEdibleEdgeTile(int _id, TileType _tileType, Direction _direction, String _animationsNames[], float _bodyScale)
    {
        super(_id, _tileType, _direction, _animationsNames, false, true, false, -1);
        mBodyScale = _bodyScale;
    }
    
    public @Override
    void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }
    
}
