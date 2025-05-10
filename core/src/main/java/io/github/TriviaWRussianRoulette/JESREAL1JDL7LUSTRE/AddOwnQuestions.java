package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class AddOwnQuestions extends BaseScreen{
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Table questionsTable;
    private ScrollPane scrollPane;
    private TextField questionField;
    private TextField choiceAField, choiceBField, choiceCField, choiceDField;
    private SelectBox<String> answerSelect;
    private TextButton nextButton, doneButton, exitButton, deleteButton;
    private final List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = -1; // -1 means no question is selected

    public AddOwnQuestions(Main game) {
        super(game);
    }

    @Override
    public void show(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        setupUI();
    }

    private void setupUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Create title and controls
        Label titleLabel = new Label("Add Your Own Questions", skin);
        titleLabel.setFontScale(1.5f);

        exitButton = new TextButton("Back", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen(game));
            }
        });

        doneButton = new TextButton("Save All Questions", skin);
        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveCurrentQuestion();  // Save final question
                if (questions.size() > 0) {
                    promptForFilenameAndSave();
                } else {
                    Dialog errorDialog = new Dialog("Error", skin);
                    errorDialog.text("Please add at least one question before saving.");
                    errorDialog.button("OK");
                    errorDialog.show(stage);
                }
            }
        });

        // Add top controls
        Table topControls = new Table();
        topControls.add(exitButton).left().padRight(20);
        topControls.add(titleLabel).expandX();
        topControls.add(doneButton).right().padLeft(20);

        mainTable.add(topControls).fillX().pad(60);
        mainTable.row();

        // Create split layout - with questions list on left
        Table splitLayout = new Table();

        // Questions list on the left
        questionsTable = new Table();
        questionsTable.top().left();
        scrollPane = new ScrollPane(questionsTable, skin);
        scrollPane.setFadeScrollBars(false);

        // Add new question button at the top of the left panel
        TextButton addNewButton = new TextButton("+ Add New Question", skin);
        addNewButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Save the current question if there is one
                saveCurrentQuestion();
                addNewQuestion();
            }
        });

        Table leftPanel = new Table();
        leftPanel.top();
        leftPanel.add(addNewButton).fillX().pad(5);
        leftPanel.row();
        leftPanel.add(scrollPane).expand().fill();

        // Question editor on the right
        Table editorPanel = createEditorPanel();

        // Add both panels to the split layout
        splitLayout.add(leftPanel).width(300).expand().fill().pad(5);
        splitLayout.add(editorPanel).expand().fill().pad(5);

        mainTable.add(splitLayout).expand().fill();

        // Initialize the UI with a blank question
        addNewQuestion();

        stage.addActor(mainTable);
    }

    private Table createEditorPanel() {
        Table editorPanel = new Table();
        editorPanel.top();

        // Create question editing fields
        Label questionLabel = new Label("Question:", skin);
        questionField = new TextField("", skin);
        questionField.setMessageText("Enter question text here");

        Label optionsLabel = new Label("Options:", skin);

        Label optionALabel = new Label("A:", skin);
        choiceAField = new TextField("", skin);

        Label optionBLabel = new Label("B:", skin);
        choiceBField = new TextField("", skin);

        Label optionCLabel = new Label("C:", skin);
        choiceCField = new TextField("", skin);

        Label optionDLabel = new Label("D:", skin);
        choiceDField = new TextField("", skin);

        Label correctAnswerLabel = new Label("Correct Answer:", skin);
        answerSelect = new SelectBox<>(skin);
        answerSelect.setItems("a", "b", "c", "d");

        // Action buttons
        nextButton = new TextButton("Save & Add New", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveCurrentQuestion();
                addNewQuestion();
            }
        });

        deleteButton = new TextButton("Delete Question", skin);
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deleteCurrentQuestion();
            }
        });

        // Add components to the editor panel
        editorPanel.add(questionLabel).left().padTop(10);
        editorPanel.row();
        editorPanel.add(questionField).fillX().height(100).pad(5);
        editorPanel.row();

        editorPanel.add(optionsLabel).left().padTop(10);
        editorPanel.row();

        Table optionsTable = new Table();
        optionsTable.add(optionALabel).width(30);
        optionsTable.add(choiceAField).expandX().fillX().pad(5);
        optionsTable.row();
        optionsTable.add(optionBLabel).width(30);
        optionsTable.add(choiceBField).expandX().fillX().pad(5);
        optionsTable.row();
        optionsTable.add(optionCLabel).width(30);
        optionsTable.add(choiceCField).expandX().fillX().pad(5);
        optionsTable.row();
        optionsTable.add(optionDLabel).width(30);
        optionsTable.add(choiceDField).expandX().fillX().pad(5);

        editorPanel.add(optionsTable).fillX();
        editorPanel.row();

        Table answerTable = new Table();
        answerTable.add(correctAnswerLabel).left().padRight(10);
        answerTable.add(answerSelect).width(100);

        editorPanel.add(answerTable).left().padTop(10);
        editorPanel.row();

        // Add action buttons
        Table actionButtons = new Table();
        actionButtons.add(deleteButton).padRight(10);
        actionButtons.add(nextButton);

        editorPanel.add(actionButtons).right().padTop(20);

        // Initially disable the editor until a question is selected
        setEditorEnabled(false);

        return editorPanel;
    }

    private void populateQuestionsList() {
        questionsTable.clear();

        for (int i = 0; i < questions.size(); i++) {
            final int questionIndex = i;
            Question question = questions.get(i);

            // Create a button with the question text (truncated if needed)
            String displayText = question.getQuestion();
            if (displayText == null || displayText.isEmpty()) {
                displayText = "New Question";
            } else if (displayText.length() > 30) {
                displayText = displayText.substring(0, 27) + "...";
            }

            TextButton questionButton = new TextButton((i + 1) + ". " + displayText, skin);

            // Highlight the currently selected question
            if (questionIndex == currentQuestionIndex) {
                questionButton.setColor(Color.CYAN);
            }

            questionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectQuestion(questionIndex);
                }
            });

            questionsTable.add(questionButton).fillX().pad(3);
            questionsTable.row();
        }
    }

    private void selectQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            // Save the current question if one was being edited
            if (currentQuestionIndex != -1) {
                saveCurrentQuestion();
            }

            currentQuestionIndex = index;
            Question question = questions.get(index);

            // Fill the editor fields with the question data
            questionField.setText(question.getQuestion());

            ObjectMap<String, String> choices = question.getChoices();
            choiceAField.setText(choices.get("a", ""));
            choiceBField.setText(choices.get("b", ""));
            choiceCField.setText(choices.get("c", ""));
            choiceDField.setText(choices.get("d", ""));

            answerSelect.setSelected(question.getAnswer());

            // Enable the editor
            setEditorEnabled(true);

            // Update the questions list to highlight the selected question
            populateQuestionsList();
        }
    }

    private void setEditorEnabled(boolean enabled) {
        questionField.setDisabled(!enabled);
        choiceAField.setDisabled(!enabled);
        choiceBField.setDisabled(!enabled);
        choiceCField.setDisabled(!enabled);
        choiceDField.setDisabled(!enabled);
        answerSelect.setDisabled(!enabled);
        deleteButton.setDisabled(!enabled);
    }

    private void saveCurrentQuestion() {
        if (currentQuestionIndex != -1 && currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);

            // Update the question with the editor values
            question.setQuestion(questionField.getText());

            ObjectMap<String, String> choices = new ObjectMap<>();
            choices.put("a", choiceAField.getText());
            choices.put("b", choiceBField.getText());
            choices.put("c", choiceCField.getText());
            choices.put("d", choiceDField.getText());

            question.setChoices(choices);
            question.setAnswer(answerSelect.getSelected());

            // Update the questions list to show any changes
            populateQuestionsList();
        }
    }

    private void addNewQuestion() {
        // Create a new empty question
        Question newQuestion = new Question();
        newQuestion.setQuestion("New Question");

        ObjectMap<String, String> choices = new ObjectMap<>();
        choices.put("a", "Option A");
        choices.put("b", "Option B");
        choices.put("c", "Option C");
        choices.put("d", "Option D");
        newQuestion.setChoices(choices);

        newQuestion.setAnswer("a");

        // Add it to the questions array
        questions.add(newQuestion);

        // Select the new question for editing
        currentQuestionIndex = questions.size() - 1;
        clearFields();
        // Update the UI
        populateQuestionsList();
        selectQuestion(currentQuestionIndex);
        setEditorEnabled(true);

        // Scroll to the bottom to show the new question
        scrollPane.setScrollY(questionsTable.getHeight());
    }

    private void deleteCurrentQuestion() {
        if (currentQuestionIndex != -1 && !questions.isEmpty()) {
            questions.remove(currentQuestionIndex);

            // Reset selection if we deleted the last question
            if (questions.isEmpty()) {
                currentQuestionIndex = -1;
                setEditorEnabled(false);
                clearFields();
                addNewQuestion(); // Always have at least one question
            } else if (currentQuestionIndex >= questions.size()) {
                currentQuestionIndex = questions.size() - 1;
                selectQuestion(currentQuestionIndex);
            } else {
                selectQuestion(currentQuestionIndex);
            }

            // Update the questions list
            populateQuestionsList();
        }
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

        Table dialogButtons = new Table();
        dialogButtons.add(saveButton).padRight(10);
        dialogButtons.add(cancelButton);
        dialog.add(dialogButtons).padTop(10);

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

                    // Show success dialog
                    Dialog successDialog = new Dialog("Success", skin);
                    successDialog.text("Questions saved successfully!");
                    successDialog.button("OK");
                    successDialog.show(stage);
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
