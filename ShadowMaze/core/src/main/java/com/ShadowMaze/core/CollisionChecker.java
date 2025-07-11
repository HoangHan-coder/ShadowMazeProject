/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.core;

import com.ShadowMaze.model.Entity;
import static com.ShadowMaze.model.Entity.Direction.DOWN;
import static com.ShadowMaze.model.Entity.Direction.LEFT;
import static com.ShadowMaze.model.Entity.Direction.RIGHT;
import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.math.Rectangle;

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

        int entityLeftMapX = entity.positionX + (int) entity.solidAreaDefaultX;
        int entityRightMapX = entityLeftMapX + (int) entity.solidArea.width;
        int entityTopMapY = entity.positionY + (int) entity.solidAreaDefaultY;
        int entityBottomMapY = entityTopMapY + (int) entity.solidArea.height;


        int entityLeftCol = entityLeftMapX / GameScreen.TILE_SIZE;
        int entityRightCol = entityRightMapX / GameScreen.TILE_SIZE;
        int entityTopRow = entityTopMapY / GameScreen.TILE_SIZE;
        int entityBottomRow = entityBottomMapY / GameScreen.TILE_SIZE;
        
        int tileNum1, tileNum2;
        
        switch (entity.currentDirection) {
            case UP -> {
                int nextTopY = entityTopMapY - entity.speed;
                int nextTopRow = nextTopY / GameScreen.TILE_SIZE;
                
                tileNum1 = gs.map.tileNum[nextTopRow][entityLeftCol];
                tileNum2 = gs.map.tileNum[nextTopRow][entityRightCol];

                if (gs.map.tiles[tileNum1].collision || gs.map.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case DOWN -> {
                int nextBottomY = entityBottomMapY + entity.speed;
                int nextBottomRow = nextBottomY / GameScreen.TILE_SIZE;
                tileNum1 = gs.map.tileNum[nextBottomRow][entityLeftCol];
                tileNum2 = gs.map.tileNum[nextBottomRow][entityRightCol];

                if (gs.map.tiles[tileNum1].collision || gs.map.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case LEFT -> {
                int nextLeftX = entityLeftMapX - entity.speed;
                int nextLeftCol = nextLeftX / GameScreen.TILE_SIZE;
                tileNum1 = gs.map.tileNum[entityTopRow][nextLeftCol];
                tileNum2 = gs.map.tileNum[entityBottomRow][nextLeftCol];

                if (gs.map.tiles[tileNum1].collision || gs.map.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case RIGHT -> {
                int nextRightX = entityRightMapX + entity.speed;
                int nextRightCol = nextRightX / GameScreen.TILE_SIZE;
                tileNum1 = gs.map.tileNum[entityTopRow][nextRightCol];
                tileNum2 = gs.map.tileNum[entityBottomRow][nextRightCol];

                if (gs.map.tiles[tileNum1].collision || gs.map.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            
        }      
    }
    

    public int checkObject(Entity entity, boolean player) {
    int index = -1;

    for (int i = 0; i < gs.obj.length; i++) {
        if (gs.obj[i] != null) {
            Rectangle entityRect = new Rectangle(
                entity.positionX + entity.solidAreaDefaultX,
                entity.positionY + entity.solidAreaDefaultY,
                entity.solidArea.width,
                entity.solidArea.height
            );

            Rectangle objectRect = new Rectangle(
                gs.obj[i].mapX + gs.obj[i].solidAreaDefaultX,
                gs.obj[i].mapY + gs.obj[i].solidAreaDefaultY,
                gs.obj[i].solidArea.width,
                gs.obj[i].solidArea.height
            );

            switch (entity.currentDirection) {
                case UP -> entityRect.y -= entity.speed;
                case DOWN -> entityRect.y += entity.speed;
                case LEFT -> entityRect.x -= entity.speed;
                case RIGHT -> entityRect.x += entity.speed;
            }

            if (entityRect.overlaps(objectRect)) {
                if (gs.obj[i].collision) {
                    entity.collisionOn = true;
                }
                if (player) {
                    index = i;
                }
            }
        }
    }

    return index;
}

}
