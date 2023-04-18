package model.buildings;

import model.Coordinate;
import util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

public class ResidentialBuilding extends GeneratedBuilding {

    /**
     * Constructor of the residential building
     * @param coords is the coordinates of the residential building
     */
    public ResidentialBuilding(Coordinate coords) {
        super(null, coords, 0.0, false, null, null, 0);

        try{
            texture = ResourceLoader.loadImage("livingZone.png");
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        firePossibility = 0.1;
        people = new ArrayList<>();
        saturationRate = SaturationRate.EMPTY;
        publicSafety = 100;
    }

    @Override
    public String getStatistics() {
        String statistics = "Residential building statistics:\n";
        statistics += "Number of people living here: " + people.size() + "\n";
        statistics += "Public safety: " + publicSafety + "\n";
        statistics += "Saturation rate: " + saturationRate + "\n";
        System.out.println(statistics);
        return statistics;
    }

    @Override
    public void updateTexture(String textureName) {
        try{
            texture = ResourceLoader.loadImage(textureName);
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }
    }
}
