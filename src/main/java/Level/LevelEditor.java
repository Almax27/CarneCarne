/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Sound.sSound;
import World.sWorld;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class LevelEditor {
    
    private TiledMap mTiledMap;
    int layerIndex;
    RootTileList rootTiles;
    TileGrid tileGrid;
    FlagProcessor mFlagProcessor;
    
    public LevelEditor(TiledMap _tiledMap)
    {
        //Why??? done in new level - deek made an oopsy
//        mTiledMap = _tiledMap;
//        layerIndex = mTiledMap.getLayerIndex("Level");
//        rootTiles = new RootTileList(mTiledMap);
//        tileGrid = new LevelTileGrid(mTiledMap, rootTiles, layerIndex);
//        mFlagProcessor = new FlagProcessor(mTiledMap, layerIndex, tileGrid.mBody, tileGrid);  //Why??? done in new level - deek made an oopsy
        
    }
    void init()
    {
        if(mFlagProcessor != null)
        {
            mFlagProcessor.run();
            mFlagProcessor = null;
        }
    }
    void newLevel(TiledMap _tiledMap)
    {
        //cleanup old level
        if(tileGrid != null)
                tileGrid.destroy();
        sWorld.destroy();
        sSound.clearPlayers();
        //init new level
        mTiledMap = _tiledMap;
        layerIndex = mTiledMap.getLayerIndex("Level");
        rootTiles = new RootTileList(mTiledMap);
        
        tileGrid = new LevelTileGrid(mTiledMap, rootTiles, layerIndex);
        if (mFlagProcessor != null)
            mFlagProcessor.cleanup();
        mFlagProcessor = new FlagProcessor(mTiledMap, layerIndex, tileGrid.mBody, tileGrid);
        mFlagProcessor.run();
    }
    private LevelEditor(TiledMap _tiledMap, RootTileList _rootTiles, int _layerIndex)
    {
        mTiledMap = _tiledMap;
        layerIndex = _layerIndex;
        rootTiles = _rootTiles;
        tileGrid = new LevelTileGrid(mTiledMap, rootTiles, layerIndex);
    }
    @Override
    public LevelEditor clone()
    {
        try
        {
            return new LevelEditor(new AnimatedTiledMap("assets/DeekTestMap.tmx"), rootTiles, layerIndex);
        }
        catch (SlickException e)
        {
            assert(false);
            return null;
        }
    }
    public void placeTile(int _x, int _y, int _rootId)
    {
        tileGrid.placeTile(_x, _y, _rootId);        
    }
    public void placeTileNoBody(int _x, int _y, int _rootId)
    {
        tileGrid.setTileId(_x, _y, _rootId);
        tileGrid.placeTileNoBody(_x, _y, _rootId);
    }
    public void destroyTile(int _xTile, int _yTile)
    {
        tileGrid.destroyTile(_xTile, _yTile);
    }
    public void update()
    {
        tileGrid.update();
    }
    
    public sLevel.TileType getTileType(int _id)
    {
        return rootTiles.getTileType(_id);
    }
    
    public sLevel.TileType getTileType(int _xTile, int _yTile)
    {
        Tile temp = tileGrid.get(_xTile, _yTile);
        return temp.mRootId.mTileType;
        
        
    }

    boolean damageTile(int _x, int _y)
    {
        return tileGrid.damageTile(_x, _y);
    }


}
