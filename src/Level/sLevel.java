/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
/**
 *
 * @author alasdair
 */
public class sLevel {
    
    public static enum TileType
    {
        eEdible,
        eSwingable,
        eIndestructible,
        eWater,
        eIce,
        eBouncy,
        eGum,
        eTar,
        eWaterMelon,
        eTileTypesMax,
    }
    public TileType getTileType(int _id)
    {
        return mLevelEditor.getTileType(_id);
    }
    private static float mParralaxXScale[];
    private static float mParralaxYScale[];
    private static AnimatedTiledMap mTiledMap;
    private static LevelEditor mLevelEditor;
    private static int layerIndex;  
    private static int midLayer; 
    private static int xTiles, yTiles;
    private enum PathInfo
    {
        eNotPassable,
        eAir,
        eLeftSlope,
        eRightSlope,
        ePathable
    };
    
    public int getTileSizeInMetres()
    {
        return 1; //just incase we want to make tiles smaller than a meter.
    }
    
    static PathInfo getPathInfo(int _xTile, int _yTile)
    {
        int id = mTiledMap.getTileId(_xTile, _yTile, layerIndex);
        if (id == 0)
        {
            return PathInfo.eAir;
        }
        else return PathInfo.eNotPassable;
    }
    private sLevel()
    {
    }
    public static void init() throws SlickException
    {
        mTiledMap = new AnimatedTiledMap("assets/Test_map3ready.tmx");
        mTiledMap.initAnimationlayer("assets/splashbig.def");
        xTiles = (int)(800.0f/64.0f)+2;
        yTiles = (int)(800.0f/64.0f)+2;
        mLevelEditor = new LevelEditor(mTiledMap);
        midLayer = mTiledMap.getLayerIndex("Level");
        mParralaxXScale = new float[mTiledMap.getLayerCount()];
        mParralaxYScale = new float[mTiledMap.getLayerCount()];
        for (int i = 0; i < mParralaxXScale.length; i++)
        {
            mParralaxXScale[i] = new Float(mTiledMap.getLayerProperty(i, "ScaleX", "1.0"));
            mParralaxYScale[i] = new Float(mTiledMap.getLayerProperty(i, "ScaleY", "1.0"));
        }
    }
    public static void placeTile(int _x, int _y, TileType _tileType)
    {
        mLevelEditor.placeTile(_x, _y, _tileType);
    }
    public static void destroyTile(int _x, int _y)
    {
        mLevelEditor.destroyTile(_x, _y);
    }
    public static void update()
    {
        mLevelEditor.update();
    }
    public static void renderBackground()
    {
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = 0; i < midLayer; i++)
        {
            Vec2 myTranslation = translation;
            myTranslation.x = myTranslation.x *(mParralaxXScale[i]);
            myTranslation.y = myTranslation.y *(mParralaxYScale[i]);
            int xStart = -(int)(myTranslation.x/64.0f);
            int yStart = -(int)(myTranslation.y/64.0f);
            int transX = (int)myTranslation.x;
            int transY = (int)myTranslation.y;
            transX = transX % 64;
            transY = transY % 64;
            mTiledMap.render(transX,transY, xStart,yStart, xStart+xTiles,yStart+yTiles, i, false);
        }
    }
    public static void renderForeground()
    {
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = midLayer; i < mTiledMap.getLayerCount(); i++)
        {
            Vec2 myTranslation = translation;
            myTranslation.x = myTranslation.x *(mParralaxXScale[i]);
            myTranslation.y = myTranslation.y *(mParralaxYScale[i]);
            int xStart = -(int)(myTranslation.x/64.0f);
            int yStart = -(int)(myTranslation.y/64.0f);
            int transX = (int)myTranslation.x;
            int transY = (int)myTranslation.y;
            transX = transX % 64;
            transY = transY % 64;
            mTiledMap.render(transX,transY, xStart,yStart, xStart+xTiles,yStart+yTiles, i, false);
        }
        mTiledMap.renderAnimatedLayer(translation.x,translation.y,800.0f,600.0f);
    }
}
