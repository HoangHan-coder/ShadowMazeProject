/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.core;

import com.ShadowMaze.model.OBJ_Key;
import com.ShadowMaze.screen.GameScreen;

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
        gs.obj[0].mapX = 11 * GameScreen.TILE_SIZE;
        gs.obj[0].mapY = 47 * GameScreen.TILE_SIZE;
    }
        
}
