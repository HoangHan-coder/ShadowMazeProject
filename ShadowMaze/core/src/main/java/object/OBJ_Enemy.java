package object;

import com.ShadowMaze.model.Map;
import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Represents an animated enemy object on the map. The enemy uses multiple
 * textures as animation frames and is drawn relative to the player's position.
 */
public class OBJ_Enemy extends SuperObject {

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private Array<Texture> frames = new Array<>();
    private float scale = 3f; 
    private int direction = 0; // 0=left, 1=down, 2=right,  3=up
    private float speed = 50f;
    private float moveTimer = 0f;
    private float moveInterval = 0.1f; 
    public float mapX, mapY;

    public int hp = 100; // máu m?c ??nh

    public int HP = 5;
    
  
    /**
     * Constructor initializes the enemy name and loads animation frames from
     * disk.
     */
    public OBJ_Enemy() {
        name = "Enemy";
        collision = true;
        // Kh?i t?o tile ban ??u (VD: ? tile hï¿½ng 5, c?t 5)
        int tileX = 5;
        int tileY = 5;


        Array<TextureRegion> regions = new Array<>();
        for (int i = 0; i < 4; i++) {
            Texture tex = new Texture(Gdx.files.internal("icons/Special" + i + ".png"));
            frames.add(tex);
            regions.add(new TextureRegion(tex));
        }


        animation = new Animation<>(0.15f, regions);


        float enemyWidth = animation.getKeyFrame(0).getRegionWidth() * scale;
        float enemyHeight = animation.getKeyFrame(0).getRegionHeight() * scale;


        mapX = tileX * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f - enemyWidth / 2f;
        mapY = tileY * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f - enemyHeight / 2f;
    }

    private boolean isWalkable(Map map, float x, float y) {
        int tileX = (int) (x / GameScreen.TILE_SIZE);
        int tileY = (int) (y / GameScreen.TILE_SIZE);

        return tileY >= 0 && tileY < map.tileNum.length
                && tileX >= 0 && tileX < map.tileNum[0].length
                && map.tileNum[tileY][tileX] == 1;
    }

    /**
     * Draws the animated enemy on the screen if it's within the visible camera
     * bounds.
     *
     * @param screen Reference to the current GameScreen for accessing batch and
     * camera offsets
     */
    @Override
    public void drawObject(GameScreen screen) {
        // Update the animation timer with the frame's delta time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get the current animation frame
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        int screenX = (int) (mapX - screen.knight.positionX + screen.knight.renderX);
        int screenY = (int) (mapY - screen.knight.positionY + screen.knight.renderY);
        if (mapX + GameScreen.TILE_SIZE > screen.knight.positionX - screen.knight.renderX
                && mapX - GameScreen.TILE_SIZE < screen.knight.positionX + screen.knight.renderX
                && mapY + GameScreen.TILE_SIZE > screen.knight.positionY - screen.knight.renderY
                && mapY - GameScreen.TILE_SIZE < screen.knight.positionY + screen.knight.renderY) {

            float frameWidth = currentFrame.getRegionWidth();   // Frame width in pixels
            float frameHeight = currentFrame.getRegionHeight(); // Frame height in pixels

            // Draw the current animation frame at the calculated screen position
            screen.batch.draw(currentFrame,
                    screenX, screenY,
                    frameWidth * scale,
                    frameHeight * scale);
        }
    }

    /**
     * Updates the enemy's movement and direction based on the current map. The
     * enemy attempts to move in the current direction. If blocked by a wall, it
     * changes to a random valid direction after a short delay.
     *
     * @param delta The time elapsed since the last frame (in seconds)
     * @param map The current game map used to check collisions
     */
    public void update(float delta, Map map) {
        moveTimer += delta; // Accumulate time since last direction change
        float dx = 0, dy = 0; // Movement deltas

        // Determine direction: 0 = left, 1 = down, 2 = right, 3 = up
        switch (direction) {
            case 0 -> dx = -1;
            // Move left
            case 1 -> dy = 1;
            // Move down
            case 2 -> dx = 1;
            // Move right
            case 3 -> dy = -1;
            // Move up
        }

        // Calculate next potential position
        float nextX = mapX + dx * speed * delta;
        float nextY = mapY + dy * speed * delta;

        // Get enemy dimensions (scaled)
        float enemyWidth = animation.getKeyFrame(0).getRegionWidth() * scale;
        float enemyHeight = animation.getKeyFrame(0).getRegionHeight() * scale;

        // Compute bounding box of the enemy at next position
        float left = nextX;
        float right = nextX + enemyWidth;
        float top = nextY + enemyHeight;
        float bottom = nextY;

        // Check if all four corners are walkable
        boolean canMove
                = isWalkable(map, left, bottom)
                && isWalkable(map, right - 1, bottom)
                && isWalkable(map, left, top - 1)
                && isWalkable(map, right - 1, top - 1);

        if (canMove) {
            mapX = nextX; // Move enemy to new X
            mapY = nextY; // Move enemy to new Y
        } else {
            // Blocked by wall: consider changing direction after interval
            if (moveTimer >= moveInterval) {
                moveTimer = 0f; // Reset timer

                // Recalculate current tile (using center of enemy)
                float enemyWidth1 = animation.getKeyFrame(0).getRegionWidth() * scale;
                float enemyHeight1 = animation.getKeyFrame(0).getRegionHeight() * scale;

                int curTileX = (int) ((mapX + enemyWidth1 / 2f) / GameScreen.TILE_SIZE);
                int curTileY = (int) ((mapY + enemyHeight1 / 2f) / GameScreen.TILE_SIZE);

                Array<Integer> possibleDirections = new Array<>();

                // Check all 4 directions for valid tiles
                for (int i = 0; i < 4; i++) {
                    int nx = curTileX, ny = curTileY;
                    switch (i) {
                        case 0 -> nx--;
                        // Left
                        case 1 -> ny++;
                        // Down
                        case 2 -> nx++;
                        // Right
                        case 3 -> ny--;
                        // Up
                    }

                    // If the neighbor tile is walkable, add it to options
                    if (ny >= 0 && ny < map.tileNum.length
                            && nx >= 0 && nx < map.tileNum[0].length
                            && map.tileNum[ny][nx] == 1) {
                        possibleDirections.add(i);
                    }
                }

                // Randomly pick a new direction if there are valid options
                if (possibleDirections.size > 0) {
                    direction = possibleDirections.random();
                }
            }
        }
    }

    public boolean isDead() {
        return hp <= 0;  // Ho?c flag nào ?ó n?u b?n có hi?u ?ng ch?t
    }

    /**
     * Disposes all texture resources used by the enemy to free memory. Must be
     * called when the object is no longer needed.
     */
    public void dispose() {
        for (Texture tex : frames) {
            tex.dispose(); // Release texture memory
        }
    }
}
