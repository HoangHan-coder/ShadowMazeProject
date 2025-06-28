/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author NgKaitou
 */
/**
 * Vẽ mê cung sử dụng SpriteBatch + texture cho ô tường.
 *
 * <p>► Mỗi ô có kích thước cố định (cellSize).  
 * ► 0 = wall, 1 = path.</p>
 */
public class MazeRenderer {
    private final int[][] maze;      // Ma trận mê cung
    private final int cellSize;      // Kích thước 1 ô (px)

    private final Texture wallTex;   // Texture cho tường
    private final SpriteBatch batch; // Batch để vẽ texture

    /**
     * @param maze      Mê cung 2D (0-wall, 1-path).
     * @param cellSize  Kích thước 1 ô (pixels).
     */
    public MazeRenderer(int[][] maze, int cellSize) {
        this.maze = maze;
        this.cellSize = cellSize;

        // ⚠️ AssetManager tốt hơn cho dự án lớn, nhưng demo đơn giản dùng trực tiếp.
        this.wallTex = new Texture(Gdx.files.internal("SkTkbD.png"));
        this.wallTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        this.batch = new SpriteBatch();
    }

    /** Vẽ toàn bộ mê cung. Gọi trong GameScreen.render(). */
    public void render() {
        batch.begin();
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze.length; x++) {

                // Tọa độ vẽ: (cột * size, hàng * size)
                float drawX = x * cellSize;
                float drawY = y * cellSize;

                if (maze[y][x] == 0) {
                    // ▒▒ Wall → vẽ texture
                    batch.draw(wallTex, drawX, drawY, cellSize, cellSize);
                } else {
                    // ░░ Path → chỉ cần để trống hoặc vẽ màu nền đơn giản
                    // Ví dụ: vẽ 1 ô trắng thông qua fillRect riêng, hoặc bỏ qua
                }
            }
            System.out.println("");
        }
        
        batch.end();
    }

    /** Giải phóng tài nguyên khi không dùng nữa. */
    public void dispose() {
        batch.dispose();
        wallTex.dispose();
    }
}

