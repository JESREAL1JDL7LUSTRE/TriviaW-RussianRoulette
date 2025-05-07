package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class AddOwnQuestions extends BaseScreen{
    private Stage stage;
    private Skin skin;
    public Table table;
    private TextField questionField;
    private TextField choiceAField, choiceBField, choiceCField, choiceDField;
    private SelectBox<String> answerSelect;
    private TextButton nextButton, doneButton, exitButton;
    private final List<Question> questions = new ArrayList<>();


    public AddOwnQuestions(Main game) {
        super(game);
    }

    @Override
    public void show(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // UI Elements
        questionField = new TextField("", skin);
        choiceAField = new TextField("", skin);
        choiceBField = new TextField("", skin);
        choiceCField = new TextField("", skin);
        choiceDField = new TextField("", skin);

        answerSelect = new SelectBox<>(skin);
        answerSelect.setItems("a", "b", "c", "d");

        nextButton = new TextButton("Next", skin);
        doneButton = new TextButton("Done", skin);
        exitButton = new TextButton("Exit", skin);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCurrentQuestion();
                clearFields();
            }
        });

        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCurrentQuestion();  // Save final question
                promptForFilenameAndSave();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen(game));
            }
        });



        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.pad(20);
        table.add(new Label("Question:", skin)).left().row();
        table.add(questionField).width(600).row();

        table.add(new Label("Choice A:", skin)).left().row();
        table.add(choiceAField).width(600).row();
        table.add(new Label("Choice B:", skin)).left().row();
        table.add(choiceBField).width(600).row();
        table.add(new Label("Choice C:", skin)).left().row();
        table.add(choiceCField).width(600).row();
        table.add(new Label("Choice D:", skin)).left().row();
        table.add(choiceDField).width(600).row();

        table.add(new Label("Correct Answer:", skin)).left().row();
        table.add(answerSelect).width(100).row();

// Create a table specifically for the buttons
        Table buttonTable = new Table();
        buttonTable.padTop(20);

// Add buttons with equal spacing
        buttonTable.add(nextButton).padRight(30);
        buttonTable.add(doneButton);
        buttonTable.add(exitButton).padLeft(30);

// Add the button table to the main table
        table.add(buttonTable).colspan(2).center().row();

        stage.addActor(table);
    }

    private void addCurrentQuestion() {
        ObjectMap<String, String> choicesMap = new ObjectMap<>();
        choicesMap.put("a", choiceAField.getText());
        choicesMap.put("b", choiceBField.getText());
        choicesMap.put("c", choiceCField.getText());
        choicesMap.put("d", choiceDField.getText());

        Question question = new Question(
            questionField.getText(),
            choicesMap,
            answerSelect.getSelected()
        );
        questions.add(question);
    }

    private void clearFields() {
        questionField.setText("");
        choiceAField.setText("");
        choiceBField.setText("");
        choiceCField.setText("");
        choiceDField.setText("");
        answerSelect.setSelected("a");
    }

    private void promptForFilenameAndSave() {
        final Window dialog = new Window("Save As", skin);
        final TextField filenameField = new TextField("questions", skin);
        TextButton saveButton = new TextButton("Save", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        dialog.add(new Label("Enter filename:", skin)).pad(10).row();
        dialog.add(filenameField).width(300).padBottom(10).row();
        dialog.add(saveButton).padRight(10);
        dialog.add(cancelButton).row();

        dialog.pack();
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.center();
        stage.addActor(dialog);

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String filename = filenameField.getText().trim();
                if (!filename.isEmpty()) {
                    saveQuestionsToFile(filename);
                    dialog.remove();
                }
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
                Gdx.app.log("Save", "User canceled save.");
            }
        });
    }

    private void saveQuestionsToFile(String filename) {
        // 1. Save questions with the correct format
        FileHandle file = Gdx.files.local("assets/" + filename + ".json");

        // Use direct JSON generation to ensure proper format for questions
        StringBuilder jsonBuilder = new StringBuilder("[\n");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"question\": \"").append(escapeJson(q.getQuestion())).append("\",\n");
            jsonBuilder.append("    \"choices\": {\n");

            ObjectMap<String, String> choices = q.getChoices();
            jsonBuilder.append("      \"a\": \"").append(escapeJson(choices.get("a"))).append("\",\n");
            jsonBuilder.append("      \"b\": \"").append(escapeJson(choices.get("b"))).append("\",\n");
            jsonBuilder.append("      \"c\": \"").append(escapeJson(choices.get("c"))).append("\",\n");
            jsonBuilder.append("      \"d\": \"").append(escapeJson(choices.get("d"))).append("\"\n");
            jsonBuilder.append("    },\n");
            jsonBuilder.append("    \"answer\": \"").append(q.getAnswer()).append("\"\n");
            jsonBuilder.append("  }");

            if (i < questions.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }
        jsonBuilder.append("]\n");

        file.writeString(jsonBuilder.toString(), false);
        Gdx.app.log("Save", "Questions saved to " + filename + ".json");

        // 2. Update topics - Using a direct approach for allTopics.json
        FileHandle allTopicsFile = Gdx.files.local("assets/allTopics.json");
        Array<String> topics = new Array<>();

        try {
            // Read existing topics file if it exists
            if (allTopicsFile.exists()) {
                String content = allTopicsFile.readString();
                if (content != null && !content.trim().isEmpty()) {
                    // Parse manually to avoid serialization issues
                    content = content.trim();
                    if (content.startsWith("[") && content.endsWith("]")) {
                        content = content.substring(1, content.length() - 1).trim();
                        // Split by commas and clean up the strings
                        if (!content.isEmpty()) {
                            String[] items = content.split(",");
                            for (String item : items) {
                                item = item.trim();
                                // Remove quotes
                                if (item.startsWith("\"") && item.endsWith("\"")) {
                                    item = item.substring(1, item.length() - 1);
                                    topics.add(item);
                                }
                            }
                        }
                    }
                }
            }

            // Check if this topic already exists
            String newFilename = filename + ".json";
            boolean found = false;
            for (String topic : topics) {
                if (topic.equals(newFilename)) {
                    found = true;
                    break;
                }
            }

            // Add if not found
            if (!found) {
                topics.add(newFilename);
            }

            // Create a simple JSON array string manually for allTopics.json
            StringBuilder topicsJsonBuilder = new StringBuilder("[\n");
            for (int i = 0; i < topics.size; i++) {
                topicsJsonBuilder.append("  \"").append(topics.get(i)).append("\"");
                if (i < topics.size - 1) {
                    topicsJsonBuilder.append(",");
                }
                topicsJsonBuilder.append("\n");
            }
            topicsJsonBuilder.append("]\n");

            // Write the formatted JSON string
            allTopicsFile.writeString(topicsJsonBuilder.toString(), false);
            Gdx.app.log("Save", "Updated allTopics.json with " + newFilename);

        } catch (Exception e) {
            Gdx.app.error("Save", "Failed to update allTopics.json: " + e.getMessage());
            e.printStackTrace();

            // Fallback - create a basic file with just this topic
            String basicJson = "[\n  \"" + filename + ".json\"\n]";
            allTopicsFile.writeString(basicJson, false);
        }
    }

    // Helper method to escape JSON strings
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); }

}
