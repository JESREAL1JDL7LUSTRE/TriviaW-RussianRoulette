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
        // Push the current screen to the stack before switching to the new one
        // Don't add FirstScreen to the history stack
        if (this.getScreen() != null && !(this.getScreen() instanceof FirstScreen)) {
            screenHistory.push((BaseScreen) this.getScreen());
        }
        super.setScreen(screen);
    }

    // Go back to the previous screen
    public void goBack() {
        // Only go back if there are screens in history
        if (!screenHistory.isEmpty()) {
            // Pop the last screen and go back to it
            BaseScreen lastScreen = screenHistory.pop(); // Pop the previous screen
            setScreen(lastScreen);  // Switch to the last screen
        } else {
            System.out.println("No previous screen to go back to!");
            // Optionally, go to the First Screen or exit if no history exists
            setScreen(new FirstScreen(this));  // Or call Gdx.app.exit() if you want to close the game
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
