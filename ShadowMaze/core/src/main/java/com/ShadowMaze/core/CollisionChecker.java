package com.ShadowMaze.core;

import object.OBJ_CaveExit;
import object.OBJ_Key;
import com.ShadowMaze.screen.GameScreen;
import object.OBJ_Enemy;
import object.OBJ_Gate;
import com.ShadowMaze.model.Entity;
import static com.ShadowMaze.model.Entity.Direction.*;
import com.badlogic.gdx.math.Rectangle;

/**
 * CollisionChecker handles collision detection between entities (like Knight)
 * and the game world, including tiles and interactable objects.
 * 
 * It supports both static tile collision and dynamic object collision (e.g. keys, gates).
 * 
 * Author: NgKaitou
 */
public class CollisionChecker {

    GameScreen gs; // Reference to the game screen context

    /**
     * Constructor links the CollisionChecker to the current GameScreen.
     * 
     * @param gs GameScreen instance to access tile map and game objects
     */
    public CollisionChecker(GameScreen gs) {
        this.gs = gs;
    }

    /**
     * Checks if an entity is about to collide with collidable tiles based on its direction and speed.
     * Sets entity.collisionOn = true if collision is detected.
     * 
     * @param entity The moving entity (e.g., player or NPC)
     */
    public void checkTile(Entity entity) {

        // Calculate entity's bounding box in map coordinates
        int entityLeftMapX = entity.positionX + (int) entity.solidAreaDefaultX;
        int entityRightMapX = entityLeftMapX + (int) entity.solidArea.width;
        int entityTopMapY = entity.positionY + (int) entity.solidAreaDefaultY;
        int entityBottomMapY = entityTopMapY + (int) entity.solidArea.height;

        // Convert pixel positions to tile indices
        int entityLeftCol = entityLeftMapX / GameScreen.TILE_SIZE;
        int entityRightCol = entityRightMapX / GameScreen.TILE_SIZE;
        int entityTopRow = entityTopMapY / GameScreen.TILE_SIZE;
        int entityBottomRow = entityBottomMapY / GameScreen.TILE_SIZE;

        int tileNum1, tileNum2;

        // Check the tile(s) the entity is about to enter based on direction
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

    /**
     * Checks for collision between an entity and game objects (e.g., keys, enemies).
     * Updates entity.collisionOn if the object has collision enabled.
     * Returns the index of the collided object if it's the player and overlapping.
     *
     * @param entity The moving entity (e.g., knight or NPC)
     * @param player If true, returns index of collided object; otherwise returns -1
     * @return index of collided object in gs.obj[] or -1 if none
     */
    public int checkObject(Entity entity, boolean player) {
        int index = -1;

        // Loop through all objects in GameScreen
        for (int i = 0; i < gs.obj.length; i++) {
            if (gs.obj[i] != null) {
                // Create hitbox (Rectangle) for entity
                Rectangle entityRect = new Rectangle(
                    entity.positionX + entity.solidAreaDefaultX,
                    entity.positionY + entity.solidAreaDefaultY,
                    entity.solidArea.width,
                    entity.solidArea.height
                );

                // Create hitbox for object
                Rectangle objectRect = new Rectangle(
                    gs.obj[i].mapX + gs.obj[i].solidAreaDefaultX,
                    gs.obj[i].mapY + gs.obj[i].solidAreaDefaultY,
                    gs.obj[i].solidArea.width,
                    gs.obj[i].solidArea.height
                );

                // Adjust entity rectangle based on its direction and speed
                switch (entity.currentDirection) {
                    case UP -> entityRect.y -= entity.speed;
                    case DOWN -> entityRect.y += entity.speed;
                    case LEFT -> entityRect.x -= entity.speed;
                    case RIGHT -> entityRect.x += entity.speed;
                }

                // Check if rectangles intersect (i.e., collision)
                if (entityRect.overlaps(objectRect)) {
                    // If object blocks movement, set collisionOn = true
                    if (gs.obj[i].collision) {
                        entity.collisionOn = true;
                    }

                    // If entity is the player, record the index of the collided object
                    if (player) {
                        index = i;
                    }
                }
            }
        }

        return index; // Return collided object index or -1 if no collision
    }
}
