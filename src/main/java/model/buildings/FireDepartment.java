package model.buildings;

public class FireDepartment extends RangedBuilding{
    private int maxFireTrucks;
    private int avaibleFireTrucks;

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
