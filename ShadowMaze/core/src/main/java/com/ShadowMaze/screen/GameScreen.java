package com.ShadowMaze.screen;

import com.ShadowMaze.core.AssetSetter;
import com.ShadowMaze.core.CollisionChecker;
<<<<<<< Updated upstream
import com.ShadowMaze.model.Entity.Direction;
=======
>>>>>>> Stashed changes
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.model.Map;
import com.ShadowMaze.model.ScoreBoard;
import com.ShadowMaze.skill.Sword;
import com.ShadowMaze.render.MirrorRenderer;
import com.ShadowMaze.skill.Fireball;
import com.badlogic.gdx.*;
import object.SuperObject;
import object.OBJ_Enemy;
import com.ShadowMaze.uis.HpBar;
import com.ShadowMaze.uis.StaminaBar;
import com.ShadowMaze.uis.UI;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
<<<<<<< Updated upstream
import com.badlogic.gdx.math.Vector2;
=======
>>>>>>> Stashed changes
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Iterator;


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

    // Map setting
    public static final int MAP_X = 121;
    public static final int MAP_Y = 83*2;
    public static final int MAP_WIDTH = MAP_X * TILE_SIZE;
    public static final int MAP_HEIGHT = MAP_Y * TILE_SIZE;

    // Maze and rendering
    protected Game game;
    public final SpriteBatch batch;
//    private int[][] maze = new int[MAX_SCREEN_ROW][MAX_SCREEN_COL];
//    private int offsetX, offsetY;

    
    // Sytem
    public Map map;
    public CollisionChecker cCheck;
    public UI ui = new UI(this);
    public SuperObject[] obj = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);
    public Knight knight;
    private StaminaBar staminaBar;
    private ShapeRenderer shapeRenderer;
    private MirrorRenderer mirrorRenderer;
    private Stage stage;
    private boolean isPaused = false;
    private HpBar hpBar;
    private Array<Sword> fireSkill = new Array<>();
    public ScoreBoard scoreBoard;

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
        staminaBar = new StaminaBar(300, 30, 200, 20, 100, staminaIcon);

        Texture hpBg = new Texture(Gdx.files.internal("menu/function/type5.png"));
        Texture hpFill = new Texture(Gdx.files.internal("menu/function/type6.png"));
        hpBar = new HpBar(170, 30, 200, 20, 100, hpBg, hpFill);
        cCheck = new CollisionChecker(this);
        knight = new Knight(this, hpBar);
        spawnEnemiesFromWalkableTiles(map, 4); // Spawn 4 enemy

//        cCheck = new CollisionChecker(this);
        // Initialize player
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        map = new Map(this, game);
        map.createButtons(stage);
        shapeRenderer = new ShapeRenderer();
        // Center the maze if needed
//        offsetX = 0; // Set to (SCREEN_WIDTH - MAX_SCREEN_COL * TILE_SIZE) / 2 if centered rendering is needed
//        offsetY = 0;
        scoreBoard = new ScoreBoard();
    }

    public void spawnEnemiesFromWalkableTiles(Map map, int numEnemies) {
        int mapHeight = map.tileNum.length;
        int mapWidth = map.tileNum[0].length;

        Array<int[]> walkableTiles = new Array<>();

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map.tileNum[y][x] == 1) {
                    walkableTiles.add(new int[]{x, y});
                }
            }
        }

        int placed = 0;
        for (int i = 0; i < obj.length && placed < numEnemies; i++) {
            if (obj[i] == null) {
                int[] tile = walkableTiles.random();
                OBJ_Enemy enemy = new OBJ_Enemy();
                enemy.mapX = tile[0] * TILE_SIZE;
                enemy.mapY = tile[1] * TILE_SIZE;
                obj[i] = enemy;
                placed++;
            }
        }
    }

    /**
     * Checks if the knight is close enough to any enemy object. If a collision
     * is detected (within half tile size), the game switches to a battle
     * screen.
     */
    private void checkGateCollisionAndChangeMap() {
//        for (SuperObject object : obj) {
//            if (object instanceof object.OBJ_CaveExit) {
//                float dx = knight.getPositionX() - object.mapX;                    // Calculate horizontal distance from player to gate
//                float dy = knight.getPositionY() - object.mapY;                    // Calculate vertical distance from player to gate
//                float distance = (float) Math.sqrt(dx * dx + dy * dy);            // Calculate Euclidean distance to gate
//
//                if (distance < TILE_SIZE / 1f) {                                   // If player is close enough to the gate
//                    String currentMap = map.getMapPath();                         // Get current map path (must be set in changeMap)
////                    String nextMap = "";                                          // Target map to switch to
//                    int spawnX = 3 * TILE_SIZE;                                   // Default spawn X after switching map
//                    int spawnY = 3 * TILE_SIZE;                                   // Default spawn Y after switching map
//
//                    System.out.println("Current map: " + currentMap);
//                    System.out.println("Knight position: ("
//                            + knight.getPositionX() / TILE_SIZE + ", "
//                            + knight.getPositionY() / TILE_SIZE + ")");
//
//                    // Determine the next map based on current map
//                    if (currentMap.equals("maps/map_01.txt")) {
////                        nextMap = "maps/map_03.txt";                              // From map 1 ? map 3
//                        spawnX = 3 * TILE_SIZE;
//                        spawnY = 3 * TILE_SIZE;
//                    } else if (currentMap.equals("maps/map_02.txt")) {
////                        nextMap = "maps/map_01.txt";                              // From map 2 ? map 1
//                        spawnX = 1 * TILE_SIZE;
//                        spawnY = 5 * TILE_SIZE;
//                    } else if (currentMap.equals("maps/map_03.txt")) {
//                        nextMap = "maps/map_01.txt";                              // From map 3 ? map 1
//                        spawnX = 5 * TILE_SIZE;
//                        spawnY = 5 * TILE_SIZE;
//                    }
//
//                    // Switch to new map if valid
//                    if (!nextMap.isEmpty()) {
//                        System.out.println("Switching to: " + nextMap);
//                        map.changeMap(nextMap);                                   // Change to the new map
//                        knight.setPosition(spawnX, spawnY);                       // Move player to the new spawn location
//                        aSetter.setObject();                                      // Reset objects on the new map
//                        spawnEnemiesFromWalkableTiles(map, 4);                    // Respawn enemies
//                    }
//
//                    break; // Stop checking after the first gate collision
//                }
//            }
//        }
    }

    @Override
    public void render(float delta) {
        // === Handle paused state (e.g., menu, cutscene) ===
        if (map.isPaused()) {
            ScreenUtils.clear(0, 0, 0, 1);  // Clear screen

            batch.begin();
            map.drawMap();                 // Draw background only
            batch.end();

            stage.act(delta);             // Update UI logic
            stage.draw();                 // Render UI
            return;
        }

        // === UPDATE PHASE ===
        knight.inputHandle(delta);
        knight.update(delta);

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null && obj[i] instanceof OBJ_Enemy enemy) {
                enemy.update(Gdx.graphics.getDeltaTime(), this);
            }
        }
<<<<<<< Updated upstream
=======

        for (Iterator<Fireball> it = fireball.iterator(); it.hasNext();) {
            Fireball fireball = it.next();
            fireball.update(delta);
            cCheck.checkFireballCollision(fireball); // G?I CHECK VA CH?M T?I ?�Y

            if (!fireball.isActive()) {
                it.remove(); // Remove n?u ?� h?t hi?u l?c
            }
        }
>>>>>>> Stashed changes

        checkGateCollisionAndChangeMap();  // Handle map transition if near gate

        hpBar.update(delta);  // Update knight HP bar
        // Nếu staminaBar cần update animation, bạn có thể gọi thêm update ở đây (nếu có)

        // === GAME RENDER PHASE ===
        batch.begin();

        map.drawMap();  // Draw tile-based background

        // Draw all objects (including enemies, items, gates...)
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].drawObject(this);  
            }
        }

        // Draw knight (once only)
        if (!map.isBackgroundOnly()) {
            knight.knightRender(delta);
            System.out.println("position: (" + knight.positionX/TILE_SIZE + ", " + knight.positionY/TILE_SIZE + ")");
        }

        // Draw scoreboard
        scoreBoard.render(batch, 30, 650);
        ui.render();
        batch.end();

        // === HUD & UI RENDER PHASE ===
        // Apply same camera to shapeRenderer if needed
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // HUD: render Knight's HP bar and Stamina icon
        hpBar.render(batch);
        staminaBar.renderIcon(batch);

        // UI Stage: pause menu, dialogs, etc.
        stage.act(delta);
        stage.draw();
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

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
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