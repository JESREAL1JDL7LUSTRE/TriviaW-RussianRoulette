package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;

public class BgMusic {
    private static Music backgroundMusic;
    private static String currentTrack = "bgm/BrainBloomTrivia.mp3"; // Default track
    private static float volume = 0.5f;
    private static Array<String> availableTracks;
    private static int currentTrackIndex = 0;

    // Initialize available tracks from JSON
    public static void initMusicOptions() {
        if (availableTracks == null) {
            Json json = new Json();
            availableTracks = json.fromJson(Array.class, String.class, Gdx.files.internal("assets/bgMusic.json"));

            // Find the index of the default track
            for (int i = 0; i < availableTracks.size; i++) {
                if (currentTrack.endsWith(availableTracks.get(i))) {
                    currentTrackIndex = i;
                    break;
                }
            }
        }
    }

    // Get all available music tracks
    public static Array<String> getAvailableTracks() {
        if (availableTracks == null) {
            initMusicOptions();
        }
        return availableTracks;
    }

    // Get currently playing track name (without path)
    public static String getCurrentTrackName() {
        return currentTrack.substring(currentTrack.lastIndexOf("/") + 1);
    }

    // Get current track index
    public static int getCurrentTrackIndex() {
        return currentTrackIndex;
    }

    // Change to next track in the list
    public static void nextTrack() {
        if (availableTracks == null) {
            initMusicOptions();
        }

        currentTrackIndex = (currentTrackIndex + 1) % availableTracks.size;
        playMusic(availableTracks.get(currentTrackIndex));
    }

    // Play specific music track by name
    public static void playMusic(String trackName) {
        // Stop current music if playing
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }

        // Set current track (add assets/ prefix if not present)
        currentTrack = trackName.startsWith("assets/") ? trackName : "assets/" + trackName;

        // Update current track index
        if (availableTracks != null) {
            for (int i = 0; i < availableTracks.size; i++) {
                if (currentTrack.endsWith(availableTracks.get(i))) {
                    currentTrackIndex = i;
                    break;
                }
            }
        }

        // Play the new track
        try {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(currentTrack));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(volume);
            backgroundMusic.play();
        } catch (Exception e) {
            Gdx.app.error("BgMusic", "Error playing music: " + currentTrack, e);
        }
    }

    // Play default or current background music
    public static void playBackgroundMusic() {
        if (backgroundMusic == null) {
            playMusic(currentTrack);
        } else if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    // Set music volume (0.0 to 1.0)
    public static void setVolume(float newVolume) {
        volume = Math.max(0, Math.min(1, newVolume)); // Clamp between 0 and 1
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);
        }
    }

    // Get current volume
    public static float getVolume() {
        return volume;
    }

    // Stop and dispose current music
    public static void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }
}
