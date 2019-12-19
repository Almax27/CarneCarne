/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.Particles.ParticleSysBase;
import Level.Tile.Direction;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
abstract public class TileGrid {
    
    Tile[][] mTiles;
    RootTileList rootTiles;
    TileRegrowth regrowingTiles;
    TileFire mTileFire;
    private int mWidth, mHeight;
    Body mBody;
    private Stack<TileGrid> mCavedInBodies = new Stack<TileGrid>();
    abstract int getTileId(int _x, int _y);
    abstract void setTileId(int _x, int _y, int _id);
    abstract void createPhysicsBody(int _x, int _y, Tile _tile);
    abstract void caveInSearch(int _x, int _y, TileType _tileType);
    abstract public void destroyTile(int _x, int _y);
    public TileGrid(RootTileList _rootTiles, int _width, int _height)
    {
        rootTiles = _rootTiles;
        mTiles = new Tile[_width][_height];
        mTileFire = new TileFire(this);
        mWidth = _width;
        mHeight = _height;
    }
    
    public Body getBody()
    {
        return mBody;
    }
    abstract void dropCheck(int _x, int _y);
    protected void init(HashMap _parameters)
    {
        mBody = sWorld.useFactory("TileFactory", _parameters);
        WaterSearcher waterSearcher = new WaterSearcher(mWidth,mHeight, this);
        for (int i = 0; i < mWidth; i++)
        {
            for (int ii = 0; ii < mHeight; ii++)
            {
                int id = getTileId(i, ii);
                if (id == 1)
                {
                    id = 1;
                }
                if (rootTiles.get(id).mTileType.equals(TileType.eWater)||
                        rootTiles.get(id).mTileType.equals(TileType.eAcid))
                {
                    waterSearcher.addTile(i,ii,rootTiles.get(id));
                }
                else
                {
                    mTiles[i][ii] = new Tile(id,rootTiles.get(id),this,i,ii);
                    createPhysicsBody(i, ii, mTiles[i][ii]);
                    //mTiles[i][ii].createPhysicsBody(i, ii);
                }
            }
        }
        waterSearcher.finish(mTiles);
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < mTiles.length; i ++)
        {
            for (int ii = 0; ii < mTiles[0].length; ii++)
            {
                if (i > 0 && ii > 0 && i < mWidth-1 && ii < mHeight-1)
                {
                    mTiles[i][ii].checkEdges(stack, this);
                }
            }
        }
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            setTileId(xTile, yTile, id);
        }
        regrowingTiles = new TileRegrowth(this,mWidth,mHeight);
    }

    public boolean damageTile(int _x, int _y)
    {
        Tile tile = mTiles[_x][_y];
        if (tile.mHealth > 1)
        {
            tile.mId += 16;
            tile.mHealth--;
            tile.mRootId = rootTiles.get(tile.mId);
            Stack<Integer> stack = new Stack<Integer>();
            mTiles[_x][_y].checkEdges(stack, this);
            while (!stack.empty())
            {
                int id = stack.pop();
                int yTile = stack.pop();
                int xTile = stack.pop();
                setTileId(xTile, yTile, id);
            }
            return false;
        }
        return true;
    }
    abstract void update();
    
    public void placeTile(int _x, int _y, int _rootId)
    {
        setTileId(_x, _y, _rootId); /// Watch out, this is new and may break things 14/Jul/11
        placeTileNoBody(_x, _y, _rootId);
        createPhysicsBody(_x, _y, mTiles[_x][_y]);
    }
    void placeTileNoBody(int _x, int _y, int _rootId)
    {
        mTiles[_x][_y].mId = _rootId;
        mTiles[_x][_y].mRootId = rootTiles.get(_rootId);
        mTiles[_x][_y].mHealth = rootTiles.get(_rootId).mMaxHealth;
        int x;
        int y;
        Stack<Integer> stack = new Stack<Integer>();

        x = _x;
        y = _y;
        mTiles[x][y].checkEdges(stack, this);
        x = _x-1;
        y = _y;
        mTiles[x][y].checkEdges(stack, this);
        x = _x+1;
        y = _y;
        mTiles[x][y].checkEdges(stack, this);
        x = _x;
        y = _y-1;
        mTiles[x][y].checkEdges(stack, this);
        x = _x;
        y = _y+1;
        mTiles[x][y].checkEdges(stack, this);
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            setTileId(xTile, yTile, id);
        }
    }
    void set(int _x, int _y, int _gid)
    {
        setTileId(_x, _y, _gid);
        mTiles[_x][_y].mId = _gid;
        mTiles[_x][_y].mRootId = rootTiles.get(_gid);
    }
    protected void destroyTileImplementation(int _x, int _y)
    {
        Stack<Integer> stack = new Stack<Integer>();
        TileType tileType = mTiles[_x][_y].getTileType();
        if (mTiles[_x][_y].mRootId.mRegrows)
            regrowingTiles.add(_x,_y, mTiles[_x][_y].mRootId);
        //mBody.destroyFixture(mTiles[_x][_y].mFixture);
        
        set(_x,_y,0);
        
        int x;
        int y;        
        
        x = _x-1;
        y = _y;
        try
        {
            mTiles[x][y].checkEdges(stack, this);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
        x = _x+1;
        y = _y;
        
        try
        {
            mTiles[x][y].checkEdges(stack, this);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
        x = _x;
        y = _y-1;
        
        try
        {
            mTiles[x][y].checkEdges(stack, this);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
        x = _x;
        y = _y+1;
        
        try
        {
            mTiles[x][y].checkEdges(stack, this);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            setTileId(xTile, yTile, id);
        }
        
        dropCheck(_x, _y-1);
        caveInSearch(_x, _y, tileType);
    }
    
    public Tile get(int _x, int _y)
    {
        return mTiles[_x][_y];
    }
    boolean boundaryFrom(int _xTile, int _yTile, Direction _direction, TileType _tileType)
    {
        Tile tile = get(_xTile, _yTile);
        if(tile == null)
            return false;
        return tile.mRootId.boundaryFrom(_direction, _tileType, MaterialEdges.GraphicalEdges);
    }

    void destroy()
    {
        while (!mCavedInBodies.isEmpty())
        {
            //mCavedInBodies.pop().destroy();
            break;
        }
        while (!mParticles.empty())
        {
            mParticles.pop().kill();
        }
        sWorld.destroyBody(mBody);
    }

    void addCaveIn(TileGrid _newTileGrid)
    {
        mCavedInBodies.add(_newTileGrid);
    }
    
    Stack<ParticleSysBase> mParticles = new Stack<ParticleSysBase>();
    void addParticles(ParticleSysBase _particle)
    {
        mParticles.add(_particle);
    }

    public void addRegrowingTile(int x, int y, int _rootId)
    {
        regrowingTiles.add(x, y, rootTiles.get(_rootId));
    }
}
