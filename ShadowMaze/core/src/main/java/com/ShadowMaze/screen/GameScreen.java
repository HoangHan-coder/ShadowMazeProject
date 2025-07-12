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

public class GameScreen implements Screen {

    // Screen setting 
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    public static final int MAX_SCREEN_COL = 27; // 73 colums
    public static final int MAX_SCREEN_ROW = 19; // 53 rows

    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

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

    public GameScreen(Game game) {
        this.batch = new SpriteBatch();
        this.game = game;
    }

    @Override
    public void show() {
        setUpGame();
        initGame();
    }

    public void setUpGame() {
        aSetter.setObject();
    }

    /**
     * Initialize the game state, player, map, textures
     */
    public void initGame() {
        // khoi tao me cung
//        int pathWidth = 1; // ví dụ 3
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
        Texture staminaIcon = new Texture(Gdx.files.internal("menu/function/dragon.png"));
        staminaBar = new StaminaBar(140, 30, 200, 20, 100, staminaIcon);
        Texture hpBg = new Texture(Gdx.files.internal("menu/function/type5.png"));
        Texture hpFill = new Texture(Gdx.files.internal("menu/function/type6.png"));
        hpBar = new HpBar(170, 30, 200, 20, 100, hpBg, hpFill);
        cCheck = new CollisionChecker(this);
        knight = new Knight(this, staminaBar, hpBar);
        spawnEnemiesFromWalkableTiles(map, 4); // Spawn 4 enemy

        // Center the maze if needed
//        offsetX = 0; // Set to (SCREEN_WIDTH - MAX_SCREEN_COL * TILE_SIZE) / 2 if centered rendering is needed
//        offsetY = 0;
    }

    public void spawnEnemiesFromWalkableTiles(Map map, int numEnemies) {
        Random random = new Random();
        int mapHeight = map.tileNum.length;
        int mapWidth = map.tileNum[0].length;

        Array<int[]> walkableTiles = new Array<>();

        // ? Duy?t to�n b? b?n ?? v� l?u l?i c�c � c� tileNum == 1
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map.tileNum[y][x] == 1) {
                    walkableTiles.add(new int[]{x, y});
                }
            }
        }

        // ? N?u kh�ng ?? tile h?p l?
        if (walkableTiles.size < numEnemies) {
            numEnemies = walkableTiles.size;
        }

        // ? Spawn enemy ? c�c � ???c ch?n ng?u nhi�n
        for (int i = 0; i < numEnemies; i++) {
            int[] tile = walkableTiles.random();
            walkableTiles.removeValue(tile, true); // tr�nh spawn tr�ng �

            OBJ_Enemy enemy = new OBJ_Enemy();
            enemy.mapX = tile[0] * GameScreen.TILE_SIZE;
            enemy.mapY = tile[1] * GameScreen.TILE_SIZE;
            obj[i] = enemy;
        }
    }

    public boolean isNearEnemy(float range) {
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Enemy) {
                OBJ_Enemy enemy = (OBJ_Enemy) object;
                float dx = knight.getPositionX() - enemy.mapX;
                float dy = knight.getPositionY() - enemy.mapY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                if (distance <= range) {
                    return true;
                }
            }
        }
        return false;
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

        // --- Update logic ---
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Enemy) {
                ((OBJ_Enemy) object).update(Gdx.graphics.getDeltaTime(), map);
            }
        }

        // --- Clear screen ---
        ScreenUtils.clear(0, 0, 0, 1);

        // --- DRAW ---
        batch.begin();
        knight.inputHandle(delta);
        knight.update(delta);
        hpBar.update(delta);
        map.drawMap();

        if (!map.isBackgroundOnly()) {
            knight.knightRender(delta);
        }
// C?p nh?t tr?ng th�i ph�m E ?? b?n
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            if (!wasEPressedLastFrame && isSkillAvailable) { // ? Ki?m tra th�m isSkillAvailable
                Vector2 start = new Vector2(knight.positionX, knight.positionY);
                Direction dir = knight.getDirection();
                if (dir != Direction.IDLE) {
                    Fireball fb = new Fireball();
                    fb.setMapSize(map.getMapWidthPixels(), map.getMapHeightPixels());
                    fb.activate(start, dir);
                    fb.shoot();
                    fireballs.add(fb);
                    currentFireball = fb;

                    isSkillAvailable = false; // ? Kh�ng cho b?n ti?p
                }
            }
            wasEPressedLastFrame = true;
        } else {
            wasEPressedLastFrame = false;
        }

// C?p nh?t & render c�c fireball ?ang ho?t ??ng
        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.update(delta);
            if (fireball.isActive()) {
                fireball.render(batch, knight.positionX, knight.positionY);
            } else {
                iterator.remove(); // Xo� n?u kh�ng c�n active
                isSkillAvailable = true; // ? Cho ph�p d�ng skill l?i
            }
        }

        // Draw all objects
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].drawObject(this);
            }
        }
        hpBar.render(batch);

        batch.end();

        // Draw UI (stamina icon)
        staminaBar.renderIcon(batch);

        // Draw stage UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        knight.dispose();
    }
}
