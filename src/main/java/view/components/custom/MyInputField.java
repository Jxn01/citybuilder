package view.components.custom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * This class represents an input field on the gui
 */
public class MyInputField {
    private final int MAXLENGTH;
    public Rectangle rect;
    private String text;

    /**
     * Constructor of the input class
     *
     * @param x      is the starting horizontal coordinate of the input field
     * @param y      is the starting vertical coordinate of the input field
     * @param width  is the input field's width
     * @param height is the input field's height
     */
    public MyInputField(int x, int y, int width, int height) {
        MAXLENGTH = 12;
        rect = new Rectangle(x, y, width, height);
        text = "";
    }

    /**
     * Checks if the input field is hovered
     *
     * @param p is the current cursor location
     * @return a boolean value
     */
    public boolean isHovered(@Nullable Point p) {
        if (p != null) {
            return rect.contains(p);
        } else {
            return false;
        }
    }

    /**
     * Adds a new character to the current content of the input field
     *
     * @param c is the new character
     */
    public void add(char c) {
        if (text.length() < MAXLENGTH) {
            text += c;
        }
    }

    /**
     * Deletes the last character from the current content of the input field
     */
    public void deleteLast() {
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
    }

    /**
     * Get the current text that the input field holds
     *
     * @return the current text
     */
    public String getText() {
        return text;
    }

    /**
     * Draw the input field on the screen
     *
     * @param gr is the graphics context of the main Panel object
     * @param p  is the current cursor position
     */
    public void draw(@NotNull Graphics2D gr, Point p) {
        gr.setColor(Color.white);
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);
        gr.setColor(Color.black);
        gr.drawRect(rect.x, rect.y, rect.width, rect.height);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));

        //make the input line "blink"
        long timestamp = System.currentTimeMillis() / 1000;
        if (timestamp % 2 == 0) {
            gr.drawString(text + "|", rect.x, rect.y + rect.height - 10);
        } else {
            gr.drawString(text, rect.x, rect.y + rect.height - 10);
        }
    }

    /**
     * Set the x coordinate of the input field
     *
     * @param x is the new x coordinate
     */
    public void setX(int x) {
        rect.x = x;
    }

    /**
     * Set the y coordinate of the input field
     *
     * @param y is the new y coordinate
     */
    public void setY(int y) {
        rect.y = y;
    }
}
