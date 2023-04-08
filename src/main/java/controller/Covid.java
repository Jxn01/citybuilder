package controller;

public class Covid extends Catastrophy {
    private static Covid instance = null;

    private Covid() {}

    public static Covid getInstance() {
        if (instance == null) {
            instance = new Covid();
        }
        return instance;
    }

    @Override
    protected void effect() {

    }
}
