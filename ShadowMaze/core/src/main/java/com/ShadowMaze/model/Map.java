/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.ShadowMaze.screen.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author NgKaitou
 */
public class Map {
    // init map
    GameScreen gs;
    public Tile[] tiles;
    public int[][] tileNum;
    private Texture resumeUp, resumeDown;
    private Texture optionsUp, optionsDown;
    private Texture quitUp, quitDown, quitDisabled;
    private Texture how;
    private Table pauseMenuTable;
    private boolean isPauseMenuVisible = false;
    private boolean isPaused = false;
    private MainMenuScreen screen;
    private Game game;

    public Map(GameScreen gs, Game game) {
        this.gs = gs;
        this.game = game;
        tiles = new Tile[10];
        tileNum = new int[GameScreen.MAP_Y][GameScreen.MAP_X];
        getImageTiles();
        loadMap();
    }

    private void loadMap() {

        try (BufferedReader reader = new BufferedReader(new FileReader("maps\\map_02.txt"))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < GameScreen.MAP_X) {
                String[] tokens = line.trim().split(" ");
                for (int col = 0; col < tokens.length && col < GameScreen.MAP_X; col++) {
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


        tiles[2] = new Tile();
        tiles[2].image = new Texture("tiles/background_tree.png");
    }

   public void drawMap() {

        // draw background
        for (int y = 0; y < GameScreen.SCREEN_HEIGHT / GameScreen.TILE_SIZE; y++) {
            for (int x = 0; x < GameScreen.SCREEN_WIDTH / GameScreen.TILE_SIZE; x++) {
                gs.batch.draw(tiles[2].image, x * 48, y * 48);
            }
        }

        // draw maze
        for (int mapRow = 0; mapRow < GameScreen.MAP_Y; mapRow++) {
            for (int mapCol = 0; mapCol < GameScreen.MAP_X; mapCol++) {
                int tile = tileNum[mapRow][mapCol];
                int mapX = mapCol * GameScreen.TILE_SIZE;
                int mapY = mapRow * GameScreen.TILE_SIZE;

                int screenX = mapX - gs.knight.positionX + gs.knight.renderX;
                int screenY = mapY - gs.knight.positionY + gs.knight.renderY;

                if (mapX + GameScreen.TILE_SIZE > gs.knight.positionX - gs.knight.renderX
                        && mapX - GameScreen.TILE_SIZE < gs.knight.positionX + gs.knight.renderX
                        && mapY + GameScreen.TILE_SIZE > gs.knight.positionY - gs.knight.renderY
                        && mapY - GameScreen.TILE_SIZE < gs.knight.positionY + gs.knight.renderY) {
                    gs.batch.draw(tiles[tile].image, screenX, screenY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
//                }
                }
            }
        }
    }

    public void changeMap(String mapFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFilePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < GameScreen.MAP_Y) {
                String[] tokens = line.trim().split(" ");
                for (int col = 0; col < tokens.length && col < GameScreen.MAP_X; col++) {
                    tileNum[row][col] = Integer.parseInt(tokens[col]);
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Failed to read map file: " + e.getMessage());
        }
    }

    public void createButtons(Stage stage) {
        // N�t nh? g�c tr�i
        resumeUp = new Texture(Gdx.files.internal("menu/function/pause.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(resumeUp));

        // B?ng menu t?m d?ng
        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();

        // T?o c�c n�t trong b?ng menu
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type1.png"))));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type3.png"))));
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type4.png"))));

        // Th�m c�c n�t v�o b?ng menu
        pauseMenuTable.add(resumeButton).size(200, 60).pad(10).row();
        pauseMenuTable.add(optionsButton).size(200, 60).pad(10).row();
        pauseMenuTable.add(quitButton).size(200, 60).pad(10);

        pauseMenuTable.setVisible(false); // ?n ban ??u
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setPaused(true);
                pauseMenuTable.setVisible(true);
            }
        });

        resumeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setPaused(false);
                pauseMenuTable.setVisible(false);
            }
        });

        // G�n s? ki?n cho n�t resume trong menu popup
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        // Table ch?a n�t pause
        Table topLeftTable = new Table();
        topLeftTable.bottom().left().padBottom(20).padLeft(20);
        topLeftTable.setFillParent(true);
        topLeftTable.add(pauseButton).size(100, 40);

        // Th�m m?i th? v�o stage
        stage.addActor(topLeftTable);       // n�t pause nh?
        stage.addActor(pauseMenuTable);     // b?ng menu hi?n ra
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
