/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.Particles.sParticleManager;
import Level.MelonSkinTile.SkinDirection;
import Level.sLevel.TileType;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class ZoomzoomTile extends NonEdibleTile
{
    private static Vec2 mBoostDirections[];
    private static String mParticleSystems[];
    SkinDirection mDirection;
    public ZoomzoomTile(int _id, SkinDirection _direction, String _name)
    {
        super(_id, TileType.eZoomzoom, true);
        mDirection = _direction;
        if (mBoostDirections == null)
        {
            mBoostDirections = new Vec2[SkinDirection.eSkinDirectionsSize.ordinal()];
            mBoostDirections[SkinDirection.eRight.ordinal()] = new Vec2(1,0);
            mBoostDirections[SkinDirection.eLeft.ordinal()] = new Vec2(-1,0);
            mBoostDirections[SkinDirection.eUp.ordinal()] = new Vec2(0,-1);
            mBoostDirections[SkinDirection.eDown.ordinal()] = new Vec2(0,1);
            
            mBoostDirections[SkinDirection.eDownLeft.ordinal()] = new Vec2(-1,1);
            mBoostDirections[SkinDirection.eDownRight.ordinal()] = new Vec2(1,1);
            mBoostDirections[SkinDirection.eUpLeft.ordinal()] = new Vec2(-1,-1);
            mBoostDirections[SkinDirection.eUpRight.ordinal()] = new Vec2(1,-1);
            
            for (int i = 0; i < mBoostDirections.length; i++)
            {
                mBoostDirections[i] = mBoostDirections[i].mul(0.5f); /// This is the scalar
            }
            
            mParticleSystems = new String[SkinDirection.eSkinDirectionsSize.ordinal()];
            String type = _name;
            mParticleSystems[SkinDirection.eRight.ordinal()] = "ZoomyRight" + type;
            mParticleSystems[SkinDirection.eLeft.ordinal()] = "ZoomyLeft" + type;
            mParticleSystems[SkinDirection.eUp.ordinal()] = "ZoomTop" + type;
            mParticleSystems[SkinDirection.eDown.ordinal()] = "ZoomyBottom" + type;
            
            mParticleSystems[SkinDirection.eDownLeft.ordinal()] = "ZoomyBottomLeft" + type;
            mParticleSystems[SkinDirection.eDownRight.ordinal()] = "ZoomyBottomRight" + type;
            mParticleSystems[SkinDirection.eUpLeft.ordinal()] = "ZoomyTopLeft" + type;
            mParticleSystems[SkinDirection.eUpRight.ordinal()] = "ZoomyTopRight" + type;
        }
    }
    
    public Vec2 getBoostDirection()
    {
        return mBoostDirections[mDirection.ordinal()];
    }
    @Override
    void createdAt(int _xTile, int _yTile, TileGrid _tileGrid)
    {
        TiledMap tiledMap = ((LevelTileGrid)_tileGrid).tiledMap;
        if (tiledMap.getTileId(_xTile, _yTile, tiledMap.getLayerIndex("Flags")) != 0)
        {
        _tileGrid.addParticles(sParticleManager.createSystem(mParticleSystems[mDirection.ordinal()], new Vec2(_xTile, _yTile).mul(64).add(new Vec2(32,32)), -1));
        }
    }
}
