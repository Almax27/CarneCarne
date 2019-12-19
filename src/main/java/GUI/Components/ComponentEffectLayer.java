/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import GUI.Components.Effects.iComponentEffect;
import java.util.ArrayDeque;

/**
 *
 * @author Aaron
 */

public class ComponentEffectLayer {
    
    ComponentEffectLayer(iComponent _component)
    {
        mComponent = _component;
    }
    
    iComponent mComponent;
    ArrayDeque<iComponentEffect> mEffects = new ArrayDeque<iComponentEffect>();
    
    void render(int _x, int _y, int _w, int _h)
    {
        for(iComponentEffect effect : mEffects)
        {
            effect.render(_x, _y, _w, _h);
        }
    }
    void update(int _delta)
    {
        for(iComponentEffect effect : mEffects)
        {
            effect.update(_delta);
        }
    }
    public void pushEffect(iComponentEffect _effect)
    {
        mEffects.push(_effect);
    }
    public void popEffect()
    {
        mEffects.pop();
    }
}
