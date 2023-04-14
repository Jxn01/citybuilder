package view;

import java.awt.*;
import java.io.IOException;
import static java.lang.Thread.sleep;

import util.ResourceLoader;

/**
 * This class implements the intro animation
 */
public class Intro {

    private Image background;
    private int alphaLevel = 255;
    private Boolean fadeInComplete;
    private Boolean waitingComplete;
    private Boolean fadeOutComplete;

    /**
     * Constructor of the Intro class
     */
    public Intro() {
        fadeInComplete = false;
        waitingComplete = false;
        fadeOutComplete = false;
        try {
            background = ResourceLoader.loadImage("background.png");
        } catch (IOException ex) {}
    }

    /**
     * draw one frame of the animation
     * @param panel is the game's main Panel object
     * @param gr is the graphics context of the Panel object
     */
    public void draw(Panel panel, Graphics2D gr) {
        //paint the monkey
        gr.drawImage(background, 0, 0, 1536 + 15, 793, null);

        //paint a black rectangle on it with an alpha level
        //the fade in and fade out effect is achieved
        //by modifying this alpha level
        Color myColour = new Color(0, 0, 0, alphaLevel);
        gr.setColor(myColour);
        gr.fillRect(0, 0, 1536 + 15, 793);

        if (!fadeInComplete && alphaLevel >= 5) {
            alphaLevel -= 5;
            return;
        }
        fadeInComplete = true;

        //wait 1,5 seconds
        if (!waitingComplete) {
            try {
                sleep(1500);
                waitingComplete = true;
            } catch (InterruptedException ignored) {
            }
            return;
        }

        //fade out phase
        if (!fadeOutComplete && alphaLevel <= 250) {
            alphaLevel += 5;
            return;
        }
        fadeOutComplete = true;

        panel.setState(MenuState.MAINMENU);
    }
}