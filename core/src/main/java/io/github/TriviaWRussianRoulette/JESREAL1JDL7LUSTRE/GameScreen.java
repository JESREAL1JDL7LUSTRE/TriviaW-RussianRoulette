package io.github.Main.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Main;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private Stage stage;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize your game world here:
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // TODO: add your roulette wheel actor, question labels, answer buttons, etc.
    }

    @Override
    public void render(float delta) {
        // 1) Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2) Update your game logic (spin wheel, check answers)
        // e.g., rouletteActor.update(delta);

        // 3) Draw everything
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
        // Dispose any game-specific textures or sounds here
    }
}
