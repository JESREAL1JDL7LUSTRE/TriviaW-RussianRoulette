package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Random;
import java.util.*;
import com.badlogic.gdx.utils.Timer;



public class GameScreen extends BaseScreen {
    private final CustomizeGameplay customizeGameplay;
    private TriviaTopic triviaTopic;
    private Stage stage;
    private Skin skin;
    private int currentQuestionIndex = 0;
    private Label questionLabel;
    private Table table;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private static final float DEAD_SOUND_DURATION = 3.2f; // seconds
    private static final float SAFE_SOUND_DURATION = 4.2f; // seconds
    private boolean isWaitingForSound = false; // Track if we're waiting for the sound to finish
    private Array<TextButton> choiceButtons = new Array<>(); // Store references to choice buttons


    public GameScreen(Main game, TriviaTopic triviaTopic, CustomizeGameplay customizeGameplay) {
        super(game);
        this.triviaTopic = triviaTopic;
        this.customizeGameplay = customizeGameplay;
    }
    @Override
    public void show() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        burger = new Burger(skin, game);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        // Background setup
        backgroundTexture = new Texture(Gdx.files.internal("GameScreen.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage); // add background first

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table); // then UI elements on top

        // Initialize waiting state to false
        isWaitingForSound = false;

        if (triviaTopic.getQuestions() == null || triviaTopic.getQuestions().size == 0) {
            Gdx.app.error("GameScreen", "No questions found for this topic!");
        } else {
            showQuestion();
            stage.addActor(burger);
        }
    }

    private void showQuestion() {
        table.clear();
        choiceButtons.clear(); // Clear previous button references

        boolean shuffleChoices = customizeGameplay.randomChoices();
        Question question = triviaTopic.getQuestions().get(currentQuestionIndex);

        Table leftCol = new Table();

        ObjectMap<String, String> choices = question.getChoices();
        Array<ObjectMap.Entry<String, String>> entries = new Array<>();

        for (ObjectMap.Entry<String, String> entry : choices.entries()) {
            ObjectMap.Entry<String, String> newEntry = new ObjectMap.Entry<>();
            newEntry.key = entry.key;
            newEntry.value = entry.value;
            entries.add(newEntry);
        }

        if (shuffleChoices) {
            entries.shuffle();
        } else {
            entries.sort((a, b) -> a.key.compareTo(b.key));
        }

        TextButton.TextButtonStyle[] buttonStyles = new TextButton.TextButtonStyle[4];

        for (int i = 0; i < 4; i++) {
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            Texture buttonTexture = new Texture(Gdx.files.internal("buttons/button" + (i + 1) + ".png"));
            style.up = new Image(buttonTexture).getDrawable();
            style.font = new BitmapFont(); // Replace with your own font if needed
            buttonStyles[i] = style;
        }

        int index = 0;
        for (ObjectMap.Entry<String, String> entry : entries) {
            final String key = entry.key;
            final String value = entry.value;

            TextButton.TextButtonStyle currentStyle = index < buttonStyles.length
                ? buttonStyles[index]
                : buttonStyles[0];

            TextButton choiceButton = new TextButton(value, currentStyle);
            choiceButton.setDisabled(false);
            choiceButton.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
            leftCol.add(choiceButton).center().pad(15).width(500).height(90).row();
            choiceButtons.add(choiceButton); // Add to our array of buttons

            choiceButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!isWaitingForSound) { // Only process clicks if not waiting
                        isWaitingForSound = true; // Set waiting flag
                        hideAllButtonsExcept(choiceButton); // Hide all buttons except the selected one

                        boolean isCorrect = key.equals(question.getAnswer());
                        if (isCorrect) {
                            Gdx.app.log("Answer", "Correct!");
                            nextQuestion();
                        } else {
                            Gdx.app.log("Answer", "Wrong!");
                            roulette();
                        }
                    }
                }
            });

            index++;
        }

        // Load texture and create Drawable
        Texture textfieldTexture = new Texture(Gdx.files.internal("buttons/textfield.png"));
        Drawable textfieldDrawable = new Image(textfieldTexture).getDrawable();

        // Create font and label style
        BitmapFont font = new BitmapFont(); // You can replace this with a custom font if desired
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Create label
        questionLabel = new Label(question.getQuestion(), labelStyle);
        questionLabel.setFontScale(2.5f);
        questionLabel.setWrap(true);
        questionLabel.setAlignment(Align.left);

        // Create question box without skin
        Table questionBox = new Table();
        questionBox.setBackground(textfieldDrawable);
        questionBox.add(questionLabel).expandX().fillX().pad(30);


        // Build layout
        table.bottom().padBottom(10); // Align top
        table.add(leftCol).bottom().left().padLeft(140).padRight(30).padBottom(10); // Raise choices up
        table.row();

        // Add question box at bottom, centered, with margin on both sides
        table.add(questionBox)
            .expandX()
            .fillX()
            .padLeft(100)
            .padRight(100)
            .padTop(40)
            .padBottom(30)
            .height(190)
            .center();

        // Make sure buttons are visible and enabled when showing a new question
        isWaitingForSound = false;
        showAllButtons();
    }

    // Helper method to hide all buttons except the selected one
    private void hideAllButtonsExcept(TextButton selectedButton) {
        for (TextButton button : choiceButtons) {
            if (button != selectedButton) {
                button.setVisible(false);
                button.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
            } else {
                // Keep the selected button visible but disabled
                button.setDisabled(true);
                button.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
                // Optional: Highlight the selected button
                button.setColor(0.8f, 0.8f, 1f, 1f);
            }
        }
        Gdx.app.log("GameScreen", "Hiding all buttons except selected");
    }

    // Helper method to show and enable all choice buttons
    private void showAllButtons() {
        for (TextButton button : choiceButtons) {
            button.setVisible(true);
            button.setDisabled(false);
            button.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
            button.setColor(1f, 1f, 1f, 1f); // Reset color
        }
        Gdx.app.log("GameScreen", "All buttons shown and enabled");
    }

    private boolean roulette() {
        int bullets = customizeGameplay.difficulty();
        int shells = 6;

        Sound Safe = Gdx.audio.newSound(Gdx.files.internal("SpinCockShootSafe.mp3"));
        Sound Dead = Gdx.audio.newSound(Gdx.files.internal("SpinCockShootDead.mp3"));

        List<String> values = new ArrayList<>();

        // Add bullets and empty chambers
        for (int i = 0; i < bullets; i++) values.add("1");
        for (int i = 0; i < shells - bullets; i++) values.add("0");

        // Shuffle to randomize order
        Collections.shuffle(values);

        // Pick one at random
        Random rand = new Random();
        String result = values.get(rand.nextInt(shells));

        System.out.println("Random value: " + result);

        if (result.equals("1")) {
            System.out.println("You die");
            Dead.play();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    uponDeath(); // Delay logic until sound is done
                }
            }, DEAD_SOUND_DURATION);
            return true;
        } else {
            System.out.println("You live");
            Safe.play();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    nextQuestion(); // Continue after sound
                }
            }, SAFE_SOUND_DURATION);
            return false;
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < triviaTopic.getQuestions().size) {
            showQuestion(); // Show next question
        } else {
            Gdx.app.log("Game", "All questions answered!");
            // TODO: Navigate to results screen or summary
            game.setScreen(new io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.YouWin(game));
        }
    }

    private void uponDeath() {
        boolean whatHappen = customizeGameplay.onDeath();
        if (whatHappen){
            game.setScreen(new io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Lose(game));
        } else {
            Gdx.app.exit();
        }
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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        // Dispose any game-specific textures or sounds here
    }
}
