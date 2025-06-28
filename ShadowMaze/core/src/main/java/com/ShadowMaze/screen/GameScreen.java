package com.ShadowMaze.screen;

import com.ShadowMaze.generator.MazeGenerator;
import com.ShadowMaze.model.Player;
import com.ShadowMaze.render.PlayerRenderer;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private final Game game;
    private final SpriteBatch batch;

    private int[][] maze;
    private int TILE_SIZE;
    private int offsetX, offsetY;

    private MazeGenerator mazeGenerator;
    private Player player;
    private PlayerRenderer playerRenderer;

    private Texture wallTexture;
    private Texture floorTexture;

    public GameScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
    }

    @Override
    public void show() {
        initGame(); // Gọi ở đây để Gdx.graphics hoạt động ổn định
    }

    /**
     * Khởi tạo mê cung và các thành phần game
     */
    private void initGame() {
        // 1. Tạo mê cung
        mazeGenerator = new MazeGenerator(21, 21); // Có thể thay đổi tùy ý
        maze = mazeGenerator.generate(1, 1);

        // 2. Lấy kích thước màn hình
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // 3. Tính TILE_SIZE tự động để maze vừa khít
        int tileSizeX = screenWidth / maze[0].length;
        int tileSizeY = screenHeight / maze.length;
        TILE_SIZE = Math.max(32, Math.min(tileSizeX, tileSizeY));

        // 4. Tính offset để canh giữa mê cung
        int mazePixelWidth = maze[0].length * TILE_SIZE;
        int mazePixelHeight = maze.length * TILE_SIZE;
        offsetX = (screenWidth - mazePixelWidth) / 2;
        offsetY = (screenHeight - mazePixelHeight) / 2;

        // 5. Load texture
        wallTexture = new Texture("wall.png");
        floorTexture = new Texture("floor.png");

        // 6. Tạo người chơi
        player = new Player(1, "Hero", 100, 0,
                mazeGenerator.getStartX(), mazeGenerator.getStartY(), 1);

        // 7. Load animation
        Animation<TextureRegion> anim = loadPlayerAnimation();
        playerRenderer = new PlayerRenderer(player, anim);
    }

    /**
     * Load các frame nhân vật từ file frame_0.png đến frame_3.png
     */
    private Animation<TextureRegion> loadPlayerAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("frame_" + i + ".png"));
        }
        return new Animation<>(0.2f, frames);
    }

    /**
     * Xử lý phím di chuyển
     */
    private void handleInput() {
        int x = player.getPositionX();
        int y = player.getPositionY();

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && maze[y + 1][x] == 1) {
            player.setPositionY(y + 1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && maze[y - 1][x] == 1) {
            player.setPositionY(y - 1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && maze[y][x - 1] == 1) {
            player.setPositionX(x - 1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && maze[y][x + 1] == 1) {
            player.setPositionX(x + 1);
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

            // 1. Vẽ nền đen trước (viền và khoảng cách)
            batch.setColor(Color.BLUE);
            batch.draw(floorTexture, drawX, drawY, TILE_SIZE, TILE_SIZE);
            batch.setColor(Color.WHITE); // reset về mặc định

            // 2. Vẽ floor phủ lên (mê cung thông)
            if (maze[y][x] == 1) {
                batch.draw(floorTexture, drawX, drawY, TILE_SIZE, TILE_SIZE);
            }

            // 3. Vẽ wall nhỏ hơn chính giữa
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
        handleInput();
        playerRenderer.update(delta);

        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        drawMaze();
        playerRenderer.render(batch, TILE_SIZE, offsetX, offsetY);
        batch.end();

        // Kiểm tra chiến thắng
        if (player.getPositionX() == mazeGenerator.getEndX()
                && player.getPositionY() == mazeGenerator.getEndY()) {
            System.out.println("🎉 YOU WIN!");
        }
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
        wallTexture.dispose();
        floorTexture.dispose();
        for (TextureRegion f : playerRenderer.getAnimation().getKeyFrames()) {
            f.getTexture().dispose();
        }
    }
}
