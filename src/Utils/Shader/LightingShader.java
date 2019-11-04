/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Shader;

import Graphics.sGraphicsManager;
import Level.Lighting.sLightsManager;
import World.sWorld;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import org.jbox2d.common.Vec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author a203945
 */
public class LightingShader extends Shader 
{
    static int maxLights = 8;
    private LightingShader(ShaderResourceManager srm, Collection<String> vertex, Collection<String> fragment) throws SlickException
    {
        super(srm, vertex, fragment);
    }
    
    ArrayList<LightSource> mLightSources = new ArrayList<LightSource>();
    FloatBuffer aux = BufferUtils.createFloatBuffer(4);
    FloatBuffer attenuation = BufferUtils.createFloatBuffer(4);
    FloatBuffer fbpos = BufferUtils.createFloatBuffer(4);
    FloatBuffer color = BufferUtils.createFloatBuffer(4);
    
    //returns null when max lights is reached
    public LightSource addLightSource(LightSource _source)
    {
        int index = mLightSources.size();
        if(index < maxLights)
        {
            mLightSources.add(_source);
            return mLightSources.get(index);
        }
        else
        {
            return null;
        }
    }
    //clears current lightSource list
    public void clearSources()
    {
        mLightSources.clear();
    }

    @Override
    public void startShader() 
    {
        
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ZERO);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        Rectangle viewport = sGraphicsManager.getClip();
        
        //WARNING: these buffers are allocated off the heap and are not properly garbage collected
        //the clear method resets the 'pointer' to the head but doesn't deallocate the memory
        //use with freak'n care
        aux.clear();
        aux.put(new float[]{(float)mLightSources.size(), s.x, s.y, sLightsManager.getAmbience()}).flip();
        
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, aux);

        for(int i = 0; i < mLightSources.size(); i++)
        {
            mLightSources.get(i).update(0);
            fbpos.clear();
            Vec2 pos = sWorld.translateToWorld(mLightSources.get(i).getPosition());
            fbpos.put(new float[]{  pos.x, 
                                    pos.y, 
                                    mLightSources.get(i).getRadius(), 
                                    mLightSources.get(i).getTick()}).flip();

            color.clear();
            color.put(new float[]{  mLightSources.get(i).getColor().a, 
                                    mLightSources.get(i).getColor().g, 
                                    mLightSources.get(i).getColor().b, 
                                    0}).flip();
            
            attenuation.clear();
            attenuation.put(new float[]{mLightSources.get(i).mConstantAttentuation, 
                                        mLightSources.get(i).mQuadraticAttentuation, 
                                        0, 
                                        0}).flip();
        
            GL11.glEnable(GL11.GL_LIGHT0 + i);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_POSITION, fbpos);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_DIFFUSE, color);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_AMBIENT, attenuation);
            
        }
        super.startShader();
    }
         
    public void endShader()
    {
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
    }
    
    
    public static LightingShader makeShader(String vertexFileName, String fragmentFileName) throws SlickException
    {
        ArrayList<String> l1 = new ArrayList<String>();
        l1.add(vertexFileName);
        ArrayList<String> l2 = new ArrayList<String>();
        l2.add(fragmentFileName);
            
        return new LightingShader(ShaderResourceManagerImpl.getSRM(), l1, l2);
    }
}
