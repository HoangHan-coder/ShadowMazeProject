/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.model;

import com.ShadowMaze.skill.Sword;
import static com.ShadowMaze.model.Entity.Direction.DOWN;
import static com.ShadowMaze.model.Entity.Direction.LEFT;
import static com.ShadowMaze.model.Entity.Direction.RIGHT;
import static com.ShadowMaze.model.Entity.Direction.UP;
import com.ShadowMaze.screen.GameScreen;
import com.ShadowMaze.skill.Fireball;
import com.ShadowMaze.uis.HpBar;
import com.ShadowMaze.uis.StaminaBar;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Knight class represents the main player character in the game. It handles
 * movement, collision detection, animation, stamina and HP logic, as well as
 * interactions with objects and map transitions.
 */
public class Knight extends Entity {

    // Position to render the character on screen (fixed to center)
    public int renderX;
    public int renderY;
   

    // Key possession state
    boolean hasKey;
    private Array<Sword> skills = new Array<>();
    public boolean isRunning = false;  // C? ki?m tra ?ang ch?y
    public int baseSpeed = 5;          // T?c ?? ?i b?
    private boolean hasFired = false; // ?� b?n k? n?ng ch?a?
    // Movement and stamina management
    public final int runSpeed = 8;
    private float staminaDrainRate;
    private float staminaRegenRate;

    // UI elements
    private StaminaBar staminaBar;
    private HpBar hpBar;

    // Game context
    GameScreen gs;
    
    //skill
    public Fireball currentFireball; // Biến để lưu quả cầu lửa hiện tại

    // Animation timing
    float stateTime;
    private Direction dir;
    private Direction lastMoveDirection = Direction.RIGHT;

    // Animations for each direction
    Animation<TextureRegion> moveUp, moveDown, moveLeft, moveRight;
    private Entity.Direction direction; // ? bi?n n�y ?� t?n t?i

    /**
     * Constructs a Knight entity.
     *
     * @param gs the current GameScreen
     * @param staminaBar the player's stamina bar
     * @param hpBar the player's health bar
     */
    public Knight(GameScreen gs, HpBar hpBar) {
        this.gs = gs;
//        this.staminaBar = staminaBar;
        this.hpBar = hpBar;
        this.speed = baseSpeed;
        this.hpBar = hpBar; // G�n HpBar
        this.hpBar = hpBar; // G�n HpBars
        setDefaultValue();
        dir = currentDirection;

    }

    /**
     * Set initial values for position, stats, animations, and hitbox.
     */
    private void setDefaultValue() {
        speed = 4;
        stateTime = 0f;
        hasKey = false;

        
        
        // Render at screen center
        renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
        renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);

        // Define collision area
        solidArea = new Rectangle();
        solidArea.x = 4;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        // Set start position
        positionX = 36 * GameScreen.TILE_SIZE;
        positionY = 28 * GameScreen.TILE_SIZE;

        // Stamina drain and regen rates
        staminaDrainRate = 30f;
        staminaRegenRate = 15f;

        // Load animations
        moveUp = loadUpAnimation();
        moveDown = loadDownAnimation();
        moveLeft = loadLeftAnimation();
        moveRight = loadRightAnimation();
    }

    /**
     * Sets the movement direction and resets animation timer if changed.
     *
     * @param direction
     */
    public void setDirection(Direction direction) {
        if (direction != currentDirection && direction != Direction.IDLE) {
            hasFired = false; // ??i h??ng ? cho ph�p b?n l?i
        }
        if (direction != Direction.IDLE) {
            lastMoveDirection = direction;
        }
        currentDirection = direction;
    }
    
    public void castFireball() {
        if (currentFireball == null || !currentFireball.isActive()) {
            currentFireball = new Fireball();
            currentFireball.setMapSize(gs.map.getMapWidthPixels(), gs.map.getMapHeightPixels());
            Direction fireDir = (currentDirection != Direction.IDLE) ? currentDirection : lastMoveDirection;
            currentFireball.activate(new Vector2(positionX, positionY), fireDir);
            currentFireball.shoot();
        }
    }


    /**
     * Updates the animation state.
     *
     * @param delta frame delta time
     */
    public void update(float delta) {
        stateTime += delta;

        for (int i = 0; i < skills.size; i++) {
            Sword skill = skills.get(i);

            
//            if (skill.isActive() && currentDirection != Direction.IDLE && skill.getDirection() != currentDirection) {
//                skill.setActive(false);  
//            }
            skill.update(delta);

            if (!skill.isActive()) {
                skills.removeIndex(i);
                i--;
            }
        }
    }

    /**
     * Set the knight's position on the map.
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Renders the knight on screen based on current animation and direction.
     *
     * @param delta
     */
    public void knightRender(float delta) {
        update(delta);
        for (Sword skill : skills) {
            skill.render(gs.batch, positionX, positionY, renderX, renderY);
        }
        Animation<TextureRegion> currentAnim = switch (currentDirection) {
            case UP ->
                moveDown;
            case DOWN ->
                moveUp;
            case LEFT ->
                moveLeft;
            case RIGHT ->
                moveRight;
            default ->
                moveDown;
        };

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        if (isDead == false) {
            gs.batch.draw(frame, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        } else {
            gs.batch.draw(image, renderX, renderY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }
        
        if (currentFireball != null && currentFireball.isActive()) {
            currentFireball.update(Gdx.graphics.getDeltaTime());
            currentFireball.render(gs.batch, positionX, positionY);
        }

        
        //debug hit box
//        ShapeRenderer shapeRenderer = new ShapeRenderer();
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.rect(
//                renderX + solidArea.x,
//                renderY + solidArea.y,
//                solidArea.width,
//                solidArea.height
//        );
//       
//        shapeRenderer.end();
    }

    public void inputHandle(float delta) {
        if (!isDead) {
            movementHandle(delta);
        }
    }
    
    /**
     * Handles input for movement, stamina, HP, collisions, and map switching.
     *
     * @param delta
     */
    public void movementHandle(float delta) {
        // Direction input
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            setDirection(Direction.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            setDirection(Direction.DOWN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            setDirection(Direction.RIGHT);
        } else {
            setDirection(Direction.IDLE);
        }
        
        //use skill
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            castFireball();
        }


        // Get current stamina/HP
//        float currentStamina = staminaBar.getCurrentStamina();
        float currentHp = hpBar.getCurrentHp();

        // Shift key affects HP/Stamina
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            isRunning = true;
            speed = runSpeed;
//            staminaBar.setCurrentStamina(Math.max(0, currentStamina - staminaDrainRate * delta));
            hpBar.setCurrentHp(currentHp - 30 * delta);
        } else {
            isRunning = false;
            speed = baseSpeed;
            if (currentHp < hpBar.getMaxHp()) {
                hpBar.setCurrentHp(currentHp + 10 * delta);
            }
//            staminaBar.regenerate(delta);
        }

        // Check collisions
        collisionOn = false;
        gs.cCheck.checkTile(this);
        
        int indexObject = gs.cCheck.checkObject(this, true);
        pickUpObject(indexObject);
        
        gs.cCheck.checkEnemyCollision(this);

        if (currentFireball != null && currentFireball.isActive()) {
            gs.cCheck.checkFireballCollision(currentFireball);
        }
        // Move if no collision
        if (!collisionOn) {
            switch (currentDirection) {
                case UP ->
                    positionY -= speed;
                case DOWN ->
                    positionY += speed;
                case LEFT ->
                    positionX -= speed;
                case RIGHT ->
                    positionX += speed;
            }

            // Reset speed after move
            speed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runSpeed : baseSpeed;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !hasFired) {
            Entity.Direction skillDir = (currentDirection == Direction.IDLE)
                    ? lastMoveDirection
                    : currentDirection;

            Sword skill = new Sword(skillDir);
            skill.activate(this, skillDir);
            hasFired = true; // ?�nh d?u ?� b?n
            skills.add(skill);
        }

// Reset hasFired n?u kh�ng c�n nh?n SPACE
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            hasFired = false;
        }
    }

    /**
     * Handles interaction with objects based on collision index.
     *
     * @param indexOfObject the index in the object array
     */
    public void pickUpObject(int indexOfObject) {
        if (indexOfObject != -1) {
            System.out.println("Knight collided with: " + gs.obj[indexOfObject].name);
            String objectName = gs.obj[indexOfObject].name;
            switch (objectName) {
                case "Key" -> {
                    hasKey = true;
                    gs.obj[indexOfObject] = null;
                }
                case "Gate" -> {
                    if (hasKey) {
                        collisionOn = false;
                        gs.obj[indexOfObject].image = new Texture("Object/gate_open.png");
                    }
                }
                case "Cave" -> {
                    if (hasKey) {
                        System.out.println("You win!");
                    }
                }
                case "Enemy" -> {
                    System.out.println("You die!");
                }
            }
        }
    }

    /**
     * Load animation for upward movement.
     */
    private Animation<TextureRegion> loadUpAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_up_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    private Animation<TextureRegion> loadDownAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_down_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    private Animation<TextureRegion> loadLeftAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_left_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    private Animation<TextureRegion> loadRightAnimation() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("knight/knight_right_" + (i + 1) + ".png"));
        }
        return new Animation<>(0.6f, frames);
    }

    /**
     * Disposes all loaded textures used in the knight.
     */
    public void dispose() {
        if (image != null) {
            image.dispose();
        }

        for (TextureRegion region : moveUp.getKeyFrames()) {
            region.getTexture().dispose();
        }
        for (TextureRegion region : moveDown.getKeyFrames()) {
            region.getTexture().dispose();
        }
        for (TextureRegion region : moveLeft.getKeyFrames()) {
            region.getTexture().dispose();
        }
        for (TextureRegion region : moveRight.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }

    public Direction getDirection() {
        return this.currentDirection != null ? this.currentDirection : Direction.IDLE;
    }

    public Direction getLastMoveDirection() {
        return lastMoveDirection;
    }
}
