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
 * GameScreen is the main screen for rendering gameplay, UI, and handling core
 * game loop functions. It includes map loading, player setup, HUD rendering,
 * and input handling.
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
     *
     * @param game The main Game instance
     */
    public GameScreen(Game game) {
        this.batch = new SpriteBatch();
        this.game = game;
    }

    /**
     * Called when this screen becomes the current screen. Initializes and sets
     * up the game.
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

    /**
     * Checks if the knight is close enough to any enemy object. If a collision
     * is detected (within half tile size), the game switches to a battle
     * screen.
     */
    private void checkEnemyCollisionAndSwitchMap() {
        // Iterate through all objects in the map
        for (SuperObject object : obj) {
            // Check if object is not null and is an instance of OBJ_Enemy
            if (object != null && object instanceof OBJ_Enemy) {
                // Calculate distance between knight and enemy
                float dx = knight.getPositionX() - object.mapX;
                float dy = knight.getPositionY() - object.mapY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                // If knight is close enough to enemy, switch to battle screen
                if (distance < GameScreen.TILE_SIZE / 2f) {
                    game.setScreen(new BattleScreen(game));
                    break; // Exit loop after switching screen
                }
            }
        }
    }

    /**
     * Called every frame to update game logic and render all components.
     * Handles input, collision detection, rendering of map, player, objects,
     * UI, and transitions to battle screen.
     *
     * @param delta Time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        // If the map is paused (e.g., in menu or cutscene), skip updates and only draw current frame
        if (map.isPaused()) {
            ScreenUtils.clear(0, 0, 0, 1);  // Clear the screen to black

            batch.begin();
            map.drawMap();                 // Draw only the map background
            batch.end();

            stage.act(delta);              // Update UI stage actors (buttons, dialogs)
            stage.draw();                  // Render the UI stage
            return;                        // Exit render early
        }

        // Handle player keyboard input (movement, interaction, etc.)
        knight.inputHandle(delta);

        // Update knight's position, state, and physics (e.g., gravity, animation)
        knight.update(delta);

        // Check if the player has collided with any enemy and transition to battle screen
        checkEnemyCollisionAndSwitchMap();

        // Clear the screen to black
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        // Draw tile-based game map relative to player's position
        map.drawMap();

        // Only render player if the map is not in "background only" mode
        // (e.g., when paused for dialog or event)
        if (!map.isBackgroundOnly()) {
            knight.knightRender(delta);
        }

        // Loop through all objects and render them if they exist
        for (SuperObject obj1 : obj) {
            if (obj1 != null) {
                obj1.drawObject(this); // Draw the object using current screen context
            }
        }

        // Render knight again (this seems redundant; might be intentional for layering)
        knight.knightRender(delta);

        // Debug output to console showing player's current tile coordinates
        System.out.println("Player at: (" + knight.getPositionX() / TILE_SIZE + ", " + knight.getPositionY() / TILE_SIZE + ")");

        batch.end();  // Finish drawing sprites

        // Apply same camera projection to shapeRenderer as SpriteBatch
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Optional: render stamina bar as shapes (currently commented out)
        // staminaBar.render(shapeRenderer);
        // Update knight's HP bar (e.g., animations, transitions)
        hpBar.update(delta);

        // Begin new batch for UI rendering
        batch.begin();
        hpBar.render(batch);               // Draw HP bar

        staminaBar.renderIcon(batch);     // Draw stamina icon

        batch.end();                       // Finish UI drawing

        // Update and draw UI stage (includes buttons, overlays, etc.)
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     *
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
