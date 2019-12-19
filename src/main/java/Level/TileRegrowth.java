/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.Particles.sParticleManager;
import Level.Tile.Direction;
import World.sWorld;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class TileRegrowth
{
    private static int nonAnimatedRegrowthTime = 120;
    private static int animatedRegrowthTime = 60;
    private LevelTileGrid mTileGrid;
    private int mFrames;
    private LinkedList<RegrowingTile> timingTiles = new LinkedList<RegrowingTile>();
    private PriorityQueue<RegrowingTile> respawningTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    private LinkedList<RegrowingTile> blockedTiles = new LinkedList<RegrowingTile>();
    //private PriorityQueue<RegrowingTile> activeTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    private RegrowingTile[][] inactiveTiles;
    public TileRegrowth(TileGrid _tileGrid, int _width, int _height)
    {
        try
        {
            mTileGrid = (LevelTileGrid)_tileGrid;
        }
        catch (ClassCastException e)
        {
            mTileGrid = null;
        }
        mFrames = 0;
        inactiveTiles = new RegrowingTile[_width][_height];
    }
    void add(int _x, int _y, RootTile _rootId)
    {
        //mTileGrid.tiledMap.createAnimatedTile(_x, _y, _rootId.mAnimationsName + "GrowBack");
        RegrowingTile tile = new RegrowingTile(_x,_y, _rootId ,mFrames+nonAnimatedRegrowthTime);
        boolean supported = false;
        try
        {
            supported = isSupported(tile);
        }
        catch(NullPointerException e)
        {
            
        }
        if (supported)
        {
            timingTiles.addLast(new RegrowingTile(_x,_y, _rootId ,mFrames+nonAnimatedRegrowthTime));
        }
        else
        {
            inactiveTiles[tile.x][tile.y] = tile;
        }
    }
    public void update()
    {
        mFrames++;
        RegrowingTile tile = null;
        try
        {
            tile = timingTiles.getFirst();
        }
        catch (NoSuchElementException e)
        {
        }
        if (tile != null && tile.timer < mFrames)
        {
            timingTiles.removeFirst();
            if (isSupported(tile))
            {
                tile.timer = mFrames + animatedRegrowthTime;
                respawningTiles.add(tile);
                /*if (tile.mRootId.getAnimationsName(RootTile.AnimationType.eSpawn).equals("Default")) /// FIXME remove this
                    mTileGrid.tiledMap.createAnimatedTile(tile.x, tile.y,"Meat" + "GrowBack");
                else*/
                    mTileGrid.tiledMap.createAnimatedTile(tile.x, tile.y,tile.mRootId.getAnimationsName(RootTile.AnimationType.eSpawn) + "GrowBack");
            }
            else
            {
                inactiveTiles[tile.x][tile.y] = tile;
            }
        }
        tile = respawningTiles.peek();
        if (tile != null && tile.timer < mFrames)
        {
            respawningTiles.remove(tile);
            if (isSupported(tile))
            {
                Vec2 bottomLeft = new Vec2(tile.x, tile.y);
                AABB aabb = new AABB(bottomLeft.add(new Vec2(0.05f,0)), bottomLeft.add(new Vec2(0.95f,0)));
                if (sWorld.searchAABB(aabb) == null)
                {
                    if (mTileGrid.mTiles[tile.x][tile.y].mRootId.mId == 0)
                    {
                        placeTile(tile);
                        sParticleManager.createSystem(tile.mRootId.getAnimationsName(RootTile.AnimationType.eSpawn) + "SpawnParticle", new Vec2(32.0f+(tile.x*64.0f), 32.0f+(tile.y*64.0f)), 1);
                    }
                }
                else
                {
                    blockedTiles.add(tile);
                }
            }
            else
            {
                inactiveTiles[tile.x][tile.y] = tile;
            }
            mTileGrid.tiledMap.destroyAnimatedTile(tile.x, tile.y);
        }
        
        for(Iterator<RegrowingTile> iter = blockedTiles.iterator(); iter.hasNext(); )
        {
            RegrowingTile blockedTile = iter.next();
            Vec2 bottomLeft = new Vec2(blockedTile.x, blockedTile.y);
            AABB aabb = new AABB(bottomLeft.add(new Vec2(0.05f,0)), bottomLeft.add(new Vec2(0.95f,0)));
            if (sWorld.searchAABB(aabb) == null)
            {
                placeTile(blockedTile);
                String name = blockedTile.mRootId.getAnimationsName(RootTile.AnimationType.eSpawn) + "SpawnParticle";
                sParticleManager.createSystem(name, new Vec2(32.0f+(blockedTile.x*64.0f), 32.0f+(blockedTile.y*64.0f)), 1);
                iter.remove();
            }
        }
    }
    private void placeTile(RegrowingTile _tile)
    {
        if (_tile.mRootId.getNext() == null)
            mTileGrid.placeTile(_tile.x, _tile.y, _tile.mRootId.mId);
        else
            mTileGrid.placeTile(_tile.x, _tile.y, _tile.mRootId.getNext().mId);
        checkInactive(_tile.x-1,_tile.y);
        checkInactive(_tile.x+1,_tile.y);
        checkInactive(_tile.x,_tile.y-1);
        checkInactive(_tile.x,_tile.y+1);
    }
    private void checkInactive(int _x, int _y)
    {
        RegrowingTile tile = inactiveTiles[_x][_y];
        if (tile != null)
        {
            inactiveTiles[_x][_y] = null;
            tile.timer = mFrames + nonAnimatedRegrowthTime;
            timingTiles.addLast(tile);
            //placeTile(tile);
        }
    }
    private boolean isSupported(RegrowingTile _tile)
    {
        if (_tile.mRootId.mAnchor)
        {
            return true;
        }
        return ((mTileGrid.boundaryFrom(_tile.x, _tile.y-1, Direction.eFromDown, _tile.mRootId.mTileType) &&
                    _tile.mRootId.boundaryFrom(Direction.eFromUp, _tile.mRootId.getTileType(), MaterialEdges.AnchorEdges)) ||
                ((mTileGrid.boundaryFrom(_tile.x-1, _tile.y, Direction.eFromRight, _tile.mRootId.mTileType) &&
                    _tile.mRootId.boundaryFrom(Direction.eFromLeft, _tile.mRootId.getTileType(), MaterialEdges.AnchorEdges))) ||
                ((mTileGrid.boundaryFrom(_tile.x, _tile.y+1, Direction.eFromUp, _tile.mRootId.mTileType) &&
                    _tile.mRootId.boundaryFrom(Direction.eFromDown, _tile.mRootId.getTileType(), MaterialEdges.AnchorEdges))) ||
                ((mTileGrid.boundaryFrom(_tile.x+1, _tile.y, Direction.eFromLeft, _tile.mRootId.mTileType) && 
                    _tile.mRootId.boundaryFrom(Direction.eFromRight, _tile.mRootId.getTileType(), MaterialEdges.AnchorEdges))));
    }
    private class RegrowingTile {

        public RegrowingTile(int _x, int _y, RootTile _rootId, int _timer)
        {
            x = _x;
            y = _y;
            mRootId = _rootId;
            timer = _timer;
        }
        int x, y;
        RootTile mRootId;
        int timer;
    }

    private static class TileComparer implements Comparator<RegrowingTile> 
    {
        public int compare(RegrowingTile x, RegrowingTile y)
        {
            if (x.timer < y.timer)
            {
                return -1;
            }
            if (x.timer > y.timer)
            {
                return 1;
            }
            return 0;
        }
    }
}
