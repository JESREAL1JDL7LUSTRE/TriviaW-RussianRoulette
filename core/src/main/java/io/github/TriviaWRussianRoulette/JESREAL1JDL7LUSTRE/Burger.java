package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Burger extends Table {
    private final Texture burgerTexture;
    private final Main game;

    public Burger(Skin skin, Main game) {
        this.game = game;
        this.setFillParent(true);
        this.top().padTop(50).padLeft(30).padRight(20); // Optional padding

        Label title = new Label(" ", skin);
        title.setFontScale(10f);
        // Load icon texture (you can share this texture app-wide if needed)
        this.burgerTexture = new Texture(Gdx.files.internal("burger.png"));
        System.out.println(Gdx.files.internal("burger.png").exists());

        // Create burger menu with items and listener
        BurgerMenu burgerMenu = new BurgerMenu(
            skin,
            new String[]{"Home", "Back", "Options", "How To Play", "Exit"},
            burgerTexture,
            selected -> {
                System.out.println("Selected: '" + selected + "'");
                switch (selected) {
                    case "Home":
                        System.out.println("Navigate to Home screen");
                        game.setScreen(new FirstScreen(game));
                        break;
                    case "Options":
                        System.out.println("Options");
                        game.setScreen(new Options(game));
                        break;
                    case "How To Play":
                        System.out.println("How To Play");
                        game.setScreen(new HowToPlay(game));
                        break;
                    case "Back":
                        System.out.println("Go Back");
                        game.goBack();
                        break;
                    case "Exit":
                        Gdx.app.exit();
                        break;
                }
            }
        );

        burgerMenu.itemList.getSelection().clear();

        // Add title on left, expanding space, then button on right
        float barHeight = 70f; // You can tweak this
        this.add(title).left().height(barHeight).expandX().center();
        this.add(burgerMenu).right().height(barHeight).center();
        this.row();

    }
    public void dispose() {
        burgerTexture.dispose();
    }
}
