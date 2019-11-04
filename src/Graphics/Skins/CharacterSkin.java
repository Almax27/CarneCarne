/*
 * 
 */
package Graphics.Skins;

import Utils.Throw;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class CharacterSkin implements iSkin
{

    public void setAlwaysOnTop(boolean _isAlwaysOnTop) {
        mAlwaysOnTop = _isAlwaysOnTop;
    }
    public void setAlpha(String _animation, float _alpha) {
        mSubSkins.get(mSkinNames.get(_animation)).setAlpha(_alpha);
    }
    

    static public class CharacterSubSkin
    {
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight, Vec2 _offset)
        {
            this(_ref, _type, _tileWidth, _tileHeight, _offset, false, false,  41);
        }
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight, Vec2 _offset, boolean _isAlwaysOnTop)
        {
            this(_ref, _type, _tileWidth, _tileHeight, _offset, false, false,  41);
            mIsAlwaysOnTop = _isAlwaysOnTop;
        }
        //flipping the skin will postfix "_h" and "_v" to the name respectively
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight, Vec2 _offset, boolean _hFlipped,  boolean _vFlipped)
        {
            this(_ref, _type, _tileWidth, _tileHeight, _offset, _hFlipped, _vFlipped,  41);
        }
        //flipping the skin will postfix "_h" and "_v" to the name respectively
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight, Vec2 _offset, boolean _hFlipped, boolean _vFlipped, int _duration)
        {
            mRef = _ref;
            mType= _type;
            mTileWidth = _tileWidth;
            mTileHeight = _tileHeight;
            mOffset = _offset;
            mDuration = _duration;
            mHFlipped = _hFlipped;
            mVFlipped = _vFlipped;
        }
        public enum SubType
        {
            e32Dir,
            eAnimated,
            eStatic
        }
        SubType mType;
        String mRef;
        int mTileWidth = 0, mTileHeight = 0;
        int mDuration = 0;
        Vec2 mOffset = null;
        boolean mHFlipped = false;
        boolean mVFlipped = false;
        boolean mIsAlwaysOnTop = false;
    }
    
    CharacterSkin(List<CharacterSubSkin> _subSkins) throws SlickException
    {
        for(CharacterSubSkin subSkin : _subSkins)
        {
            String absoluteRef = "assets/characters/" + subSkin.mRef + ".png";
            switch(subSkin.mType)
            {
                case e32Dir:
                {
                    SpriteSheet ss = new SpriteSheet(absoluteRef, subSkin.mTileWidth, subSkin.mTileHeight);
                    //ensure 6x6 tiles
                    int reqWidth = 6;
                    int reqHeight = 6;
                    if(ss.getHorizontalCount() != reqWidth || ss.getVerticalCount() != reqHeight)
                        Throw.err(absoluteRef + " MUST BE " + reqWidth + "x" + reqHeight + " TILES");
                    for(int y = 0; y < reqHeight; y++)
                    {
                        for(int x = 0; x < reqWidth; x++)
                        {
                            String name = subSkin.mRef + "_" + mDirections.get(x + (y*reqWidth));
                            iSkin skin = new StaticSkin(ss.getSprite(x, y));
                            skin.setAlwaysOnTop(subSkin.mIsAlwaysOnTop);
                            addSubSkin(skin, name, subSkin.mOffset);
                        }
                    }
                    break;
                }
                case eAnimated:
                {
                    SpriteSheet ss = new SpriteSheet(absoluteRef, subSkin.mTileWidth, subSkin.mTileHeight);;
                    String postFix = "";
                    if(subSkin.mHFlipped || subSkin.mVFlipped)
                    {
                        ss = new SpriteSheet(ss.getFlippedCopy(subSkin.mHFlipped, subSkin.mVFlipped), subSkin.mTileWidth, subSkin.mTileHeight);
                        if(subSkin.mHFlipped)
                            postFix += "_h";
                        if(subSkin.mVFlipped)
                            postFix += "_v";
                    }
                    iSkin skin = new AnimatedSkin(ss, subSkin.mDuration);
                    skin.setAlwaysOnTop(subSkin.mIsAlwaysOnTop);
                    addSubSkin(skin, subSkin.mRef+postFix, subSkin.mOffset);
                    break;
                }
                case eStatic:
                {
                    Image image = new Image(absoluteRef);
                    iSkin skin = new StaticSkin(image);
                    skin.setAlwaysOnTop(subSkin.mIsAlwaysOnTop);
                    addSubSkin(skin, subSkin.mRef, subSkin.mOffset);
                    break;
                }
            }
        }
    }
    
    static ArrayList<String> mDirections = /*S->W->N->E->S*/
            new ArrayList(Arrays.asList("s","ssbw", "sbw","ssw","swbs","sw","swbw","wsw","wbs",
                                        "w","wbn","wnw","nwbw","nw","nwbn","nnw","nbw", "nnbw",
                                        "n","nnbe", "nbe","nne","nebn","ne","nebe","ene","ebn", 
                                        "e","ebs","ese","sebe","se","sebs","sse","sbe", "ssbe"));
    
    List<iSkin> mSubSkins = new ArrayList<iSkin>();
    List<Vec2> mOffsets = new ArrayList<Vec2>();
    HashMap<String, Integer> mSkinNames = new HashMap<String, Integer>();
    PriorityQueue<Integer> mCurrentSkins = new PriorityQueue<Integer>();
    
    float mRotation = 0;
    boolean mAlwaysOnTop = false;
    
    //will ignore subskins being added with same name as existing ones
    protected final void addSubSkin(iSkin _skin, String _name, Vec2 _offset)
    {
        if(!mSkinNames.containsKey(_name))
        {
            int arrayPos = mSubSkins.size();
            mSubSkins.add(_skin);
            mSkinNames.put(_name, arrayPos);
            mOffsets.add(_offset);
        }
    }
    
    public void render(float _x, float _y)
    {
        for(Integer i : mCurrentSkins)
        {
            mSubSkins.get(i).render(_x + mOffsets.get(i).x, _y + mOffsets.get(i).y);
        }
    }
    
    public void setRotation(float _radians)
    {
        for(iSkin subSkin : mSubSkins)
        {
            subSkin.setRotation(_radians);
        }
    }

    public final float activateSubSkin(String _animation, boolean _isLooping, float _speed) {    
        if(mSkinNames.containsKey(_animation))
        {
            int ref = mSkinNames.get(_animation);
            if(mCurrentSkins.contains(ref))//return if already activated
                return 0;
            mCurrentSkins.add(ref);
            
            iSkin skin = mSubSkins.get(ref);
            
            skin.restart();
            skin.setIsLooping(_isLooping);
            skin.setSpeed(_speed);
            
            return skin.getDuration();
        }
        else return 0;
    }

    public final void deactivateSubSkin(String _animation) 
    {
        Integer ref = mSkinNames.get(_animation);
        if(ref != null)//return if already activated
        {
            mSubSkins.get(ref).stop();
            mCurrentSkins.remove(ref);
        }
    }
    
    public void restart() {
        //do nothing
    }
    
    public void setRotation(String _subSkin, float _radians)
    {
        if(mSkinNames.containsKey(_subSkin))
            mSubSkins.get(mSkinNames.get(_subSkin)).setRotation(_radians);
    }
    
    public void setDimentions(String _subSkin, float _w, float _h) {
        if(mSkinNames.containsKey(_subSkin))
            mSubSkins.get(mSkinNames.get(_subSkin)).setDimentions(_w, _h);
    }
    
    public void setOffset(String _subSkin, Vec2 _offset) {
            Integer ref = mSkinNames.get(_subSkin);
            if(ref != null)
                mOffsets.set(ref, _offset);
    }
    public Vec2 getOffset(String _subSkin) {
        Integer ref = mSkinNames.get(_subSkin);
        if(ref != null)
            return mOffsets.get(ref);
        else return null;
    }
    
    public void setAlpha(float _alpha) {
        for(iSkin subSkin : mSubSkins)
        {
            subSkin.setAlpha(_alpha);
        }
    }

    public boolean isAnimating(String _subSkin) {
        Integer ref = mSkinNames.get(_subSkin);
        if(ref != null)
            return mSubSkins.get(ref).isAnimating();
        else return false;
    }
    
    public void stop(String _subSkin) {
        Integer ref = mSkinNames.get(_subSkin);
        if(ref != null)
            mSubSkins.get(ref).stop();
    }

    public void stopAt(String _subSkin, int _index) {
        Integer ref = mSkinNames.get(_subSkin);
        if(ref != null)
            mSubSkins.get(ref).stopAt(_index);
    }
    
    public boolean isAnimating() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setIsLooping(boolean _isLooping) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpeed(float _speed)
    {
        int ref = mSkinNames.get("car_fly"); // Hiiiiiiya
        iSkin skin = mSubSkins.get(ref); 
        skin.setSpeed(_speed);
    }

    public float getDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAt(int _index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
