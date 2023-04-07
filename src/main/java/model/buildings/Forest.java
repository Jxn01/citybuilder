package model.buildings;

public class Forest extends RangedBuilding {
    private int growTime = 10;
    private int growStage = 0;

    public void grow() {
        System.out.println("The tree is growing.");
    }

    @Override
    public String getStatistics() {
        System.out.println("Get statistics");
        return "1";
    }

    @Override
    public void setTexture() {
        System.out.println("Set texture");
    }

    @Override
    public void enableEffect() {

    }

    @Override
    public void disableEffect() {

    }

    @Override
    public void effect() {

    }
}
