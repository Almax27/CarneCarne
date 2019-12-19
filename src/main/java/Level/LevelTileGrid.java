/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public final class LevelTileGrid extends TileGrid
{
    AnimatedTiledMap tiledMap;
    private int layerIndex;
    public LevelTileGrid(TiledMap _tiledMap, RootTileList _rootTiles, int _layerIndex)
    {
        super(_rootTiles, _tiledMap.getWidth(), _tiledMap.getHeight());
        tiledMap = (AnimatedTiledMap)_tiledMap;
        layerIndex = _layerIndex;
        HashMap parameters = new HashMap();
        //parameters.put("isDynamic", false);
        parameters.put("position", new Vec2(0, 0));
        parameters.put("angle", 0.0f);
        init(parameters);
    }
    int getTileId(int _x, int _y)
    {
        return tiledMap.getTileId(_x, _y, layerIndex);
    }
    void setTileId(int _x, int _y, int _id)
    {
        tiledMap.setTileId(_x, _y, layerIndex, _id);
    }
    void createPhysicsBody(int _x, int _y, Tile _tile)
    {
        mTiles[_x][_y].createPhysicsBody(mBody);
    }
    public void destroyTile(int _x, int _y)
    {
        destroyTileImplementation(_x, _y);
    }
    @Override
    public void update()
    {
        regrowingTiles.update();
        mTileFire.update();
        tiledMap.update();
    }
    boolean dropChecking = false;
    void caveInSearch(int _x, int _y, TileType _tileType)
    {
        if (!dropChecking)
        {
            CaveInSearcher search = new CaveInSearcher(this, tiledMap, layerIndex, mBody, 0, 0);
            search.destroy(_x, _y, mTiles[_x][_y], _tileType);
        }
    }
    void dropCheck(int _x, int _y)
    {
        /*if (!dropChecking && mTiles[_x][_y].dothDropeth())
        {
            dropChecking = true;
            if (mTiles[_x][_y].mFixture != null)
                mTiles[_x][_y].destroyFixture();
            HashMap parameters = new HashMap();
            Tile tile = mTiles[_x][_y].clone();
            tile.mXTile = _x;
            tile.mYTile = _y;
            parameters.put("isDynamic", true);
            parameters.put("position", new Vec2(0,0));
            parameters.put("angle", 0.0f);
            parameters.put("linearVelocity", new Vec2(0,0));
            parameters.put("angularVelocity", 0.0f);
            Body body = sWorld.useFactory("TileFactory", parameters);
            tile.createPhysicsBody(body);
            destroyTile(_x, _y);
            dropChecking = false;
        }*/
    }
}
