package controller;

public class Firestorm extends Catastrophy {
    private static Firestorm instance = null;

    private Firestorm() {}

    public static Firestorm getInstance() {
        if (instance == null) {
            instance = new Firestorm();
        }
        return instance;
    }

    @Override
    protected void effect() {

    }
}
