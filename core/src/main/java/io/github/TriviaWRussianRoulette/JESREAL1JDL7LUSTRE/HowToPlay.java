package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class HowToPlay extends BaseScreen {

    public HowToPlay(Main game) {
        super(game);
    }
    @Override
    public void show() {
        super.show();

        Label title = new Label("How To Play", skin);
        System.out.println("How To play Screen");
        title.setFontScale(2);
        title.setPosition(100, 300);
        stage.addActor(title);
    }
}
