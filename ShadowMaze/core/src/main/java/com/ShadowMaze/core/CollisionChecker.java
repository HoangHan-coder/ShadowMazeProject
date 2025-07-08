/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.core;

import com.ShadowMaze.model.Entity;
import com.ShadowMaze.screen.GameScreen;

/**
 *
 * @author NgKaitou
 */
public class CollisionChecker {
    
    GameScreen gs;

    public CollisionChecker(GameScreen gs) {
        this.gs = gs;
    }
    
    public void checkTile(Entity entity) {
        int entityLeftMapX = entity.positionX + (int)entity.solidArea.x;
        int entityRightMapX = entity.positionX + (int)entity.solidArea.x + (int)entity.solidArea.width;
        int entityTopMapY = entity.positionY + (int)entity.solidArea.y;
        int entityBottomtMapY = entity.positionY + (int)entity.solidArea.y + (int)entity.solidArea.height ;
        
        int entityLeftCol = entityLeftMapX / GameScreen.TILE_SIZE;
        int entityRightCol = entityRightMapX / GameScreen.TILE_SIZE;
        int entityTopRow = entityTopMapY / GameScreen.TILE_SIZE;
        int entityBottomRow = entityBottomtMapY / GameScreen.TILE_SIZE;
        
        int tileNum1, tileNum2;
        
        switch (entity.currentDirection) {
            case UP:
                break;
            default:
                throw new AssertionError();
        }
    }
}
