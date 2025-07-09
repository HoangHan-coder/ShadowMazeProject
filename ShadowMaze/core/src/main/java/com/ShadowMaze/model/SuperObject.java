/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author NgKaitou
 */
public class SuperObject {
    
    public String name;
    public Texture image;
    public boolean collision = false;
    public int mapX;
    public int mapY;
    
    public void drawObject(GameScreen gs) {
        
        int screenX = mapX - gs.knight.positionX + gs.knight.renderX;
        int screenY = mapY - gs.knight.positionY + gs.knight.renderY;

        if (mapX + GameScreen.TILE_SIZE > gs.knight.positionX - gs.knight.renderX
                && mapX - GameScreen.TILE_SIZE < gs.knight.positionX + gs.knight.renderX
                && mapY + GameScreen.TILE_SIZE > gs.knight.positionY - gs.knight.renderY
                && mapY - GameScreen.TILE_SIZE < gs.knight.positionY + gs.knight.renderY) {
            gs.batch.draw(image, screenX, screenY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }
    }
    
}
