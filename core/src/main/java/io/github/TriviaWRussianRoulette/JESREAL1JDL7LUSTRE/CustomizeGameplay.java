package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CustomizeGameplay extends BaseScreen{
    private enum Step {
        DIFFICULTY,
        RANDOM_CHOICES,
        ON_DEATH,
        COMPLETE
    }

    private Step currentStep = Step.DIFFICULTY;

    private int selectedDifficulty = 1;
    private boolean selectedRandomChoices = false;
    private boolean selectedOnDeath = true;

    public CustomizeGameplay(Main game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        renderStepUI();
    }

    private void renderStepUI() {
        stage.clear(); // Clear previous UI
        super.show();
        Table table = new Table();
        table.setFillParent(true); // Make the table take up the whole screen
        table.center();            // Center everything by default

        stage.addActor(table);     // Add the table to the stage

        switch (currentStep) {
            case DIFFICULTY:
                showDifficultyStep(table);
                break;
            case RANDOM_CHOICES:
                showRandomChoicesStep(table);
                break;
            case ON_DEATH:
                showOnDeathStep(table);
                break;
            case COMPLETE:
                goToNextScreen();
                break;
        }
    }

    private void showDifficultyStep(Table table) {
        Label label = new Label("Select Difficulty:", skin);
        TextButton easy = new TextButton("Easy", skin);
        TextButton medium = new TextButton("Medium", skin);
        TextButton hard = new TextButton("Hard", skin);

        easy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 1;
                currentStep = Step.RANDOM_CHOICES;
                renderStepUI();
            }
        });

        medium.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 2;
                currentStep = Step.RANDOM_CHOICES;
                renderStepUI();
            }
        });

        hard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedDifficulty = 3;
                currentStep = Step.RANDOM_CHOICES;
                renderStepUI();
            }
        });

        // Add them to the table in vertical order
        table.add(label).padBottom(20).row();
        table.add(easy).padBottom(10).row();
        table.add(medium).padBottom(10).row();
        table.add(hard).row();
    }

    private void showRandomChoicesStep(Table table) {
        Label label = new Label("Enable random choices?", skin);
        TextButton yes = new TextButton("Yes", skin);
        TextButton no = new TextButton("No", skin);

        yes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedRandomChoices = true;
                currentStep = Step.ON_DEATH;
                renderStepUI();
            }
        });

        no.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedRandomChoices = false;
                currentStep = Step.ON_DEATH;
                renderStepUI();
            }
        });

        table.add(label).padBottom(20).row();
        table.add(yes).padBottom(10).row();
        table.add(no).padBottom(10).row();
    }
    private void showOnDeathStep(Table table) {
        Label label = new Label("What happen upon Death", skin);
        TextButton yes = new TextButton("reincarnate", skin);
        TextButton no = new TextButton("Die in peace", skin);

        yes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedOnDeath = true;
                currentStep = Step.COMPLETE;
                renderStepUI();
            }
        });

        no.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedOnDeath = false;
                currentStep = Step.COMPLETE;
                renderStepUI();
            }
        });

        table.add(label).padBottom(20).row();
        table.add(yes).padBottom(10).row();
        table.add(no).padBottom(10).row();
    }
    private void goToNextScreen() {
        // You can pass 'this' as CustomizeGameplay into the next screen
        game.setScreen(new TopicChoice(game, this));
    }

    public int difficulty() {
        return selectedDifficulty;
    }

    public boolean randomChoices() {
        return selectedRandomChoices;
    }

    public boolean onDeath() {
        return selectedOnDeath;
    }

}
