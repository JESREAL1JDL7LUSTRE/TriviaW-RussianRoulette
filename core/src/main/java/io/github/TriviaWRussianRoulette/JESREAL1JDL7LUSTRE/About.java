package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class About extends BaseScreen {

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

        // Main container
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Title
        Label title = new Label("About", skin);
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
                goToPreviousPage();
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

        // Next button
        TextButton nextBtn = new TextButton(">", skin);
        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToNextPage();
            }
        });
        navTable.add(nextBtn).pad(10).width(100);

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
        contentTable.add(pageTitle).pad(10).row();

        // Create content box with text
        Table boxTable = new Table();
        boxTable.setBackground(skin.getDrawable("default-pane")); // Use your skin's box drawable

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
        boxTable.add(progressLabel).padTop(20).row();

        contentTable.add(boxTable).expand().fill();
    }

    private void renderAboutGamePage(Table boxTable) {
        Label contentLabel = new Label(aboutGameContent, skin);
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);

        ScrollPane scrollPane = new ScrollPane(contentLabel, skin);
        scrollPane.setFadeScrollBars(false);
        boxTable.add(scrollPane).pad(20).width(600).height(300).row();

        // Add game logo or screenshot - FIXED PATH
        try {
            // Use Gdx.files.internal for assets
            Texture texture = new Texture(Gdx.files.internal("Home.png"));
            Image gameImage = new Image(texture);

            Container<Image> imageContainer = new Container<>(gameImage);
            imageContainer.maxWidth(400);
            imageContainer.maxHeight(200);

            boxTable.add(imageContainer).pad(10).row();
        } catch (Exception e) {
            // Error handling with debug output
            Gdx.app.log("About", "Could not load image: Home.png", e);
            Label noImageLabel = new Label("(Game Logo)", skin);
            noImageLabel.setAlignment(Align.center);
            boxTable.add(noImageLabel).pad(20).height(100).row();
        }
    }

    private void renderDevPage(Table boxTable, int devIndex) {
        // Dev description
        Label descLabel = new Label(devDescriptions[devIndex], skin);
        descLabel.setWrap(true);
        descLabel.setAlignment(Align.center);

        ScrollPane scrollPane = new ScrollPane(descLabel, skin);
        scrollPane.setFadeScrollBars(false);
        boxTable.add(scrollPane).pad(20).width(600).height(150).row();

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
                    devTable.add(nameLabel).pad(5).row();

                    Label emailLabel = new Label(emails[index], skin);
                    devTable.add(emailLabel).pad(2).row();

                    Label linkLabel = new Label(links[index], skin);
                    devTable.add(linkLabel).pad(2).row();

                    // Individual developer image - FIXED PATH
                    try {
                        // Use the image from devIndividualImages array or fallback to name
                        String imagePath = images[index];

                        // Check if file exists, if not try with the developer's name
                        if (!Gdx.files.internal(imagePath).exists()) {
                            imagePath = names[index] + ".png";
                        }

                        // If still not found, use default profile
                        if (!Gdx.files.internal(imagePath).exists()) {
                            imagePath = "defprofile.png";
                        }

                        Texture texture = new Texture(Gdx.files.internal(imagePath));
                        Image devImage = new Image(texture);

                        Container<Image> imageContainer = new Container<>(devImage);
                        imageContainer.maxWidth(180);
                        imageContainer.maxHeight(180);

                        devTable.add(imageContainer).pad(10).width(180).height(180).row();
                    } catch (Exception e) {
                        // Error handling with debug output
                        Gdx.app.log("About", "Could not load developer image for: " + names[index], e);

                        // Try loading the default profile picture as fallback
                        try {
                            Texture texture = new Texture(Gdx.files.internal("defprofile.png"));
                            Image devImage = new Image(texture);

                            Container<Image> imageContainer = new Container<>(devImage);
                            imageContainer.maxWidth(180);
                            imageContainer.maxHeight(180);

                            devTable.add(imageContainer).pad(10).width(180).height(180).row();
                        } catch (Exception e2) {
                            // If even default profile fails, show text placeholder
                            Label noImageLabel = new Label("(Photo)", skin);
                            noImageLabel.setAlignment(Align.center);
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
        Label contentLabel = new Label(
            "Want to learn how to play our game? It's easy and fun!\n\n" +
                "This section will give you a brief overview of the gameplay. " +
                "For a more detailed tutorial, check out the 'How To Play' section " +
                "from the main menu.",
            skin
        );
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);

        ScrollPane scrollPane = new ScrollPane(contentLabel, skin);
        scrollPane.setFadeScrollBars(false);
        boxTable.add(scrollPane).pad(20).width(600).height(200).row();

        // Tutorial button
        TextButton tutorialBtn = new TextButton("Go to Tutorial", skin);
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
        // Dispose any textures or resources if needed
    }
}
