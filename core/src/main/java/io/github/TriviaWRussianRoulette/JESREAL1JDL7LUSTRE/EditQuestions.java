package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EditQuestions extends BaseScreen {

    private Table mainTable;
    private Array<String> topicNames;
    private static final Json json = new Json();
    private Texture backgroundTexture;
    private Image backgroundImage;

    public EditQuestions(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        burger = new Burger(skin, game);

        backgroundTexture = new Texture(Gdx.files.internal("Gameplay.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Gdx.input.setInputProcessor(stage);

        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(40).padLeft(60).padRight(60);

        // Title
        Label titleLabel = new Label("Add New Topic or Select to Edit Questions", skin);
        titleLabel.setFontScale(2f);
        mainTable.add(titleLabel).center().colspan(6).padBottom(20).padTop(25);
        mainTable.row();

        loadAllTopics();
        displayTopics();

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
        }
    }

    private void displayTopics() {
        int columns = 5;
        int buttonWidth = 300;
        int buttonHeight = 70;

        Table gridTable = new Table();
        gridTable.defaults().space(15).pad(15);

        // Add "Create New Topic" button FIRST (above the grid)
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/blue_button.png"))));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/blue_button_pressed.png"))));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;
        style.font = new BitmapFont();

        TextButton newTopicButton = new TextButton("+ Create New Topic", style);
        newTopicButton.getLabel().setFontScale(1.4f);
        newTopicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AddOwnQuestions(game));
            }
        });

        gridTable.add(newTopicButton).width(buttonWidth).height(buttonHeight).colspan(columns).padBottom(20).center();
        gridTable.row();


        for (int i = 0; i < topicNames.size; i++) {
            final String topicName = topicNames.get(i);
            String displayName = topicName.replace(".json", "");

            Drawable grayUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
            Drawable grayDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));
            TextButton.TextButtonStyle grayStyle = new TextButton.TextButtonStyle();
            grayStyle.up = grayUp;
            grayStyle.down = grayDown;
            grayStyle.font = new BitmapFont();

            TextButton topicButton = new TextButton(displayName, grayStyle);
            topicButton.getLabel().setFontScale(1.4f);
            topicButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    loadQuestionsForEditing(topicName);
                }
            });

            gridTable.add(topicButton).width(buttonWidth).height(buttonHeight);

            if ((i + 1) % columns == 0) {
                gridTable.row();
            }
        }

        mainTable.add(gridTable).colspan(6).center();
    }

    private void loadQuestionsForEditing(String topicName) {
        FileHandle topicFile = Gdx.files.local("assets/" + topicName);

        if (topicFile.exists()) {
            try {
                Question[] questionsArray = json.fromJson(Question[].class, topicFile.readString());

                if (questionsArray == null || questionsArray.length == 0) {
                    Gdx.app.error("QuestionLoad", "No questions found in file: " + topicName);
                    questionsArray = new Question[0];
                }

                TriviaTopic topic = new TriviaTopic(topicName.replace(".json", ""));
                for (Question question : questionsArray) {
                    topic.addQuestion(question);
                }

                game.setScreen(new QuestionEditorScreen(game, topic, topicName));

            } catch (Exception e) {
                Gdx.app.error("QuestionLoad", "Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Gdx.app.error("QuestionLoad", "Topic file not found: " + topicName);
            TriviaTopic topic = new TriviaTopic(topicName.replace(".json", ""));
            game.setScreen(new QuestionEditorScreen(game, topic, topicName));
        }
    }
}
