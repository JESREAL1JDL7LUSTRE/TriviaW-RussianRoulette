package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HowToPlay extends BaseScreen {

    private enum Step {
        STEP1,
        STEP2,
        STEP3,
        DONE
    }

    private Step currentStep = Step.STEP1;
    private Table contentTable;

    // Tutorial content
    private final String[] stepTitles = {
        "Step 1: Getting Started",
        "Step 2: Playing the Game",
        "Step 3: Winning Strategies",
        "You're Ready to Play!"
    };

    private final String[] stepContents = {
        "Welcome to Trivia with Russian Roulette!\n\nThis game combines trivia knowledge with the thrill of chance. Answer questions correctly to earn points, but be careful - wrong answers might trigger the roulette!",
        "Each round, you'll be presented with multiple-choice questions. Select your answer carefully. If you're correct, you'll earn points based on difficulty. If you're wrong, you might lose points or face the dreaded roulette wheel!",
        "Tips for success:\n• Read questions carefully\n• Use your lifelines wisely\n• Balance risk and reward\n• Remember that harder questions are worth more points",
        "Congratulations! You now know how to play Trivia with Russian Roulette. Press 'Play Game' to start your adventure, or review the instructions again if needed."
    };

    // Image paths - replace these with your actual image paths
    private final String[] stepImagePaths = {
        "assets/Home.png",
        "assets/Home.png",
        "assets/Home.png",
        "assets/Home.png"
    };

    public HowToPlay(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        // Main container
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Title
        Label title = new Label("How To Play", skin);
        title.setFontScale(2);
        mainTable.add(title).pad(20).row();

        // Content container
        contentTable = new Table();
        mainTable.add(contentTable).expand().fill().pad(20).row();

        // Navigation buttons row
        Table navTable = new Table();
        mainTable.add(navTable).pad(20);

        // Back button
        TextButton backBtn = new TextButton("<", skin);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToPreviousStep();
            }
        });
        navTable.add(backBtn).pad(10).width(100);

        // Main menu button
        TextButton menuBtn = new TextButton("Main Menu", skin);
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstScreen(game));
            }
        });
        navTable.add(menuBtn).pad(10).width(150);

        // Next/Done button
        TextButton nextBtn = new TextButton(">", skin);
        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToNextStep();
            }
        });
        navTable.add(nextBtn).pad(10).width(100);

        // Render initial step
        renderCurrentStep();
    }

    private void renderCurrentStep() {
        contentTable.clear();

        // Get current step index
        int stepIndex = 0;
        switch (currentStep) {
            case STEP1: stepIndex = 0; break;
            case STEP2: stepIndex = 1; break;
            case STEP3: stepIndex = 2; break;
            case DONE: stepIndex = 3; break;
        }

        // Step title
        Label stepTitle = new Label(stepTitles[stepIndex], skin);
        stepTitle.setFontScale(1.5f);
        contentTable.add(stepTitle).pad(10).row();

        // Create content box with text
        Table boxTable = new Table();
        boxTable.setBackground(skin.getDrawable("default-pane")); // Use your skin's box drawable

        Label contentLabel = new Label(stepContents[stepIndex], skin);
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);

        // Create a scroll pane in case the text is long
        ScrollPane scrollPane = new ScrollPane(contentLabel, skin);
        scrollPane.setFadeScrollBars(false);
        boxTable.add(scrollPane).pad(20).width(600).row();

        // Try to load and add the image
        try {
            Texture texture = new Texture(Gdx.files.internal(stepImagePaths[stepIndex]));
            Image stepImage = new Image(texture);

            // Limit the image size if needed
            Container<Image> imageContainer = new Container<>(stepImage);
            imageContainer.maxWidth(400);
            imageContainer.maxHeight(300);

            boxTable.add(imageContainer).pad(10);
        } catch (Exception e) {
            // Handle missing images gracefully
            Label noImageLabel = new Label("(Image placeholder)", skin);
            noImageLabel.setAlignment(Align.center);
            boxTable.add(noImageLabel).pad(20).height(150);
        }

        // Add step indicator
        Label progressLabel = new Label("Step " + (stepIndex + 1) + " of 4", skin);
        boxTable.add(progressLabel).padTop(20);

        contentTable.add(boxTable).expand().fill();
    }

    private void goToNextStep() {
        switch (currentStep) {
            case STEP1:
                currentStep = Step.STEP2;
                break;
            case STEP2:
                currentStep = Step.STEP3;
                break;
            case STEP3:
                currentStep = Step.DONE;
                break;
            case DONE:
                // If done, go back to main menu or start the game
                game.setScreen(new FirstScreen(game));
                return;
        }
        renderCurrentStep();
    }

    private void goToPreviousStep() {
        switch (currentStep) {
            case STEP1:
                game.setScreen(new FirstScreen(game));
                return;
            case STEP2:
                currentStep = Step.STEP1;
                break;
            case STEP3:
                currentStep = Step.STEP2;
                break;
            case DONE:
                currentStep = Step.STEP3;
                break;
        }
        renderCurrentStep();
    }

    @Override
    public void dispose() {
        super.dispose();
        // Dispose any textures or resources if needed
    }
}
