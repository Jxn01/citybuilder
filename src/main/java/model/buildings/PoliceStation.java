package model.buildings;

public class PoliceStation extends RangedBuilding{

    @Override
    public String getStatistics() {
        System.out.println("Get statistics");
        return "1";
    }

    @Override
    public void setTexture() {
        System.out.println("Set texture");
    }
}
