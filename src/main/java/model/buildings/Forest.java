package model.buildings;

public class Forest extends RangedBuilding{
    private int growTime = 10;
    private int growStage = 0;

    public void grow(){
        System.out.println("The tree is growing.");
    };
}
