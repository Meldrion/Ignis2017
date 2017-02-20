package lu.innocence.ignis.engine;

import javafx.scene.image.Image;

/**
 * Created by Fabien Steines
 * Last Update on: 20.02.2017.
 */
public class EventPage {

    private String characterSpriteName;
    private Image spriteImage;

    public String getCharacterSpriteName() {
        return characterSpriteName;
    }

    public void setCharacterSpriteName(String characterSpriteName) {
        this.characterSpriteName = characterSpriteName;
    }

    public void setSpriteImage(Image spriteImage) {
        this.spriteImage = spriteImage;
    }

    public Image getSpriteImage() {
        return spriteImage;
    }

}
