package view.gui.game;

import controller.GameManager;
import org.jetbrains.annotations.NotNull;
import util.Logger;
import view.components.custom.MyButton;
import view.components.custom.MyInputField;
import view.gui.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * This class implements the tax menu of the game gui
 */
public class TaxMenu extends GameMenu {
    private final @NotNull Rectangle taxMenuArea;
    private final Color taxMenuColor;
    MyButton xBtn;
    MyInputField input;
    MyButton modifyBtn;

    /**
     * Constructor of the tax menu
     *
     * @param game is the main Game object
     */
    public TaxMenu(Game game) {
        super(game);

        this.game = game;
        xBtn = new MyButton(1228 + 192, 50, 40, 40, "x");
        modifyBtn = new MyButton(960 + 192, 130, 120, 40, "modify");
        input = new MyInputField(630 + 192, 125, 325, 50);
        taxMenuArea = new Rectangle(268 + 192, 50, 1000, 150);
        taxMenuColor = Color.white;
    }

    /**
     * Draw the tax menu on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(@NotNull Graphics2D gr) {
        if (getIsOpen()) {
            paintTaxMenuArea(gr);

            xBtn.draw(gr, game.getMousePosition());

            gr.setColor(Color.black);
            gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            gr.drawString("Adó", 750 + 192, 80);

            gr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            gr.drawString("Éves adó módosítása ($/fő/év): ", 340 + 192, 160);

            input.draw(gr, game.getMousePosition());
            modifyBtn.draw(gr, game.getMousePosition());
        }
    }

    /**
     * Draw the tax menu's background area on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    private void paintTaxMenuArea(@NotNull Graphics2D gr) {
        gr.setColor(taxMenuColor);
        int x = taxMenuArea.x;
        int y = taxMenuArea.y;
        int width = taxMenuArea.width;
        int height = taxMenuArea.height;
        gr.fillRect(x, y, width, height);
    }

    /**
     * Click event handler of the tax menu
     *
     * @param p is the current mouse position
     */
    @Override
    public void click(Point p) {
        if (getIsOpen()) {
            if (xBtn.isHovered(p)) {

                Logger.log("Closed the tax menu");
                setIsOpen(false);

            } else if (modifyBtn.isHovered(p)) {

                Logger.log("Modify-tax button clicked");
                String inputText = input.getText();
                GameManager.getGameData().setYearlyTaxes(Integer.parseInt(inputText));
                setIsOpen(false);

            }
        }
    }

    /**
     * The keypress event handler of the tax menu
     *
     * @param e is the KeyEvent
     */
    public void keyPressed(@NotNull KeyEvent e) {
        if (getIsOpen()) {
            char c = e.getKeyChar();
            if (c == KeyEvent.VK_BACK_SPACE) {
                input.deleteLast();
            } else if (Character.isDigit(c)) {
                input.add(c);
            }
        }


    }

    /**
     * Get the tax menu's area as a rectangle
     * This is important for click event exceptions
     *
     * @return an arraylist of rectangles
     */
    @Override
    public @NotNull ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        if (this.getIsOpen()) {
            areas.add(taxMenuArea);
        }
        return areas;
    }
}
