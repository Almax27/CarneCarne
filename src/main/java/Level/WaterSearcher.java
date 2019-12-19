/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import World.sWorld;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author alasdair
 */
class WaterSearcher {
    
    private class WaterTile
    {
        int x, y;
        RootTile id;
        WaterTile(int _x, int _y, RootTile _id)
        {
            x = _x;
            y = _y;
            id = _id;
        }
    }
    private boolean flags[][];
    private LinkedList<WaterTile> tiles;
    private TileGrid mTileGrid;
    public WaterSearcher(int _width, int _height, TileGrid _tileGrid)
    {
        mTileGrid = _tileGrid;
        flags = new boolean[_width][_height];
        for (int i = 0; i < _width; i++)
            for (int ii = 0; ii < _height; ii++)
                flags[i][ii] = false;;
        tiles = new LinkedList<WaterTile>();
    }
    public void addTile(int _x, int _y, RootTile _id)
    {
        tiles.add(new WaterTile(_x, _y, _id));
        flags[_x][_y] = true;
    }
    public void finish(Tile[][] _tiles)
    {
        for(WaterTile tile : tiles)
        {
            if (flags[tile.x][tile.y] == true)
            {
                path(tile, _tiles);
            }
        }
    }
    private class TileNode
    {
        int x, y;
        public TileNode(int _x, int _y)
        {
            x = _x;
            y = _y;
        }
    }
    private void path(WaterTile _tile, Tile[][] _tiles)
    {
        Stack<TileNode> workingSet = new Stack<TileNode>();
        Stack<TileNode> currentBody = new Stack<TileNode>();
        int highestSurface = 100000;
        workingSet.push(new TileNode(_tile.x, _tile.y));
        while (!workingSet.isEmpty())
        {
            TileNode node = workingSet.pop();
            
            int x,y;
            
            x = node.x-1;
            y = node.y;
            if (flags[x][y])
            {
                flags[x][y] = false;
                workingSet.push(new TileNode(x,y));
                currentBody.push(new TileNode(x,y));
                if (y < highestSurface)
                {
                    highestSurface = y;
                }
            }
            x = node.x+1;
            y = node.y;
            if (flags[x][y])
            {
                flags[x][y] = false;
                workingSet.push(new TileNode(x,y));
                currentBody.push(new TileNode(x,y));
                if (y < highestSurface)
                {
                    highestSurface = y;
                }
            }
            x = node.x;
            y = node.y-1;
            if (flags[x][y])
            {
                flags[x][y] = false;
                workingSet.push(new TileNode(x,y));
                currentBody.push(new TileNode(x,y));
                if (y < highestSurface)
                {
                    highestSurface = y;
                }
            }
            x = node.x;
            y = node.y+1;
            if (flags[x][y])
            {
                flags[x][y] = false;
                workingSet.push(new TileNode(x,y));
                currentBody.push(new TileNode(x,y));
                if (y < highestSurface)
                {
                    highestSurface = y;
                }
            }
        }
        /*HashMap params = new HashMap();
        params.put("Tiles", currentBody);
        params.put("TileType", _tile.id);
        sWorld.useFactory("WaterTileFactory", params);*/
        while (!currentBody.isEmpty())
        {
            TileNode node = currentBody.pop();
            int x = node.x;
            int y = node.y;
            _tiles[x][y] = new Tile(_tile.id.mId,_tile.id,mTileGrid,x,y);
            _tiles[x][y].setWaterHeight(highestSurface);
            _tiles[x][y].createPhysicsBody(mTileGrid.mBody);
        }
    }
}
