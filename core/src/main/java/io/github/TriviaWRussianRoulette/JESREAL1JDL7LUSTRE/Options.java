package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Options extends BaseScreen {
    public Options(Main game) {
        super(game); // just stores `game` reference, don't touch stage or skin here
    }

    @Override
    public void show() {
        super.show(); // initializes stage, skin, and burger

        // Now it's safe to add actors
        Label title = new Label("Options", skin);
        System.out.println("Options Screen");
        title.setFontScale(2);
        title.setPosition(100, 300);
        stage.addActor(title);

        // Add other UI components
    }
}
