/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.core;

import object.OBJ_CaveExit;
import object.OBJ_Key;
import com.ShadowMaze.screen.GameScreen;
import object.OBJ_Enemy;
import object.OBJ_Gate;

/**
 *
 * @author NgKaitou
 */
public class AssetSetter {

    GameScreen gs;

    public AssetSetter(GameScreen gs) {
        this.gs = gs;
    }

    public void setObject() {

        gs.obj[0] = new OBJ_Key();
        gs.obj[0].mapX = 35 * GameScreen.TILE_SIZE; // 35 62
        gs.obj[0].mapY = 62 * GameScreen.TILE_SIZE;
        gs.obj[1] = new OBJ_CaveExit();
        gs.obj[1].mapX = 88 * GameScreen.TILE_SIZE;
        gs.obj[1].mapY = 21 * GameScreen.TILE_SIZE;
        
        gs.obj[2] = new OBJ_Gate();
        gs.obj[2].mapX = 88 * GameScreen.TILE_SIZE;
        gs.obj[2].mapY = 19 * GameScreen.TILE_SIZE;

        gs.obj[3] = new OBJ_Enemy();  // Thêm enemy dùng gi?ng h?t
        gs.obj[3].mapX = 33 * GameScreen.TILE_SIZE;
        gs.obj[3].mapY = 24 * GameScreen.TILE_SIZE;
    }

}
