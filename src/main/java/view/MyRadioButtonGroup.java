package view;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implementation of a radio button group
 */
public class MyRadioButtonGroup {

    private ArrayList<MyRadioButton> buttons;

    /**
     * Constructor of the radiobutton
     */
    public MyRadioButtonGroup() {
        buttons = new ArrayList<>();
    }

    /**
     * Add a new button to the buttongroup
     * @param btn is the new button
     */
    public void add(MyRadioButton btn) {
        buttons.add(btn);
    }

    /**
     * Checks if any button is hovered
     * @param p is the current cursor loaction
     * @return 
     */
    public Boolean isAnyBtnHovered(Point p) {
        if (p != null) {
            for (int i = 0; i < buttons.size(); ++i) {
                if (buttons.get(i).isHovered(p)) {
                    return true;
                }
            }
            return false;
        } else return false;
    }

    /**
     * Upon a click event, select one button from the group
     * @param p is the current cursor loaction
     */
    public void select(Point p) {
        for (int i = 0; i < buttons.size(); ++i) {
            buttons.get(i).setSelected(false);
            if (buttons.get(i).isHovered(p)) {
                buttons.get(i).setSelected(true);
            }
        }
    }

    /**
     * Checks if there is already a selected button
     * @return a Boolean value
     */
    public Boolean hasSelected() {
        for (int i = 0; i < buttons.size(); ++i) {
            if (buttons.get(i).getSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the currently selected button from the group
     */
    public void removeSelectedBtn() {
        for (int i = 0; i < buttons.size(); ++i) {
            if (buttons.get(i).getSelected()) {
                buttons.remove(i);
            }
        }
        for (int i = 0; i < buttons.size(); ++i) {
            buttons.get(i).setY(80 + i * 60);
        }
    }

    /**
     * draw the button group on the screen
     * @param gr is the game's main Panel object
     * @param cursorPos is the graphics context of the main Panel object
     */
    public void draw(Graphics2D gr, Point cursorPos) {
        for (int i = 0; i < buttons.size(); ++i) {
            buttons.get(i).draw(gr, cursorPos);
        }
    }

}