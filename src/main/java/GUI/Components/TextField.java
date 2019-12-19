/*
 * Souce taken from slick implementation of TextField
 * Now extends iComponent to fit in with hierarchical system
 * -- changed render so as it not render background or border
 * -- main constructor now passes position and dimensions through to iComponent
 * Aaron.
 */

package GUI.Components;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

/**
 * A single text field supporting text entry
 * 
 * @author kevin
 */
public class TextField extends iComponent 
{
	/** The key repeat interval */
	private static final int INITIAL_KEY_REPEAT_INTERVAL = 400;
	/** The key repeat interval */
	private static final int KEY_REPEAT_INTERVAL = 50;

	/** The maximum number of characters allowed to be input */
	private int maxCharacter = 10000;

	/** The value stored in the text field */
	private String value = "";
        private String defaultValue = "";

	/** The font used to render text in the field */
	private Font font;

	/** The text color */
	private Color text = Color.gray;
        private Color selectedTextColor = Color.white;
        private Color unselectedTextColor = Color.gray;

	/** The current cursor position */
	private int cursorPos;

	/** True if the cursor should be visible */
	private boolean visibleCursor = false;

	/** The last key pressed */
	private int lastKey = -1;
	
	/** The last character pressed */
	private char lastChar = 0;
	
	/** The time since last key repeat */
	private long repeatTimer;
	
	/** The text before the paste in */
	private String oldText;
	
	/** The cursor position before the paste */
	private int oldCursorPos;
	
	/** True if events should be consumed by the field */
	private boolean consume = true;
        
        /** True after first selection */
        private boolean used = false;
	
	/**
	 * Create a new text field
	 * 
	 * @param container
	 *            The container rendering this field
	 * @param font
	 *            The font to use in the text field
	 * @param x
	 *            The x coordinate of the top left corner of the text field
	 * @param y
	 *            The y coordinate of the top left corner of the text field
	 * @param width
	 *            The width of the text field
	 * @param height
	 *            The height of the text field
	 * @param listener 
	 * 			  The listener to add to the text field
	 */
	public TextField(GUIContext container, Font font, int x, int y, int width,
					 int height, ComponentListener listener) {
		this(container,font,x,y,width,height);
		addListener(listener);
	}
	
	/**
	 * Create a new text field
	 * 
	 * @param container
	 *            The container rendering this field
	 * @param font
	 *            The font to use in the text field
	 * @param x
	 *            The x coordinate of the top left corner of the text field
	 * @param y
	 *            The y coordinate of the top left corner of the text field
	 * @param width
	 *            The width of the text field
	 * @param height
	 *            The height of the text field
	 */
	public TextField(GUIContext container, Font font, int x, int y, int width,
			int height) {
		super(container, new Vector2f(x, y), new Vector2f(width,height));
		this.font = font;
	}

	/**
	 * Indicate if the input events should be consumed by this field
	 * 
	 * @param consume True if events should be consumed by this field
	 */
	public void setConsumeEvents(boolean consume) {
		this.consume = consume;
	}
	
	/**
	 * Deactivate the key input handling for this field
	 */
	public void deactivate() {
		setFocus(false);
	}

	/**
	 * Set the text color.
	 * 
	 * @param color
	 *            The color to use for the text
	 */
	public void setTextColor(Color color) {
		unselectedTextColor = color;
                text = unselectedTextColor;
	}
        
        public void setSelectedTextColor(Color color)
        {
            selectedTextColor = color;
        }

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#render(org.newdawn.slick.gui.GUIContext,
	 *      org.newdawn.slick.Graphics)
	 */
        int mCursorTimer = 0;
        int mCursorDelay = 400;
	protected void renderSelf(GUIContext guic, Graphics g, Vector2f _globalPos) throws SlickException {
		if (lastKey != -1) {
			if (input.isKeyDown(lastKey)) {
				if (repeatTimer < System.currentTimeMillis()) {
					repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
					keyPressed(lastKey, lastChar);
				}
			} else {
				lastKey = -1;
			}
		}
		Rectangle oldClip = g.getClip();
		g.setWorldClip(_globalPos.x,_globalPos.y,getWidth(), getHeight());
		
		// Someone could have set a color for me to blend...
		Color clr = g.getColor();

		g.setColor(text.multiply(clr));
		Font temp = g.getFont();

                String str = defaultValue;
                if(used)
                    str = value;
                
		int cpos = font.getWidth(str.substring(0, cursorPos));
		int tx = 0;
		if (cpos > getWidth()) {
			tx = getWidth() - cpos - font.getWidth("_");
		}
                
		g.translate(tx + 2, 0);
                    g.setFont(font);
                    g.drawString(str, _globalPos.x + 1, _globalPos.y + 1);
                    if(mIsSelected && mCursorTimer > mCursorDelay)
                    {
                        mCursorTimer = 0;
                        visibleCursor = !visibleCursor;
                    }
                    if (visibleCursor) 
                    {
                        g.drawString("_", _globalPos.x + 1 + cpos + 2, _globalPos.y + 1);
                    }
		g.translate(-tx - 2, 0);

		g.setColor(clr);
		g.setFont(temp);
		g.clearWorldClip();
		g.setClip(oldClip);
	}

	/**
	 * Get the value in the text field
	 * 
	 * @return The value in the text field
	 */
	public String getText() {
		return value;
	}

	/**
	 * Set the value to be displayed in the text field
	 * 
	 * @param value
	 *            The value to be displayed in the text field
	 */
	public void setText(String value) {
		this.value = value;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}
        
        public void setDefaultText(String value)
        {
            this.defaultValue = value;
        }

	/**
	 * Set the position of the cursor
	 * 
	 * @param pos
	 *            The new position of the cursor
	 */
	public void setCursorPos(int pos) {
		cursorPos = pos;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}

	/**
	 * Indicate whether the mouse cursor should be visible or not
	 * 
	 * @param visibleCursor
	 *            True if the mouse cursor should be visible
	 */
	public void setCursorVisible(boolean visibleCursor) {
		this.visibleCursor = visibleCursor;
	}

	/**
	 * Set the length of the allowed input
	 * 
	 * @param length
	 *            The length of the allowed input
	 */
	public void setMaxLength(int length) {
		maxCharacter = length;
		if (value.length() > maxCharacter) {
			value = value.substring(0, maxCharacter);
		}
	}

	/**
	 * Do the paste into the field, overrideable for custom behaviour
	 * 
	 * @param text The text to be pasted in
	 */
	protected void doPaste(String text) {
		recordOldPosition();
		
		for (int i=0;i<text.length();i++) {
			keyPressed(-1, text.charAt(i));
		}
	}
	
	/**
	 * Record the old position and content
	 */
	protected void recordOldPosition() {
		oldText = getText();
		oldCursorPos = cursorPos;
	}
	
	/**
	 * Do the undo of the paste, overrideable for custom behaviour
	 * 
	 * @param oldCursorPos before the paste
	 * @param oldText The text before the last paste
	 */
	protected void doUndo(int oldCursorPos, String oldText) {
		if (oldText != null) {
			setText(oldText);
			setCursorPos(oldCursorPos);
		}
	}
	
	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (hasFocus() || mIsSelected) {
			if (key != -1)
			{
				if ((key == Input.KEY_V) && 
				   ((input.isKeyDown(Input.KEY_LCONTROL)) || (input.isKeyDown(Input.KEY_RCONTROL)))) {
					String text = Sys.getClipboard();
					if (text != null) {
						doPaste(text);
					}
					return;
				}
				if ((key == Input.KEY_Z) && 
				   ((input.isKeyDown(Input.KEY_LCONTROL)) || (input.isKeyDown(Input.KEY_RCONTROL)))) {
					if (oldText != null) {
						doUndo(oldCursorPos, oldText);
					}
					return;
				}
				
				// alt and control keys don't come through here
				if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
					return;
				}
				if (input.isKeyDown(Input.KEY_LALT) || input.isKeyDown(Input.KEY_RALT)) {
					return;
				}
			}
			
			if (lastKey != key) {
				lastKey = key;
				repeatTimer = System.currentTimeMillis() + INITIAL_KEY_REPEAT_INTERVAL;
			} else {
				repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
			}
			lastChar = c;
			
			if (key == Input.KEY_LEFT) {
				if (cursorPos > 0) {
					cursorPos--;
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_RIGHT) {
				if (cursorPos < value.length()) {
					cursorPos++;
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_BACK) {
				if ((cursorPos > 0) && (value.length() > 0)) {
					if (cursorPos < value.length()) {
						value = value.substring(0, cursorPos - 1)
								+ value.substring(cursorPos);
					} else {
						value = value.substring(0, cursorPos - 1);
					}
					cursorPos--;
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_DELETE) {
				if (value.length() > cursorPos) {
					value = value.substring(0,cursorPos) + value.substring(cursorPos+1);
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if ((c < 127) && (c > 31) && (value.length() < maxCharacter)) {
				if (cursorPos < value.length()) {
					value = value.substring(0, cursorPos) + c
							+ value.substring(cursorPos);
				} else {
					value = value.substring(0, cursorPos) + c;
				}
				cursorPos++;
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_RETURN) {
				notifyListeners();
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
                                mIsSelected = false;
			}

		}
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#setFocus(boolean)
	 */
	public void setFocus(boolean focus) {
		lastKey = -1;
		
		super.setFocus(focus);
	}
    boolean mIsSelected = false;
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if(getShape().contains(x, y))
        {
            mIsSelected = true;
            if(used == false)
            {
                used = true;
            }
        }
        else
        {
            mIsSelected = false;
        }
    }

        
    @Override
    protected boolean updateSelf(int _delta) {
        if(mIsSelected)
        {
            text = selectedTextColor;
            visibleCursor = true;
        }
        else
        {
            text = unselectedTextColor;
            visibleCursor = false;
        }
        mCursorTimer += _delta;
        return true;
    }
}
