/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import AI.sPathFinding;
import Events.AreaEvents.sAreaEvents;
import Events.GenericEvent;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Level.Lighting.sLightsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
/**
 *
 * @author alasdair
 */
public class sLevel
{
    public static void placeTile(int _x, int _y, int _rootId)
    {
        mLevelEditor.placeTile(_x, _y, _rootId);
    }
    public static void placeTileNoBody(int _x, int _y, int _rootId)
    {
        mLevelEditor.placeTileNoBody(_x, _y, _rootId);
    }

    static public TileGrid getTileGrid()
    {
        return mLevelEditor.tileGrid;
    }

    public static Vec2 getLevelDimensions()
    {
        return new Vec2(mLevelEditor.tileGrid.mTiles.length, mLevelEditor.tileGrid.mTiles[0].length);
    }

    
    public static enum TileType
    {
        eEmpty,
        eEdible,
        eSwingable,
        eIndestructible,
        eWater,
        eAcid,
        eIce,
        eBouncy,
        eGum,
        eTar,
        eChilli,
        eBurningTar,
        eMelonFlesh,
        eMelonSkin,
        eSpikes,
        eZoomzoom,
        eTileTypesMax,
    }
    public static TileType getTileType(int _id)
    {
        return mLevelEditor.getTileType(_id);
    }
    private static float mParralaxXScale[];
    private static float mParralaxYScale[];
    private static AnimatedTiledMap mTiledMap;
    private static LevelEditor mLevelEditor;
    private static int flagsLayer; /// Ignore this one
    private static int midLayer; 
    private static int xTiles = 0, yTiles = 0;
    public enum PathInfo
    {
        eNotPassable,
        eAir,
        eLeftSlope,
        eRightSlope,
        ePathable
    };
    
    public static int getVisibleTileCount() {return xTiles*yTiles;}
    public static int getTileSizeInMetres()
    {
        return 1; //just incase we want to make tiles smaller than a meter.
    }
    
    public static PathInfo getPathInfo(int _xTile, int _yTile)
    {
        TileType type = mLevelEditor.getTileType(_xTile,_yTile);
        if (type == TileType.eEmpty)
        {
            return PathInfo.eAir;
        }
        else return PathInfo.eNotPassable;
    }
    private sLevel()
    {
    }
    public static RootTile getRootTile(int _rootId)
    {
        return mLevelEditor.rootTiles.get(_rootId);
    }    
    
    public void destroyTile(int _xTile, int _yTile)
    {
        mLevelEditor.destroyTile(_xTile, _yTile);
    }
    
    public static void init(String _map)
    {
        try
        {
            mTiledMap = new AnimatedTiledMap("assets/Tiles/Maps/" + _map + ".tmx");
            //mTiledMap = new AnimatedTiledMap("assets/tutorial.tmx");
        }
        catch (SlickException e)
        {
            throw new UnsupportedOperationException(_map+ " loading failed");
        }
        mLevelEditor = new LevelEditor(mTiledMap);
        mParralaxXScale = new float[mTiledMap.getLayerCount()];
        mParralaxYScale = new float[mTiledMap.getLayerCount()];
        for (int i = 0; i < mParralaxXScale.length; i++)
        {
            mParralaxXScale[i] = 1.0f;
            mParralaxYScale[i] = 1.0f;
        }
        flagsLayer = mTiledMap.getLayerIndex("Flags");
    }
    //returns next level
    public static String newLevel(String _map)
    {
        sEvents.triggerEvent(new GenericEvent("newLevel"));
        //clean up old level
        sAreaEvents.clearEvents();
        sWorld.destroy();
        sPathFinding.cleanup();
        sParticleManager.cleanupInstancedSystems();
        sLightsManager.destroyAllLightSources();
        try
        {
            mTiledMap = null; //this should encourage the gc to hupto.
            mTiledMap = new AnimatedTiledMap("assets/Maps/" + _map + ".tmx");
            mTiledMap.initAnimationlayer("assets/Maps/Tilesets/Images/Special/TileAnimation.def");
        }
        catch (SlickException e)
        {
            throw new UnsupportedOperationException(_map + " loading failed");
        }
        //initialise level editor if null
        if (mLevelEditor == null)
            mLevelEditor = new LevelEditor(mTiledMap);
        
        mParralaxXScale = new float[mTiledMap.getLayerCount()];
        mParralaxYScale = new float[mTiledMap.getLayerCount()];
        for (int i = 0; i < mParralaxXScale.length; i++)
        {
            mParralaxXScale[i] = new Float(mTiledMap.getLayerProperty(i, "ScaleX", "1.0"));
            mParralaxYScale[i] = new Float(mTiledMap.getLayerProperty(i, "ScaleY", "1.0"));
        }
        flagsLayer = mTiledMap.getLayerIndex("Flags");
        midLayer = mTiledMap.getLayerIndex("Level");
        mLevelEditor.newLevel(mTiledMap);
        return mTiledMap.getMapProperty("NextMap", null);
    }
    public static void update()
    {
        mLevelEditor.update();
    }
    public static void renderBackground(Graphics _graphics)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xTiles = (int)(s.x/64.0f)+2;
        yTiles = (int)(s.y/64.0f)+2;
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = 0; i < midLayer; i++)
        {
            if (i != flagsLayer)
            {
                Vec2 myTranslation = translation.clone();
                myTranslation.x = myTranslation.x *(mParralaxXScale[i]);
                myTranslation.y = myTranslation.y *(mParralaxYScale[i]);
                int xStart = -(int)(myTranslation.x/64.0f);
                int yStart = -(int)(myTranslation.y/64.0f);
                int transX = (int)myTranslation.x;
                int transY = (int)myTranslation.y;
                transX = transX % 64;
                transY = transY % 64;
                mTiledMap.render(transX,transY, xStart,yStart, xTiles,yTiles, i, false);
            }
        }
        int xStart = -(int)(translation.x/64.0f);
        int yStart = -(int)(translation.y/64.0f);
        mTiledMap.renderAnimatedLayer(translation.x,translation.y,xStart,yStart,xTiles,yTiles);
    }
    public static void renderForeground()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xTiles = (int)(s.x/64.0f)+2;
        yTiles = (int)(s.y/64.0f)+2;
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = midLayer; i < mTiledMap.getLayerCount(); i++) /// I fixed it you terrible, terrible person
        {
            if (i != flagsLayer)
            {
                Vec2 myTranslation = translation.clone();
                myTranslation.x = myTranslation.x *(mParralaxXScale[i]);
                myTranslation.y = myTranslation.y *(mParralaxYScale[i]);
                int xStart = -(int)(myTranslation.x/64.0f);
                int yStart = -(int)(myTranslation.y/64.0f);
                int transX = (int)myTranslation.x;
                int transY = (int)myTranslation.y;
                transX = transX % 64;
                transY = transY % 64;
                mTiledMap.render(transX,transY, xStart,yStart,xTiles,yTiles, i, false);
            }
        }     
        
    }
}
