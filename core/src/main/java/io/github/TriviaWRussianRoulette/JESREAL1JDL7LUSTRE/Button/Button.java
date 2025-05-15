package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE.Button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Button {
    private static TextButton.TextButtonStyle redStyle;
    private static TextButton.TextButtonStyle grayStyle;
    private static TextButton.TextButtonStyle blueStyle;
    private static TextButton.TextButtonStyle goldenStyle;

    /**
     * Returns a TextButtonStyle for red buttons
     * @return TextButtonStyle with red button textures
     */
    public static TextButton.TextButtonStyle getRedStyle() {
        if (redStyle == null) {
            Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/red_button.png"))));
            Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/red_button_pressed.png"))));

            redStyle = new TextButton.TextButtonStyle();
            redStyle.up = upDrawable;
            redStyle.down = downDrawable;
            redStyle.font = new BitmapFont();
        }
        return redStyle;
    }

    /**
     * Returns a TextButtonStyle for gray buttons
     * @return TextButtonStyle with gray button textures
     */
    public static TextButton.TextButtonStyle getGrayStyle() {
        if (grayStyle == null) {
            Drawable grayUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button.png"))));
            Drawable grayDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"))));

            grayStyle = new TextButton.TextButtonStyle();
            grayStyle.up = grayUp;
            grayStyle.down = grayDown;
            grayStyle.font = new BitmapFont();
        }
        return grayStyle;
    }

    /**
     * Creates a red button with the given text
     * @param text The text to display on the button
     * @param fontScale The scale factor for the font size
     * @return TextButton with red styling
     */
    public static TextButton createRedButton(String text, float fontScale) {
        TextButton button = new TextButton(text, getRedStyle());
        button.getLabel().setFontScale(fontScale);
        return button;
    }

    /**
     * Returns a TextButtonStyle for blue buttons
     * @return TextButtonStyle with blue button textures
     */
    public static TextButton.TextButtonStyle getBlueStyle() {
        if (blueStyle == null) {
            Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/blue_button.png"))));
            Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/blue_button_pressed.png"))));

            blueStyle = new TextButton.TextButtonStyle();
            blueStyle.up = upDrawable;
            blueStyle.down = downDrawable;
            blueStyle.font = new BitmapFont();
        }
        return blueStyle;
    }

    /**
     * Returns a TextButtonStyle for golden buttons
     * Note: This method requires a Skin to be passed as it builds upon an existing TextButtonStyle
     * @param skin The Skin containing the base TextButtonStyle and font
     * @return TextButtonStyle with golden button texture
     */
    public static TextButton.TextButtonStyle getGoldenStyle(Skin skin) {
        if (goldenStyle == null) {
            // Create golden button style based on an existing style from the skin
            goldenStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
            Texture buttonTexture = new Texture(Gdx.files.internal("images/borderForButton.png"));
            goldenStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
            goldenStyle.font = skin.getFont("default-font");
            goldenStyle.fontColor = Color.BLACK;
        }
        return goldenStyle;
    }

    /**
     * Creates a golden button with the given text
     * @param text The text to display on the button
     * @param fontScale The scale factor for the font size
     * @param skin The Skin containing the base TextButtonStyle and font
     * @return TextButton with golden styling
     */
    public static TextButton createGoldenButton(String text, float fontScale, Skin skin) {
        TextButton button = new TextButton(text, getGoldenStyle(skin));
        button.getLabel().setFontScale(fontScale);
        return button;
    }

    /**
     * Disposes of any resources used by button styles to prevent memory leaks
     * Should be called when the application is being closed or when styles are no longer needed
     */
    public static void dispose() {
        if (redStyle != null && redStyle.font != null) {
            redStyle.font.dispose();
        }
        if (grayStyle != null && grayStyle.font != null) {
            grayStyle.font.dispose();
        }
        if (blueStyle != null && blueStyle.font != null) {
            blueStyle.font.dispose();
        }
        if (goldenStyle != null) {
            // Note: We don't dispose the golden style's font as it comes from the skin
            // and the skin will handle disposal
        }
        // Note: The Textures should be disposed separately if necessary
    }
}
