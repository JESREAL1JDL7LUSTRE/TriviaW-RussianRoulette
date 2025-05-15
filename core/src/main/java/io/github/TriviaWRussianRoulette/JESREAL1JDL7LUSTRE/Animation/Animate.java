package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Animate extends Actor {
    private Texture spriteSheet;
    private TextureRegion[] frames;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private boolean looping;
    private int frameLimit; // Limit animation to this many frames (0 = all frames)

    public Animate(String spritePath, int rows, int cols, float frameDuration, boolean looping) {
        this(spritePath, rows, cols, frameDuration, looping, 0);
    }

    public Animate(String spritePath, int rows, int cols, float frameDuration, boolean looping, int frameLimit) {
        this.looping = looping;
        this.frameLimit = frameLimit;
        this.stateTime = 0f;

        // Load sprite sheet
        spriteSheet = new Texture(Gdx.files.internal(spritePath));

        // Calculate frame dimensions
        int frameWidth = spriteSheet.getWidth() / cols;
        int frameHeight = spriteSheet.getHeight() / rows;

        // Extract frames from sprite sheet
        TextureRegion[][] tmp = TextureRegion.split(
            spriteSheet, frameWidth, frameHeight);

        // Calculate total frames (respecting frameLimit if set)
        int totalFrames = rows * cols;
        if (frameLimit > 0 && frameLimit < totalFrames) {
            totalFrames = frameLimit;
        }

        frames = new TextureRegion[totalFrames];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (index < totalFrames) {
                    frames[index++] = tmp[i][j];
                }
            }
        }

        // Create animation
        animation = new Animation<>(frameDuration, frames);

        // Set actor size based on frame dimensions
        setSize(frameWidth, frameHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    private boolean flipX = false;
    private boolean flipY = false;

    /**
     * Sets whether to flip the sprite horizontally.
     * @param flip True to flip horizontally, false otherwise
     */
    public void setFlipX(boolean flip) {
        this.flipX = flip;
    }

    /**
     * Sets whether to flip the sprite vertically.
     * @param flip True to flip vertically, false otherwise
     */
    public void setFlipY(boolean flip) {
        this.flipY = flip;
    }

    /**
     * Gets whether the sprite is flipped horizontally.
     * @return True if flipped horizontally, false otherwise
     */
    public boolean isFlippedX() {
        return flipX;
    }

    /**
     * Gets whether the sprite is flipped vertically.
     * @return True if flipped vertically, false otherwise
     */
    public boolean isFlippedY() {
        return flipY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1, 1, 1, parentAlpha * getColor().a);

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, looping);

        if (currentFrame != null) {
            batch.draw(
                currentFrame,
                flipX ? getX() + getWidth() : getX(),
                flipY ? getY() + getHeight() : getY(),
                flipX ? -getOriginX() : getOriginX(),
                flipY ? -getOriginY() : getOriginY(),
                flipX ? -getWidth() : getWidth(),
                flipY ? -getHeight() : getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
            );
        }
    }

    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(stateTime);
    }

    public void resetAnimation() {
        stateTime = 0f;
    }

    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }
}
