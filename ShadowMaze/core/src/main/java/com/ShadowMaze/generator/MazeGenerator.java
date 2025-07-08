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

    private final int pathWidth;

    public int getPathWidth() {
        return pathWidth;
    }

    public MazeGenerator(int width, int height, int pathWidth) {
        this.width = width;
        this.height = height;
        this.pathWidth = pathWidth;
        maze = new int[height][width];
        visited = new boolean[height][width];
    }

    public int[][] generate(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;

        for (int y = 0; y < height; y++) {
            Arrays.fill(maze[y], 0);
        }
        for (int y = 0; y < height; y++) {
            Arrays.fill(visited[y], false);
        }

        dfs(startX, startY);

        // Đặt điểm kết thúc là ô xa nhất từ điểm bắt đầu
        findFarthestPath();

        return maze;
    }

    private void dfs(int x, int y) {
    visited[y][x] = true;
    fillPath(x, y); // Vẽ đường ở vị trí hiện tại

    List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
    Collections.shuffle(dirs);

    for (int dir : dirs) {
        int nx = x + dx[dir] * (pathWidth + 1);
        int ny = y + dy[dir] * (pathWidth + 1);

        if (isInBounds(nx, ny) && !visited[ny][nx]) {
            // Đục tường giữa
            for (int i = 1; i <= pathWidth; i++) {
                int ix = x + dx[dir] * i;
                int iy = y + dy[dir] * i;
                fillPath(ix, iy);
            }

            dfs(nx, ny);
        }
    }
}


    private void fillPath(int x, int y) {
    for (int dy = 0; dy < pathWidth; dy++) {
        for (int dx = 0; dx < pathWidth; dx++) {
            int px = x + dx;
            int py = y + dy;
            if (px >= 0 && px < width && py >= 0 && py < height) {
                maze[py][px] = 1;
            }
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
    return x >= 0 && x + pathWidth <= width &&
           y >= 0 && y + pathWidth <= height;
}


    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
