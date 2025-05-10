package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Random;
import java.util.*;


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

    public GameScreen(Main game, TriviaTopic triviaTopic, CustomizeGameplay customizeGameplay) {
        super(game);
        this.triviaTopic = triviaTopic;
        this.customizeGameplay = customizeGameplay;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Background setup
        backgroundTexture = new Texture(Gdx.files.internal("GameScreen.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage); // add background first

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table); // then UI elements on top

        if (triviaTopic.getQuestions() == null || triviaTopic.getQuestions().size == 0) {
            Gdx.app.error("GameScreen", "No questions found for this topic!");
        } else {
            showQuestion();
        }
    }

    private void showQuestion() {
        table.clear();
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

        for (ObjectMap.Entry<String, String> entry : entries) {
            final String key = entry.key;
            final String value = entry.value;

            TextButton choiceButton = new TextButton(value, skin);
            leftCol.add(choiceButton).center().pad(15).width(500).height(90).row();

            choiceButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    boolean isCorrect = key.equals(question.getAnswer());
                    if (isCorrect) {
                        Gdx.app.log("Answer", "Correct!");
                    } else {
                        Gdx.app.log("Answer", "Wrong!");
                        if (roulette()) {
                            uponDeath();
                        }
                    }
                    nextQuestion();
                }
            });
        }

        // Create question box
        Table questionBox = new Table(skin);
        questionBox.setBackground("textfield");
        questionLabel = new Label(question.getQuestion(), skin);
        questionLabel.setFontScale(1.2f);
        questionLabel.setWrap(true);
        questionLabel.setAlignment(Align.left);
        questionBox.add(questionLabel).expandX().fillX().pad(20);

        // Build layout
        table.bottom().padBottom(10); // Align top
        table.add(leftCol).bottom().left().padLeft(100).padRight(30).padBottom(10); // Raise choices up
        table.row();

        // Add question box at bottom, centered, with margin on both sides
        table.add(questionBox)
            .expandX()
            .fillX()
            .padLeft(100)
            .padRight(100)
            .padTop(40)
            .padBottom(30)
            .height(180)
            .center();
    }



    private boolean roulette() {
        int bullets = customizeGameplay.difficulty();
        int shells = 6;

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
            return true;  // You got shot
        } else {
            System.out.println("You live");
            return false; // You survived
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
            game.setScreen(new io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.FirstScreen(game));
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
