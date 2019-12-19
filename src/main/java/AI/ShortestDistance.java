/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;
import java.lang.Math;


/**
 *
 * @author Graham
 */
public class ShortestDistance implements iHeuristic
{
    public float getCost(int _xStart, int _yStart, int _xTarget, int _yTarget) 
    {
        //return (float)Math.sqrt((Math.pow((_xTarget - _xStart), 2))+(Math.pow((_yTarget - _xStart),2)));
        return (float)((Math.pow((_xTarget - _xStart), 2))+(Math.pow((_yTarget - _xStart),2)));
    }
}
