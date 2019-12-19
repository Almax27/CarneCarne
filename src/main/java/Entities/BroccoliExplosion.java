/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;

/**
 *
 * @author alasdair
 */
public class BroccoliExplosion extends Entity
{
    private int mTimer;
    public BroccoliExplosion(iSkin _skin, int _timer)
    {
        super(_skin);
        mTimer = _timer;
    }
    @Override
    public void update()
    {
        mTimer--;
        if (mTimer == 0)
        {
            kill(CauseOfDeath.eMundane, this);
        }
    }
    
    @Override
    public void render()
    {
        
    }
    
}
