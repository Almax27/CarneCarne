/*
 * This class handles the creation and destruction of lights in the scene
 * the body camera uses this class to get all visible lights to add to the shader
 */
package Level.Lighting;

import Graphics.sGraphicsManager;
import Utils.Shader.LightSource;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Aaron
 */
public class sLightsManager 
{
    private sLightsManager()
    {    
    }
    
    private static HashMap<Integer, LightSource> mLightSources = new HashMap<Integer, LightSource>();
    static int mVisibleLightCount;
    static int mKeyCount = 0;
    static float mAmbience = 0.8f;
    
    public static int getInstancedLightCount() {return mLightSources.size();}
    public static int getVisibleLightCount() {return mVisibleLightCount;}
    
    public static void setAmbience(float _ambience){mAmbience = _ambience;}
    public static float getAmbience(){return mAmbience;}
    //Position and radius are in world space (pixels)
    public static int createLightSource(Vec2 _position, float _radius, Color _color, float _constantAttentuation, float _quadraticAttentuation)
    {
        int key = mKeyCount++;
        LightSource newSource = new LightSource(_position,_color, _constantAttentuation, _radius, _quadraticAttentuation);
        mLightSources.put(key, newSource);
        return key;
    }
    
    public static void destroyLightSource(int _key)
    {
        mLightSources.remove(_key);
    }
    
    public static void destroyAllLightSources()
    {
        mLightSources.clear();
    }
    
    public static ArrayList<LightSource> getVisible(Rectangle _viewport)
    {
        ArrayList<LightSource> visibleList = new ArrayList<LightSource>();
        for(LightSource source : mLightSources.values())
        {
            Vec2 pos = sWorld.translateToWorld(source.getPosition());
            Circle light = new Circle(pos.x + _viewport.getX(), pos.y + _viewport.getY(), source.getRadius(), 10); 
            if(_viewport.intersects(light))
                visibleList.add(source);
        }
        mVisibleLightCount = visibleList.size();
        return visibleList;
    }
    
    //get lightSource by it's known position
    public static LightSource getByPosition(Vec2 _pos)
    {
        for(LightSource source : mLightSources.values())
        {
            if(source.getPosition().equals(_pos))
                return source;
        }
        return null;
    }
    
}
