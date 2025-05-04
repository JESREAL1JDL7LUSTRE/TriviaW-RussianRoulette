package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen extends ScreenAdapter {
    private final Main game;
    private Stage stage;

    public FirstScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // 1) Create a Stage and set input to it
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 2) Build a simple table layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // 3) Create buttons
        TextButton playBtn = new TextButton("Play", game.uiSkin);
        TextButton Options = new TextButton("Options", game.uiSkin);
        TextButton HowToPlay = new TextButton("How To Play", game.uiSkin);
        TextButton About = new TextButton("About", game.uiSkin);
        TextButton exitBtn = new TextButton("Exit", game.uiSkin);

        // 4) Add listeners
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Switch to the gameplay screen
                game.setScreen(new io.github.Main.JESREAL1JDL7LUSTRE.GameScreen(game));
            }
        });
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // 5) Lay out buttons in the table
        table.add(playBtn).width(200).height(50).pad(10);
        table.row();
        table.add(Options).width(200).height(50).pad(10);
        table.row();
        table.add(HowToPlay).width(200).height(50).pad(10);
        table.row();
        table.add(About).width(200).height(50).pad(10);
        table.row();
        table.add(exitBtn).width(200).height(50).pad(10);
    }

    @Override
    public void render(float delta) {
        // Clear and draw UI
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
