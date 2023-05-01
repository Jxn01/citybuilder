package model;

import model.buildings.generated.IndustrialWorkplace;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.ServiceWorkplace;
import model.enums.Effect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    Person person;

    @BeforeEach
    void setUp() {
        this.person = new Person(false);
    }

    @Test
    void calculateDeceaseChanceWhenYoungerThan65Test() {
        final double expected = 0.0;
        person.setAge(12);
        final double actual = person.calculateDeceaseChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateDeceaseChanceWhenYoungerThan70Test() {
        final double expected = 0.2;
        person.setAge(65);
        final double actual = person.calculateDeceaseChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateDeceaseChanceWhenYoungerThan80Test() {
        final double expected = 0.5;
        person.setAge(70);
        final double actual = person.calculateDeceaseChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateDeceaseChanceWhenYoungerThan90Test() {
        final double expected = 0.8;
        person.setAge(80);
        final double actual = person.calculateDeceaseChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateDeceaseChanceWhenOlderThan90Test() {
        final double expected = 1.0;
        person.setAge(90);
        final double actual = person.calculateDeceaseChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenFounderTest() {
        final double expected = 0.0;
        person.setFounder(true);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenAllBadEffectsTest() {
        final double expected = 1.0;
        person.setName("Test");
        person.addEffect(Effect.WORK_DISTANCE);
        person.addEffect(Effect.BAD_BUDGET);
        person.addEffect(Effect.BAD_WORKPLACE_DIST);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenTwoBadEffectsTest() {
        final double expected = 0.5;
        person.addEffect(Effect.WORK_DISTANCE);
        person.addEffect(Effect.BAD_BUDGET);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenOneBadEffectTest() {
        final double expected = 0.5;
        person.addEffect(Effect.WORK_DISTANCE);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenNoEffectsTest() {
        final double expected = 0.2;
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenOneGoodEffectTest() {
        final double expected = 0.1;
        person.addEffect(Effect.POLICE_STATION);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenTwoGoodEffectsTest() {
        final double expected = 0.1;
        person.addEffect(Effect.POLICE_STATION);
        person.addEffect(Effect.FOREST);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateMoveAwayChanceWhenThreeGoodEffectsTest() {
        final double expected = 0.0;
        person.addEffect(Effect.POLICE_STATION);
        person.addEffect(Effect.FOREST);
        person.addEffect(Effect.PUBLIC_SAFETY);
        final double actual = person.calculateMoveAwayChance();
        assertEquals(expected, actual);
    }

    @Test
    void calculateDistanceToWorkTest() {
        final int expected = 0;
        final double actual = person.calculateDistanceToWork();
        assertEquals(expected, actual);
    }

    @Test
    void addEffectTest() {
        //todo
    }

    @Test
    void removeEffectTest() {
        //todo
    }

    @Test
    void isRetiredTest() {
        this.person.setAge(70);
        assertTrue(person.isRetired());
    }

    @Test
    void isNotRetiredTest() {
        this.person.setAge(60);
        assertFalse(person.isRetired());
    }

    @Test
    void deceaseTest() {
        this.person.moveAway();
        this.person.decease();
        assertEquals(this.person.getName(), "Deceased");
    }

    @Test
    void moveAwayTest() {
        this.person.moveAway();
        assertEquals(this.person.getName(), "Moved away");
    }

    @Test
    void setAndGetAgeTest() {
        this.person.setAge(12);
        assertEquals(this.person.getAge(), 12);
    }

    @Test
    void setAndGetNameTest() {
        this.person.setName("Test");
        assertEquals(this.person.getName(), "Test");
    }

    @Test
    void setAndGetEffectsTest() {
        Set<Effect> effects = new TreeSet<>();
        effects.add(Effect.POLICE_STATION);
        effects.add(Effect.FOREST);
        this.person.setEffects(effects);
        assertEquals(this.person.getEffects(), effects);
    }

    @Test
    void setAndGetHomeTest() {
        ResidentialBuilding b = new ResidentialBuilding(new Coordinate(0, 0));
        this.person.setHome(b);
        assertEquals(this.person.getHome(), b);
    }

    @Test
    void setAndGetWorkplaceIndustrialTest() {
        IndustrialWorkplace b = new IndustrialWorkplace(new Coordinate(0, 0));
        this.person.setWorkplace(b);
        assertEquals(this.person.getWorkplace(), b);
    }

    @Test
    void setAndGetWorkplaceServiceTest() {
        ServiceWorkplace b = new ServiceWorkplace(new Coordinate(0, 0));
        this.person.setWorkplace(b);
        assertEquals(this.person.getWorkplace(), b);
    }

    @Test
    void setAndGetFounderTest() {
        this.person.setFounder(true);
        assertTrue(this.person.isFounder());
    }

    @Test
    void equalsHashCodeTest() {
        ResidentialBuilding home = new ResidentialBuilding(new Coordinate(0, 0));
        IndustrialWorkplace workplace = new IndustrialWorkplace(new Coordinate(0, 0));

        Person person1 = new Person(false);
        person1.setName("Test");
        person1.setAge(12);
        person1.setFounder(true);
        person1.addEffect(Effect.POLICE_STATION);
        person1.setHome(home);
        person1.setWorkplace(workplace);

        Person person2 = new Person(false);
        person2.setName("Test");
        person2.setAge(12);
        person2.setFounder(true);
        person2.addEffect(Effect.POLICE_STATION);
        person2.setHome(home);
        person2.setWorkplace(workplace);

        assertEquals(person1, person2);
        assertEquals(person1.hashCode(), person2.hashCode());
    }

}