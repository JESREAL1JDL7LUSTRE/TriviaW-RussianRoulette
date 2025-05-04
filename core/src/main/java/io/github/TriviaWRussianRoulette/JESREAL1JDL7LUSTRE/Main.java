package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    // Shared across all screens:
    public AssetManager assets;
    public Skin uiSkin;
    public BitmapFont font;

    @Override
    public void create() {
        // 1) Initialize asset manager and load UI assets
        assets = new AssetManager();
        assets.load("assets/uiskin.json", Skin.class);
        assets.finishLoading();  // or use an async loading screen

        // 2) Grab the skin (and any fonts, textures, etc.)
        uiSkin = assets.get("assets/uiskin.json", Skin.class);
        font = new BitmapFont();  // default font; replace with your own if desired

        // 3) Set the very first screen
        setScreen(new FirstScreen(this));
    }

    @Override
    public void dispose() {
        // Clean up everything
        if (getScreen() != null) getScreen().dispose();
        assets.dispose();
        font.dispose();
    }
}
