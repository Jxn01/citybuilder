package view.components.custom;

import java.awt.*;

/**
 * Implementation of one radio button
 */
public class MyRadioButton {
    private final Rectangle rect;
    private boolean selected;
    public String name;
    public String time;
    public String funds;
    
    /**
     * Constructor of one radio button on the SavedGame submenu
     * @param x is the button's x coordinate on the screen
     * @param y is the button's y coordinate on the screen
     * @param width is the width of the button
     * @param height is the height of the button
     * @param n is the saved game's name, held by the button
     * @param t is the saved game's time, held by the button
     * @param f is the saved game's funds, held by the button
     */
    public MyRadioButton(int x, int y, int width, int height, String n, String t, String f) {
        rect = new Rectangle(x, y, width, height);
        name = n;
        time = t;
        funds = f;
        selected = false;
    }

    /**
     * Getter for selected
     * @return selected
     */
    public boolean getSelected() {
        return selected;
    }

    /**
     * Setter for selected
     * @param selected is the new value for selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /**
     * Checks if the button is currently hovered
     * @param p is the cursor position
     * @return a boolean value
     */
    public boolean isHovered(Point p) {
        if (p != null) {
            return rect.contains(p);
        } else return false;
    }

    /**
     * draw the radiobutton on the screen
     * @param gr is the graphics context of the game panel
     * @param cursorPos is the current cursor position
     */
    public void draw(Graphics2D gr, Point cursorPos) {
        //fill the button with color 
        if(selected) {
            gr.setColor(Color.green);
        } else if(isHovered(cursorPos)) {
            gr.setColor(Color.pink);
        } else {
            gr.setColor(Color.white);
        }
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);

        //draw the button's border
        gr.setColor(Color.DARK_GRAY);
        gr.drawRect(rect.x, rect.y, rect.width, rect.height);

        //write the button's text
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString(name + " ; " + time + " ; " + funds, rect.x + 20, rect.y + (rect.height / 2) + 10);
    }

    /**
     * Getter for the button's width
     * @param width is the new width of the button
     */
    public void setWidth(int width){
        rect.width = width;
    }

    /**
     * Setter for the button's x coordinate
     * @param x is the new x coordinate of the button
     */
    public void setX(int x){
        rect.x = x;
    }

    /**
     * This function allows the buttongroup to adjust
     * how high the button is when the user
     * deletes another radiobutton from the buttongroup
     * @param y is the new y coordinate on the screen
     */
    public void setY(int y) {
        rect.y = y;
    }
}
