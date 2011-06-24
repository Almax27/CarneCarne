/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Stack;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author A203946
 */
public class TileGrid {
    
    private Tile[][] mTiles;
    private TiledMap tiledMap;
    private RootTileList rootTiles;
    private int layerIndex;
    //private static PriorityQueue<RegrowingTile> regrowingTiles;
    private TileRegrowth regrowingTiles;
    public TileGrid(TiledMap _tiledMap, RootTileList _rootTiles, int _layerIndex)
    {
        tiledMap = _tiledMap;
        rootTiles = _rootTiles;
        layerIndex = _layerIndex;
        WaterSearcher waterSearcher = new WaterSearcher(_tiledMap.getWidth(),_tiledMap.getHeight());
        mTiles = new Tile[_tiledMap.getWidth()][_tiledMap.getHeight()];
        for (int i = 0; i < _tiledMap.getWidth(); i++)
        {
            for (int ii = 0; ii < _tiledMap.getHeight(); ii++)
            {
                int id = _tiledMap.getTileId(i, ii, layerIndex);
                if (_rootTiles.get(id).mTileType.equals(TileType.eWater))
                {
                    waterSearcher.addTile(i,ii,_rootTiles.get(id));
                    //mTiles[i][ii] = new Tile(id,_rootTiles.get(id));
                    //mTiles[i][ii].createPhysicsBody(i, ii);
                }
                else
                {
                    mTiles[i][ii] = new Tile(id,_rootTiles.get(id));
                    mTiles[i][ii].createPhysicsBody(i, ii);
                }
            }
        }
        waterSearcher.finish(mTiles);
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < mTiles.length; i ++)
        {
            for (int ii = 0; ii < mTiles[0].length; ii++)
            {
                if (i > 0 && ii > 0 && i < _tiledMap.getWidth()-1 && ii <_tiledMap.getHeight()-1)
                {
                    mTiles[i][ii].checkEdges(i, ii, stack, this);
                }
            }
        }
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            _tiledMap.setTileId(xTile, yTile, _layerIndex, id);
        }
        //regrowingTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
        regrowingTiles = new TileRegrowth(this,_tiledMap.getWidth(),_tiledMap.getHeight());
    }

    boolean damageTile(int _x, int _y)
    {
        Tile tile = mTiles[_x][_y];
        if (tile.mHealth > 1)
        {
            tile.mId += 16;
            tile.mHealth--;
            tile.mRootId = rootTiles.get(tile.mId);
            Stack<Integer> stack = new Stack<Integer>();
            mTiles[_x][_y].checkEdges(_x,_y, stack, this);
            while (!stack.empty())
            {
                int id = stack.pop();
                int yTile = stack.pop();
                int xTile = stack.pop();
                tiledMap.setTileId(xTile, yTile, layerIndex, id);
            }
            return false;
        }
        return true;
    }
    void update()
    {
        regrowingTiles.update();
    }
    public void placeTile(int _x, int _y, int _rootId)
    {
        mTiles[_x][_y].mId = _rootId;
        mTiles[_x][_y].mRootId = rootTiles.get(_rootId);
        mTiles[_x][_y].createPhysicsBody(_x, _y);
        int x;
        int y;
        Stack<Integer> stack = new Stack<Integer>();

        x = _x;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x-1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x+1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y-1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y+1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            tiledMap.setTileId(xTile, yTile, layerIndex, id);
        }
    }
    void set(int _x, int _y, int _gid)
    {
        tiledMap.setTileId(_x, _y, layerIndex, _gid);
        mTiles[_x][_y].mId = _gid;
        mTiles[_x][_y].mRootId = rootTiles.get(_gid);
        
    }
    public void destroyTile(int _x, int _y)
    {
        Stack<Integer> stack = new Stack<Integer>();
        
        if (mTiles[_x][_y].mRootId.mRegrows)
            regrowingTiles.add(_x,_y, mTiles[_x][_y].mRootId);
        
        set(_x,_y,0);
        
        int x;
        int y;        
        
        x = _x-1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x+1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y-1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y+1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            tiledMap.setTileId(xTile, yTile, layerIndex, id);
        }
        CaveInSearcher search = new CaveInSearcher(this, tiledMap, layerIndex);
        search.destroy(_x, _y);
    }
    
    public Tile get(int _x, int _y)
    {
        return mTiles[_x][_y];
    }
    private static MaterialEdges mMaterialEdges = new MaterialEdges();
    boolean boundaryFrom(int _xTile, int _yTile, Direction _direction, TileType _tileType)
    {
        Tile tile = get(_xTile, _yTile);
        return tile.mRootId.boundaryFrom(_direction, _tileType, mMaterialEdges);
        /*TileShape shape = tile.mRootId.mShape;
        switch (shape)
        {
            case eEmpty:
            {
                return false;
            }
            case eBlock:
            {
                //return mMaterialEdges.check(_tileType, tile.mRootId.mTileType);
                return tile.mRootId.boundaryFrom(_direction, _tileType, mMaterialEdges);
            }
            case eSlope:
            {
                return tile.mRootId.boundaryFrom(_direction, _tileType, mMaterialEdges);
                //return mMaterialEdges.check(_tileType, tile.mRootId.mTileType);
            }
            case eUndefined:
            {
                return false;
            }
        }*/
        /*int slope = tile.mRootId.mSlopeType;
        switch (slope)
        {
            case 0:
            {
                if (_direction == Direction.eFromDown || _direction == Direction.eFromLeft)
                {
                    return mMaterialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
            case 1:
            {
                if (_direction == Direction.eFromDown || _direction == Direction.eFromRight)
                {
                    return mMaterialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
            case 2:
            {
                if (_direction == Direction.eFromUp || _direction == Direction.eFromRight)
                {
                    return mMaterialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
            case 3:
            {
                if (_direction == Direction.eFromUp || _direction == Direction.eFromLeft)
                {
                    return mMaterialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
        }*/
        //return false;
    }
}
