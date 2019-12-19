/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.CaveInSearcher.TempTile;
import Level.sLevel.TileType;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class PlatformTileGrid extends CaveInTileGrid
{
    public PlatformTileGrid(RootTileList _rootTiles, TiledMap _tiledMap, int _xTrans, int _yTrans, int _width, int _height, int _layerIndex, Vec2 _position, float _angle, Vec2 _linearVelocity, float _angularVelocity)
    {
        super(_rootTiles, _tiledMap, _xTrans, _yTrans, _width, _height, _layerIndex, _position, _angle, _linearVelocity, _angularVelocity, 0, 0);
    }
    
    @Override
    protected BodyType getBodyType()
    {
        return BodyType.KINEMATIC;
    }
    @Override
    public void finish(ArrayList<TempTile> tiles)
    {
        super.finish(tiles);
        mBody.setLinearVelocity(new Vec2(0.1f,0));
    }
    
    public int getWidth()
    {
        return ids.length;
    }
    public int getHeight()
    {
        return ids[0].length;
    }
    @Override
    void caveInSearch(int _x, int _y, TileType _tileType)
    {
    }
}
