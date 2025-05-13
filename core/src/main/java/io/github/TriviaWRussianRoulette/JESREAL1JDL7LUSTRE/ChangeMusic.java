package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class ChangeMusic extends BaseScreen {
    private Slider volumeSlider;
    private SelectBox<String> musicSelectBox;
    private Label currentTrackLabel;

    public ChangeMusic(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Table table = new Table();
        table.setFillParent(true);
        table.center(); // Center everything horizontally and vertically
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

        Array<String> trackNames = BgMusic.getAvailableTracks();
        musicSelectBox = new SelectBox<>(skin);
        musicSelectBox.setItems(trackNames);

        // Set current selection
        String currentTrack = BgMusic.getCurrentTrackName();
        for (int i = 0; i < trackNames.size; i++) {
            if (currentTrack.equals(trackNames.get(i))) {
                musicSelectBox.setSelectedIndex(i);
                break;
            }
        }

        musicSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BgMusic.playMusic(musicSelectBox.getSelected());
                updateCurrentTrackLabel();
            }
        });

        table.add(musicSelectBox).width(700).padBottom(100).row();

        Label nowPlayingLabel = new Label("Now Playing", skin);
        nowPlayingLabel.setFontScale(2f);
        table.add(nowPlayingLabel).center().padBottom(10).row();

        currentTrackLabel = new Label(BgMusic.getCurrentTrackName(), skin);
        currentTrackLabel.setFontScale(1.5f);
        table.add(currentTrackLabel).center().padBottom(40).row();

        Drawable upDrawable   = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up   = upDrawable;
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
}
