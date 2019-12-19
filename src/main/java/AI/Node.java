/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author Graham
 */
public class Node {
    
    public int mX, mY;
    public float mCost;
    public float mHeuristic;
    public Node mParent;
    public int mDepth;
    Node(int _x, int _y)
    {
        mX = _x; mY = _y;
    }
}
