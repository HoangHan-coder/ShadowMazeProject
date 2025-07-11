package com.ShadowMaze.screen;

import com.ShadowMaze.core.AssetSetter;
import com.ShadowMaze.core.CollisionChecker;
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.model.Map;
import com.ShadowMaze.render.MirrorRenderer;
import com.badlogic.gdx.*;
import object.SuperObject;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import object.OBJ_Enemy;
import com.ShadowMaze.uis.HpBar;
import com.ShadowMaze.uis.StaminaBar;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameScreen is the main screen for rendering gameplay, UI, and handling core game loop functions.
 * It includes map loading, player setup, HUD rendering, and input handling.
 */
public class GameScreen implements Screen {

    // Tile and screen settings
    public static final int ORIGINAL_TILE_SIZE = 16;  // Base tile size in pixels
    public static final int SCALE = 3;                // Scale factor for rendering
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;  // Scaled tile size

    public static final int MAX_SCREEN_COL = 27;      // Number of horizontal tiles on screen
    public static final int MAX_SCREEN_ROW = 19;      // Number of vertical tiles on screen

    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;   // Screen width in pixels
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;  // Screen height in pixels

    // Map settings
    public static final int MAP_X = 121;              // Map width in tiles
    public static final int MAP_Y = 83;               // Map height in tiles
    public static final int MAP_WIDTH = MAP_X * TILE_SIZE;   // Full map width in pixels
    public static final int MAP_HEIGHT = MAP_Y * TILE_SIZE;  // Full map height in pixels

    // Game and rendering components
    protected final Game game;              // Reference to the LibGDX game object
    public final SpriteBatch batch;         // Batch used for drawing textures

    public Map map;                         // Game map
    public CollisionChecker cCheck;         // Collision detection system
    public SuperObject[] obj = new SuperObject[10];   // Array of objects in the world
    public AssetSetter aSetter = new AssetSetter(this);  // Utility for placing objects
    public Knight knight;                   // Player character
    private StaminaBar staminaBar;          // UI stamina bar
    private ShapeRenderer shapeRenderer;    // Used for drawing UI shapes
    private MirrorRenderer mirrorRenderer;  // (Possibly unused here)
    private Stage stage;                    // UI stage for handling buttons and overlays
    private boolean isPaused = false;       // Game pause state
    private HpBar hpBar;                    // UI HP bar

    /**
     * Constructor that sets up the main batch and stores game reference.
     * @param game The main Game instance
     */
    public GameScreen(Game game) {
        this.batch = new SpriteBatch();
        this.game = game;
    }

    /**
     * Called when this screen becomes the current screen.
     * Initializes and sets up the game.
     */
    @Override
    public void show() {
        setUpGame();    // Setup objects in the map
        initGame();     // Initialize other game elements (player, map, HUD)
    }

    /**
     * Sets up objects in the game using AssetSetter.
     */
    public void setUpGame() {
        aSetter.setObject();
    }

    /**
     * Initializes the map, HUD, player, collision checker and UI buttons.
     */
    public void initGame() {
        stage = new Stage(new ScreenViewport());  // Create new stage with viewport
        Gdx.input.setInputProcessor(stage);       // Set input to be handled by the stage

        map = new Map(this, game);                // Load map
        map.createButtons(stage);                 // Add map buttons to stage

        shapeRenderer = new ShapeRenderer();      // Used for optional shape drawing

        // Load textures and create UI bars
        Texture staminaIcon = new Texture(Gdx.files.internal("menu/function/dragon.png"));
        staminaBar = new StaminaBar(140, 30, 200, 20, 100, staminaIcon);

        Texture hpBg = new Texture(Gdx.files.internal("menu/function/type5.png"));
        Texture hpFill = new Texture(Gdx.files.internal("menu/function/type6.png"));
        hpBar = new HpBar(170, 30, 200, 20, 100, hpBg, hpFill);

        cCheck = new CollisionChecker(this);      // Create collision checker
        knight = new Knight(this, staminaBar, hpBar);  // Create player knight
    }

    private void checkEnemyCollisionAndSwitchMap() {
        for (SuperObject object : obj) {
            if (object != null && object instanceof OBJ_Enemy) {
                float dx = knight.getPositionX() - object.mapX;
                float dy = knight.getPositionY() - object.mapY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance < GameScreen.TILE_SIZE / 2f) {
                    game.setScreen(new BattleScreen(game));
                    break;
                }
            }
        }
    }

    // hello
    @Override
    public void render(float delta) {
        if (map.isPaused()) {
            ScreenUtils.clear(0, 0, 0, 1);
            batch.begin();
            map.drawMap();
            batch.end();
            stage.act(delta);
            stage.draw();
            return;
        }

        knight.inputHandle(delta);
        knight.update(delta);
        checkEnemyCollisionAndSwitchMap();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        map.drawMap();
        if (!map.isBackgroundOnly()) {
            knight.knightRender(delta);
        }
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].drawObject(this);  // Draw individual object
            }
        }

        // Render player character
        knight.knightRender(delta);

        // Debug print player location (tile-based)
        System.out.println("Player at: (" + knight.getPositionX() / TILE_SIZE + ", " + knight.getPositionY() / TILE_SIZE + ")");

        batch.end();               // End drawing batch

        // Set shapeRenderer to use same camera projection
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Optional: Uncomment to render stamina bar as shapes
        // staminaBar.render(shapeRenderer);

        // Update and render HP bar
        hpBar.update(delta);
        batch.begin();
        hpBar.render(batch);

        // Render stamina icon
        staminaBar.renderIcon(batch);

        // Update and draw UI stage
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     * @param width New width
     * @param height New height
     */
    @Override
    public void resize(int width, int height) {
        // No resize behavior yet
    }

    /**
     * Called when the game is paused.
     */
    @Override
    public void pause() {
        // No pause behavior implemented
    }

    /**
     * Called when the game is resumed.
     */
    @Override
    public void resume() {
        // No resume behavior implemented
    }

    /**
     * Called when this screen is no longer the current screen.
     */
    @Override
    public void hide() {
        // No hide behavior implemented
    }

    /**
     * Dispose all disposable assets to free memory.
     */
    @Override
    public void dispose() {
        batch.dispose();       // Dispose sprite batch
        knight.dispose();      // Dispose knight assets
        // Add more disposals as needed (e.g., textures, UI)
    }
}
