package object;

import com.ShadowMaze.model.Map;
import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    private float speed = 350f;
    private float moveTimer = 0f;
    private float moveInterval = 0.1f;
    public int hp = 125;
    private Animation<TextureRegion> animUp;
    private Animation<TextureRegion> animDown;
    private Animation<TextureRegion> animLeft;
    private Animation<TextureRegion> animRight;

    private Animation<TextureRegion> currentAnimation;

    /**
     * Constructor initializes the enemy name and loads animation frames from
     * disk.
     */
    public OBJ_Enemy() {
        setDefaultValue();
    }

    private void setDefaultValue() {
        name = "Enemy";
        collision = true;
        scale = 3f;
        direction = 1; // m?c ??nh ?i xu?ng

        animUp = loadAnimation("icons/up");
        animDown = loadAnimation("icons/down");
        animLeft = loadAnimation("icons/left");
        animRight = loadAnimation("icons/right");

        currentAnimation = animDown;

        TextureRegion firstFrame = currentAnimation.getKeyFrame(0);
        float enemyWidth = firstFrame.getRegionWidth() * scale;
        float enemyHeight = firstFrame.getRegionHeight() * scale;

        mapX = 5 * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f - enemyWidth / 2f;
        mapY = 5 * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f - enemyHeight / 2f;

        solidArea = new Rectangle(10, 10, enemyWidth - 20, enemyHeight - 20);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        currentAnimation = animDown;
        animation = currentAnimation; // ? thêm dòng này

    }

    private Animation<TextureRegion> loadAnimation(String folderPath) {
        Array<TextureRegion> regions = new Array<>();
        FileHandle dir = Gdx.files.internal(folderPath);

        for (FileHandle file : dir.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);
                this.frames.add(tex); // L?u vào danh sách ?? dispose() sau
                regions.add(new TextureRegion(tex)); // Ch? thêm TextureRegion vào animation
            }
        }

        return new Animation<>(0.1f, regions, Animation.PlayMode.LOOP);
    }

    private boolean isWalkable(Map map, float x, float y) {
        int tileX = (int) (x / GameScreen.TILE_SIZE);
        int tileY = (int) (y / GameScreen.TILE_SIZE);

        return tileY >= 0 && tileY < map.tileNum.length
                && tileX >= 0 && tileX < map.tileNum[0].length
                && map.tileNum[tileY][tileX] == 1;
    }

    @Override
    public void drawObject(GameScreen gs) {
        if (!isDead()) {
            drawMonster(gs);
        }
    }

    /**
     * Draws the animated enemy on the screen if it's within the visible camera
     * bounds.
     *
     * @param screen Reference to the current GameScreen for accessing batch and
     * camera offsets
     */
    public void drawMonster(GameScreen screen) {
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
            screen.batch.draw(currentFrame, screenX, screenY, frameWidth * scale, frameHeight * scale);
        }
    }

    public void update(float delta, GameScreen gs) {
        // Update movement timer
        moveTimer += delta;
        float dx = 0, dy = 0;

        // Get Knight's current position
        float knightX = gs.knight.positionX;
        float knightY = gs.knight.positionY;

        // Calculate distance between this enemy and the Knight
        float distance = Vector2.dst(mapX, mapY, knightX, knightY);

        // Check if the Knight is within chasing distance (3 tiles)
        boolean isChasing = distance < GameScreen.TILE_SIZE * 3;

        if (isChasing) {
            // Calculate the direction toward the Knight
            float diffX = knightX - mapX;
            float diffY = knightY - mapY;

            // Move in the direction of the larger distance (horizontal or vertical)
            if (Math.abs(diffX) > Math.abs(diffY)) {
                dx = (diffX > 0) ? 1 : -1;
            } else {
                dy = (diffY > 0) ? 1 : -1;
            }
        } else {
            // If not chasing, move in the current random direction
            switch (direction) {
                case 0 ->
                    dx = -1; // Move left
                case 1 ->
                    dy = 1;  // Move up
                case 2 ->
                    dx = 1;  // Move right
                case 3 ->
                    dy = -1; // Move down
            }
        }
        // ? C?p nh?t h??ng và animation
        if (dx < 0) {
            direction = 0;
            currentAnimation = animLeft;
        } else if (dx > 0) {
            direction = 2;
            currentAnimation = animRight;
        } else if (dy < 0) {
            direction = 3;
            currentAnimation = animDown;
        } else if (dy > 0) {
            direction = 1;
            currentAnimation = animUp;
        }
        animation = currentAnimation;
        // Calculate next position based on direction and speed
        float nextX = mapX + dx * speed * delta;
        float nextY = mapY + dy * speed * delta;

        // Get the enemy's width and height for collision detection
        float enemyWidth = animation.getKeyFrame(0).getRegionWidth() * scale;
        float enemyHeight = animation.getKeyFrame(0).getRegionHeight() * scale;

        // Define the enemy's bounding box corners
        float left = nextX;
        float right = nextX + enemyWidth;
        float top = nextY + enemyHeight;
        float bottom = nextY;

        // Check if the next position is walkable (all 4 corners)
        boolean canMove = isWalkable(gs.map, left, bottom)
                && isWalkable(gs.map, right - 1, bottom)
                && isWalkable(gs.map, left, top - 1)
                && isWalkable(gs.map, right - 1, top - 1);

        if (canMove) {
            // Move to next position if walkable
            mapX = nextX;
            mapY = nextY;
        } // If not chasing and movement interval has passed, change direction randomly
        else if (!isChasing && moveTimer >= moveInterval) {
            moveTimer = 0f;

            // Calculate current tile position of enemy's center
            float centerX = mapX + enemyWidth / 2f;
            float centerY = mapY + enemyHeight / 2f;
            int curTileX = (int) (centerX / GameScreen.TILE_SIZE);
            int curTileY = (int) (centerY / GameScreen.TILE_SIZE);

            // Collect all possible directions where the enemy can move
            Array<Integer> possibleDirections = new Array<>();
            for (int i = 0; i < 4; i++) {
                int nx = curTileX, ny = curTileY;

                // Compute neighbor tile based on direction
                switch (i) {
                    case 0 ->
                        nx--;     // Left
                    case 1 ->
                        ny++;     // Up
                    case 2 ->
                        nx++;     // Right
                    case 3 ->
                        ny--;     // Down
                }

                // Check bounds and walkability of the neighbor tile
                if (ny >= 0 && ny < gs.map.tileNum.length
                        && nx >= 0 && nx < gs.map.tileNum[0].length
                        && gs.map.tileNum[ny][nx] == 1) {
                    possibleDirections.add(i);
                }
            }

            // If there are valid directions, choose one randomly
            if (possibleDirections.size > 0) {
                direction = possibleDirections.random();
            }
        }
    }

    public void takeDamage(int amount) {
        if (!isDead()) {
            hp -= amount;
            System.out.println("Enemy takes " + amount + " damage. HP: " + hp);
            if (hp <= 0) {
                isDead();
            }
        }
    }

    public boolean isDead() {
        return hp <= 0;  // Ho?c flag nï¿½o ?ï¿½ n?u b?n cï¿½ hi?u ?ng ch?t
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
