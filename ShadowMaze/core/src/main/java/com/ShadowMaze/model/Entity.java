/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * The Entity class represents a movable game object in the world.
 * It provides basic properties like position, movement speed,
 * collision handling area, and facing direction.
 * 
 * This class is typically extended by player characters or NPCs.
 * 
 * Author: NgKaitou
 */
public class Entity {

    /** X coordinate of the entity in the map (in pixels) */
    public int positionX;

    /** Y coordinate of the entity in the map (in pixels) */
    public int positionY;

    /** Movement speed of the entity in pixels per frame */
    public int speed;

    /** Image representing this entity (optional if animated separately) */
    Texture image;

    /** Rectangle used to detect collision with tiles and objects */
    public Rectangle solidArea = new Rectangle();

    /** Default X offset of solidArea from top-left corner of the entity */
    public float solidAreaDefaultX;

    /** Default Y offset of solidArea from top-left corner of the entity */
    public float solidAreaDefaultY;

    /**
     * Enum representing possible directions an entity can face or move
     */
    public static enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE
    }

    /** Current facing or moving direction of the entity */
    public Direction currentDirection = Direction.IDLE;

    /** Flag to indicate whether entity is colliding with a tile or object */
    public boolean collisionOn = false;

    /** @return the current X position (in pixels) */
    public int getPositionX() {
        return positionX;
    }

    /** 
     * Set the entity's X position.
     * @param positionX the new X position (in pixels)
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /** @return the current Y position (in pixels) */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Set the entity's Y position.
     * @param positionY the new Y position (in pixels)
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /** @return the texture used for rendering this entity */
    public Texture getImage() {
        return image;
    }

    /**
     * Assign a texture image to this entity.
     * @param image a Texture object
     */
    public void setImage(Texture image) {
        this.image = image;
    }
}
