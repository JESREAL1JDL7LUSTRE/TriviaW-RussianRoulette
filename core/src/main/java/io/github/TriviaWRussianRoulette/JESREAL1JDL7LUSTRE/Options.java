package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class Options extends BaseScreen {
    public Options(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show(); // initializes stage, skin, and burger

        // Initialize BgMusic options
        BgMusic.initMusicOptions();

        // Create title
        Label title = new Label("Options", skin);
        title.setFontScale(2);
        title.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 100, Align.center);
        stage.addActor(title);

        Drawable upDrawable   = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up   = upDrawable;
        style.down = downDrawable;
        style.font = new BitmapFont();

        TextButton musicOptionsBtn = new TextButton("Music", style);
        musicOptionsBtn.getLabel().setFontScale(1.4f);

        musicOptionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Switch to the gameplay screen
                game.setScreen(new ChangeMusic(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(musicOptionsBtn).width(250).height(70).pad(10);

    }
}
