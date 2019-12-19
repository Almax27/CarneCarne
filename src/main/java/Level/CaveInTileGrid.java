/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Entities.Entity;
import Entities.sEntityFactory;
import Graphics.Skins.TiledSkin;
import Level.CaveInSearcher.TempTile;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class CaveInTileGrid extends TileGrid
{
    private int mXTrans, mYTrans;
    private int mTotalXTransform, mTotalYTransform;
    int ids[][];
    private TiledMap mTiledMap;
    private int mLayerIndex;
    Vec2 mPosition;
    float mAngle;
    Vec2 mLinearVelocity;
    float mAngularVelocity;
    public CaveInTileGrid(RootTileList _rootTiles, TiledMap _tiledMap, int _xTrans, int _yTrans, int _width, int _height, int _layerIndex, Vec2 _position, float _angle, Vec2 _linearVelocity, float _angularVelocity, int _totalXTransform, int _totalYTransform)
    {
        super(_rootTiles, _width, _height);
        mPosition = _position;
        mAngle = _angle;
        mLinearVelocity = _linearVelocity;
        mAngularVelocity = _angularVelocity;
        mXTrans = _xTrans;
        mYTrans = _yTrans;
        ids = new int[_width][_height];
        mTiledMap = _tiledMap;
        mLayerIndex = _layerIndex;
        mTotalXTransform = _totalXTransform;
        mTotalYTransform = _totalYTransform;
    }

    public void setTempId(int _x, int _y, int _id)
    {
        ids[_x-mXTrans][_y-mYTrans] = _id;
    }
    protected BodyType getBodyType()
    {
        return BodyType.DYNAMIC;
    }
    public void finish(ArrayList<TempTile> tiles)
    {
        for (TempTile tile: tiles)
        {
            setTempId(tile.x, tile.y, tile.getId());
            tile.x -= mXTrans;
            tile.y -= mYTrans;
        }
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("bodyType", getBodyType());
        //parameters.put("position", new Vec2(mXTrans, mYTrans).add(mPosition));
        parameters.put("position", mPosition);
        parameters.put("angle", mAngle);
        parameters.put("linearVelocity", mLinearVelocity);
        parameters.put("angularVelocity", mAngularVelocity);
        init(parameters);
        parameters = new HashMap<String, Object>();
        parameters.put("tiles", tiles);
        parameters.put("body",mBody);
        parameters.put("width",ids.length);
        parameters.put("height",ids[0].length);
        //try
        {
        mBody.setUserData(sEntityFactory.create("CaveIn", parameters)); /// FIXME put a nullpointerexception catch around this
        mBody.setLinearVelocity(mLinearVelocity);
        mBody.setAngularVelocity(mAngularVelocity);
        }
        //catch (NullPointerException e)
        {
            
        }
    }
    @Override
    int getTileId(int _x, int _y)
    {
        return ids[_x][_y];
    }

    @Override
    void setTileId(int _x, int _y, int _id) 
    {
        ids[_x][_y] = _id;
        if (mBody != null)
        {
            Entity entity = (Entity)mBody.getUserData();
            if (entity != null)
            {
                TiledSkin skin = (TiledSkin)entity.mSkin;
                skin.setId(_x,_y,_id);
            }
        }
    }
    void createPhysicsBody(int _x, int _y, Tile _tile)
    {
        int id = ids[_x][_y];
        if (id != 0)
        {
            TileSet tileSet = mTiledMap.findTileSet(id);
            int idDiff = id - tileSet.firstGID;
            Image image = tileSet.tiles.getSubImage(idDiff%tileSet.tilesAcross, idDiff/tileSet.tilesAcross);
            HashMap parameters = new HashMap();
            parameters.put("isDynamic", true);
            parameters.put("img", image);

            /// Individual tiles
            if (mBody != null)
            {
                mTiles[_x][_y].createPhysicsBody(parameters, mBody);
                Fixture fixture = mTiles[_x][_y].mFixture;
            }
            //mLastBody = body;
            //parameters.put("Body", fixture);
            //sEntityFactory.create("CaveInTileFactory", parameters);
        }
    }
    @Override
    public void update()
    {
        mTileFire.update();
    }
    public void destroyTile(int _x, int _y)
    {
        RootTile roottile = mTiles[_x][_y].getRootTile();
        if (!searching && mBody.getType().equals(BodyType.DYNAMIC))
        {
            sLevel.getTileGrid().regrowingTiles.add(_x + mTotalXTransform, _y + mTotalYTransform, roottile);
        }
        destroyTileImplementation(_x, _y);
        if (mBody.getFixtureList() == null)
        {
            sWorld.destroyBody(mBody);
            mBody = null;
            
        }
    }
    boolean searching = false;
    void caveInSearch(int _x, int _y, TileType _tileType)
    {
        if (!searching)
        {
            searching = true;
            CaveInSearcher search = new CaveInSearcher(this, mTiledMap, mLayerIndex, mBody, mTotalXTransform, mTotalYTransform);
            search.destroy(_x, _y, mTiles[_x][_y], _tileType);
            sWorld.destroyBody(mBody);
            ((Entity)mBody.getUserData()).setBody(null);
            mBody.setUserData(null);
            searching = false;
        }
    }
    void dropCheck(int _x, int _y)
    {
        
    }
}
