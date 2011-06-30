/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author alasdair
 */
class AIEntityState
{
    static private final int tarStickingTimer = 60;
    static private final int jumpReload = 60;
    enum State
    {
        eFalling,
        eStanding,
        eStandingOnTar,
        eStillCoveredInTar,
        eIce,
        eDead,
        eJumping,
        eStatesMax,
    }
    private State mState;
    private int mTarCount, mIceCount, mContactCount;
    private int mTimer;
    private AIEntity mEntity;
    
    AIEntityState(AIEntity _entity)
    {
        mEntity = _entity;
        mState = State.eFalling;
        mTarCount = mIceCount = mContactCount = mTimer = 0;
    }
    
    State getState()
    {
        return mState;
    }
    
    void update(int _tarCount, int _iceCount, int _contactCount)
    {
        mTarCount = _tarCount;
        mIceCount = _iceCount;
        mContactCount = _contactCount;
        mTimer++;
        update();
    }
    void kill()
    {
        changeState(State.eDead);
        mTimer = 0;
    }
    void unkill()
    {
        changeState(State.eFalling);
        update();
    }
    void jump()
    {
        changeState(State.eJumping);
        mTimer = 0;
    }
    
    private void update()
    {
        switch (mState)
        {
            case eFalling:
            {
                if (mContactCount != 0)
                {
                    changeState(State.eStanding);
                    update();
                }
                break;
            }
            case eJumping:
            {
                if (mContactCount == 0)
                {
                    changeState(State.eFalling);
                    update();
                }
                else if (mTimer == jumpReload)
                {
                    changeState(State.eStanding);
                    update();
                }
                break;
            }
            case eStanding:
            {
                if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                else if (mIceCount != 0)
                {
                    changeState(State.eIce);
                }
                break;
            }
            case eStandingOnTar:
            {
                if (mTarCount == 0)
                {
                    changeState(State.eStillCoveredInTar);
                    mTimer = 0;
                }
                break;
            }
            case eStillCoveredInTar:
            {
                if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                else
                {
                    if (mTimer == tarStickingTimer)
                    {
                        changeState(State.eFalling);
                        update();
                    }
                }
                break;
            }
            case eIce:
            {
                if (mIceCount == 0)
                {
                    changeState(State.eFalling);
                    update();
                }
                else if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                break;
            }
            case eDead:
            {
                break;
            }
        }
    }
    private void changeState(State _newState)
    {
        mState = _newState;
    }
}
