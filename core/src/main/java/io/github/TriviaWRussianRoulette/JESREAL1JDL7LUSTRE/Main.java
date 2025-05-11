package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Stack;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    // Shared across all screens:
    public AssetManager assets;
    public Skin uiSkin;
    public BitmapFont font;
    private Stack<BaseScreen> screenHistory;
    private boolean isGoingBack = false;

    @Override
    public void create() {
        assets = new AssetManager();

        assets.load("uiskin.json", Skin.class);
        assets.finishLoading();

        uiSkin = assets.get("uiskin.json", Skin.class);

        screenHistory = new Stack<>();
        BgMusic.initMusicOptions();
        BgMusic.playBackgroundMusic();
        setScreen(new FirstScreen(this));
    }


    // Override setScreen to manage screen stack
    @Override
    public void setScreen(Screen screen) {
        if (!isGoingBack && getScreen() != null && screen instanceof BaseScreen) {
            screenHistory.push((BaseScreen) getScreen());
        }

        isGoingBack = false; // reset for future transitions
        super.setScreen(screen);
    }

    public void goBack() {
        if (!screenHistory.isEmpty()) {
            isGoingBack = true;
            setScreen(screenHistory.pop());
        } else {
            System.out.println("No previous screen to go back to.");
            // Optionally exit or reset
            setScreen(new FirstScreen(this)); // or Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        // Clean up everything
        if (getScreen() != null) getScreen().dispose();
        assets.dispose();
        font.dispose();
    }
}
