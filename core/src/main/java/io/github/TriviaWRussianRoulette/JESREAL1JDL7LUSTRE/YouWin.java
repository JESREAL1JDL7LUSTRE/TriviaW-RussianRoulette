package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class YouWin extends BaseScreen {
    private Texture frameTexture;
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

        Drawable grayUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        Drawable grayDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));
        TextButton.TextButtonStyle grayStyle = new TextButton.TextButtonStyle();
        grayStyle.up = grayUp;
        grayStyle.down = grayDown;
        grayStyle.font = new BitmapFont();

// Load image and label
        Texture frameTexture = new Texture(Gdx.files.internal("speechbubble1.png"));
        frameTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image frameImage = new Image(frameTexture);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.BLACK;

        Label title = new Label("You Win This Time", labelStyle);
        title.setFontScale(2f);
        title.setAlignment(Align.center);
        title.setWrap(true);

// STEP 1: Choose a desired max width for the label
        float maxLabelWidth = 400f; // Or whatever size makes sense in your UI
        title.setWidth(maxLabelWidth); // force the label's width
        title.layout(); // triggers computation of preferred height at that width

// STEP 2: Resize label height and image accordingly
        float labelHeight = title.getPrefHeight() + 20f;
        title.setSize(maxLabelWidth, labelHeight); // fully specify size

        float padding = 40f; // optional visual margin around text
        frameImage.setSize(maxLabelWidth + padding, labelHeight + padding);

// STEP 3: Add to stack
        Stack textFrame = new Stack();
        textFrame.setSize(frameImage.getWidth(), frameImage.getHeight());
        textFrame.addActor(frameImage);
        textFrame.addActor(title);

// STEP 4: Position the stack wherever you want
        textFrame.setPosition(900, 700);
        stage.addActor(textFrame);


        // Button table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playBtn = new TextButton("Play Again", grayStyle);
        TextButton exitBtn = new TextButton("Exit", grayStyle);

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
