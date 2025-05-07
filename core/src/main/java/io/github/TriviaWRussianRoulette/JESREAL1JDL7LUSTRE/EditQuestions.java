package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class EditQuestions extends BaseScreen {

    private Table mainTable;
    private Array<String> topicNames;
    private static final Json json = new Json();

    public EditQuestions(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Add title
        Label titleLabel = new Label("Edit Questions", skin);
        titleLabel.setFontScale(2.0f);

        // Create back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen(game));
            }
        });

        // Add components to main table
        mainTable.add(titleLabel).colspan(3).pad(50);
        mainTable.row();

        // Load and display topics
        loadAllTopics();
        displayTopics();

        stage.addActor(mainTable);
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

            if ((i + 1) % 3 == 0) {
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
        if (topicNames.size % 3 != 0) {
            topicsTable.row();
        }

        topicsTable.add(newTopicButton).width(300).height(50).colspan(3).pad(20);

        mainTable.add(scrollPane).expand().fill().colspan(3);
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

    private void createNewTopic() {
        // Logic to create a new topic would go here
        // For now, we'll create an empty topic with a default name
        String newTopicName = "NewTopic_" + System.currentTimeMillis() + ".json";
        TriviaTopic newTopic = new TriviaTopic(newTopicName.replace(".json", ""));

        // Add it to topicNames for the next time this screen is shown
        topicNames.add(newTopicName);

        // Update the master file
        updateMasterTopicList();

        // Go to the editor for this new topic
        game.setScreen(new QuestionEditorScreen(game, newTopic, newTopicName));
    }

    private void updateMasterTopicList() {
        FileHandle masterFile = Gdx.files.local("assets/allTopics.json");
        String[] topicArray = new String[topicNames.size];
        for (int i = 0; i < topicNames.size; i++) {
            topicArray[i] = topicNames.get(i);
        }

        String jsonString = json.toJson(topicArray);
        masterFile.writeString(jsonString, false);
    }
}
