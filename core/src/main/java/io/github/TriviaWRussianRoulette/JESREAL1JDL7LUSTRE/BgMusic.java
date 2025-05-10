package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BgMusic {
    private static Music backgroundMusic;

    public static void playBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/bgMusic.mp3"));
            backgroundMusic.setLooping(true);  // <-- This makes it repeat
            backgroundMusic.setVolume(0.5f);   // Volume: 0.0 to 1.0
            backgroundMusic.play();
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }
}
