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
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
public class BlockTile extends RootTile
{
    public BlockTile(int _id, sLevel.TileType _tileType, String _animationsNames[], boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        super(TileShape.eBlock, _id, _tileType, _animationsNames, _regrows, _anchor, _isFlammable, _maxHealth);
    }
    public BlockTile(int _id, sLevel.TileType _tileType, String _animationsNames[], boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth, TileShape _shape)
    {
        super(_shape, _id, _tileType, _animationsNames, _regrows, _anchor, _isFlammable, _maxHealth);
    }
    protected Shape createShape(int _xTile, int _yTile)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f, new Vec2(_xTile,_yTile), 0);
        return shape;
    }
    public Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile)
    {
        Shape shape = createShape(_xTile, _yTile);
        return fixtureWithMaterial(shape, _body, _tile);
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean ULDR[] = new boolean[4];
        
        try
        {
            ULDR[3] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);//(id != 0);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        try
        {
            ULDR[2] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        try
        {
            ULDR[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        try
        {
            ULDR[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        int textureUnit = 0;
        for (int i = 0; i < 4; i++)
        {
            if (!ULDR[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }
}
