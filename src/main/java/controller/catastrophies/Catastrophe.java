package controller.catastrophies;

import model.GameData;
import view.gui.Game;

/**
 * This class represents a catastrophe.
 */
public abstract class Catastrophe {
    /**
     * Effect of the catastrophe.
     */
    public abstract void effect(GameData gameData);
}
