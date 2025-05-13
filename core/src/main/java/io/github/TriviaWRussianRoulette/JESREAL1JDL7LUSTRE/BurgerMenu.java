package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.ObjectSet;

public class BurgerMenu extends Table {

    private Texture dropdownBackgroundTexture;
    private Texture itemBackgroundTexture;
    private Texture itemBackgroundTextureOnClick;
    private Texture redButtonTexture;
    private Texture redButtonPressedTexture;

    private final ImageButton burgerButton;
    private Table menuContainer;
    private boolean isMenuVisible = false;

    public final ListSelection itemList = new ListSelection();

    public interface SelectionListener {
        void selected(String item);
    }

    public class ListSelection {
        private String selected = null;
        private final ObjectSet<SelectionListener> listeners = new ObjectSet<>();

        public String getSelected() {
            return selected;
        }

        public void setSelected(String item) {
            this.selected = item;
            for (SelectionListener listener : listeners) {
                listener.selected(item);
            }
        }

        public void clear() {
            selected = null;
        }

        public void addListener(SelectionListener listener) {
            listeners.add(listener);
        }

        public void removeListener(SelectionListener listener) {
            listeners.remove(listener);
        }

        public ListSelection getSelection() {
            return this;
        }
    }

    public interface MenuSelectionListener {
        void onItemSelected(String selectedItem);
    }

    public BurgerMenu(Skin skin, String[] items, Texture burgerIconTexture, MenuSelectionListener listener) {
        this.setTouchable(Touchable.enabled);
        this.top().left().pad(1);

        // --- Burger Button ---
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(burgerIconTexture));
        burgerButton = new ImageButton(buttonStyle);

        // --- Load Textures ---
        dropdownBackgroundTexture = new Texture(Gdx.files.internal("frameForWords.png"));
        itemBackgroundTexture = new Texture(Gdx.files.internal("buttons/gray_button.png"));
        itemBackgroundTextureOnClick = new Texture(Gdx.files.internal("buttons/gray_button_pressed.png"));
        redButtonTexture = new Texture(Gdx.files.internal("buttons/red_button.png"));
        redButtonPressedTexture = new Texture(Gdx.files.internal("buttons/red_button_pressed.png"));

        // --- Set up the menu container with background ---
        menuContainer = new Table();
        menuContainer.setBackground(new TextureRegionDrawable(new TextureRegion(dropdownBackgroundTexture)));
        menuContainer.setVisible(isMenuVisible);
        menuContainer.pad(20);

        float itemHeight = 40;
        float menuWidth = 160;
        float menuPadding = 10;
        float itemSpacing = 2;

        for (int i = 0; i < items.length; i++) {
            String item = items[i];

            // Create a button style (red for first, gray for others)
            Button.ButtonStyle itemButtonStyle = new Button.ButtonStyle();
            if (i == 0) {
                itemButtonStyle.up = new TextureRegionDrawable(new TextureRegion(redButtonTexture));
                itemButtonStyle.down = new TextureRegionDrawable(new TextureRegion(redButtonPressedTexture));
            } else {
                itemButtonStyle.up = new TextureRegionDrawable(new TextureRegion(itemBackgroundTexture));
                itemButtonStyle.down = new TextureRegionDrawable(new TextureRegion(itemBackgroundTextureOnClick));
            }

            // Create the button with the label
            final Button itemButton = new Button(itemButtonStyle);
            Label label = new Label(item, skin);
            label.setFontScale(1.1f); // Optional: make text a bit larger
            itemButton.add(label).center();

            menuContainer.add(itemButton)
                .width(menuWidth - menuPadding * 2)
                .height(itemHeight)
                .padBottom(itemSpacing)
                .fillX()
                .row();

            // Add the selection listener
            itemButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (listener != null) {
                        listener.onItemSelected(item);
                    }
                    itemList.setSelected(item);
                    isMenuVisible = false;
                    menuContainer.setVisible(isMenuVisible);
                    itemList.clear();
                }
            });
        }

        burgerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMenuVisible = !isMenuVisible;
                menuContainer.setVisible(isMenuVisible);
            }
        });

        this.add(burgerButton).size(90, 70).padBottom(5).row();
        this.add(menuContainer).width(menuWidth + 30).padTop(1);
    }

    public void dispose() {
        if (dropdownBackgroundTexture != null) dropdownBackgroundTexture.dispose();
        if (itemBackgroundTexture != null) itemBackgroundTexture.dispose();
        if (itemBackgroundTextureOnClick != null) itemBackgroundTextureOnClick.dispose();
        if (redButtonTexture != null) redButtonTexture.dispose();
        if (redButtonPressedTexture != null) redButtonPressedTexture.dispose();
    }
}
