/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import Graphics.sGraphicsManager;
import Utils.sFontLoader;
import java.util.ArrayList;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public class Text extends iComponent{
    public Text(GUIContext _context, UnicodeFont _font, String _str, Vector2f _position, boolean _sizeToText) 
    {
        super(_context);
        mOriginalFont = _font;
        mFont = mOriginalFont;
        setTextString(_str);
        setLocalTranslation(_position);
        if(_sizeToText)
            resizeToText();
    }
    
    UnicodeFont mOriginalFont = null;
    UnicodeFont mFont = null;
    Integer mFontSize = null;
    String mTextString = "";
    ArrayList<String> mWrappedStrings = new ArrayList<String>();
    boolean mIsWrapped = false;
    boolean mIsSizedToFit = false;
    private float mScale = 1.0f;
    
    @Override
    protected boolean updateSelf(int _delta) 
    {
        //do nothing
        return true;
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException 
    {
        grphcs.translate(_globalPos.x, _globalPos.y);
        grphcs.scale(mScale, mScale);
        for(int i = 0; i < mWrappedStrings.size(); i++)
        {
            if(mFont == null)
                super.renderSelf(guic, grphcs, _globalPos);
            else
                mFont.drawString(0, i*mFont.getHeight(mTextString), mWrappedStrings.get(i), getColor());
        }
        grphcs.scale(1/mScale, 1/mScale);
        grphcs.translate(-_globalPos.x, -_globalPos.y);
    }
    
    public final void setFont(UnicodeFont _font)
    {
        if(_font != null)
            mFont = _font;
    }
    
    public final void setIsTextSizedToFit(boolean _sizedToFit){mIsSizedToFit = _sizedToFit;}
    public final boolean getIsTextSizedToFit(){return mIsSizedToFit;}
    
    public final void setIsWrapped(boolean _wordWrap){mIsWrapped = _wordWrap;}
    public final boolean getIsWrapped(){return mIsWrapped;}
    
    public final String getTextString(){return mTextString;}
    public final void setTextString(String _str)
    {
        if(_str == null || _str.equals(mTextString))
            return;
        
        mTextString = _str;
        if(mIsWrapped)
        {
            calcWrappedString(_str);
        }
        else
        {
            mWrappedStrings.clear();
            mWrappedStrings.add(_str);
        }
        sizeTextToFit();
    }
    
    /*
     * helper functions
     */
    private void sizeTextToFit()
    {
        if(mIsSizedToFit)
        {
            mScale = 1.0f;
            if(mIsWrapped) //rewrap text as it has been resized
            {
                //this algorithm assumes the text is initially smaller for efficiency
                while(mOriginalFont.getHeight(mTextString)*mWrappedStrings.size()*mScale < getDimensions().y)
                {
                    mScale += 0.05f;
                    calcWrappedString(mTextString);
                }
                while(mOriginalFont.getHeight(mTextString) *mWrappedStrings.size()*mScale > getDimensions().y)
                {
                    mScale -= 0.01f;
                    calcWrappedString(mTextString);
                }
            }
            else
            {
                mScale = getDimensions().x / mOriginalFont.getWidth(mTextString);
                mScale = Math.min(mScale, getDimensions().y / (mOriginalFont.getHeight(mTextString)*mWrappedStrings.size()));
            }
            //mFont = sFontLoader.scaleFont(mOriginalFont, mScale);
        }
    }
    public final void resizeToText()
    {
        Vector2f dimensions = new Vector2f(mFont.getWidth(mTextString),mFont.getHeight(mTextString));
        setDimensions(dimensions);
    }
    
    private void calcWrappedString(String _str)
    {
        mWrappedStrings.clear();
        float width = (float)mOriginalFont.getWidth(_str) * mScale;
        if(width < getDimensions().x)
            mWrappedStrings.add(_str);
        else
        {
            ArrayList<String> wordList = new ArrayList<String>();
            String tempWord = "";
            for(int i = 0; i < _str.length(); i++)
            {
                char nextChar = _str.charAt(i);
                if(nextChar != ' ') //build a word
                    tempWord += nextChar;
                else //add word to list
                {
                    wordList.add(tempWord);
                    tempWord = "";
                }
            }
            wordList.add(tempWord); //add last word

            String line = "";
            for(String word : wordList)
            {
                if((float)mOriginalFont.getWidth(line+word) * mScale < getDimensions().x)
                    line += word + ' ';
                else
                {
                    mWrappedStrings.add(line);
                    line = word + ' ';
                }
            }
            mWrappedStrings.add(line); //add last line
        }
    }
    
}
