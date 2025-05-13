package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Align;

public class YouWin extends BaseScreen {

    private Texture backgroundTexture;
    private Image backgroundImage;

    public YouWin(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("win.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Title label
        Label title = new Label("You Win This Time!", skin); // optional style
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
        table.top().padTop(300); // Move everything down a bit from top
        table.add(title).padBottom(40).colspan(1).center().row();
        table.add(playBtn).width(250).height(70).pad(10).center().row();
        table.add(mainMenuBtn).width(250).height(70).pad(10).center().row();
        table.add(exitBtn).width(250).height(70).pad(10).center();

    }

    @Override
    public void dispose() {
        super.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
