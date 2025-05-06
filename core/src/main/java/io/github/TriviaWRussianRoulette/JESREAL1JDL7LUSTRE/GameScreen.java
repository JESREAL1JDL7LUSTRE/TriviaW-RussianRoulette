package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends BaseScreen {
    private TriviaTopic triviaTopic;

    public GameScreen(Main game, TriviaTopic triviaTopic) {
        super(game);
        this.triviaTopic = triviaTopic;
    }

    @Override
    public void show() {
        super.show();

        // Safety check to avoid null pointer exceptions
        if (triviaTopic == null) {
            Gdx.app.error("GameScreen", "TriviaTopic is null!");
            return;
        }

        // Log the selected topic to verify it's passed correctly
        Gdx.app.log("GameScreen", "Selected Topic: " + triviaTopic.getTopic());

        // Check if the topic has questions
        if (triviaTopic.getQuestions() == null || triviaTopic.getQuestions().size == 0) {
            Gdx.app.error("GameScreen", "No questions found for this topic!");
        } else {
            // Log all questions to verify they're loaded correctly
            for (Question question : triviaTopic.getQuestions()) {
                Gdx.app.log("GameScreen", "Question: " + question.getQuestion());
                Gdx.app.log("GameScreen", "Choices: " + question.getChoices());
                Gdx.app.log("GameScreen", "Answer: " + question.getAnswer());
            }
        }

        // Initialize game world
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
        super.render(delta);
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
