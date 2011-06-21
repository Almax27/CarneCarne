/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;

/**
 *
 * @author alasdair
 */
public class LeftDownSlope extends SlopeTile
{
    public LeftDownSlope(int _id, sLevel.TileType _tileType)
    {
        super(_id, 1, _tileType);
    }
    
    public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid)
    {
        _boundaries[0] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
        _boundaries[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
    }
}
