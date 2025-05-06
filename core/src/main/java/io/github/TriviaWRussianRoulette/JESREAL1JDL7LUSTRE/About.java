package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class About extends BaseScreen {
    private Stage stage;
    private final SpriteBatch batch;
    private final BitmapFont font;

    public About(Main game) {
        super(game);
        batch = new SpriteBatch();
        font  = new BitmapFont();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1) clear the screen to a visible color (dark gray)
        super.render(delta);
        super.draw();

        // 2) draw something
        batch.begin();
        font.draw(batch,
            "Welcome to Trivia Russian Roulette!",
            Gdx.graphics.getWidth() * 0.1f,
            Gdx.graphics.getHeight() * 0.5f);
        batch.end();
    }

}
