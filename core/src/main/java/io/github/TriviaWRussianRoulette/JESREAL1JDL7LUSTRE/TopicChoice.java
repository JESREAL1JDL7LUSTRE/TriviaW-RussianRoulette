package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Button.Button;

public class TopicChoice extends BaseScreen {

    private Texture bgTexture;
    private Image backgroundImage;
    private Array<String> topicNames;
    private static final Json json = new Json();
    private final CustomizeGameplay customizeGameplay;
    private Table mainTable;

    public TopicChoice(Main game, CustomizeGameplay customizeGameplay) {
        super(game);
        this.customizeGameplay = customizeGameplay;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        burger = new Burger(skin, game);

        // Background
        bgTexture = new Texture(Gdx.files.internal("images/ForDefaultBg.png"));
        backgroundImage = new Image(bgTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Gdx.input.setInputProcessor(stage);

        // Main Table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(40).padLeft(60).padRight(60);

        // Title
        Label titleLabel = new Label("Select a Topic", skin);
        titleLabel.setFontScale(2f);
        mainTable.add(titleLabel).center().colspan(6).padBottom(20).padTop(25);
        mainTable.row();

        loadAllTopics();
        displayTopicButtons();

        stage.addActor(mainTable);
        stage.addActor(burger);
    }

    private void loadAllTopics() {
        FileHandle masterFile = Gdx.files.local("assets/allTopics.json");
        topicNames = new Array<>();

        if (masterFile.exists()) {
            String[] topicFiles = json.fromJson(String[].class, masterFile.readString());
            for (String topicFilename : topicFiles) {
                topicNames.add(topicFilename);
            }
        } else {
            Gdx.app.error("TopicLoad", "Master topic list not found: assets/allTopics.json");
            topicNames.add("test1.json");
        }
    }

    private void displayTopicButtons() {
        int columns = 5;
        int buttonWidth = 300;
        int buttonHeight = 70;

        Table gridTable = new Table();
        gridTable.defaults().space(15).pad(15);

        for (int i = 0; i < topicNames.size; i++) {
            final String topicName = topicNames.get(i);
            String displayName = topicName.replace(".json", "");

            TextButton topicButton = createStyledButton(displayName);
            topicButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    TriviaTopic triviaTopic = loadQuestionsForTopic(topicName);
                    if (triviaTopic != null) {
                        game.setScreen(new GameScreen(game, triviaTopic, customizeGameplay));
                    }
                }
            });

            gridTable.add(topicButton).width(buttonWidth).height(buttonHeight);
            if ((i + 1) % columns == 0) {
                gridTable.row();
            }
        }

        Table wrapperTable = new Table();
        wrapperTable.add(gridTable).center();
        mainTable.row().padTop(20);
        mainTable.add(wrapperTable).expandX().center();

    }

    private TextButton createStyledButton(String text) {
        TextButton button = new TextButton(text, Button.getGrayStyle());
        button.getLabel().setFontScale(1.4f);
        return button;
    }

    private TriviaTopic loadQuestionsForTopic(String topicName) {
        FileHandle topicFile = Gdx.files.local("assets/" + topicName);

        if (topicFile.exists()) {
            try {
                Question[] questionsArray = json.fromJson(Question[].class, topicFile.readString());
                if (questionsArray == null || questionsArray.length == 0) {
                    Gdx.app.error("TopicLoad", "No questions found in file: " + topicName);
                    return null;
                }

                TriviaTopic topic = new TriviaTopic(topicName.replace(".json", ""));
                for (Question q : questionsArray) {
                    topic.addQuestion(q);
                }
                return topic;

            } catch (Exception e) {
                Gdx.app.error("TopicLoad", "Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            Gdx.app.error("TopicLoad", "Topic file not found: " + topicName);
            return null;
        }
    }
}
