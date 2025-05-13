package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;

public class TopicChoice extends BaseScreen {
    private final CustomizeGameplay customizeGameplay;
    private static final Json json = new Json();
    private Array<String> topicNames; // Store the topic names (or filenames)
    public Table table;

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
                    return IfNoTopic();
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
                return IfNoTopic();
            }
        } else {
            Gdx.app.error("TopicLoad", "Topic file not found: " + topicName);
            return IfNoTopic();
        }
    }

    public TriviaTopic IfNoTopic() {
        TextButton addOwnQuestionsBtn = new TextButton("Add Topic/Questions", game.uiSkin);

        addOwnQuestionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AddOwnQuestions(game));
            }
        });

        table.add(addOwnQuestionsBtn).width(200).height(50).pad(10).padLeft(900);
        table.row();

        return null;
    }


    @Override
    public void show() {
        super.show();
        loadAllTopics();

        Table table = new Table();
        table.top().left();
        table.setFillParent(false); // Adjust position and size

        table.setPosition(10, 750); // Manually move table down
        table.pad(10);
        table.defaults().space(15); // Space between the topics

        // Loop through topic names and create clickable labels for each
        for (int i = 0; i < topicNames.size; i++) {
            final String topicName = topicNames.get(i);
            // Display topic name without .json extension
            String displayName = topicName.replace(".json", "");
            TextButton  topicLabel = createStyledButton(displayName);

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

            table.add(topicLabel).width(300).height(70);

            if ((i + 1) % 6 == 0) {
                table.row(); // Move to the next row in the table
            }
        }

        stage.addActor(table);
    }

    private TextButton createStyledButton(String text) {
        Drawable grayUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        Drawable grayDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));

        TextButton.TextButtonStyle grayStyle = new TextButton.TextButtonStyle();
        grayStyle.up = grayUp;
        grayStyle.down = grayDown;
        grayStyle.font = new BitmapFont();

        // Create the button with the gray style
        TextButton button = new TextButton(text, grayStyle);
        button.getLabel().setFontScale(1.5f);
        return button;
    }
}
