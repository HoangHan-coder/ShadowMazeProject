package com.ShadowMaze.generator;

import java.util.*;

public class MazeGenerator {
    private final int width, height;
    private final int[][] maze; // 0: wall, 1: path
    private final boolean[][] visited;

    private final int[] dx = {0, 0, -1, 1};
    private final int[] dy = {-1, 1, 0, 0};

    private int startX, startY;
    private int endX, endY;

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        maze = new int[height][width];
        visited = new boolean[height][width];
    }

    public int[][] generate(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;

        for (int y = 0; y < height; y++) Arrays.fill(maze[y], 0);
        for (int y = 0; y < height; y++) Arrays.fill(visited[y], false);

        dfs(startX, startY);

        // Đặt điểm kết thúc là ô xa nhất từ điểm bắt đầu
        findFarthestPath();

        return maze;
    }

    private void dfs(int x, int y) {
        visited[y][x] = true;
        maze[y][x] = 1;

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);
        for (int dir : dirs) {
            int nx = x + dx[dir] * 2;
            int ny = y + dy[dir] * 2;

            if (isInBounds(nx, ny) && !visited[ny][nx]) {
                maze[y + dy[dir]][x + dx[dir]] = 1; // Đục tường
                dfs(nx, ny);
            }
        }
    }

    private void findFarthestPath() {
        int maxDist = -1;
        for (int y = 1; y < height; y += 2) {
            for (int x = 1; x < width; x += 2) {
                if (maze[y][x] == 1) {
                    int dist = Math.abs(x - startX) + Math.abs(y - startY);
                    if (dist > maxDist) {
                        maxDist = dist;
                        endX = x;
                        endY = y;
                    }
                }
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x > 0 && x < width - 1 && y > 0 && y < height - 1;
    }

    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getEndX() { return endX; }
    public int getEndY() { return endY; }
}
