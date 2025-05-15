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
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Button.Button;

public class YouWin extends BaseScreen {

    private Texture backgroundTexture;
    private Image backgroundImage;
    private Sound winSound;
    private Animate wizardAnimation;

    public YouWin(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        winSound = Gdx.audio.newSound(Gdx.files.internal("bgm/Win.mp3"));
        winSound.play(1.0f);

        backgroundTexture = new Texture(Gdx.files.internal("images/win.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Create the king wizard animation (all frames)
        wizardAnimation = new Animate("sprites/wizard_death.png", 4, 4, 0.2f, true);
        // Position the animation in a prominent spot
        wizardAnimation.setSize(500, 500);
        wizardAnimation.setPosition(
            Gdx.graphics.getWidth() / 2 - 250,  // Center horizontally
            Gdx.graphics.getHeight() - 500      // Near top of screen
        );
        stage.addActor(wizardAnimation);

        // Title label
        Label title = new Label("You Win This Time!", skin); // optional style
        title.setFontScale(3);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        TextButton playBtn = new TextButton("Play Again", Button.getRedStyle());
        playBtn.getLabel().setFontScale(1.4f);
        TextButton exitBtn = new TextButton("Exit", Button.getGrayStyle());
        exitBtn.getLabel().setFontScale(1.4f);
        TextButton mainMenuBtn = new TextButton("Main Menu", Button.getGrayStyle());
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
        if (winSound != null) winSound.dispose();
        if (wizardAnimation != null) wizardAnimation.dispose();
    }
}
