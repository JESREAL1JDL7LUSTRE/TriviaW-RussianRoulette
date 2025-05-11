package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
        bgTexture = new Texture(Gdx.files.internal("FirstScreen.png"));
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // 2) Build a simple table layout
        Table table = new Table();
        table.setFillParent(true);
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

        TextButton playBtn = new TextButton("Play", style);
        TextButton optionsBtn = new TextButton("Options", grayStyle);
        TextButton howToPlayBtn = new TextButton("How To Play", grayStyle);
        TextButton aboutBtn = new TextButton("About", grayStyle);
        TextButton exitBtn = new TextButton("Exit", grayStyle);
        TextButton addOwnQuestionsBtn = new TextButton("Add Topic/Questions", grayStyle);
        TextButton addEditQuestionsBtn = new TextButton("Add Edit Questions", grayStyle);

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
        table.add(playBtn).width(250).height(70).pad(10).padLeft(1000);
        table.row();
        table.add(addOwnQuestionsBtn).width(250).height(70).pad(10).padLeft(1000);
        table.row();
        table.add(addEditQuestionsBtn).width(250).height(70).pad(10).padLeft(1000);
        table.row();
        table.add(optionsBtn).width(250).height(70).pad(10).padLeft(1000);
        table.row();
        table.add(howToPlayBtn).width(250).height(70).pad(10).padLeft(1000);
        table.row();
        table.add(aboutBtn).width(250).height(70).pad(10).padLeft(1000);
        table.row();
        table.add(exitBtn).width(250).height(70).pad(10).padLeft(1000);
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
