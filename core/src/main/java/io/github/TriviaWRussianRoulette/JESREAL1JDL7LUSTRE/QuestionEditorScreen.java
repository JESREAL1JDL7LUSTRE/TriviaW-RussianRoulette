package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Button.Button;

public class QuestionEditorScreen extends BaseScreen {

    private TriviaTopic topic;
    private String topicFileName;
    private Array<Question> questions;
    private Table mainTable;
    private Table questionsTable;
    private ScrollPane scrollPane;
    private int currentQuestionIndex = -1;
    private static final Json json = new Json();
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Texture frameTexture;

    // Editing components
    private TextField questionField;
    private TextField optionAField;
    private TextField optionBField;
    private TextField optionCField;
    private TextField optionDField;
    private SelectBox<String> correctAnswerSelect;
    private TextButton saveButton, backButton, deleteButton;

    public QuestionEditorScreen(Main game, TriviaTopic topic, String topicFileName) {
        super(game);
        this.topic = topic;
        this.topicFileName = topicFileName;
        this.questions = new Array<>();

        // Convert the questions from the topic to an Array for easier manipulation
        for (int i = 0; i < topic.getQuestionCount(); i++) {
            questions.add(topic.getQuestion(i));
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        // Load background textures
        backgroundTexture = new Texture(Gdx.files.internal("images/Gameplay.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        frameTexture = new Texture(Gdx.files.internal("images/frameForWords.png"));

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Image frameImage = new Image(frameTexture);
        Container<Image> frameContainer = new Container<>(frameImage);
        frameContainer.fill().pad(50);
        mainTable.add(frameContainer).expand().fill();

        setupUI();
    }

    private void setupUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Create title with background image
        Texture labelBgTexture = new Texture(Gdx.files.internal("images/forLabels.png"));
        Image labelBg = new Image(labelBgTexture);
        labelBg.setSize(400, 100);

        Label titleLabel = new Label("Edit Questions: " + topic.getTopic(), skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setAlignment(Align.center);

        Stack titleStack = new Stack();
        titleStack.setSize(400, 100);
        titleStack.add(labelBg);
        titleStack.add(titleLabel);

        mainTable.add(titleStack).padTop(30).center();
        mainTable.row();

        // Create split layout - questions list on left, editor on right
        Table splitLayout = new Table();

        // Questions list on the left
        questionsTable = new Table();
        questionsTable.top().left();
        scrollPane = new ScrollPane(questionsTable, skin);
        scrollPane.setFadeScrollBars(false);

        // Add new question button at the top
        TextButton addNewButton = new TextButton("+ Add New Question", skin);
        addNewButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
        splitLayout.add(editorPanel).width(500).expand().fill().padLeft(200).padTop(90);
        splitLayout.add(leftPanel).width(600).expand().fill().padRight(300).padBottom(50);

        mainTable.add(splitLayout).expand().fill();
        mainTable.row();

        // Bottom buttons
        backButton = new TextButton("Back", Button.getGrayStyle());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EditQuestions(game));
            }
        });

        saveButton = new TextButton("Save All Changes", Button.getBlueStyle());
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveAllChanges();
            }
        });

        Table bottomButtons = new Table();
        bottomButtons.add(backButton).width(250).height(70).padRight(20);
        bottomButtons.add(saveButton).width(250).height(70);
        mainTable.add(bottomButtons).padBottom(20).center().bottom();

        // Populate the questions list
        populateQuestionsList();

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
        optionAField = new TextField("", skin);

        Label optionBLabel = new Label("B:", skin);
        optionBField = new TextField("", skin);

        Label optionCLabel = new Label("C:", skin);
        optionCField = new TextField("", skin);

        Label optionDLabel = new Label("D:", skin);
        optionDField = new TextField("", skin);

        Label correctAnswerLabel = new Label("Correct Answer:", skin);
        correctAnswerSelect = new SelectBox<>(skin);
        correctAnswerSelect.setItems("a", "b", "c", "d");

        // Action buttons
        TextButton saveQuestionButton = new TextButton("Save Question", skin);
        saveQuestionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveCurrentQuestion();
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
        optionsTable.add(optionAField).expandX().fillX().pad(5);
        optionsTable.row();
        optionsTable.add(optionBLabel).width(30);
        optionsTable.add(optionBField).expandX().fillX().pad(5);
        optionsTable.row();
        optionsTable.add(optionCLabel).width(30);
        optionsTable.add(optionCField).expandX().fillX().pad(5);
        optionsTable.row();
        optionsTable.add(optionDLabel).width(30);
        optionsTable.add(optionDField).expandX().fillX().pad(5);

        editorPanel.add(optionsTable).fillX();
        editorPanel.row();

        Table answerTable = new Table();
        answerTable.add(correctAnswerLabel).left().padRight(10);
        answerTable.add(correctAnswerSelect).width(100);

        editorPanel.add(answerTable).left().padTop(10);
        editorPanel.row();

        Table actionButtons = new Table();
        actionButtons.add(deleteButton).padRight(10);
        actionButtons.add(saveQuestionButton);

        editorPanel.add(actionButtons).right().padTop(20);

        // Initially disable the editor until a question is selected
        setEditorEnabled(false);

        return editorPanel;
    }

    private void populateQuestionsList() {
        questionsTable.clear();

        for (int i = 0; i < questions.size; i++) {
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
        if (index >= 0 && index < questions.size) {
            // Save the current question if one was being edited
            if (currentQuestionIndex != -1) {
                saveCurrentQuestion();
            }

            currentQuestionIndex = index;
            Question question = questions.get(index);

            // Fill the editor fields with the question data
            questionField.setText(question.getQuestion());

            ObjectMap<String, String> choices = question.getChoices();
            optionAField.setText(choices.get("a", ""));
            optionBField.setText(choices.get("b", ""));
            optionCField.setText(choices.get("c", ""));
            optionDField.setText(choices.get("d", ""));

            correctAnswerSelect.setSelected(question.getAnswer());

            // Enable the editor
            setEditorEnabled(true);

            // Update the questions list to highlight the selected question
            populateQuestionsList();
        }
    }

    private void setEditorEnabled(boolean enabled) {
        questionField.setDisabled(!enabled);
        optionAField.setDisabled(!enabled);
        optionBField.setDisabled(!enabled);
        optionCField.setDisabled(!enabled);
        optionDField.setDisabled(!enabled);
        correctAnswerSelect.setDisabled(!enabled);
        deleteButton.setDisabled(!enabled);
    }

    private void saveCurrentQuestion() {
        if (currentQuestionIndex != -1) {
            Question question = questions.get(currentQuestionIndex);

            // Update the question with the editor values
            question.setQuestion(questionField.getText());

            ObjectMap<String, String> choices = new ObjectMap<>();
            choices.put("a", optionAField.getText());
            choices.put("b", optionBField.getText());
            choices.put("c", optionCField.getText());
            choices.put("d", optionDField.getText());
            question.setChoices(choices);

            question.setAnswer(correctAnswerSelect.getSelected());

            // Update the questions list to show any changes
            populateQuestionsList();
        }
    }

    private void deleteCurrentQuestion() {
        if (currentQuestionIndex != -1 && questions.size > 0) {
            questions.removeIndex(currentQuestionIndex);

            // Reset selection if we deleted the last question
            if (questions.size == 0) {
                currentQuestionIndex = -1;
                setEditorEnabled(false);
                clearFields();
                addNewQuestion(); // Always have at least one question
            } else if (currentQuestionIndex >= questions.size) {
                currentQuestionIndex = questions.size - 1;
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
        optionAField.setText("");
        optionBField.setText("");
        optionCField.setText("");
        optionDField.setText("");
        correctAnswerSelect.setSelected("a");
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
        currentQuestionIndex = questions.size - 1;
        clearFields();
        // Update the UI
        populateQuestionsList();
        selectQuestion(currentQuestionIndex);
        setEditorEnabled(true);
        // Scroll to the bottom to show the new question
        scrollPane.setScrollY(questionsTable.getHeight());
    }

    private void saveAllChanges() {
        // First save the current question being edited
        saveCurrentQuestion();

        // Update the topic with all questions
        topic.clearQuestions();
        for (Question question : questions) {
            topic.addQuestion(question);
        }

        // Convert to JSON and save to file
        Question[] questionsArray = new Question[questions.size];
        for (int i = 0; i < questions.size; i++) {
            questionsArray[i] = questions.get(i);
        }

        String jsonString = json.toJson(questionsArray);
        FileHandle file = Gdx.files.local("assets/"+topicFileName);
        file.writeString(jsonString, false);

        // Show a success message
        Dialog successDialog = new Dialog("Success", skin);
        successDialog.text("All changes saved successfully!");
        successDialog.button("OK");
        successDialog.show(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (frameTexture != null) {
            frameTexture.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
