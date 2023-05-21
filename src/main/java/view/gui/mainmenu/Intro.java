package view.gui.mainmenu;

import org.jetbrains.annotations.NotNull;
import util.ResourceLoader;
import view.components.Panel;
import view.enums.MenuState;

import java.awt.*;
import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * This class implements the intro animation
 */
public class Intro {

    private final view.components.Panel panel;
    private Image background;
    private int alphaLevel = 255;
    private boolean fadeInComplete;
    private boolean waitingComplete;
    private boolean fadeOutComplete;

    /**
     * Constructor of the Intro class
     *
     * @param panel is the Game's main Panel object
     */
    public Intro(view.components.Panel panel) {
        this.panel = panel;
        fadeInComplete = false;
        waitingComplete = false;
        fadeOutComplete = false;
        try {
            background = ResourceLoader.loadImage("background.png");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * draw one frame of the animation
     *
     * @param panel is the game's main Panel object
     * @param gr    is the graphics context of the Panel object
     */
    public void draw(@NotNull Panel panel, @NotNull Graphics2D gr) {
        //paint the monkey
        gr.drawImage(background, 0, 0, panel.width(), panel.height(), null);

        //paint a black rectangle on it with an alpha level
        //the fade in and fade out effect is achieved
        //by modifying this alpha level
        Color myColour = new Color(0, 0, 0, alphaLevel);
        gr.setColor(myColour);
        gr.fillRect(0, 0, panel.width(), panel.height());

        if (!fadeInComplete && alphaLevel >= 5) {
            alphaLevel -= 5;
        } else {
            fadeInComplete = true;

            //wait 1,5 seconds
            if (!waitingComplete) {
                try {
                    sleep(1500);
                    waitingComplete = true;
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            } else if (!fadeOutComplete && alphaLevel <= 250) { //fade out phase
                alphaLevel += 5;
            } else {
                fadeOutComplete = true;
                panel.setState(MenuState.MAINMENU);
            }
        }
    }
}