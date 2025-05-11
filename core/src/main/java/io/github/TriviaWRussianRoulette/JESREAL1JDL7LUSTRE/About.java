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

public class About extends BaseScreen {

    private Texture bgTexture;        // 1.png - Background
    private Texture frameTexture;      // 2.png - Frame for words
    private Texture profileBorderTexture; // 3.png - Profile border

    private enum Pages {
        ABOUTGAME,
        DEV1, // coder @jesreal & @bea
        DEV2, // prototype @angel
        DEV3, // sprites @gil & @franilyn
        LEARNHOWTOPLAY
    }

    private Pages currentPage = Pages.ABOUTGAME;
    private Table contentTable;

    // Page titles
    private final String[] pageTitles = {
        "About Game",
        "Coders",
        "Prototype Designer",
        "Sprite Makers",
        "Learn How To Play"
    };

    // About Game content
    private final String aboutGameContent =
        "Welcome to Trivia with Russian Roulette!\n\n" +
            "This game combines trivia knowledge with the thrill of chance. " +
            "Answer questions correctly to earn points, but be careful - " +
            "wrong answers might trigger the roulette!\n\n" +
            "This project was created by students from [Your University/School Name] " +
            "as part of their [Course/Program Name] requirements.\n\n" +
            "Navigate through the pages to learn more about our development team.";

    // Developer descriptions
    private final String[] devDescriptions = {
        "Our coding team worked tirelessly to bring this game to life. " +
            "From implementing the trivia system to creating the roulette mechanics, " +
            "they built every aspect of the game's functionality.",

        "The prototype designer crafted the initial concept and game flow. " +
            "Their vision helped shape what the game would become and provided " +
            "the blueprint for development.",

        "Our talented sprite artists created all the visual elements you see in the game. " +
            "Their creativity and artistic skills gave our game its unique look and feel.",

        "Want to learn how to play? Check out our comprehensive tutorial to master " +
            "the game mechanics and develop winning strategies!"
    };

    // Developer information
    private final String[][] devNames = {
        {"Jesreal", "Bea"},
        {"Angel"},
        {"Gil", "Franilyn"},
        {}
    };

    private final String[][] devEmails = {
        {"jesreal@email.com", "bea@email.com"},
        {"angel@email.com"},
        {"gil@email.com", "franilyn@email.com"},
        {}
    };

    private final String[][] devLinks = {
        {"github.com/jesreal", "github.com/bea"},
        {"github.com/angel"},
        {"github.com/gil", "github.com/franilyn"},
        {}
    };

    private final String[][] devIndividualImages = {
        {"defprofile.png", "defprofile.png"},
        {"defprofile.png"},
        {"defprofile.png", "defprofile.png"},
        {}
    };

    public About(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        // Load textures
        bgTexture = new Texture(Gdx.files.internal("ForDefaultBg.png"));
        frameTexture = new Texture(Gdx.files.internal("frameForWords.png"));
        profileBorderTexture = new Texture(Gdx.files.internal("borderForProfile.png"));

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

        Texture labelTexture = new Texture(Gdx.files.internal("forLabels.png"));
        labelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(labelTexture));

        Label title = new Label("About", labelStyle);
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

        // Create golden button style for navigation
        TextButton.TextButtonStyle goldenButtonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        Texture buttonTexture = new Texture(Gdx.files.internal("borderForButton.png")); // Using profile border for buttons
        goldenButtonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        goldenButtonStyle.font = skin.getFont("default-font");
        goldenButtonStyle.fontColor = Color.BLACK;

        // Back button with golden style
        TextButton backBtn = new TextButton("<", goldenButtonStyle);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToPreviousPage();
            }
        });

        // Next button with golden style
        TextButton nextBtn = new TextButton(">", goldenButtonStyle);
        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToNextPage();
            }
        });

        // Add navigation buttons to match the UI in the image
        // Position the buttons at the bottom with appropriate spacing
        navTable.add(backBtn).size(80, 80).padRight(120);

        // Main menu button
        TextButton menuBtn = new TextButton("Menu", goldenButtonStyle);
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstScreen(game));
            }
        });
        navTable.add(menuBtn).size(80, 80);

        navTable.add(nextBtn).size(80, 80).padLeft(120);

        overlayTable.add(navTable).padBottom(40);

        // Render initial page
        renderCurrentPage();
    }

    private void renderCurrentPage() {
        contentTable.clear();

        // Get current page index
        int pageIndex = currentPage.ordinal();

        // Page title
        Label pageTitle = new Label(pageTitles[pageIndex], skin);
        pageTitle.setFontScale(1.5f);
        pageTitle.setColor(Color.BLACK); // Match text color with theme
        contentTable.add(pageTitle).pad(10).row();

        // Create content box with transparent background
        Table boxTable = new Table();
        // Use a transparent background as content sections will have their own backgrounds
        boxTable.setBackground(skin.newDrawable("white", new Color(1, 1, 1, 0.0f)));

        // Different content based on page
        if (pageIndex == 0) {
            // About Game page
            renderAboutGamePage(boxTable);
        } else if (pageIndex == 4) {
            // Learn How To Play page
            renderLearnHowToPlayPage(boxTable);
        } else {
            // Developer pages
            renderDevPage(boxTable, pageIndex - 1);
        }

        // Add page indicator
        Label progressLabel = new Label("Page " + (pageIndex + 1) + " of " + Pages.values().length, skin);
        progressLabel.setColor(Color.BLACK);
        boxTable.add(progressLabel).padTop(20).row();

        contentTable.add(boxTable).expand().fill();
    }

    private void renderAboutGamePage(Table boxTable) {
        // Create a container with frame texture background
        Table contentContainer = new Table();
        contentContainer.setBackground(new TextureRegionDrawable(new TextureRegion(frameTexture)));

        Label contentLabel = new Label(aboutGameContent, skin);
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);
        contentLabel.setColor(Color.BLACK); // Match text color with theme

        // Add padding inside the container
        contentContainer.add(contentLabel).pad(20).expand().fill();

        // Add the content container to the box table
        boxTable.add(contentContainer).pad(20).width(600).height(300).row();

        // Add game logo or screenshot
        try {
            Texture texture = new Texture(Gdx.files.internal("ForDefaultBg.png"));
            Image gameImage = new Image(texture);

            Container<Image> imageContainer = new Container<>(gameImage);
            imageContainer.maxWidth(400);
            imageContainer.maxHeight(200);

            boxTable.add(imageContainer).pad(10).row();
        } catch (Exception e) {
            Gdx.app.log("About", "Could not load image: Home.png", e);
            Label noImageLabel = new Label("(Game Logo)", skin);
            noImageLabel.setAlignment(Align.center);
            noImageLabel.setColor(Color.BLACK);
            boxTable.add(noImageLabel).pad(20).height(100).row();
        }
    }

    private void renderDevPage(Table boxTable, int devIndex) {
        // Create a container for dev description with frame texture background
        Table descContainer = new Table();
        descContainer.setBackground(new TextureRegionDrawable(new TextureRegion(frameTexture)));

        // Dev description
        Label descLabel = new Label(devDescriptions[devIndex], skin);
        descLabel.setWrap(true);
        descLabel.setAlignment(Align.center);
        descLabel.setColor(Color.BLACK); // Match text color with theme

        // Add padding inside the container
        descContainer.add(descLabel).pad(20).expand().fill();

        // Add the description container to the box table
        boxTable.add(descContainer).pad(20).width(600).height(150).row();

        // Dev team members in a grid
        Table teamGridTable = new Table();
        String[] names = devNames[devIndex];
        String[] emails = devEmails[devIndex];
        String[] links = devLinks[devIndex];
        String[] images = devIndividualImages[devIndex];

        // Calculate grid layout
        int columns = Math.min(2, names.length); // Max 2 columns
        int rows = (int)Math.ceil(names.length / (float)columns);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int index = row * columns + col;
                if (index < names.length) {
                    // Create a container for each developer
                    Table devTable = new Table();

                    // Developer info
                    Label nameLabel = new Label(names[index], skin);
                    nameLabel.setFontScale(1.2f);
                    nameLabel.setColor(Color.BLACK);
                    devTable.add(nameLabel).pad(5).row();

                    Label emailLabel = new Label(emails[index], skin);
                    emailLabel.setColor(Color.BLACK);
                    devTable.add(emailLabel).pad(2).row();

                    Label linkLabel = new Label(links[index], skin);
                    linkLabel.setColor(Color.BLACK);
                    devTable.add(linkLabel).pad(2).row();

                    // Individual developer image with profile border
                    try {
                        // Use the image from devIndividualImages array or fallback
                        String imagePath = images[index];

                        // Check if file exists, if not try with the developer's name
                        if (!Gdx.files.internal(imagePath).exists()) {
                            imagePath = names[index] + ".png";
                        }

                        // If still not found, use default profile
                        if (!Gdx.files.internal(imagePath).exists()) {
                            imagePath = "defprofile.png";
                        }

                        Texture profileTexture = new Texture(Gdx.files.internal(imagePath));
                        Image devImage = new Image(profileTexture);

                        // Create a stack to layer profile border over the image
                        Stack profileStack = new Stack();

                        // Add the profile image
                        Container<Image> imageContainer = new Container<>(devImage);
                        imageContainer.maxWidth(160);
                        imageContainer.maxHeight(160);
                        profileStack.add(imageContainer);

                        // Add the profile border on top
                        Image borderImage = new Image(profileBorderTexture);
                        profileStack.add(borderImage);

                        devTable.add(profileStack).pad(10).width(180).height(180).row();
                    } catch (Exception e) {
                        // Error handling with debug output
                        Gdx.app.log("About", "Could not load developer image for: " + names[index], e);

                        // Try loading the default profile picture with border
                        try {
                            Texture texture = new Texture(Gdx.files.internal("defprofile.png"));
                            Image devImage = new Image(texture);

                            // Create a stack to layer profile border over the image
                            Stack profileStack = new Stack();

                            // Add the profile image
                            Container<Image> imageContainer = new Container<>(devImage);
                            imageContainer.maxWidth(160);
                            imageContainer.maxHeight(160);
                            profileStack.add(imageContainer);

                            // Add the profile border on top
                            Image borderImage = new Image(profileBorderTexture);
                            profileStack.add(borderImage);

                            devTable.add(profileStack).pad(10).width(180).height(180).row();
                        } catch (Exception e2) {
                            // If even default profile fails, show text placeholder
                            Label noImageLabel = new Label("(Photo)", skin);
                            noImageLabel.setAlignment(Align.center);
                            noImageLabel.setColor(Color.BLACK);
                            devTable.add(noImageLabel).pad(10).width(180).height(100).row();
                        }
                    }

                    // Add to grid with padding
                    teamGridTable.add(devTable).pad(10).width(280).height(300);
                }
            }
            teamGridTable.row();
        }

        boxTable.add(teamGridTable).pad(10).row();
    }

    private void renderLearnHowToPlayPage(Table boxTable) {
        // Create a container with frame texture background
        Table contentContainer = new Table();
        contentContainer.setBackground(new TextureRegionDrawable(new TextureRegion(frameTexture)));

        Label contentLabel = new Label(
            "Want to learn how to play our game? It's easy and fun!\n\n" +
                "This section will give you a brief overview of the gameplay. " +
                "For a more detailed tutorial, check out the 'How To Play' section " +
                "from the main menu.",
            skin
        );
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);
        contentLabel.setColor(Color.BLACK); // Match text color with theme

        // Add padding inside the container
        contentContainer.add(contentLabel).pad(20).expand().fill();

        // Add the content container to the box table
        boxTable.add(contentContainer).pad(20).width(600).height(200).row();

        // Create custom button style for the tutorial button
        TextButton.TextButtonStyle tutorialButtonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        Texture buttonTexture = new Texture(Gdx.files.internal("borderForButton.png")); // Using profile border for buttons
        tutorialButtonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        tutorialButtonStyle.font = skin.getFont("default-font");
        tutorialButtonStyle.fontColor = Color.BLACK;

        // Tutorial button
        TextButton tutorialBtn = new TextButton("Go to Tutorial", tutorialButtonStyle);
        tutorialBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new HowToPlay(game));
            }
        });
        boxTable.add(tutorialBtn).pad(20).width(200).height(60).row();
    }

    private void goToNextPage() {
        int nextOrdinal = (currentPage.ordinal() + 1) % Pages.values().length;
        currentPage = Pages.values()[nextOrdinal];
        renderCurrentPage();
    }

    private void goToPreviousPage() {
        int prevOrdinal = currentPage.ordinal() - 1;
        if (prevOrdinal < 0) {
            prevOrdinal = Pages.values().length - 1;
        }
        currentPage = Pages.values()[prevOrdinal];
        renderCurrentPage();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (bgTexture != null) bgTexture.dispose();
        if (frameTexture != null) frameTexture.dispose();
        if (profileBorderTexture != null) profileBorderTexture.dispose();
    }
}
