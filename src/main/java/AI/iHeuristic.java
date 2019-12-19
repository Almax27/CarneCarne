/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author Graham
 */
public interface iHeuristic
{
    public float getCost(int _xStart, int _yStart, int _xTarget, int _yTarget);
}
