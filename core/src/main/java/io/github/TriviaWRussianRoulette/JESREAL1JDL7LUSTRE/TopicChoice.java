package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;

public class TopicChoice extends BaseScreen {
    private final CustomizeGameplay customizeGameplay;
    private static final Json json = new Json();
    private Array<String> topicNames; // Store the topic names (or filenames)

    public TopicChoice(Main game, CustomizeGameplay customizeGameplay) {
        super(game);
        this.customizeGameplay = customizeGameplay;
    }

    private void loadAllTopics() {
        FileHandle masterFile = Gdx.files.local("assets/allTopics.json");

        topicNames = new Array<>(); // Only load topic names, not questions yet

        if (masterFile.exists()) {
            String[] topicFiles = json.fromJson(String[].class, masterFile.readString());

            for (String topicFilename : topicFiles) {
                topicNames.add(topicFilename); // Just store filenames of topics
            }
        } else {
            Gdx.app.error("TopicLoad", "Master topic list not found: " + "assets/allTopics.json");
            // Create a default topic if the file doesn't exist
            topicNames.add("test1.json");
        }
    }

    // This method loads the questions for the selected topic
    private TriviaTopic loadQuestionsForTopic(String topicName) {
        FileHandle topicFile = Gdx.files.local("assets/" + topicName);

        if (topicFile.exists()) {
            try {
                // Create a new TriviaTopic to hold the questions
                TriviaTopic triviaTopic = new TriviaTopic(topicName.replace(".json", ""));

                // Parse the JSON array directly into an array of Question objects
                Question[] questionsArray = json.fromJson(Question[].class, topicFile.readString());

                // Check if questions were loaded successfully
                if (questionsArray == null || questionsArray.length == 0) {
                    Gdx.app.error("TopicLoad", "No questions found in file: " + topicName);
                    return createDefaultTopic(topicName);
                }

                // Add all questions to the topic
                for (Question question : questionsArray) {
                    triviaTopic.addQuestion(question);
                }

                Gdx.app.log("TopicLoaded", "Loaded topic: " + topicName + " with " + questionsArray.length + " questions");
                return triviaTopic;

            } catch (Exception e) {
                Gdx.app.error("TopicLoad", "Error parsing JSON: " + e.getMessage());
                e.printStackTrace();
                return createDefaultTopic(topicName);
            }
        } else {
            Gdx.app.error("TopicLoad", "Topic file not found: " + topicName);
            return createDefaultTopic(topicName);
        }
    }

    // Create a default topic with sample questions if file loading fails
    private TriviaTopic createDefaultTopic(String topicName) {
        Gdx.app.log("TopicLoad", "Creating default topic for: " + topicName);
        TriviaTopic defaultTopic = new TriviaTopic(topicName.replace(".json", ""));

        // Create a sample question
        Question sampleQuestion = new Question();
        sampleQuestion.setQuestion("Sample question for " + topicName + "?");

        // Add some choices
        sampleQuestion.getChoices().put("a", "Option A");
        sampleQuestion.getChoices().put("b", "Option B");
        sampleQuestion.getChoices().put("c", "Option C");
        sampleQuestion.getChoices().put("d", "Option D");

        sampleQuestion.setAnswer("a");

        // Add the question to the topic
        defaultTopic.addQuestion(sampleQuestion);

        return defaultTopic;
    }

    @Override
    public void show() {
        super.show();
        loadAllTopics();

        Table table = new Table();
        table.top().left();
        table.setFillParent(false); // Adjust position and size

        table.setPosition(0, 750); // Manually move table down
        table.pad(10);
        table.defaults().space(10); // Space between the topics

        // Loop through topic names and create clickable labels for each
        for (int i = 0; i < topicNames.size; i++) {
            final String topicName = topicNames.get(i);
            // Display topic name without .json extension
            String displayName = topicName.replace(".json", "");
            Label topicLabel = new Label(displayName, skin);
            topicLabel.setFontScale(1.5f);

            topicLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        // Load questions for the selected topic
                        TriviaTopic triviaTopic = loadQuestionsForTopic(topicName);

                        if (triviaTopic != null) {
                            Gdx.app.log("Topic Clicked", "Topic selected: " + triviaTopic.getTopic());
                            game.setScreen(new GameScreen(game, triviaTopic, customizeGameplay));
                        } else {
                            Gdx.app.error("Topic Clicked", "Failed to load topic: " + topicName);
                        }
                    } catch (Exception e) {
                        Gdx.app.error("Topic Clicked", "Error loading topic: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            table.add(topicLabel).width(300).height(50);

            if ((i + 1) % 6 == 0) {
                table.row(); // Move to the next row in the table
            }
        }

        stage.addActor(table);
    }
}
