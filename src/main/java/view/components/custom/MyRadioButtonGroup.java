package view.components.custom;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implementation of a radio button group
 */
public class MyRadioButtonGroup {

    private final ArrayList<MyRadioButton> buttons;

    /**
     * Constructor of the radiobutton
     */
    public MyRadioButtonGroup() {
        buttons = new ArrayList<>();
    }

    /**
     * Add a new button to the buttongroup
     *
     * @param btn is the new button
     */
    public void add(MyRadioButton btn) {
        buttons.add(btn);
    }

    /**
     * Checks if any button is hovered
     *
     * @param p is the current cursor location
     * @return a boolean value
     */
    public boolean isAnyBtnHovered(Point p) {
        if (p != null) {
            for (MyRadioButton button : buttons) {
                if (button.isHovered(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Upon a click event, select one button from the group
     *
     * @param p is the current cursor location
     */
    public void select(Point p) {
        for (MyRadioButton button : buttons) {
            button.setSelected(false);
            if (button.isHovered(p)) {
                button.setSelected(true);
            }
        }
    }

    /**
     * Checks if there is already a selected button
     *
     * @return a boolean value
     */
    public boolean hasSelected() {
        for (MyRadioButton button : buttons) {
            if (button.getSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the selected button
     *
     * @return an integer value
     */
    public int getSelectedIndex() {
        for (int i = 0; i < buttons.size(); ++i) {
            if (buttons.get(i).getSelected()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes the currently selected button from the group
     */
    public void removeSelectedBtn() {
        int toRemove = -1;
        for (int i = 0; i < buttons.size(); ++i) {
            if (buttons.get(i).getSelected()) {
                toRemove = i;
                break;
            }
        }
        if (toRemove != -1) {
            buttons.remove(toRemove);
        }
        for (int i = 0; i < buttons.size(); ++i) {
            buttons.get(i).setY(80 + i * 60);
        }
    }

    /**
     * draw the button group on the screen
     *
     * @param gr        is the game's main Panel object
     * @param cursorPos is the graphics context of the main Panel object
     */
    public void draw(Graphics2D gr, Point cursorPos) {
        for (MyRadioButton button : buttons) {
            button.draw(gr, cursorPos);
        }
    }

    public void setWidth(int width) {
        for (MyRadioButton button : buttons) {
            button.setWidth(width);
        }
    }

    public void setX(int x) {
        for (MyRadioButton button : buttons) {
            button.setX(x);
        }
    }

}