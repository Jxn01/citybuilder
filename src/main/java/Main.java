import util.Logger;
import view.components.Frame;

public class Main {
    public static void main(String[] args) {
        Logger.log("Starting the game...");
        Frame game = new Frame();
        game.init();
    }
}