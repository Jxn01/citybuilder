package model.buildings;

import model.Coordinate;
import util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

public class ServiceWorkplace extends Workplace {

    /**
     * Constructor of the service workplace
     * @param coords is the coordinates of the service workplace
     */
    public ServiceWorkplace(Coordinate coords) {
        super(null, coords, 0.0, false, null, null, 0);

        try{
            texture = ResourceLoader.loadImage("serviceZone.png");
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        people = new ArrayList<>();
        saturationRate = SaturationRate.EMPTY;
        publicSafety = 100;
    }

    @Override
    public String getStatistics() {
        String statistics = "Industrial workplace statistics:\n";
        statistics += "Number of people working here: " + people.size() + "\n";
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
