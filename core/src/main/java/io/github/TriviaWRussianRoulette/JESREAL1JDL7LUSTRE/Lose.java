package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Animation.Animate;

public class Lose extends BaseScreen {

    private Texture backgroundTexture;
    private Image backgroundImage;
    private Sound loseSound;
    private Animate kingAnimation;

    public Lose(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        loseSound = Gdx.audio.newSound(Gdx.files.internal("Lose.mp3"));
        loseSound.play(1.0f);

        backgroundTexture = new Texture(Gdx.files.internal("GameScreen.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Create the king wizard animation (all frames)
        kingAnimation = new Animate("assets/king_death.png", 3, 4, 0.2f, true);
        // Position the animation in a prominent spot
        kingAnimation.setSize(500, 500);
        kingAnimation.setPosition(
            Gdx.graphics.getWidth() / 2 - 250,  // Center horizontally
            Gdx.graphics.getHeight() - 500      // Near top of screen
        );
        stage.addActor(kingAnimation);

        // Title label
        Label title = new Label("You Lose  :(", skin);
        title.setFontScale(3);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Drawable upDrawable   = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/red_button.png"))));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/red_button_pressed.png"))));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up   = upDrawable;
        style.down = downDrawable;
        style.font = new BitmapFont();

        Drawable grayUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        Drawable grayDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));
        TextButton.TextButtonStyle grayStyle = new TextButton.TextButtonStyle();
        grayStyle.up = grayUp;
        grayStyle.down = grayDown;
        grayStyle.font = new BitmapFont();

        TextButton playBtn = new TextButton("Play Again", style);
        playBtn.getLabel().setFontScale(1.4f);
        TextButton exitBtn = new TextButton("Exit", grayStyle);
        exitBtn.getLabel().setFontScale(1.4f);
        TextButton mainMenuBtn = new TextButton("Main Menu", grayStyle);
        mainMenuBtn.getLabel().setFontScale(1.4f);

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CustomizeGameplay(game));
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        mainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstScreen(game));
            }
        });

// Layout
        table.top().padTop(300);
        table.add(title).padBottom(40).colspan(1).center().row();
        table.add(playBtn).width(250).height(70).pad(10).center().row();
        table.add(mainMenuBtn).width(250).height(70).pad(10).center().row();
        table.add(exitBtn).width(250).height(70).pad(10).center();

    }

    @Override
    public void dispose() {
        super.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (loseSound != null) loseSound.dispose();
        if (kingAnimation != null) kingAnimation.dispose();
    }
}
