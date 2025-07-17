package com.ShadowMaze.core;

import object.OBJ_CaveExit;
import object.OBJ_Key;
import com.ShadowMaze.screen.GameScreen;
import object.OBJ_Chest;
import object.OBJ_Gate;
import object.OBJ_Gem;

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
        gs.obj[0].mapX = 55 * GameScreen.TILE_SIZE;
        gs.obj[0].mapY = 36 * GameScreen.TILE_SIZE;

        gs.obj[7] = new OBJ_Key();
        gs.obj[7].mapX = 87 * GameScreen.TILE_SIZE;
        gs.obj[7].mapY = 24 * GameScreen.TILE_SIZE;
        
        gs.obj[8] = new OBJ_Key();
        gs.obj[8].mapX = 65 * GameScreen.TILE_SIZE;
        gs.obj[8].mapY = 18 * GameScreen.TILE_SIZE;

        gs.obj[1] = new OBJ_Chest();
        gs.obj[1].name = "ScollFireChest";
        gs.obj[1].mapX = 26 * GameScreen.TILE_SIZE;
        gs.obj[1].mapY = 65 * GameScreen.TILE_SIZE;
        
        gs.obj[2] = new OBJ_Chest();
        gs.obj[2].name = "ScollIceChest";
        gs.obj[2].mapX = 92 * GameScreen.TILE_SIZE;
        gs.obj[2].mapY = 61 * GameScreen.TILE_SIZE;
        
        gs.obj[3] = new OBJ_Chest();
        gs.obj[3].name = "ScollThunderChest";
        gs.obj[3].mapX = 34 * GameScreen.TILE_SIZE;
        gs.obj[3].mapY = 19 * GameScreen.TILE_SIZE;
        

        gs.obj[4] = new OBJ_Gate();
        gs.obj[4].mapX = 34 * GameScreen.TILE_SIZE;
        gs.obj[4].mapY = 17 * GameScreen.TILE_SIZE;
        
        gs.obj[5] = new OBJ_Gate();
        gs.obj[5].mapX = 92 * GameScreen.TILE_SIZE;
        gs.obj[5].mapY = 59 * GameScreen.TILE_SIZE;
        
        gs.obj[9] = new OBJ_Gate();
        gs.obj[9].mapX = 26 * GameScreen.TILE_SIZE;
        gs.obj[9].mapY = 63 * GameScreen.TILE_SIZE;
        
        
        gs.obj[6] = new OBJ_CaveExit();
        gs.obj[6].mapX = 25 * GameScreen.TILE_SIZE;
        gs.obj[6].mapY = 44 * GameScreen.TILE_SIZE;
        
        
        gs.obj[11] = new OBJ_Gem();
        gs.obj[11].mapX = 69 * GameScreen.TILE_SIZE;
        gs.obj[11].mapY = 40 * GameScreen.TILE_SIZE;
        
        gs.obj[12] = new OBJ_Gem();
        gs.obj[12].mapX = 70 * GameScreen.TILE_SIZE;
        gs.obj[12].mapY = 40 * GameScreen.TILE_SIZE;
        
        gs.obj[13] = new OBJ_Gem();
        gs.obj[13].mapX = 71 * GameScreen.TILE_SIZE;
        gs.obj[13].mapY = 40 * GameScreen.TILE_SIZE;
        
        
        // map 1
        gs.obj[10] = new OBJ_CaveExit();
        gs.obj[10].mapX = 88 * GameScreen.TILE_SIZE;
        gs.obj[10].mapY = 104 * GameScreen.TILE_SIZE;
        
        gs.obj[14] = new OBJ_Gem();
        gs.obj[14].mapX = 87 * GameScreen.TILE_SIZE;
        gs.obj[14].mapY = 105 * GameScreen.TILE_SIZE;
        
        gs.obj[15] = new OBJ_Gem();
        gs.obj[15].mapX = 88 * GameScreen.TILE_SIZE;
        gs.obj[15].mapY = 105 * GameScreen.TILE_SIZE;
        
        gs.obj[16] = new OBJ_Gem();
        gs.obj[16].mapX = 89 * GameScreen.TILE_SIZE;
        gs.obj[16].mapY = 105 * GameScreen.TILE_SIZE;
        
        gs.obj[17] = new OBJ_Chest();
        gs.obj[17].name = "ScollFireChest";
        gs.obj[17].mapX = 33 * GameScreen.TILE_SIZE;
        gs.obj[17].mapY = 147 * GameScreen.TILE_SIZE;
        
        gs.obj[18] = new OBJ_Chest();
        gs.obj[18].name = "ScollIceChest";
        gs.obj[18].mapX = 35 * GameScreen.TILE_SIZE;
        gs.obj[18].mapY = 147 * GameScreen.TILE_SIZE;
        
        gs.obj[19] = new OBJ_Chest();
        gs.obj[19].name = "ScollThunderChest";
        gs.obj[19].mapX = 37 * GameScreen.TILE_SIZE;
        gs.obj[19].mapY = 147 * GameScreen.TILE_SIZE;
        
        gs.obj[20] = new OBJ_Key();
        gs.obj[20].mapX = 53 * GameScreen.TILE_SIZE;
        gs.obj[20].mapY = 123 * GameScreen.TILE_SIZE;
        
        gs.obj[21] = new OBJ_Gate();
        gs.obj[21].mapX = 36 * GameScreen.TILE_SIZE;
        gs.obj[21].mapY = 142 * GameScreen.TILE_SIZE;
    }
        
}
