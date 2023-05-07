package view.gui.game;

import util.Logger;
import view.components.custom.MyButton;
import view.gui.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents the catastrophe menu on the gui
 *
 * @author Felhasználó
 */
public class CatastropheMenu extends GameMenu {
    private final Rectangle menuArea;
    private final Color menuColor;
    Game game;
    MyButton virusBtn, fireBtn, econBtn;
    MyButton xBtn;

    /**
     * Constructor of the statistics menu
     *
     * @param game is the main Game object
     */
    public CatastropheMenu(Game game) {
        super(game);

        this.game = game;
        econBtn = new MyButton(268 + 30 + 192, 120, 250, 300, "econ");
        virusBtn = new MyButton(268 + 375 + 192, 120, 250, 300, "virus");
        fireBtn = new MyButton(988 + 192, 120, 250, 300, "fire");
        xBtn = new MyButton(1228 + 192, 50, 40, 40, "x");
        menuArea = new Rectangle(268 + 192, 50, 1000, 400);
        menuColor = Color.white;
    }

    /**
     * Draw the catastrophe menu on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr) {
        if (getIsOpen()) {
            paintStatsMenuArea(gr);

            econBtn.draw(gr, game.getMousePosition());
            virusBtn.draw(gr, game.getMousePosition());
            fireBtn.draw(gr, game.getMousePosition());
            xBtn.draw(gr, game.getMousePosition());

            gr.setColor(Color.black);
            gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            gr.drawString("Katasztrófák", 700 + 192, 80);

            //gr.drawString("Építési adatok", 278, 520);
        }
    }

    /**
     * Draw the catastrophe menu's background area on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    private void paintStatsMenuArea(Graphics2D gr) {
        gr.setColor(menuColor);
        int x = menuArea.x;
        int y = menuArea.y;
        int width = menuArea.width;
        int height = menuArea.height;
        gr.fillRect(x, y, width, height);
    }

    /**
     * Click event handler of the statistics menu
     *
     * @param p is the current mouse position
     */
    @Override
    public void click(Point p) {
        if (getIsOpen()) {
            if (xBtn.isHovered(p)) {

                Logger.log("Closed catastrophe menu");
                this.setIsOpen(false);

            } else if (virusBtn.isHovered(p)) {

                Logger.log("Virus button clicked");
                game.getPanel().getGameManager().evokeCovid();
                this.setIsOpen(false);

            } else if (fireBtn.isHovered(p)) {

                Logger.log("Fire button clicked");
                game.getPanel().getGameManager().evokeFirestorm();
                this.setIsOpen(false);

            } else if (econBtn.isHovered(p)) {

                Logger.log("Economy button clicked");
                game.getPanel().getGameManager().evokeFinancialCrisis();
                this.setIsOpen(false);

            }
        }
    }

    /**
     * Get the statistics menu's area as a rectangle
     * This is important for click event exceptions
     *
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        if (this.getIsOpen()) {
            areas.add(menuArea);
        }
        return areas;
    }
}
