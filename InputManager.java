package game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author Niren Orie
 */
public class InputManager {
//    int [] keyStates;
//    int numKeys;
//    int current;
//    public InputManager()
//    {
//        current = 0;
//        numKeys = 256;
//        keyStates = new int[numKeys];
//    }
//
//    public void setKeyDown(int code)
//    {
//        if(!contains(code))
//        {
//            keyStates[current] = code;
//            current++;
//        }
//    }
//
//    public void setKeyUp(int code)
//    {
//        if(contains(code))
//        {
//            int i = 0;
//            while(keyStates[i] != code) i++;
//            keyStates[i] = keyStates[current-1];
//            current--;
//        }
//    }
//
//    public boolean isKeyUp(int code)
//    {
//        if(contains(code))
//            return false;
//        return true;
//    }
//
//    public boolean isKeyDown(int code)
//    {
//        if(contains(code))
//            return true;
//        return false;
//    }
//
//    boolean contains(int code)
//    {
//        for(int i = 0; i < current; i++)
//        {
//            if(keyStates[i] == code)
//                return true;
//        }
//        return false;
//    }
    boolean [][] keyStates;
    int numKeys;
    int  currState,lastState;
    public InputManager()
    {
        currState = 1;
        lastState = 0;
        numKeys = 525;
        keyStates = new boolean[numKeys][2];//2 column array for last key state and current key state
    }

    public void setKeyDown(int code)
    {
       // System.out.println("setKeyDown()");
        keyStates[code][lastState] = keyStates[code][currState];
        keyStates[code][currState] = true;
    }

    public void setKeyUp(int code)
    {
       // System.out.println("setKeyUp()");
        keyStates[code][lastState] = keyStates[code][currState];
        keyStates[code][currState] = false;
    }

    public boolean isKeyDown(int code)
    {
        return keyStates[code][currState];
    }

    //this returns true if a key was pressed and released
    public boolean isKeyTyped(int code){
        if(keyStates[code][lastState]&&!keyStates[code][currState]){
            keyStates[code][lastState] = false;
            return true;
        }
            
        return false;
    }
    //check for the inital key down
    //this returns true only if the key was up in the previous update but down now
    //created to prevent player from jumping continuously while the jump key was held down
    public boolean isInitPressed(int code){
        //System.out.println("Init pressed ks: " + keyStates[code][lastState] + " " + keyStates[code][currState]);
        if(!keyStates[code][lastState]&&keyStates[code][currState]){
            keyStates[code][lastState] = true;
            return true;
        }

        return false;
    }
}
