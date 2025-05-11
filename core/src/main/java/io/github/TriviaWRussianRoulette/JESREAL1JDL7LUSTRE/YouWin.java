package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

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
        backgroundTexture = new Texture(Gdx.files.internal("GameScreen.png")); // Adjust path
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage); // Add background first (goes behind everything)

        // Title label
        Label title = new Label("You Win This time", skin); // optional style
        title.setFontScale(2);
        title.setPosition(100, 300);
        stage.addActor(title);

        // Button table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playBtn = new TextButton("Play Again", game.uiSkin);
        TextButton exitBtn = new TextButton("Exit", game.uiSkin);

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

        table.add(playBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(exitBtn).width(200).height(50).pad(10).padLeft(1000);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
