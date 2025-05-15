package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CustomizeGameplay extends BaseScreen {
    private enum Step {
        DIFFICULTY,
        RANDOM_CHOICES,
        ON_DEATH,
        COMPLETE
    }

    Sound loadBullet1 = Gdx.audio.newSound(Gdx.files.internal("Load1bullet.mp3"));
    Sound loadBullet2 = Gdx.audio.newSound(Gdx.files.internal("Load2bullets.mp3"));
    Sound loadBullet3 = Gdx.audio.newSound(Gdx.files.internal("Load3bullets.mp3"));

    private Step currentStep = Step.DIFFICULTY;
    private int selectedDifficulty = 1;
    private boolean selectedRandomChoices = false;
    private boolean selectedOnDeath = true;
    private Texture bgTexture;

    private TextButton.TextButtonStyle grayButton;

    public CustomizeGameplay(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        // Load gray button style
        grayButton = new TextButton.TextButtonStyle();
        grayButton.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        grayButton.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));
        grayButton.font = new BitmapFont();

        // Create burger first but don't add it yet
        burger = new Burger(skin, game);

        // Setup the initial UI without adding burger yet
        setupStepUI();

        // Now add burger on top of everything else
        stage.addActor(burger);

        // Set input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    private void setupStepUI() {
        // Don't clear the stage here - we want to keep the burger
        // Add or replace background
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        bgTexture = new Texture(Gdx.files.internal("Gameplay.png"));
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        background.setName("background");

        // Remove old background if it exists
        if (stage.getRoot().findActor("background") != null) {
            stage.getRoot().findActor("background").remove();
        }

        // Add background at the bottom of the z-order
        stage.addActor(background);
        background.toBack();

        // Remove old content table if it exists
        if (stage.getRoot().findActor("contentTable") != null) {
            stage.getRoot().findActor("contentTable").remove();
        }

        // Add new content table
        Table table = new Table();
        table.setName("contentTable");
        table.setFillParent(true);
        table.center();
        table.defaults().padBottom(30).size(250, 70);
        stage.addActor(table);

        switch (currentStep) {
            case DIFFICULTY:
                showDifficultyStep(table);
                break;
            case RANDOM_CHOICES:
                showRandomChoicesStep(table);
                break;
            case ON_DEATH:
                showOnDeathStep(table);
                break;
            case COMPLETE:
                goToNextScreen();
                break;
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text, skin);
        label.setFontScale(3f);
        label.setAlignment(Align.center);
        return label;
    }

    private TextButton createStyledButton(String text) {
        TextButton button = new TextButton(text, grayButton);
        button.getLabel().setFontScale(1.5f);
        return button;
    }

    private void showDifficultyStep(Table table) {
        Label label = createStyledLabel("Select Difficulty:");
        TextButton easy = createStyledButton("Easy 1/6 Bullet");
        TextButton medium = createStyledButton("Medium 2/6 Bullets");
        TextButton hard = createStyledButton("Hard 3/6 Bullets");

        easy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 1;
                currentStep = Step.RANDOM_CHOICES;
                loadBullet1.play();
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        medium.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 2;
                currentStep = Step.RANDOM_CHOICES;
                loadBullet2.play();
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        hard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 3;
                currentStep = Step.RANDOM_CHOICES;
                loadBullet3.play();
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        table.add(label).padBottom(50).row();
        table.add(easy).row();
        table.add(medium).row();
        table.add(hard).row();
    }

    private void showRandomChoicesStep(Table table) {
        Label label = createStyledLabel("Enable random choices?");
        TextButton yes = createStyledButton("Yes");
        TextButton no = createStyledButton("No");

        yes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedRandomChoices = true;
                currentStep = Step.ON_DEATH;
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        no.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedRandomChoices = false;
                currentStep = Step.ON_DEATH;
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        table.add(label).padBottom(50).row();
        table.add(yes).row();
        table.add(no).row();
    }

    private void showOnDeathStep(Table table) {
        Label label = createStyledLabel("What happens upon Death?");
        TextButton reincarnate = createStyledButton("Reincarnate");
        TextButton die = createStyledButton("Die in Peace");

        reincarnate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedOnDeath = true;
                currentStep = Step.COMPLETE;
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        die.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedOnDeath = false;
                currentStep = Step.COMPLETE;
                setupStepUI();
                // No need to re-add burger, it's preserved
            }
        });

        table.add(label).padBottom(50).row();
        table.add(reincarnate).row();
        table.add(die).row();
    }

    private void goToNextScreen() {
        game.setScreen(new TopicChoice(game, this));
    }

    public int difficulty() {
        return selectedDifficulty;
    }

    public boolean randomChoices() {
        return selectedRandomChoices;
    }

    public boolean onDeath() {
        return selectedOnDeath;
    }

    @Override
    public void render(float delta) {
        // 1) Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3) Draw everything
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }
}
