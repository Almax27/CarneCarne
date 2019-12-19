/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components.Effects;

import GUI.Components.iComponent;

/**
 *
 * @author a203945
 */
public interface iComponentEffect 
{
    abstract void render(int _x, int _y, int _w, int _h);
    abstract void update(int _delta);
}
