<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.core;

=======
package com.ShadowMaze.core;

import static com.ShadowMaze.model.Entity.Direction.UP;
import static com.ShadowMaze.model.Entity.Direction.DOWN;
import static com.ShadowMaze.model.Entity.Direction.LEFT;
import static com.ShadowMaze.model.Entity.Direction.RIGHT;

>>>>>>> cdb984c22c4c47711bb36fdef5bf83223f1d7b33
import com.ShadowMaze.model.Entity;
import com.ShadowMaze.screen.GameScreen;

/**
<<<<<<< HEAD
 *
 * @author NgKaitou
 */
public class CollisionChecker {
    
=======
 * Ki?m tra va ch?m c?a th?c th? (Entity) v?i tile map.
 */
public class CollisionChecker {

>>>>>>> cdb984c22c4c47711bb36fdef5bf83223f1d7b33
    GameScreen gs;

    public CollisionChecker(GameScreen gs) {
        this.gs = gs;
    }
<<<<<<< HEAD
    
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
=======

    public void checkTile(Entity entity) {

        int entityLeftMapX = entity.positionX + (int) entity.solidArea.x;
        int entityRightMapX = entityLeftMapX + (int) entity.solidArea.width;
        int entityTopMapY = entity.positionY + (int) entity.solidArea.y;
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
>>>>>>> cdb984c22c4c47711bb36fdef5bf83223f1d7b33
        }
    }
}
