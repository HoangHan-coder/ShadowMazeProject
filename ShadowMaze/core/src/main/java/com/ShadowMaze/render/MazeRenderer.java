package com.ShadowMaze.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Vẽ mê cung sử dụng SpriteBatch với texture cho tường và nền.
 * 
 * <p>0 = wall, 1 = path</p>
 */
public class MazeRenderer {
    private final int[][] maze;     // Mê cung 2D
    private final int cellSize;     // Kích thước 1 ô (px)

    private final Texture wallTexture;
    private final Texture floorTexture;

    /**
     * @param maze      Ma trận mê cung (0: wall, 1: path)
     * @param cellSize  Kích thước mỗi ô (pixels)
     */
    public MazeRenderer(int[][] maze, int cellSize) {
        this.maze = maze;
        this.cellSize = cellSize;

        // Nạp texture
        wallTexture = new Texture(Gdx.files.internal("wall.png"));
        floorTexture = new Texture(Gdx.files.internal("floor.png"));

        // Đảm bảo hình ảnh không bị mờ nếu scale nhỏ
        wallTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        floorTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    /**
     * Vẽ toàn bộ mê cung bằng batch được truyền từ ngoài.
     * @param batch SpriteBatch đang dùng trong GameScreen
     */
    public void render(SpriteBatch batch) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                float drawX = x * cellSize;
                float drawY = y * cellSize;

                if (maze[y][x] == 0) {
                    batch.draw(wallTexture, drawX, drawY, cellSize, cellSize);
                } else {
                    batch.draw(floorTexture, drawX, drawY, cellSize, cellSize);
                }
            }
        }
    }

    public void dispose() {
        wallTexture.dispose();
        floorTexture.dispose();
    }
}
