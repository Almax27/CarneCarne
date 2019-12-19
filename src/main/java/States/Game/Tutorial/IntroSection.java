/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
abstract public class IntroSection
{
    protected int mTimer;
    protected Vec2 mPosition;
    protected int mPlayerNumber;
    protected iSkin mSkin;
    protected iSkin xBoxAnim;
    private String xBoxAnim1Name, xBoxAnim2Name;
    protected float mXboxScale;
    public IntroSection(Vec2 _position, int _playerNumber, String _xBoxAnim1Name, String _xBoxAnim2Name, float _xBoxScale)
    {
        mTimer = 0;
        mPosition = _position;
        mPlayerNumber = _playerNumber;
        xBoxAnim1Name = _xBoxAnim1Name;
        xBoxAnim2Name = _xBoxAnim2Name;
        mXboxScale = _xBoxScale;
    }
    
    public IntroSection update()
    {
        mTimer++;
        return updateImpl();
    }
    abstract public IntroSection updateImpl();
    final public void render()
    {
        float scale = sGraphicsManager.getScreenDimensions().x;
        float scaleY = sGraphicsManager.getScreenDimensions().y;
        if (mTimer > 120 && xBoxAnim1Name != null)
        {
            if (mTimer % 60 == 0)
            {
                HashMap params = new HashMap();
                params.put("ref", xBoxAnim1Name);
                xBoxAnim = sSkinFactory.create("static", params);
            }
            else if ((mTimer + 30) % 60 == 0)
            {
                HashMap params = new HashMap();
                params.put("ref", xBoxAnim2Name);
                xBoxAnim = sSkinFactory.create("static", params);
            }
            if (xBoxAnim != null)
            {
                xBoxAnim.setDimentions(0.20f*scaleY*mXboxScale, 0.20f*scaleY);
                xBoxAnim.render(0.2f,0.36f*scaleY);
            }
        }
        //renderInternal(scale);
        if (mSkin != null)
        {
            mSkin.setDimentions(0.4f*scale, 0.4f*scale*0.67f);
            mSkin.render(0.6f*scale,0);
        }                                                                                                   
    }
    abstract protected void renderInternal(float scale);

    abstract void cleanup();
}
