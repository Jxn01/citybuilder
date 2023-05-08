package model.enums;

/**
 * This class represents the various effects that can effect a resident
 */
public enum Effect {
    FOREST(10),
    PUBLIC_SAFETY(10),
    POLICE_STATION(10),
    STADIUM(10),
    BAD_BUDGET(-10),
    BAD_WORKPLACE_DIST(-10),
    WORK_DISTANCE(-10),
    INDUSTRIAL_NEARBY(-10),
    HOMELESS(-10),
    UNEMPLOYED(-10),
    ON_FIRE(-10);

    private final int value;

    Effect(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
