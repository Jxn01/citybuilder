package view.gui.game;

import controller.GameManager;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import util.Logger;
import view.components.custom.MyButton;
import view.enums.MenuState;
import view.gui.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class implements the hamburger menu of the game gui
 */
public class HamburgerMenu extends GameMenu {
    private final @NotNull Rectangle hamburgerMenuArea;
    private final Color hamburgerMenuColor;
    MyButton saveBtn;
    MyButton newGameBtn;
    MyButton exitBtn;

    /**
     * Constructor of the hamburger menu
     *
     * @param game is the main game object
     */
    public HamburgerMenu(Game game) {
        super(game);

        saveBtn = new MyButton(0, 40, 120, 40, "saveGame");
        newGameBtn = new MyButton(0, 90, 120, 40, "newGame");
        exitBtn = new MyButton(0, 140, 120, 40, "exit");
        hamburgerMenuArea = new Rectangle(0, 40, 120, 120);
        hamburgerMenuColor = Color.white;
    }

    /**
     * Draw the hamburger menu on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(@NotNull Graphics2D gr) {
        if (getIsOpen()) {
            paintHamburgerMenuArea(gr);
            newGameBtn.draw(gr, game.getMousePosition());
            saveBtn.draw(gr, game.getMousePosition());
            exitBtn.draw(gr, game.getMousePosition());
        }
    }

    /**
     * Draw the hamburger menu's background area on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    private void paintHamburgerMenuArea(@NotNull Graphics2D gr) {
        gr.setColor(hamburgerMenuColor);
        int x = hamburgerMenuArea.x;
        int y = hamburgerMenuArea.y;
        int width = hamburgerMenuArea.width;
        int height = hamburgerMenuArea.height;
        gr.fillRect(x, y, width, height);
    }

    /**
     * Upon a click event, call the appropriate button's functionality
     *
     * @param p is the current cursor location
     */
    @Override
    public void click(Point p) {
        if (getIsOpen()) {
            if (saveBtn.isHovered(p)) {

                Logger.log("Save button clicked");
                GameData gd = GameManager.getGameData();
                game.getPanel().getGameManager().saveGame(gd);
                setIsOpen(false);

            } else if (newGameBtn.isHovered(p)) {

                Logger.log("New game button clicked");
                setIsOpen(false);
                game.setState(MenuState.NEWGAME);

            } else if (exitBtn.isHovered(p)) {

                Logger.log("Exit button clicked");
                game.exit();
            }
        }
    }

    /**
     * Get the hamburger menu's area as a rectangle
     * This is important for click event exceptions
     *
     * @return an arraylist of rectangles (with one rectangle)
     */
    @Override
    public @NotNull ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        if (this.getIsOpen()) {
            areas.add(hamburgerMenuArea);
        }
        return areas;
    }
}
