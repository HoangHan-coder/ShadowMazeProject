package com.ShadowMaze.screen;

import com.ShadowMaze.core.AssetSetter;
import com.ShadowMaze.core.CollisionChecker;
import com.ShadowMaze.model.Entity.Direction;
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.model.Map;
import com.ShadowMaze.skill.Sword;
import com.ShadowMaze.render.MirrorRenderer;
import com.ShadowMaze.skill.Fireball;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.Iterator;
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

    // Map setting
    public static final int MAP_X = 121;
    public static final int MAP_Y = 83;
    public static final int MAP_WIDTH = MAP_X * TILE_SIZE;
    public static final int MAP_HEIGHT = MAP_Y * TILE_SIZE;

    // Maze and rendering
    protected Game game;
    public final SpriteBatch batch;
//    private int[][] maze = new int[MAX_SCREEN_ROW][MAX_SCREEN_COL];
//    private int offsetX, offsetY;

    public Map map;
    public CollisionChecker cCheck;
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

    private Array<Fireball> fireballs = new Array<>();
    private Fireball currentFireball;
    private boolean wasEPressedLastFrame = false;
    private boolean isSkillAvailable = true;

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
        staminaBar = new StaminaBar(140, 30, 200, 20, 100, staminaIcon);

        Texture hpBg = new Texture(Gdx.files.internal("menu/function/type5.png"));
        Texture hpFill = new Texture(Gdx.files.internal("menu/function/type6.png"));
        hpBar = new HpBar(170, 30, 200, 20, 100, hpBg, hpFill);
        cCheck = new CollisionChecker(this);
        knight = new Knight(this, staminaBar, hpBar);
        spawnEnemiesFromWalkableTiles(map, 4); // Spawn 4 enemy
// khoi tao me cung
//        int pathWidth = 1; // vÃ­ dá»¥ 3
//        mazeGenerator = new MazeGenerator(MAX_SCREEN_COL, MAX_SCREEN_ROW, pathWidth);
//        maze = mazeGenerator.generate(1, 1);
        map = new Map(this, game);
        // print me cung random
//        for (int x = 0; x < maze.length; x++) {
//            for (int y = 0; y < maze[0].length; y++) {
//                System.out.print(maze[x][y] + " ");
//            }
//            System.out.println();
//        }
        // print me cung tu file
//           for (int x = 0; x < map.tileNum.length; x++) {
//            for (int y = 0; y < map.tileNum[0].length; y++) {
//                System.out.print(map.tileNum[x][y]+" ");
//            }
//            System.out.println();
//        }

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
    }

    public void spawnEnemiesFromWalkableTiles(Map map, int numEnemies) {
        Random random = new Random();
        int mapHeight = map.tileNum.length;
        int mapWidth = map.tileNum[0].length;

        Array<int[]> walkableTiles = new Array<>();

        // L?c ô ?i ???c
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map.tileNum[y][x] == 1) {
                    walkableTiles.add(new int[]{x, y});
                }
            }
        }

        int placed = 0;
        for (int i = 0; i < obj.length && placed < numEnemies; i++) {
            if (obj[i] == null) { // Ch? ??t n?u ch?a có object
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
        for (SuperObject object : obj) {
            if (object instanceof object.OBJ_CaveExit) {
                float dx = knight.getPositionX() - object.mapX;                    // Calculate horizontal distance from player to gate
                float dy = knight.getPositionY() - object.mapY;                    // Calculate vertical distance from player to gate
                float distance = (float) Math.sqrt(dx * dx + dy * dy);            // Calculate Euclidean distance to gate

                if (distance < TILE_SIZE / 1f) {                                   // If player is close enough to the gate
                    String currentMap = map.getMapPath();                         // Get current map path (must be set in changeMap)
                    String nextMap = "";                                          // Target map to switch to
                    int spawnX = 3 * TILE_SIZE;                                   // Default spawn X after switching map
                    int spawnY = 3 * TILE_SIZE;                                   // Default spawn Y after switching map

                    System.out.println("Current map: " + currentMap);
                    System.out.println("Knight position: ("
                            + knight.getPositionX() / TILE_SIZE + ", "
                            + knight.getPositionY() / TILE_SIZE + ")");

                    // Determine the next map based on current map
                    if (currentMap.equals("maps/map_01.txt")) {
                        nextMap = "maps/map_03.txt";                              // From map 1 ? map 3
                        spawnX = 3 * TILE_SIZE;
                        spawnY = 3 * TILE_SIZE;
                    } else if (currentMap.equals("maps/map_02.txt")) {
                        nextMap = "maps/map_01.txt";                              // From map 2 ? map 1
                        spawnX = 1 * TILE_SIZE;
                        spawnY = 5 * TILE_SIZE;
                    } else if (currentMap.equals("maps/map_03.txt")) {
                        nextMap = "maps/map_01.txt";                              // From map 3 ? map 1
                        spawnX = 5 * TILE_SIZE;
                        spawnY = 5 * TILE_SIZE;
                    }

                    // Switch to new map if valid
                    if (!nextMap.isEmpty()) {
                        System.out.println("Switching to: " + nextMap);
                        map.changeMap(nextMap);                                   // Change to the new map
                        knight.setPosition(spawnX, spawnY);                       // Move player to the new spawn location
                        aSetter.setObject();                                      // Reset objects on the new map
                        spawnEnemiesFromWalkableTiles(map, 4);                    // Respawn enemies
                    }

                    break; // Stop checking after the first gate collision
                }
            }
        }
    }

    /**
     * Checks if the player (knight) is within a specified range of any enemy.
     *
     * @param range The maximum distance to check (in pixels).
     * @return true if an enemy is within the given range, false otherwise.
     */
    public boolean isNearEnemy(float range) {
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Enemy) {
                OBJ_Enemy enemy = (OBJ_Enemy) object;

                float dx = knight.getPositionX() - enemy.mapX;          // Horizontal distance between knight and enemy
                float dy = knight.getPositionY() - enemy.mapY;          // Vertical distance between knight and enemy
                float distance = (float) Math.sqrt(dx * dx + dy * dy);  // Calculate Euclidean distance

                if (distance <= range) {                                // If enemy is within range, return true
                    return true;
                }
            }
        }
        return false;                                                   // No enemy is within range
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
        // --- Update logic ---
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Enemy) {
                ((OBJ_Enemy) object).update(Gdx.graphics.getDeltaTime(), map);
            }
        }

        // --- DRAW ---
        batch.begin();
        knight.inputHandle(delta);

        // Update knight's position, state, and physics (e.g., gravity, animation)
        knight.update(delta);
        checkGateCollisionAndChangeMap();
        hpBar.update(delta);
        map.drawMap();

        if (!map.isBackgroundOnly()) {
            knight.knightRender(delta);
        }
// C?p nh?t tr?ng thái phím E ?? b?n
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            if (!wasEPressedLastFrame && isSkillAvailable) { // ? Ki?m tra thêm isSkillAvailable
                Vector2 start = new Vector2(knight.positionX, knight.positionY);
                Direction dir = knight.getDirection();
                if (dir != Direction.IDLE) {
                    Fireball fb = new Fireball();
                    fb.setMapSize(map.getMapWidthPixels(), map.getMapHeightPixels());
                    fb.activate(start, dir);
                    fb.shoot();
                    fireballs.add(fb);
                    currentFireball = fb;

                    isSkillAvailable = false; // ? Không cho b?n ti?p
                }
            }
            wasEPressedLastFrame = true;
        } else {
            wasEPressedLastFrame = false;
        }

// C?p nh?t & render các fireball ?ang ho?t ??ng
        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.update(delta);
            if (fireball.isActive()) {
                fireball.render(batch, knight.positionX, knight.positionY);
            } else {
                iterator.remove(); // Xoá n?u không còn active
                isSkillAvailable = true; // ? Cho phép dùng skill l?i
            }
        }

        // Draw all objects
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].drawObject(this);
            }
        }

        batch.end();
        hpBar.render(batch);
        // Draw UI (stamina icon)
        staminaBar.renderIcon(batch);

        // Draw stage UI
        batch.begin();

        // Draw tile-based game map relative to player's position
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
        hpBar.render(batch);               // Draw HP bar

        staminaBar.renderIcon(batch);     // Draw stamina icon

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
