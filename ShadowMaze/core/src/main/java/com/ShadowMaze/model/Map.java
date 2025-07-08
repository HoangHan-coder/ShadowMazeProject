/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author NgKaitou
 */
public class Map {

    GameScreen gs;
    Tile[] tiles;
    public int[][] tileNum;

    public Map(GameScreen gs) {
        this.gs = gs;
        tiles = new Tile[10];
        tileNum = new int[GameScreen.MAP_Y][GameScreen.MAP_X];
        getImageTiles();
        loadMap();
    }

    private void loadMap() {

        try (BufferedReader reader = new BufferedReader(new FileReader("maps\\map_03.txt"))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row <GameScreen.MAP_X) {
                String[] tokens = line.trim().split(" ");
                for (int col = 0; col < tokens.length && col <GameScreen.MAP_X; col++) {
                    tileNum[row][col] = Integer.parseInt(tokens[col]);
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Failed to read map file: " + e.getMessage());
        }
    }

    private void getImageTiles() {
        
        
        tiles[0] = new Tile();
        tiles[0].image = new Texture("tiles/stone.png");
        tiles[0].collision = true;

        tiles[1] = new Tile();
        tiles[1].image = new Texture("tiles/soil.png");
        
        tiles[2] = new Tile();
        tiles[2].image = new Texture("tiles/background_01.png");
    }
    
    public void drawMap() {
        
        // draw background
        for (int y = 0; y < GameScreen.SCREEN_HEIGHT / GameScreen.TILE_SIZE; y++) {
            for (int x = 0; x < GameScreen.SCREEN_WIDTH / GameScreen.TILE_SIZE; x++) {
                gs.batch.draw(tiles[2].image, x * 48, y * 48);
            }
        }
        
        // draw maze
        for (int mapRow = 0; mapRow <GameScreen.MAP_Y; mapRow++) {
            for (int mapCol = 0; mapCol <GameScreen.MAP_X; mapCol++) {
                int tile = tileNum[mapRow][mapCol];
                int mapX = mapCol * GameScreen.TILE_SIZE;
                int mapY = mapRow * GameScreen.TILE_SIZE;
                
                int screenX = mapX - gs.knight.positionX + gs.knight.renderX;
                int screenY = mapY - gs.knight.positionY + gs.knight.renderY;
                
                if (mapX + GameScreen.TILE_SIZE > gs.knight.positionX - gs.knight.renderX
                        && mapX - GameScreen.TILE_SIZE < gs.knight.positionX + gs.knight.renderX
                        && mapY + GameScreen.TILE_SIZE > gs.knight.positionY - gs.knight.renderY
                        && mapY - GameScreen.TILE_SIZE < gs.knight.positionY + gs.knight.renderY ) {
                    gs.batch.draw(tiles[tile].image, screenX, screenY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
                }
            }
        }
    }
}
