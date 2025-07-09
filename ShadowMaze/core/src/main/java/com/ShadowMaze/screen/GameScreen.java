package com.ShadowMaze.screen;
import com.ShadowMaze.core.AssetSetter;
import com.ShadowMaze.core.CollisionChecker;
import com.ShadowMaze.generator.MazeGenerator;
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.model.Map;
import com.ShadowMaze.model.Player;
import com.ShadowMaze.render.MirrorRenderer;
import com.ShadowMaze.render.PlayerRenderer;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ShadowMaze.model.SuperObject;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    protected final Game game;
    public final SpriteBatch batch;

    private int[][] maze;
    private int offsetX, offsetY;
    private Player npc; // nh�n v?t ph?
    private PlayerRenderer npcRenderer;
    // Screen setting 
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    public static final int MAX_SCREEN_COL = 27; // 73 colums
    public static final int MAX_SCREEN_ROW = 19; // 53 rows

    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

    // Map setting
    public static final int MAP_X = 73;
    public static final int MAP_Y = 53;
    public static final int MAP_WIDTH = MAP_X * TILE_SIZE;
    public static final int MAP_HEIGHT = MAP_Y * TILE_SIZE;

    public Map map;
    public CollisionChecker cCheck;
    public SuperObject[] obj = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);
    public Knight knight;
    private Player player;
    private long lastMoveTime = 0;
    private final long moveDelay = 150_000_000; // 150ms delay khi gi? ph�m
    private Texture wallTexture;
    private Texture floorTexture;
    private MirrorRenderer mirrorRenderer;
    private Stage stage;
    private boolean isPaused = false;

    public GameScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        cCheck = new CollisionChecker(this);
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
    private void initGame() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        map = new Map(this, game);
        map.createButtons(stage);
        knight = new Knight(this);
        

    }


    @Override
    public void render(float delta) {
        if (map.isPaused()) {
            ScreenUtils.clear(0, 0, 0, 1);
            batch.begin();
            map.drawMap();                        // v? b?n ??
            batch.end();
            stage.act(delta);
            stage.draw();
            return;
        }

        // Ch? ch?y n?u KH�NG pause
        knight.inputHandle();                 // x? l� ph�m
        knight.update(delta);                 // (n?u b?n c� update ri�ng)
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        map.drawMap();                        // v? b?n ??
        map.createButtons(stage);
        knight.knightRender(delta);          // v? nh�n v?t
        map.drawMap();
        
        // render object
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].drawObject(this);
            }
        }
        
        knight.knightRender(delta);
        System.out.println("Player at: (" + knight.getPositionX()/TILE_SIZE + ", " + knight.getPositionY()/TILE_SIZE + ")");
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
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
