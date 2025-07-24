package com.ShadowMaze.model;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.ShadowMaze.screen.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Map class handles tile-based rendering, pause menu buttons, background
 * overlays, enemy spawn positioning, and map switching logic. It connects
 * closely with GameScreen and Knight for rendering and positioning.
 */
public class Map {

    // Reference to the current GameScreen
    GameScreen gs;

    // Tile image data and map layout
    public Tile[] tiles;                // Array of tile types
    public int[][] tileNum;            // 2D array storing tile indices

    // UI elements and textures
    private Texture resumeUp;
    private Table pauseMenuTable;

    // Pause state and background control
    private boolean isPaused = false;

    // Reference to main menu screen and game context
    private Game game;

    // Optional background overlay
    private Texture backgroundImage;
    private boolean showBackground = false;
    private String mapPath = "maps/map_02.txt"; // Default current map path (used to track which map is active)

    /**
     * Constructor for Map. Initializes tiles and loads map from file.
     *
     * @param gs Reference to GameScreen
     * @param game Reference to main Game object
     */
    public Map(GameScreen gs, Game game) {
        this.gs = gs;
        this.game = game;
        tiles = new Tile[10]; // Prepare 10 tile slots
        tileNum = new int[GameScreen.MAP_Y][GameScreen.MAP_X]; // Allocate map grid
        getImageTiles(); // Load tile textures
        loadMap();       // Load tile layout from file
    }

    /**
     * Reads a text-based map layout and populates tileNum[][] with tile
     * indices.
     */
    private void loadMap() {
        try (BufferedReader reader = new BufferedReader(new FileReader("maps/map_02.txt"))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < GameScreen.MAP_Y) {
                String[] tokens = line.trim().split(" ");
                for (int col = 0; col < tokens.length && col < GameScreen.MAP_X; col++) {
                    tileNum[GameScreen.MAP_Y - 1 - row][col] = Integer.parseInt(tokens[col]);
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Failed to read map file: " + e.getMessage());
        }
    }

    /**
     * Loads textures into the tile array.
     */
    private void getImageTiles() {
        tiles[0] = new Tile();
        tiles[0].image = new Texture("tiles/stone.png");
//        tiles[0].collision = true; // You can uncomm ent this if this tile blocks movement

        tiles[1] = new Tile();
        tiles[1].image = new Texture("tiles/grass.png");

        tiles[2] = new Tile();
        tiles[2].image = new Texture("tiles/background_tree.png");
    }

    /**
     * Returns valid, non-collidable tile positions near the knight within a
     * given radius.
     */
    public List<Vector2> getValidSpawnsNearKnight(int centerX, int centerY, int radiusTiles) {
        List<Vector2> positions = new ArrayList<>();

        for (int row = centerY - radiusTiles; row <= centerY + radiusTiles; row++) {
            for (int col = centerX - radiusTiles; col <= centerX + radiusTiles; col++) {
                if (row >= 0 && row < GameScreen.MAP_Y && col >= 0 && col < GameScreen.MAP_X) {
                    int tile = tileNum[row][col];
                    if (!tiles[tile].collision) {
                        float worldX = col * GameScreen.TILE_SIZE;
                        float worldY = row * GameScreen.TILE_SIZE;
                        positions.add(new Vector2(33, 24)); // May be placeholder? Should be (worldX, worldY)
                    }
                }
            }
        }

        return positions;
    }

    /**
     * Renders the map tiles relative to the player's screen position.
     */
    public void drawMap() {
        // Draw a background image if set
        if (showBackground && backgroundImage != null) {
            gs.batch.draw(backgroundImage, 0, 0, GameScreen.SCREEN_WIDTH, GameScreen.SCREEN_HEIGHT);
            return;
        }

        // Draw static background tiles
        for (int y = 0; y < GameScreen.SCREEN_HEIGHT / GameScreen.TILE_SIZE; y++) {
            for (int x = 0; x < GameScreen.SCREEN_WIDTH / GameScreen.TILE_SIZE; x++) {
                gs.batch.draw(tiles[2].image, x * 48, y * 48);
            }
        }

        // Draw actual maze tiles from map data
        for (int mapRow = 0; mapRow < GameScreen.MAP_Y; mapRow++) {
            for (int mapCol = 0; mapCol < GameScreen.MAP_X; mapCol++) {
                int tile = tileNum[mapRow][mapCol];
                int mapX = mapCol * GameScreen.TILE_SIZE;
                int mapY = mapRow * GameScreen.TILE_SIZE;

                float screenX = mapX - gs.knight.positionX + gs.knight.renderX;
                float screenY = mapY - gs.knight.positionY + gs.knight.renderY;

                // Only draw if tile is within screen bounds
                if (mapX + GameScreen.TILE_SIZE > gs.knight.positionX - gs.knight.renderX
                        && mapX - GameScreen.TILE_SIZE < gs.knight.positionX + gs.knight.renderX
                        && mapY + GameScreen.TILE_SIZE > gs.knight.positionY - gs.knight.renderY
                        && mapY - GameScreen.TILE_SIZE < gs.knight.positionY + gs.knight.renderY) {
                    gs.batch.draw(tiles[tile].image, screenX, screenY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
                }
            }
        }
    }

    /**
     * Sets the map background image to show instead of tile map.
     *
     * @param imagePath file path to background texture
     */
    public void setBackground(String imagePath) {
        backgroundImage = new Texture(Gdx.files.internal(imagePath));
        showBackground = true;
    }

    /**
     * Creates pause button and in-game menu including Resume, Options, and
     * Quit.
     *
     * @param stage The UI stage to add buttons to
     */
    public void createButtons(Stage stage) {
        resumeUp = new Texture(Gdx.files.internal("menu/function/pause.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(resumeUp));

        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();

        // Create menu buttons
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type7.png"))));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type3.png"))));
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type4.png"))));

        // Add buttons to table with padding
        pauseMenuTable.add(resumeButton).size(200, 60).pad(10).row();
        pauseMenuTable.add(optionsButton).size(200, 60).pad(10).row();
        pauseMenuTable.add(quitButton).size(200, 60).pad(10);

        pauseMenuTable.setVisible(false);

        // Pause button toggles menu visibility
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused(true);
                pauseMenuTable.setVisible(true);
            }
        });

        // Resume button hides pause menu
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused(false);
                pauseMenuTable.setVisible(false);
            }
        });

        // Quit button transitions to main menu with fade effect
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        // Add pause button to top-left corner of screen
        Table topLeftTable = new Table();
        topLeftTable.bottom().left().padBottom(20).padLeft(20);
        topLeftTable.setFillParent(true);
        topLeftTable.add(pauseButton).size(100, 40);

        stage.addActor(topLeftTable);
        stage.addActor(pauseMenuTable);
    }

    /**
     * Returns a list of all tiles that are valid spawn points for enemies.
     *
     * @return
     */
    public List<Vector2> getValidEnemySpawnPositions() {
        List<Vector2> validPositions = new ArrayList<>();

        for (int row = 0; row < GameScreen.MAP_Y; row++) {
            for (int col = 0; col < GameScreen.MAP_X; col++) {
                int tileIndex = tileNum[row][col];
                if (!tiles[tileIndex].collision) {
                    float worldX = col * GameScreen.TILE_SIZE;
                    float worldY = row * GameScreen.TILE_SIZE;
                    validPositions.add(new Vector2(worldX, worldY));
                }
            }
        }

        return validPositions;
    }

    public int getMapWidthPixels() {
        return GameScreen.MAP_X * GameScreen.TILE_SIZE;
    }

    public int getMapHeightPixels() {
        return GameScreen.MAP_Y * GameScreen.TILE_SIZE;
    }

    /**
     * Sets the game's pause state.
     *
     * @param paused true to pause, false to resume
     */
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    /**
     * Returns whether the game is currently paused.
     *
     * @return
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Returns whether only background is shown instead of tile map.
     *
     * @return
     */
    public boolean isBackgroundOnly() {
        return showBackground;
    }

    public String getMapPath() {
        return this.mapPath;
    }
}
