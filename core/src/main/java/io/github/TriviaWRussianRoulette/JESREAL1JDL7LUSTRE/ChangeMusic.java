package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

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

    public ChangeMusic(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Load textures for custom dropdown (similar to BurgerMenu)
        dropdownBackgroundTexture = new Texture(Gdx.files.internal("frameForWords.png"));
        itemBackgroundTexture = new Texture(Gdx.files.internal("buttons/gray_button.png"));
        itemBackgroundTextureOnClick = new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"));

        // Get current track selection
        selectedTrack = BgMusic.getCurrentTrackName();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(50);
        stage.addActor(table);

        // === Volume control ===
        Label volumeLabel = new Label("Music Volume", skin);
        volumeLabel.setFontScale(2f);
        table.add(volumeLabel).center().padBottom(10).row();

        volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(BgMusic.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BgMusic.setVolume(volumeSlider.getValue());
            }
        });
        table.add(volumeSlider).width(700).padBottom(40).row();

        Label musicLabel = new Label("Background Music", skin);
        musicLabel.setFontScale(2f);
        table.add(musicLabel).center().padBottom(10).row();

        // Create custom dropdown selector button (similar to burger button)
        TextButton.TextButtonStyle dropdownButtonStyle = new TextButton.TextButtonStyle();
        dropdownButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        dropdownButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));
        dropdownButtonStyle.font = new BitmapFont();

        TextButton dropdownButton = new TextButton(selectedTrack, dropdownButtonStyle);
        dropdownButton.getLabel().setFontScale(1.4f);
        table.add(dropdownButton).width(700).height(70).padBottom(20).row();

        // Set up the dropdown container with background (similar to BurgerMenu)
        musicDropdownContainer = new Table();
        musicDropdownContainer.setBackground(new TextureRegionDrawable(new TextureRegion(dropdownBackgroundTexture)));
        musicDropdownContainer.setVisible(isMusicDropdownVisible);
        musicDropdownContainer.pad(20);

        // Add the dropdown items
        Array<String> trackNames = BgMusic.getAvailableTracks();
        float itemHeight = 40;
        float menuWidth = 640;
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

        table.add(musicDropdownContainer).width(700).padBottom(20).row();

        Label nowPlayingLabel = new Label("Now Playing", skin);
        nowPlayingLabel.setFontScale(2f);
        table.add(nowPlayingLabel).center().padBottom(10).row();

        currentTrackLabel = new Label(BgMusic.getCurrentTrackName(), skin);
        currentTrackLabel.setFontScale(1.5f);
        table.add(currentTrackLabel).center().padBottom(40).row();

        // Back button
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;
        style.font = new BitmapFont();
        TextButton backButton = new TextButton("Back", style);
        backButton.getLabel().setFontScale(1.4f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goBack();
            }
        });

        table.add(backButton).width(200).height(60).center();
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
    }
}
