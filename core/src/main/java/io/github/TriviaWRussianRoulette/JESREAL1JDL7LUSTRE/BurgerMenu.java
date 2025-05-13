package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class BurgerMenu extends Table {

    private final ImageButton burgerButton;
    private final ScrollPane dropdown;
    final List<String> itemList;

    public interface MenuSelectionListener {
        void onItemSelected(String selectedItem);
    }

    public BurgerMenu(Skin skin, String[] items, Texture burgerIconTexture, MenuSelectionListener listener) {
        this.setTouchable(Touchable.enabled);
        this.top().left().pad(5);

        // --- Burger Button ---
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(burgerIconTexture));
        burgerButton = new ImageButton(buttonStyle);

        // --- Menu Items List ---
        itemList = new List<>(skin);
        itemList.setItems(items);
        itemList.getSelection().setRequired(false);

        // --- ScrollPane (Dropdown) ---
        dropdown = new ScrollPane(itemList, skin);
        dropdown.setVisible(false);
        dropdown.setFadeScrollBars(false);
        dropdown.setScrollingDisabled(true, false);

        // Burger click toggles menu
        burgerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dropdown.setVisible(!dropdown.isVisible());
            }
        });

        // List item selected
        itemList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = itemList.getSelected();
                if (selected != null && listener != null) {
                    listener.onItemSelected(selected);
                }
                dropdown.setVisible(false);
                itemList.getSelection().clear(); // Reset selection for next time
            }
        });

        // Layout
        this.add(burgerButton).size(90, 70).padBottom(5).row();
        this.add(dropdown).width(120).height(100).left().padTop(5);
    }
}
