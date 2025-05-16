package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Button.Button;

public class HowToPlay extends BaseScreen {

    private Texture bgTexture;        // Background
    private Texture frameTexture;      // Frame for words
    private Texture profileBorderTexture; // Profile border (if needed for images)

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
        "Each round, you'll be presented with multiple-choice questions. Select your answer carefully. If you're correct you move on. If you're wrong, you might die or get lucky when facing the dreaded roulette wheel!",
        "Tips for success:\n• Read questions carefully\n• Study Well\n• Be smart dont be a dumdum",
        "Congratulations! You now know how to play Trivia with Russian Roulette. Press 'Play Game' to start your adventure, or review the instructions again if needed."
    };

    // Image paths - replace these with your actual image paths
    private final String[] stepImagePaths = {
        "assets/images/Logo.png",
        "assets/images/HTP2.png",
        "assets/images/HTP3.png",
        "assets/images/Logo.png"
    };

    public HowToPlay(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        // Load textures
        bgTexture = new Texture(Gdx.files.internal("images/ForDefaultBg.png"));
        frameTexture = new Texture(Gdx.files.internal("images/frameForWords.png"));
        profileBorderTexture = new Texture(Gdx.files.internal("images/borderForProfile.png"));

        // Set up the background
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Create main container
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Add the frame as background container
        Image frameImage = new Image(frameTexture);
        Container<Image> frameContainer = new Container<>(frameImage);
        frameContainer.fill().pad(50); // Adjust padding as needed
        mainTable.add(frameContainer).expand().fill();

        // Create content table that will sit on top of the frame
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);
        stage.addActor(overlayTable);

        Texture labelTexture = new Texture(Gdx.files.internal("images/forLabels.png"));
        labelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(labelTexture));

        Label title = new Label("How To Play", labelStyle);
        title.setAlignment(Align.center);
        title.setSize(labelTexture.getWidth(), labelTexture.getHeight());
        title.setFontScale(2);
        title.setColor(Color.WHITE);

        overlayTable.add(title).pad(20).padTop(60).center().row();

        // Content container
        contentTable = new Table();
        overlayTable.add(contentTable).expand().fill().pad(20).padBottom(100).row();

        // Navigation buttons row
        Table navTable = new Table();

        // Back button with golden style
        TextButton backBtn = new TextButton("<", Button.getGoldenStyle(skin));
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToPreviousStep();
            }
        });

        // Next button with golden style
        TextButton nextBtn = new TextButton(">", Button.getGoldenStyle(skin));
        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToNextStep();
            }
        });

        // Add navigation buttons to match the UI in the image
        navTable.add(backBtn).size(80, 80).padRight(120);

        // Main menu button
        TextButton menuBtn = new TextButton("Menu", Button.getGoldenStyle(skin));
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstScreen(game));
            }
        });
        navTable.add(menuBtn).size(80, 80);

        navTable.add(nextBtn).size(80, 80).padLeft(120);

        overlayTable.add(navTable).padBottom(40);

        // Render initial step
        renderCurrentStep();
    }

    private void renderCurrentStep() {
        contentTable.clear();

        // Get current step index
        int stepIndex = currentStep.ordinal();

        // Step title
        Label stepTitle = new Label(stepTitles[stepIndex], skin);
        stepTitle.setFontScale(1.5f);
        stepTitle.setColor(Color.BLACK); // Match text color with theme
        contentTable.add(stepTitle).pad(10).row();

        // Create content box with transparent background
        Table boxTable = new Table();
        // Use a transparent background as content sections will have their own backgrounds
        boxTable.setBackground(skin.newDrawable("white", new Color(1, 1, 1, 0.0f)));

        // Create a container with frame texture background for text content
        Table contentContainer = new Table();
        contentContainer.setBackground(new TextureRegionDrawable(new TextureRegion(frameTexture)));

        Label contentLabel = new Label(stepContents[stepIndex], skin);
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);
        contentLabel.setColor(Color.BLACK); // Match text color with theme

        // Add padding inside the container
        contentContainer.add(contentLabel).pad(20).expand().fill();

        // Add the content container to the box table
        boxTable.add(contentContainer).pad(20).width(600).height(200).row();

        // Try to load and add the image with border
        try {
            Texture texture = new Texture(Gdx.files.internal(stepImagePaths[stepIndex]));
            Image stepImage = new Image(texture);

            // Create a stack to layer profile border over the image
            Stack imageStack = new Stack();

            // Add the step image
            Container<Image> imageContainer = new Container<>(stepImage);
            imageContainer.maxWidth(300);
            imageContainer.maxHeight(200);
            imageStack.add(imageContainer);

            // Add the border on top (optional, if you want to frame images)
            Image borderImage = new Image(profileBorderTexture);
            imageStack.add(borderImage);

            boxTable.add(imageStack).pad(10).width(320).height(220).row();
        } catch (Exception e) {
            // Handle missing images gracefully
            Gdx.app.log("HowToPlay", "Could not load step image: " + stepImagePaths[stepIndex], e);
            Label noImageLabel = new Label("(Image placeholder)", skin);
            noImageLabel.setAlignment(Align.center);
            noImageLabel.setColor(Color.BLACK);
            boxTable.add(noImageLabel).pad(20).height(150).row();
        }

        // Add step indicator
        Label progressLabel = new Label("Step " + (stepIndex + 1) + " of " + Step.values().length, skin);
        progressLabel.setColor(Color.BLACK);
        boxTable.add(progressLabel).padTop(20).row();

        // Add 'Play Game' button on the final step
        if (stepIndex == Step.DONE.ordinal()) {
            TextButton playBtn = new TextButton("Play Game", Button.getRedStyle());
            playBtn.getLabel().setFontScale(1.4f);
            playBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // Navigate to the game screen
                    game.setScreen(new CustomizeGameplay(game)); // Assuming you have a GameScreen class
                }
            });
            boxTable.add(playBtn).pad(20).width(200).height(60).row();
        }

        contentTable.add(boxTable).expand().fill();
    }

    private void goToNextStep() {
        int nextOrdinal = (currentStep.ordinal() + 1) % Step.values().length;
        currentStep = Step.values()[nextOrdinal];
        renderCurrentStep();
    }

    private void goToPreviousStep() {
        int prevOrdinal = currentStep.ordinal() - 1;
        if (prevOrdinal < 0) {
            prevOrdinal = Step.values().length - 1;
        }
        currentStep = Step.values()[prevOrdinal];
        renderCurrentStep();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (bgTexture != null) bgTexture.dispose();
        if (frameTexture != null) frameTexture.dispose();
        if (profileBorderTexture != null) profileBorderTexture.dispose();
    }
}
