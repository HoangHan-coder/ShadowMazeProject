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
    private PlayerRenderer playerRenderer;
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
//    private PlayerRenderer playerRender;

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
        player = new Player(1, "Hero", 100, 0, 1, 1, 1);
        

    }

    /**
     * Load các frame nhân vật từ file frame_0.png đến frame_3.png
     */
    private Animation<TextureRegion> loadPlayerAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("Avatar\\frame_" + i + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    private Animation<TextureRegion> loadPlayerItemCheast() {
        TextureRegion[] frames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            frames[i] = new TextureRegion(new Texture("Character\\framee" + i + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    /**
     * Xử lý phím di chuyển
     */
    private void handleInput() {
        long currentTime = TimeUtils.nanoTime();
        if (currentTime - lastMoveTime < moveDelay) {
            return;
        }

        int x = player.getPositionX();
        int y = player.getPositionY();

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && maze[y + 1][x] == 1) {
            player.setPositionY(y + 1);
            lastMoveTime = currentTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && maze[y - 1][x] == 1) {
            player.setPositionY(y - 1);
            lastMoveTime = currentTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && maze[y][x - 1] == 1) {
            player.setPositionX(x - 1);
            lastMoveTime = currentTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && maze[y][x + 1] == 1) {
            player.setPositionX(x + 1);
            lastMoveTime = currentTime;
        }
    }

    /**
     * Vẽ mê cung với texture
     */
    private void drawMaze() {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                float drawX = offsetX + x * TILE_SIZE;
                float drawY = offsetY + y * TILE_SIZE;

                // 1. Vẽ n�?n đen trước (vi�?n và khoảng cách)
                batch.setColor(Color.BLUE);
                batch.draw(floorTexture, drawX, drawY, TILE_SIZE, TILE_SIZE);
                batch.setColor(Color.WHITE); // reset v�? mặc định

                // 2. Vẽ floor phủ lên (mê cung thông)
                if (maze[y][x] == 1) {
                    batch.draw(floorTexture, drawX, drawY, TILE_SIZE, TILE_SIZE);
                } // 3. Vẽ wall nh�? hơn chính giữa
                else if (maze[y][x] == 0) {
                    float shrink = 0.9f; // shrink 50% wall
                    float wallSize = TILE_SIZE * shrink;
                    float offset = (TILE_SIZE - wallSize) / 2;

                    batch.draw(wallTexture, drawX + offset, drawY + offset, wallSize, wallSize);
                }
            }
        }
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
