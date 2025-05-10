package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

        TextButton musicOptionsBtn = new TextButton("Music", game.uiSkin);

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

        table.add(musicOptionsBtn).width(200).height(50).pad(10).padRight(1000);

    }
}
