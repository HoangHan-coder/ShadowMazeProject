package com.ShadowMaze.core;

import com.ShadowMaze.model.Entity;
import object.OBJ_CaveExit;
import object.OBJ_Key;
import com.ShadowMaze.screen.GameScreen;
import object.OBJ_Coin;
import object.OBJ_Enemy;
import object.OBJ_Gate;

/**
 * AssetSetter is responsible for placing game objects (e.g., keys, gates, enemies)
 * onto the map at predefined positions. It helps initialize the gameplay environment.
 * This class works closely with GameScreen and its obj[] array.
 * 
 * Example objects placed: Key, Gate, Enemy, Cave Exit.
 * 
 * Author: NgKaitou
 */
public class AssetSetter {

    GameScreen gs; // Reference to GameScreen where the objects will be added

    /**
     * Constructor links this setter to a specific GameScreen instance.
     * 
     * @param gs the GameScreen where objects will be added
     */
    public AssetSetter(GameScreen gs) {
        this.gs = gs;
    }

    /**
     * Places predefined game objects (key, cave exit, gate, enemy) onto the map.
     * Object positions are hardcoded and scaled based on tile size.
     */
    public void setObject() {

        // Create a key object and place it at tile (35, 62)
        gs.obj[0] = new OBJ_Key();
        gs.obj[0].mapX = 35 * GameScreen.TILE_SIZE;
        gs.obj[0].mapY = 62 * GameScreen.TILE_SIZE;

        // Create a cave exit and place it at tile (88, 21)
        gs.obj[1] = new OBJ_CaveExit();
        gs.obj[1].mapX = 88 * GameScreen.TILE_SIZE;
        gs.obj[1].mapY = 21 * GameScreen.TILE_SIZE;

        // Create a gate and place it just below the cave exit at (88, 19)
        gs.obj[2] = new OBJ_Gate();
        gs.obj[2].mapX = 88 * GameScreen.TILE_SIZE;
        gs.obj[2].mapY = 19 * GameScreen.TILE_SIZE;
        
        gs.obj[4] = new OBJ_Gate();
        gs.obj[4].mapX = 37 * GameScreen.TILE_SIZE;
        gs.obj[4].mapY = 28 * GameScreen.TILE_SIZE;
        
    }
        
}
