package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ChangeMusic extends BaseScreen {
    private Slider volumeSlider;
    private Label currentTrackLabel;
    private Table musicDropdownContainer;
    private boolean isMusicDropdownVisible = false;
    private String selectedTrack;

    // Textures for custom dropdown
    private Texture dropdownBackgroundTexture;
    private Texture itemBackgroundTexture;
    private Texture itemBackgroundTextureOnClick;

    // Background and frame textures
    private Texture bgTexture;
    private Texture frameTexture;
    private Texture labelTexture;

    public ChangeMusic(Main game) {
        super(game);
    }

    @Override
    public void show() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        burger = new Burger(skin, game);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures
        bgTexture = new Texture(Gdx.files.internal("images/ForDefaultBg.png"));
        frameTexture = new Texture(Gdx.files.internal("images/frameForWords.png"));
        labelTexture = new Texture(Gdx.files.internal("images/forLabels.png"));
        labelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Load textures for custom dropdown (similar to BurgerMenu)
        dropdownBackgroundTexture = new Texture(Gdx.files.internal("images/frameForWords.png"));
        itemBackgroundTexture = new Texture(Gdx.files.internal("buttons/gray_button.png"));
        itemBackgroundTextureOnClick = new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"));

        // Get current track selection
        selectedTrack = BgMusic.getCurrentTrackName();

        // Set up the background
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Create main container with frame
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

        // Create styled title label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(labelTexture));

        Label title = new Label("Music Settings", labelStyle);
        title.setAlignment(Align.center);
        title.setSize(labelTexture.getWidth(), labelTexture.getHeight());
        title.setFontScale(2);
        title.setColor(Color.WHITE);

        overlayTable.add(title).pad(20).padTop(60).center().row();

        // Content container for all music controls
        Table contentTable = new Table();
        overlayTable.add(contentTable).expand().fill().pad(20).padBottom(100).row();

        // === Volume control ===
        Label volumeLabel = new Label("Music Volume", skin);
        volumeLabel.setFontScale(1.5f);
        volumeLabel.setColor(Color.BLACK);
        contentTable.add(volumeLabel).center().padBottom(10).row();

        volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(BgMusic.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BgMusic.setVolume(volumeSlider.getValue());
            }
        });
        contentTable.add(volumeSlider).width(500).padBottom(30).row();

        Label musicLabel = new Label("Background Music", skin);
        musicLabel.setFontScale(1.5f);
        musicLabel.setColor(Color.BLACK);
        contentTable.add(musicLabel).center().padBottom(10).row();

        // Create custom dropdown selector button
        TextButton.TextButtonStyle dropdownButtonStyle = new TextButton.TextButtonStyle();
        dropdownButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/red_button.png"))));
        dropdownButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/red_button_pressed.png"))));
        dropdownButtonStyle.font = new BitmapFont();

        TextButton dropdownButton = new TextButton(selectedTrack, dropdownButtonStyle);
        dropdownButton.getLabel().setFontScale(1.4f);
        contentTable.add(dropdownButton).width(500).height(70).padBottom(20).row();

        // Set up the dropdown container with background
        musicDropdownContainer = new Table();
        musicDropdownContainer.setBackground(new TextureRegionDrawable(new TextureRegion(dropdownBackgroundTexture)));
        musicDropdownContainer.setVisible(isMusicDropdownVisible);
        musicDropdownContainer.pad(20);

        // Add the dropdown items
        Array<String> trackNames = BgMusic.getAvailableTracks();
        float itemHeight = 40;
        float menuWidth = 480;
        float menuPadding = 10;
        float itemSpacing = 2;

        for (final String trackName : trackNames) {
            // Create button style
            Button.ButtonStyle itemButtonStyle = new Button.ButtonStyle();
            itemButtonStyle.up = new TextureRegionDrawable(new TextureRegion(itemBackgroundTexture));
            itemButtonStyle.down = new TextureRegionDrawable(new TextureRegion(itemBackgroundTextureOnClick));

            // Create the button with the label
            final Button itemButton = new Button(itemButtonStyle);
            Label label = new Label(trackName, skin);
            label.setFontScale(1.1f);
            itemButton.add(label).center();

            musicDropdownContainer.add(itemButton)
                .width(menuWidth - menuPadding * 2)
                .height(itemHeight)
                .padBottom(itemSpacing)
                .fillX()
                .row();

            // Add click listener
            itemButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedTrack = trackName;
                    BgMusic.playMusic(trackName);
                    updateCurrentTrackLabel();
                    dropdownButton.setText(trackName);
                    isMusicDropdownVisible = false;
                    musicDropdownContainer.setVisible(isMusicDropdownVisible);
                }
            });
        }

        // Add click listener to the dropdown button
        dropdownButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMusicDropdownVisible = !isMusicDropdownVisible;
                musicDropdownContainer.setVisible(isMusicDropdownVisible);
            }
        });

        contentTable.add(musicDropdownContainer).width(500).padBottom(20).row();

        Label nowPlayingLabel = new Label("Now Playing", skin);
        nowPlayingLabel.setFontScale(1.5f);
        nowPlayingLabel.setColor(Color.BLACK);
        contentTable.add(nowPlayingLabel).center().padBottom(10).row();

        currentTrackLabel = new Label(BgMusic.getCurrentTrackName(), skin);
        currentTrackLabel.setFontScale(1.4f);
        currentTrackLabel.setColor(Color.BLACK);
        contentTable.add(currentTrackLabel).center().padBottom(40).row();

        // Navigation button row
        Table navTable = new Table();

        // Back button using the same style from HowToPlay
        TextButton.TextButtonStyle goldenButtonStyle = new TextButton.TextButtonStyle();
        goldenButtonStyle.up =  new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        goldenButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));
        goldenButtonStyle.font = skin.getFont("default-font");
        goldenButtonStyle.fontColor = Color.WHITE;

        TextButton backButton = new TextButton("Back", goldenButtonStyle);
        backButton.getLabel().setFontScale(1.4f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goBack();
            }
        });

        contentTable.add(backButton).width(180).height(60).center().padTop(10);
        stage.addActor(burger);
    }

    private void updateCurrentTrackLabel() {
        if (currentTrackLabel != null) {
            currentTrackLabel.setText(BgMusic.getCurrentTrackName());
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Ensure the label stays updated
        updateCurrentTrackLabel();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (dropdownBackgroundTexture != null) dropdownBackgroundTexture.dispose();
        if (itemBackgroundTexture != null) itemBackgroundTexture.dispose();
        if (itemBackgroundTextureOnClick != null) itemBackgroundTextureOnClick.dispose();
        if (bgTexture != null) bgTexture.dispose();
        if (frameTexture != null) frameTexture.dispose();
        if (labelTexture != null) labelTexture.dispose();
    }
}
