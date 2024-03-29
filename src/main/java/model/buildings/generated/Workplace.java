package model.buildings.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Coordinate;
import model.Person;

import java.util.ArrayList;

/**
 * This class represents a workplace that is generated by the game.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IndustrialWorkplace.class, name = "industrial"),
        @JsonSubTypes.Type(value = ServiceWorkplace.class, name = "service")
})
public abstract class Workplace extends GeneratedBuilding {
    /**
     * Constructor of the workplace
     *
     * @param coords          is the coordinates of the workplace
     * @param firePossibility is the fire possibility of the workplace
     * @param isOnFire        is the workplace on fire
     * @param people          is the people in the workplace
     * @param publicSafety    is the public safety of the workplace
     */
    @JsonCreator
    public Workplace(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("people") ArrayList<Person> people, @JsonProperty("publicSafety") int publicSafety) {
        super(coords, firePossibility, isOnFire, people, publicSafety);
    }
}
