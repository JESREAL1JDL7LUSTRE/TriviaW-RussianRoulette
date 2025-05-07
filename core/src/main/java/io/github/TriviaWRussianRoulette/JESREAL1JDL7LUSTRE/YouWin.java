package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class YouWin extends BaseScreen{
    public YouWin(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Label title = new Label("You Win This time", skin);
        title.setFontScale(2);
        title.setPosition(100, 300);
        stage.addActor(title);
    }
}
