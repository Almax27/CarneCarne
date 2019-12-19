/*
 * This class extends the tiledMap class to support animated tile
 * Assumes the there is a layer called "Animations"
 * Assumes horizontal animations
 * Animations are set but adding the propeties:
 *   animRef: ref   -- "bob"    (name of animation on the spritesheet)
 *   duration: t    -- 300      (frame duration in milliseconds)
 *   maxTimeOffset  -- 1000     (max initial offset)
 */  

package Level;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Animation;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * @author Aaron
 */
public class AnimatedTiledMap extends TiledMap
{
    class AnimatedTile
    {
        AnimatedTile(Animation _anim, Vec2 _pos)
        {
            mAnimation = _anim;
            mPosition = _pos;
        }
        
        private Animation mAnimation = null;
        private Vec2 mPosition = null;
        
        void render(float worldTransX, float worldTransY)
        {
            mAnimation.draw(mPosition.x*64.0f + worldTransX, mPosition.y*64.0f + worldTransY);
        }
    }
    
    private AnimatedTile[][] mAnimatedLayer = null;
    private PackedSpriteSheet mTileMapAnimations = null;
    private boolean mIsLoaded = false;
    private Random mRand = new Random();
    
    AnimatedTiledMap(String _mapRef) throws SlickException
    {
        //construct parent
        super(_mapRef, "assets/Maps/Tilesets");
        //super(_mapRef, true);
        
        
        timingTiles = new PriorityQueue<TimingOutTile>(10, new TileComparer());
    }
    
    public void initAnimationlayer(String _packedAnimSheetRef) throws SlickException
    {
        for(Object obj : tileSets)
        {
            TileSet set = (TileSet)obj;
            //set.tiles.getTexture().setTextureFilter().;
        }
        if(mIsLoaded == false)
        {
            mIsLoaded = true;
            
            mAnimatedLayer = new AnimatedTile[getWidth()][getHeight()];
            
            //load packed sprite sheet
            mTileMapAnimations = new PackedSpriteSheet(_packedAnimSheetRef);
            if(mTileMapAnimations == null)
                return;

            //iterate through animations layer
            int layerIndex = getLayerIndex("Animations");
            if(layerIndex == -1)
                return;
            for(int x = 0; x < getWidth(); x++)
            {
                for(int y = 0; y < getHeight(); y++)
                {
                    //get tileID
                    int tileID = getTileId(x, y, layerIndex);

                    String property;

                    //get animation name
                    property = getTileProperty(tileID, "animRef", null);
                    if(property == null)
                        continue;
                    String animRef = property;

                    //get duration
                    property = getTileProperty(tileID, "duration", null);
                    if(property == null)
                        continue;
                    int duration = Integer.parseInt(property);
                   
                    property = getTileProperty(tileID, "maxTimeOffset", null);
                    if(property == null)
                        continue;
                    int maxTimeOffset = Integer.parseInt(property);
                    
                    property = getTileProperty(tileID, "randomiseSpeed", "false");
                    boolean isRandSpeed = Boolean.parseBoolean(property);

                    //no fail - create animation
                    SpriteSheet sheet = mTileMapAnimations.getSpriteSheet(animRef);
                    Animation anim = new Animation(sheet,duration);
                    
                    //ofset start of animation
                    if(maxTimeOffset > 0)
                        anim.update(mRand.nextInt(maxTimeOffset));
                    
                    //randomise speed slightly
                    if(isRandSpeed)
                    {
                        float randSpeed = mRand.nextFloat()%1.25f;
                        anim.setSpeed(Math.max(0.75f, randSpeed));
                    }
                    
                    mAnimatedLayer[x][y] = new AnimatedTile(anim, new Vec2(x,y));
                }
            }
        }
    }
    void createAnimatedTile(int _xPos, int _yPos, String _ref)
    {
        if (_ref.equals("BoneGrowBack")) /// FIXME greg sucks
            _ref = "MeatGrowBack";
        createAnimatedTile(_xPos, _yPos, _ref, 41, 0, false);
    }
    void createAnimatedTile(int _xPos, int _yPos, String _ref, int _duration, int _maxTimeOffset, boolean _isRandSpeed)
    {
        if(_xPos < getWidth() && _yPos < getHeight())
        {
            //no fail - create animation
            SpriteSheet sheet = null;
            try
            {
                sheet = mTileMapAnimations.getSpriteSheet(_ref);
            }
            catch(Throwable ex)
            {
                System.err.println(ex.getMessage());
            }
            Animation anim = new Animation(sheet,_duration);

            //ofset start of animation
            if(_maxTimeOffset > 0)
                anim.update(mRand.nextInt(_maxTimeOffset));

            //randomise speed slightly
            if(_isRandSpeed)
            {
                float randSpeed = mRand.nextFloat()%1.25f;
                anim.setSpeed(Math.max(0.75f, randSpeed));
            }

            mAnimatedLayer[_xPos][_yPos] = new AnimatedTile(anim, new Vec2(_xPos,_yPos));
        }
    }
    
    void destroyAnimatedTile(int _xPos, int _yPos)
    {
        if(_xPos < getWidth() && _yPos < getHeight())
            mAnimatedLayer[_xPos][_yPos] = null;
    }
    
     /*
     * _x:      x translation
     * _y:      y translation
     * _width:  width of screen (pixels)
     * _height: height of screen (pixels)
     */
    public void renderAnimatedLayer(float _x, float _y, int _startX, int _startY, int _width, int _height)
    {
        _startX = Math.max(0, _startX);
        _startY = Math.max(0, _startY);
        if (mIsLoaded)
        {
            //assumes lists of equal length
            int endX = _startX + _width;
            int endY = _startY + _height;
            for(int i = _startX; i < endX; i++)
            {
                for(int j = _startY; j < endY; j++)
                {
                    if(i < getWidth() && j < getHeight())
                    {
                        if(mAnimatedLayer[i][j] != null)
                            mAnimatedLayer[i][j].render(_x, _y);
                    }
                }
            }
        }
    }
    private class TimingOutTile
    {
        int mX, mY, mTimer;
        TimingOutTile(int _x, int _y, int _timer)
        {
            mX = _x;
            mY = _y;
            mTimer = _timer;
        }
    }
    private static class TileComparer implements Comparator<TimingOutTile> 
    {
        public int compare(TimingOutTile x, TimingOutTile y)
        {
            if (x.mTimer < y.mTimer)
            {
                return -1;
            }
            if (x.mTimer > y.mTimer)
            {
                return 1;
            }
            return 0;
        }
    }
    PriorityQueue<TimingOutTile> timingTiles;
    int mFrames = 0;
    void createTimingOutAnimatedTile(int _x, int _y, String _ref, int _timer)
    {
        timingTiles.add(new TimingOutTile(_x, _y, mFrames + _timer));
        createAnimatedTile(_x, _y, _ref);
    }
    public void update()
    {
        mFrames++;
        TimingOutTile tile = timingTiles.peek();
        if (tile != null && tile.mTimer < mFrames)
        {
            timingTiles.remove(tile);
            mAnimatedLayer[tile.mX][tile.mY] = null;
        }
    }
}
