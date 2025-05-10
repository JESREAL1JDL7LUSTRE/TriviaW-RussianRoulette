package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FirstScreen extends BaseScreen {
    private Texture bgTexture;

    public FirstScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        // 1) Create a Stage and set input to it
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 2) Add background
        bgTexture = new Texture(Gdx.files.internal("Home.png"));
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // 2) Build a simple table layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // 3) Create buttons
        TextButton playBtn = new TextButton("Play", game.uiSkin);
        TextButton optionsBtn = new TextButton("Options", game.uiSkin);
        TextButton howToPlayBtn = new TextButton("How To Play", game.uiSkin);
        TextButton aboutBtn = new TextButton("About", game.uiSkin);
        TextButton exitBtn = new TextButton("Exit", game.uiSkin);
        TextButton addOwnQuestionsBtn = new TextButton("Add Topic/Questions", game.uiSkin);
        TextButton addEditQuestionsBtn = new TextButton("Add Edit Questions", game.uiSkin);

        // 4) Add listeners
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Switch to the gameplay screen
                game.setScreen(new CustomizeGameplay(game));
            }
        });
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        aboutBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new About(game));
            }
        });
        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Options(game));
            }
        });
        howToPlayBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new HowToPlay(game));
            }
        });
        addOwnQuestionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AddOwnQuestions(game));
            }
        });
        addEditQuestionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EditQuestions(game));
            }
        });

        // 5) Lay out buttons in the table
        table.add(playBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(addOwnQuestionsBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(addEditQuestionsBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(optionsBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(howToPlayBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(aboutBtn).width(200).height(50).pad(10).padLeft(1000);
        table.row();
        table.add(exitBtn).width(200).height(50).pad(10).padLeft(1000);
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
        super.dispose();
        stage.dispose();
        if (bgTexture != null) {
            bgTexture.dispose();
        }
    }
}
