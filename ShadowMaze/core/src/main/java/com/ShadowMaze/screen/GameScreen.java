package com.ShadowMaze.screen;

import com.ShadowMaze.core.AssetSetter;
import com.ShadowMaze.core.CollisionChecker;
import com.ShadowMaze.generator.MazeGenerator;
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.model.Map;
import com.ShadowMaze.model.SuperObject;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

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
    public static final int MAP_X = 73;
    public static final int MAP_Y = 53;
    public static final int MAP_WIDTH = MAP_X * TILE_SIZE;
    public static final int MAP_HEIGHT = MAP_Y * TILE_SIZE;
    
    // Maze and rendering
    public final SpriteBatch batch;
//    private int[][] maze = new int[MAX_SCREEN_ROW][MAX_SCREEN_COL];
//    private int offsetX, offsetY;
    
    public Map map;
    public CollisionChecker cCheck;
    public SuperObject[] obj = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);
    public Knight knight;
    public GameScreen() {
        this.batch = new SpriteBatch();
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
          map = new Map(this);
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
        knight = new Knight(this);
        

        // Center the maze if needed
//        offsetX = 0; // Set to (SCREEN_WIDTH - MAX_SCREEN_COL * TILE_SIZE) / 2 if centered rendering is needed
//        offsetY = 0;
    }

    @Override
    public void render(float delta) {
        knight.inputHandle();
        
//        float speed = 5f;
//        player.setRenderX(MathUtils.lerp(player.getRenderX(), player.getPositionX(), speed * delta));
//        player.setRenderY(MathUtils.lerp(player.getRenderY(), player.getPositionY(), speed * delta));
        // Clear screen
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
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
