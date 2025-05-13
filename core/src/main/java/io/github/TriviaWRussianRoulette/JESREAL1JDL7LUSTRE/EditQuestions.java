package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EditQuestions extends BaseScreen {

    private Table mainTable;
    private Array<String> topicNames;
    private static final Json json = new Json();

    public EditQuestions(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        burger = new Burger(skin, game);
        // Now add burger on top of everything else

        Gdx.input.setInputProcessor(stage);


        mainTable = new Table();
        mainTable.setFillParent(true);

        // Add title
        Label titleLabel = new Label("Edit Questions", skin);
        titleLabel.setFontScale(2.0f);

        // Add components to main table
        mainTable.add(titleLabel).colspan(6).pad(50);
        mainTable.row();

        // Load and display topics
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
        Table topicsTable = new Table();
        topicsTable.top().left();
        topicsTable.defaults().space(10);

        // Create scrollable table for topics
        ScrollPane scrollPane = new ScrollPane(topicsTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        // Loop through topic names and create clickable labels for each
        for (int i = 0; i < topicNames.size; i++) {
            final String topicName = topicNames.get(i);
            String displayName = topicName.replace(".json", "");

            TextButton topicButton = new TextButton(displayName, skin);
            topicButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    loadQuestionsForEditing(topicName);
                }
            });

            topicsTable.add(topicButton).width(300).height(50);

            if ((i + 1) % 6 == 0) {
                topicsTable.row();
            }
        }

        // Add a button to create a new topic
        TextButton newTopicButton = new TextButton("+ Create New Topic", skin);
        newTopicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AddOwnQuestions(game));
            }
        });

        // Add an extra row if needed
        if (topicNames.size % 6 != 0) {
            topicsTable.row();
        }

        topicsTable.add(newTopicButton).width(300).height(50).colspan(6).pad(20);

        mainTable.add(scrollPane).expand().fill().colspan(6);
    }

    private void loadQuestionsForEditing(String topicName) {
        FileHandle topicFile = Gdx.files.local("assets/" + topicName);

        if (topicFile.exists()) {
            try {
                // Parse the JSON array directly into an array of Question objects
                Question[] questionsArray = json.fromJson(Question[].class, topicFile.readString());

                if (questionsArray == null || questionsArray.length == 0) {
                    Gdx.app.error("QuestionLoad", "No questions found in file: " + topicName);
                    questionsArray = new Question[0];
                }

                // Create a new topic to hold these questions
                TriviaTopic topic = new TriviaTopic(topicName.replace(".json", ""));
                for (Question question : questionsArray) {
                    topic.addQuestion(question);
                }

                // Display question editor screen
                game.setScreen(new QuestionEditorScreen(game, topic, topicName));

            } catch (Exception e) {
                Gdx.app.error("QuestionLoad", "Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Gdx.app.error("QuestionLoad", "Topic file not found: " + topicName);
            // Create a new empty topic
            TriviaTopic topic = new TriviaTopic(topicName.replace(".json", ""));
            game.setScreen(new QuestionEditorScreen(game, topic, topicName));
        }
    }

}
