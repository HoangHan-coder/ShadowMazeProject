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
 * Animated enemy that moves in the world (map space) and renders relative
 * to the Knight's camera origin.
 *
 * Hybrid AI behavior:
 * <ul>
 *   <li>Smooth pixel-based movement (speed * delta) so motion looks fluid.</li>
 *   <li>At tile centers (or when blocked), pick a new direction.</li>
 *   <li>Bias toward moving closer to the Knight when within chase range.</li>
 *   <li>Inject randomness so enemies are not perfectly predictable.</li>
 *   <li>Avoid 180° reverse turns unless no other path.</li>
 * </ul>
 */
public class OBJ_Enemy extends SuperObject {

    // --- Animation state ---
    private Animation<TextureRegion> animation;  // Active animation used this frame
    private float stateTime = 0f;                // Time accumulator for animation playback
    private final Array<Texture> frames = new Array<>(); // Raw textures for disposal

    // Visual scale for drawing (all directional animations scaled the same)
    private float scale = 3f;

    /**
     * Internal direction index for AI pathing.
     * 0 = LEFT, 1 = UP, 2 = RIGHT, 3 = DOWN.
     */
    private int direction = 1;

    // Movement speed in pixels/second
    private final float normalSpeed = 50f;  // Base movement speed when idle or patrolling
    private final float chaseSpeed = 100f;   // Increased movement speed when chasing the Knight
    private float speed = normalSpeed; // Current movement speed applied to the enemy

    // Timers for throttling random re‑steering when idle
    private float moveTimer = 0f;
    private float moveInterval = 0.1f; // seconds between idle steering checks

    // Enemy HP
    public int hp = 125;

    // Directional animations
    private Animation<TextureRegion> animUp;
    private Animation<TextureRegion> animDown;
    private Animation<TextureRegion> animLeft;
    private Animation<TextureRegion> animRight;
    private Animation<TextureRegion> currentAnimation; // cached pointer to current dir anim

    // ===== HYBRID TILE STEERING =====

    // Current tile coords (computed from enemy center)
    private int curTileX, curTileY;

    // Target tile to move toward (AI steering)
    private int targetTileX, targetTileY;
    private boolean hasTargetTile = false; // false -> need to pick new tile

    // How close (in pixels) we must be to tile center before choosing next tile
    private static final float TILE_CENTER_EPS = 4f;

    // Behavior weighting: probability of choosing tile that reduces distance to Knight
    private static final float CHASE_BIAS_CHASE = 1f; // when near Knight
    private static final float CHASE_BIAS_IDLE  = 0.5f; // when roaming
    private float chaseBias = CHASE_BIAS_IDLE;          // current bias applied

    // Last chosen direction index (0..3). Used to avoid instant 180° turns.
    private int lastDirIndex = -1;

    // Cached sprite dimensions (after scale) for center math
    private float enemyWidth;
    private float enemyHeight;
    private float enemyHalfW;
    private float enemyHalfH;

    /**
     * Direction lookup table: index -> {dx, dy} in tile units.
     * 0=L, 1=U, 2=R, 3=D
     */
    private static final int[][] DIRS = {
            {-1, 0}, // LEFT
            { 0, 1}, // UP
            { 1, 0}, // RIGHT
            { 0,-1}  // DOWN
    };

    /**
     * Constructor initializes textures, animation, collision bounds, and spawn defaults.
     */
    public OBJ_Enemy() {
        setDefaultValue();
    }

    /**
     * Loads directional animations, sets starting position & collision area,
     * and initializes animation references.
     */
    private void setDefaultValue() {
        name = "Enemy";
        collision = true;
        scale = 3f;
        direction = 1; // default/facing (UP) — visual only; AI will update

        // Load animation sets from folders
        animUp    = loadAnimation("icons/up");
        animDown  = loadAnimation("icons/down");
        animLeft  = loadAnimation("icons/left");
        animRight = loadAnimation("icons/right");

        // Default facing/down until AI changes it
        currentAnimation = animDown;

        // Use first frame to calculate scaled size
        TextureRegion firstFrame = currentAnimation.getKeyFrame(0);
        enemyWidth  = firstFrame.getRegionWidth() * scale;
        enemyHeight = firstFrame.getRegionHeight() * scale;
        enemyHalfW  = enemyWidth / 2f;
        enemyHalfH  = enemyHeight / 2f;

        // Spawn at map tile (5,5) — horizontally & vertically centered in that tile
        mapX = 5 * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f - enemyHalfW;
        mapY = 5 * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f - enemyHalfH;

        // Collision rectangle (in enemy local space, shrunk inward 10px each side)
        solidArea = new Rectangle(10, 10, enemyWidth - 20, enemyHeight - 20);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Active animation reference
        animation = currentAnimation;

        // Force first steering decision on next update
        hasTargetTile = false;
    }

    /**
     * Loads all PNGs in a folder into an Animation that loops.
     * @param folderPath internal path (LibGDX files)
     */
    private Animation<TextureRegion> loadAnimation(String folderPath) {
        Array<TextureRegion> regions = new Array<>();
        FileHandle dir = Gdx.files.internal(folderPath);

        for (FileHandle file : dir.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);
                this.frames.add(tex);                 // keep texture so we can dispose later
                regions.add(new TextureRegion(tex));  // wrap as region for animation playback
            }
        }
        // 0.1s per frame, loop
        return new Animation<>(0.1f, regions, Animation.PlayMode.LOOP);
    }

    /**
     * Returns true if the map tile at (tx,ty) is walkable (tileNum==1 by your convention).
     */
    private boolean tileWalkable(Map map, int tx, int ty) {
        return ty >= 0 && ty < map.tileNum.length
            && tx >= 0 && tx < map.tileNum[0].length
            && map.tileNum[ty][tx] == 1;
    }

    /** World X of tile center. */
    private float tileCenterX(int tileX) {
        return tileX * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f;
    }

    /** World Y of tile center. */
    private float tileCenterY(int tileY) {
        return tileY * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE / 2f;
    }

    /**
     * Compute current tile coords from the enemy's center (mapX/mapY assumed top-left).
     */
    private void updateCurrentTile() {
        float centerX = mapX + enemyHalfW;
        float centerY = mapY + enemyHalfH;
        curTileX = (int)(centerX / GameScreen.TILE_SIZE);
        curTileY = (int)(centerY / GameScreen.TILE_SIZE);
    }

    /**
     * Has enemy reached the active target tile (within pixel epsilon)?
     */
    private boolean reachedTargetTile() {
        if (!hasTargetTile) return true;
        float tx = tileCenterX(targetTileX) - enemyHalfW; // convert to top-left world
        float ty = tileCenterY(targetTileY) - enemyHalfH;
        return Math.abs(mapX - tx) <= TILE_CENTER_EPS && Math.abs(mapY - ty) <= TILE_CENTER_EPS;
    }

    /**
     * Returns the direction index opposite to the input (0<->2, 1<->3).
     */
    private int oppositeDir(int d) {
        return switch (d) {
            case 0 -> 2; // LEFT <-> RIGHT
            case 2 -> 0;
            case 1 -> 3; // UP <-> DOWN
            case 3 -> 1;
            default -> -1;
        };
    }

    /**
     * Pick the next tile to move toward, combining chase (toward Knight) with randomness.
     * Avoids reversing direction unless forced.
     */
    private void chooseNextTile(GameScreen gs, boolean isChasing) {
        updateCurrentTile();

        // Adjust desire to chase based on whether Knight is "near"
        chaseBias = isChasing ? CHASE_BIAS_CHASE : CHASE_BIAS_IDLE;

        // Knight tile
        int knightTileX = (int)(gs.knight.positionX / GameScreen.TILE_SIZE);
        int knightTileY = (int)(gs.knight.positionY / GameScreen.TILE_SIZE);

        Array<int[]> options = new Array<>();
        int bestDir = -1;
        float bestDist = Float.MAX_VALUE;

        // Scan all 4 neighbor tiles
        for (int dirIdx = 0; dirIdx < 4; dirIdx++) {
            // Skip immediate 180° reversal if we have an alternative
            if (lastDirIndex != -1 && oppositeDir(lastDirIndex) == dirIdx) {
                continue;
            }

            int nx = curTileX + DIRS[dirIdx][0];
            int ny = curTileY + DIRS[dirIdx][1];
            if (tileWalkable(gs.map, nx, ny)) {
                options.add(new int[]{nx, ny, dirIdx});

                // Manhattan distance to Knight if we move into this tile
                float dist = Math.abs(knightTileX - nx) + Math.abs(knightTileY - ny);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestDir = dirIdx;
                }
            }
        }

        // No valid option except going back? Allow reversal.
        if (options.size == 0 && lastDirIndex != -1) {
            int backIdx = oppositeDir(lastDirIndex);
            int nx = curTileX + DIRS[backIdx][0];
            int ny = curTileY + DIRS[backIdx][1];
            if (tileWalkable(gs.map, nx, ny)) {
                options.add(new int[]{nx, ny, backIdx});
                bestDir = backIdx;
            }
        }

        // Still no options -> stuck, do nothing.
        if (options.size == 0) {
            hasTargetTile = false;
            return;
        }

        // Choose final direction: chase with probability chaseBias, else random among options
        int chosenDir;
        if (bestDir != -1 && Math.random() < chaseBias) {
            chosenDir = bestDir;
        } else {
            chosenDir = options.random()[2];
        }

        // Set the new target tile
        targetTileX = curTileX + DIRS[chosenDir][0];
        targetTileY = curTileY + DIRS[chosenDir][1];
        lastDirIndex = chosenDir;
        hasTargetTile = true;

        // Update facing animation to match chosen direction
        switch (chosenDir) {
            case 0 -> currentAnimation = animLeft;
            case 1 -> currentAnimation = animUp;
            case 2 -> currentAnimation = animRight;
            case 3 -> currentAnimation = animDown;
        }
        animation = currentAnimation;
        direction = chosenDir; // keep in sync with old code usage if referenced elsewhere
    }

    /**
     * Move toward the active target tile with smooth (pixel-based) motion.
     */
    private void moveTowardTarget(float delta) {
        if (!hasTargetTile) return;

        // Convert target tile center to top-left sprite origin
        float targetX = tileCenterX(targetTileX) - enemyHalfW;
        float targetY = tileCenterY(targetTileY) - enemyHalfH;

        float dx = targetX - mapX;
        float dy = targetY - mapY;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);

        float step = speed * delta;
        if (step >= dist) {
            // Reached (or overshot) -> snap to tile center
            mapX = targetX;
            mapY = targetY;
            hasTargetTile = false;
            return;
        }

        // Normalized move vector
        float vx = dx / dist;
        float vy = dy / dist;
        mapX += vx * step;
        mapY += vy * step;
    }

    // ------------------------------------------------------------------------
    // Rendering
    // ------------------------------------------------------------------------

    @Override
    public void drawObject(GameScreen gs) {
        if (!isDead()) {
            drawMonster(gs);
        }
    }

    /**
     * Draws the animated enemy on the screen if it's within the visible camera bounds.Converts world (mapX/mapY) to screen using Knight's camera origin (renderX/renderY).
     * @param screen
     */
    public void drawMonster(GameScreen screen) {
        // Advance animation
        stateTime += Gdx.graphics.getDeltaTime();

        // Current animation frame
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        // Convert world -> screen
        int screenX = (int)(mapX - screen.knight.positionX + screen.knight.renderX);
        int screenY = (int)(mapY - screen.knight.positionY + screen.knight.renderY);

        // Frustum culling: only draw if inside screen bounds + 1 tile margin
        if (mapX + GameScreen.TILE_SIZE > screen.knight.positionX - screen.knight.renderX
         && mapX - GameScreen.TILE_SIZE < screen.knight.positionX + screen.knight.renderX
         && mapY + GameScreen.TILE_SIZE > screen.knight.positionY - screen.knight.renderY
         && mapY - GameScreen.TILE_SIZE < screen.knight.positionY + screen.knight.renderY) {

            float frameWidth  = currentFrame.getRegionWidth();
            float frameHeight = currentFrame.getRegionHeight();

            screen.batch.draw(currentFrame, screenX, screenY, frameWidth * scale, frameHeight * scale);
        }
    }

    // ------------------------------------------------------------------------
    // Update (HYBRID AI) - call each frame from GameScreen
    // ------------------------------------------------------------------------
    public void update(float delta, GameScreen gs) {
        moveTimer += delta;

        // Refresh sprite size from current animation (handles differing frame sizes)
        TextureRegion key = animation.getKeyFrame(0);
        enemyWidth  = key.getRegionWidth() * scale;
        enemyHeight = key.getRegionHeight() * scale;
        enemyHalfW  = enemyWidth / 2f;
        enemyHalfH  = enemyHeight / 2f;

//        // Compute tile distance to Knight
//        int knightTileX = (int)(gs.knight.positionX / GameScreen.TILE_SIZE);
//        int knightTileY = (int)(gs.knight.positionY / GameScreen.TILE_SIZE);
//        updateCurrentTile();
//        float distTile = Math.abs(knightTileX - curTileX) + Math.abs(knightTileY - curTileY);
//
//        // Within chase radius? (3 tiles)
//        boolean isChasing = distTile <= 3;

        // Calculate the distance between enemy and the Knight
        float knightX = gs.knight.positionX;
        float knightY = gs.knight.positionY;
        float distance = Vector2.dst(mapX, mapY, knightX, knightY);

        // Determine if the enemy should start chasing the Knight (within 3 tiles)
        boolean isChasing = distance < GameScreen.TILE_SIZE * 3;

        // Update speed based on current state: chasing or idle
        speed = isChasing ? chaseSpeed : normalSpeed;

        // Need new steering decision?
        if (!hasTargetTile || reachedTargetTile() || (!isChasing && moveTimer >= moveInterval)) {
            moveTimer = 0f;
            chooseNextTile(gs, isChasing);
        }

        // Move toward the current target tile
        moveTowardTarget(delta);
    }

    // ------------------------------------------------------------------------
    // Combat / State
    // ------------------------------------------------------------------------

    /**
     * Apply damage; logs HP after hit.Death handled by isDead().
     * @param amount
     */
    public void takeDamage(int amount) {
        if (!isDead()) {
            hp -= amount;
            System.out.println("Enemy takes " + amount + " damage. HP: " + hp);
        }
    }

    /**
     * Returns true if HP has reached zero (or below).
     * @return 
     */
    public boolean isDead() {
        return hp <= 0;
    }

    // ------------------------------------------------------------------------
    // Cleanup
    // ------------------------------------------------------------------------

    /**
     * Dispose of all textures loaded by this enemy instance.
     */
    public void dispose() {
        for (Texture tex : frames) {
            tex.dispose();
        }
    }
}
