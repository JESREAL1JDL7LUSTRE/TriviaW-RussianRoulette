package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
        table.pad(50);
        stage.addActor(table);

        // Volume control section
        table.add(new Label("Music Volume:", skin)).left().padRight(20);

        volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(BgMusic.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BgMusic.setVolume(volumeSlider.getValue());
            }
        }); // <-- fixed closing brace and parenthesis here

        table.add(volumeSlider).expandX().fillX();
        table.row().padTop(30);

        // Music selection section
        table.add(new Label("Background Music:", skin)).left().padRight(20);

        // Get available tracks from BgMusic class
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

        table.add(musicSelectBox).expandX().fillX();
        table.row().padTop(10);

        // Current track display
        table.add(new Label("Now Playing:", skin)).left().padRight(20);
        currentTrackLabel = new Label(BgMusic.getCurrentTrackName(), skin);
        table.add(currentTrackLabel).left();
        table.row().padTop(40);

        // Add Apply and Back buttons
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goBack();
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(backButton);
        table.add(buttonTable).colspan(2).center();
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
