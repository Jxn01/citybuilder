package model.buildings;

public abstract class Building {
    protected Image texture;
    protected Coordinate coords;
    protected double firePossibility;
    protected boolean isOnFire;

    // return value will be modified 
    protected abstract String getStatistics();

    // parameterlist will be modified
    protected abstract void setTexture();
}
