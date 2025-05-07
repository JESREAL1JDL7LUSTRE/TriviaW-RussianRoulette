package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
        // Initialize asset manager and load UI assets
        assets = new AssetManager();
        assets.load("assets/uiskin.json", Skin.class);
        assets.finishLoading();  // or use an async loading screen

        // Grab the skin (and any fonts, textures, etc.)
        uiSkin = assets.get("assets/uiskin.json", Skin.class);
        font = new BitmapFont();  // default font; replace with your own if desired

        // Initialize the screen history stack
        screenHistory = new Stack<>();

        // Set the very first screen (no history at this point)
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
