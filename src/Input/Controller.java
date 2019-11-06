package Input;

import org.newdawn.slick.Input;

public abstract class Controller 
{
    public static Controller create(Input _input, int _inputId, int _playerIndex)
    {
        if(_input.getAxisCount(_inputId) >= 4)
        {
            return new XBoxController(_inputId, _playerIndex);
        }
        return null;
    }

    int mInputId;
    int mPlayer;
    
    public Controller(int _inputId, int _player)
    {
        mInputId = _inputId;
        mPlayer = _player;
    }

    public void update(Input _input, int _delta)
    {

    }
}