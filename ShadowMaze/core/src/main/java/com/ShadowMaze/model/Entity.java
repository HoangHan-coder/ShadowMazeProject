/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author NgKaitou
 */
public class Entity {
    public int positionX; // position x in map
    public int positionY; // position y in map
    public int speed;
    Texture image;
    
    public Rectangle solidArea = new Rectangle();
    
    public static enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE
    }
    public Direction currentDirection = Direction.IDLE;
    public boolean collisionOn = false;
    
    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }
    
    
}
