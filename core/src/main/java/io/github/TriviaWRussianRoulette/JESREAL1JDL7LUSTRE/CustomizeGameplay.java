package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Button.Button;

public class CustomizeGameplay extends BaseScreen {

    private enum Step {
        DIFFICULTY,
        SHOW_GUN_ANIMATION,
        RANDOM_CHOICES,
        ON_DEATH,
        COMPLETE
    }

    Sound loadBullet1 = Gdx.audio.newSound(Gdx.files.internal("bgm/Load1bullet.mp3"));
    Sound loadBullet2 = Gdx.audio.newSound(Gdx.files.internal("bgm/Load2bullets.mp3"));
    Sound loadBullet3 = Gdx.audio.newSound(Gdx.files.internal("bgm/Load3bullets.mp3"));

    private Step currentStep = Step.DIFFICULTY;
    private int selectedDifficulty = 1;
    private boolean selectedRandomChoices = false;
    private boolean selectedOnDeath = true;
    private Texture bgTexture;
    private Table contentTable;

    // Animation related
    private Animation<TextureRegion> easyGunAnim, mediumGunAnim, hardGunAnim;
    private float animationTime = 0f;
    private Image animationImage;

    public CustomizeGameplay(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        burger = new Burger(skin, game);
        stage.addActor(burger);

        // Prepare animations
        prepareGunAnimations();

        setupStepUI();

        Gdx.input.setInputProcessor(stage);
    }

    private void prepareGunAnimations() {

        easyGunAnim = loadAnimation("sprites/gun_easy.png", 3, 5, 0.3f);
        mediumGunAnim = loadAnimation("sprites/gun_medium.png", 3, 5, 0.5f);
        hardGunAnim = loadAnimation("sprites/gun_hard.png", 4, 5, 0.55f);

        animationImage = new Image();
        animationImage.setVisible(false);
        stage.addActor(animationImage);
    }

    private Animation<TextureRegion> loadAnimation(String filePath, int cols, int rows, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    private void setupStepUI() {
        // Remove previous content table if exists
        if (contentTable != null) {
            contentTable.remove();
            contentTable = null;
        }

        if (bgTexture != null) {
            bgTexture.dispose();
        }
        bgTexture = new Texture(Gdx.files.internal("images/Gameplay.png"));
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        background.setName("background");

        Actor oldBg = stage.getRoot().findActor("background");
        if (oldBg != null) oldBg.remove();

        stage.addActor(background);
        background.toBack();


        if (currentStep == Step.SHOW_GUN_ANIMATION) {
            animationImage.setVisible(true);
            animationTime = 0f; // reset animation time
            float width = 700f;
            float height = 700f;

            animationImage.setSize(width, height);
            animationImage.setPosition(
                (Gdx.graphics.getWidth() - width) / 2f,
                (Gdx.graphics.getHeight() - height) / 2f
            );

        } else {
            animationImage.setVisible(false);

            contentTable = new Table();
            contentTable.setName("contentTable");
            contentTable.setFillParent(true);
            contentTable.center();
            contentTable.defaults().padBottom(30).size(250, 70);
            stage.addActor(contentTable);

            switch (currentStep) {
                case DIFFICULTY:
                    showDifficultyStep(contentTable);
                    break;
                case RANDOM_CHOICES:
                    showRandomChoicesStep(contentTable);
                    break;
                case ON_DEATH:
                    showOnDeathStep(contentTable);
                    break;
                case COMPLETE:
                    goToNextScreen();
                    break;
            }
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text, skin);
        label.setFontScale(3f);
        label.setAlignment(Align.center);
        return label;
    }

    private TextButton createStyledButton(String text) {
        TextButton button = new TextButton(text, Button.getGrayStyle());
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
                loadBullet1.play();
                currentStep = Step.SHOW_GUN_ANIMATION;
                setupStepUI();
            }
        });

        medium.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 2;
                loadBullet2.play();
                currentStep = Step.SHOW_GUN_ANIMATION;
                setupStepUI();
            }
        });

        hard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 3;
                loadBullet3.play();
                currentStep = Step.SHOW_GUN_ANIMATION;
                setupStepUI();
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
            }
        });

        no.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedRandomChoices = false;
                currentStep = Step.ON_DEATH;
                setupStepUI();
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
            }
        });

        die.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedOnDeath = false;
                currentStep = Step.COMPLETE;
                setupStepUI();
            }
        });

        table.add(label).padBottom(50).row();
        table.add(reincarnate).row();
        table.add(die).row();
    }

    private void goToNextScreen() {
        game.setScreen(new TopicChoice(game, this));
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update animation time if in gun animation step
        if (currentStep == Step.SHOW_GUN_ANIMATION) {
            animationTime += delta;

            Animation<TextureRegion> currentAnim;
            switch (selectedDifficulty) {
                case 1: currentAnim = easyGunAnim; break;
                case 2: currentAnim = mediumGunAnim; break;
                case 3: currentAnim = hardGunAnim; break;
                default: currentAnim = easyGunAnim; break;
            }

            TextureRegion frame = currentAnim.getKeyFrame(animationTime, false);
            animationImage.setDrawable(new TextureRegionDrawable(frame));

            // When animation finishes, proceed to next step
            if (currentAnim.isAnimationFinished(animationTime)) {
                currentStep = Step.RANDOM_CHOICES;
                setupStepUI();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (bgTexture != null) bgTexture.dispose();
        loadBullet1.dispose();
        loadBullet2.dispose();
        loadBullet3.dispose();
        // Dispose of your textures for animations as needed
    }

    // Getters for selections if needed externally
    public int difficulty() {
        return selectedDifficulty;
    }

    public boolean randomChoices() {
        return selectedRandomChoices;
    }

    public boolean onDeath() {
        return selectedOnDeath;
    }
}
