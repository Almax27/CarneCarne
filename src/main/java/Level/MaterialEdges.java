/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;

/**
 *
 * @author alasdair
 */
class MaterialEdges
{
    static MaterialEdges GraphicalEdges = new MaterialEdges(true);
    static MaterialEdges AnchorEdges = new MaterialEdges(false);
    private boolean flags[][];

    public MaterialEdges(boolean _graphicalEdge)
    {
        flags = new boolean[TileType.eTileTypesMax.ordinal()][TileType.eTileTypesMax.ordinal()];
        for (int i = 0; i < TileType.eTileTypesMax.ordinal(); i++)
            for (int ii = 0; ii < TileType.eTileTypesMax.ordinal(); ii++)
                flags[i][ii] = true;
        if (_graphicalEdge)
        {
            setFalse(TileType.eWater.ordinal(),TileType.eIce.ordinal());
            setFalse(TileType.eWater.ordinal(),TileType.eEdible.ordinal());

            flags[TileType.eSwingable.ordinal()][TileType.eIce.ordinal()] = false;
            //setFalse(TileType.eSwingable.ordinal(),TileType.eIce.ordinal());
        }
        else
        {
            /// All anchor edges are true, for the moment anyway
        }
        for (int i = 0; i < TileType.eTileTypesMax.ordinal(); i++)
        {
            flags[i][TileType.eZoomzoom.ordinal()] = false;
        }
    }
    
    private void setFalse(int i, int ii)
    {
        flags[i][ii] = false;
        flags[ii][i] = false;
    }
    
    boolean check(TileType _source, TileType _target)
    {
        return flags[_source.ordinal()][_target.ordinal()];
    }
}
