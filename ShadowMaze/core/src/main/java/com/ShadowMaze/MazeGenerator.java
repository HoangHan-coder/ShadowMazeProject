/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze;

import java.util.*;

public class MazeGenerator {
    private final int width, height;
    private final int[][] maze; // 0: wall, 1: path
    private final boolean[][] visited;

    private final int[] dx = {0, 0, -1, 1};
    private final int[] dy = {-1, 1, 0, 0};

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        maze = new int[height][width];
        visited = new boolean[height][width];
    }

    public int[][] generate() {
        // Fill everything with walls
        for (int y = 0; y < height; y++)
            Arrays.fill(maze[y], 0);

        dfs(1, 1); // Bắt đầu từ ô (1,1)

        return maze;
    }

    private void dfs(int x, int y) {
        visited[y][x] = true;
        maze[y][x] = 1; // Mark as path

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs); // Randomize directions

        for (int dir : dirs) {
            int nx = x + dx[dir] * 2;
            int ny = y + dy[dir] * 2;

            if (isInBounds(nx, ny) && !visited[ny][nx]) {
                maze[y + dy[dir]][x + dx[dir]] = 1; // Đục tường ở giữa
                dfs(nx, ny);
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x > 0 && x < width - 1 && y > 0 && y < height - 1;
    }
}


