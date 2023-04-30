package model;

import model.enums.Effect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    Person person;

    @BeforeEach
    void setUp() {
        this.person = new Person(false);
    }

    @Test
    void call_calculateDeceaseChance_when_younger_sixtyfive() {
        final double expected = 0.0;
        person.setAge(12);
        final double actual = person.calculateDeceaseChance();

        assertEquals(expected,actual);
    }

    @Test
    void call_calculateDeceaseChance_when_younger_seventy() {
        final double expected = 0.2;
        person.setAge(65);
        final double actual = person.calculateDeceaseChance();

        assertEquals(expected,actual);
    }

    @Test
    void call_calculateDeceaseChance_when_younger_eigthty() {
        final double expected = 0.5;
        person.setAge(70);
        final double actual = person.calculateDeceaseChance();

        assertEquals(expected,actual);
    }

    @Test
    void call_calculateDeceaseChance_when_younger_ninety() {
        final double expected = 0.8;
        person.setAge(80);
        final double actual = person.calculateDeceaseChance();

        assertEquals(expected,actual);
    }

    @Test
    void call_calculateDeceaseChance_when_older_ninety() {
        final double expected = 1.0;
        person.setAge(90);
        final double actual = person.calculateDeceaseChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateMoveAwayChance_zero() {
        final double expected = 0.0;
        person.setFounder(true);
        final double actual = person.calculateMoveAwayChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateMoveAwayChance_one() {
        final double expected = 1.0;
        person.setName("SOLIDER");
        person.setFounder(false);
        final ArrayList<Effect> effects = new ArrayList<>();
        effects.add(Effect.WORK_DISTANCE);
        effects.add(Effect.WORK_DISTANCE);
        effects.add(Effect.WORK_DISTANCE);
        effects.add(Effect.WORK_DISTANCE);
        effects.add(Effect.WORK_DISTANCE);
        person.setEffects(effects);
        final double actual = person.calculateMoveAwayChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateMoveAwayChance_zeroPoundFive() {
        final double expected = 0.5;
        person.setFounder(false);

        person.addEffect(Effect.WORK_DISTANCE);
        person.addEffect(Effect.WORK_DISTANCE);
        person.addEffect(Effect.WORK_DISTANCE);

        final double actual = person.calculateMoveAwayChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateMoveAwayChance_zeroPoundTwo() {
        final double expected = 0.2;
        person.setFounder(false);
        final ArrayList<Effect> effects = new ArrayList<>();
        effects.add(Effect.WORK_DISTANCE);
        person.addEffect(Effect.WORK_DISTANCE);
        person.removeEffect(Effect.WORK_DISTANCE);
        person.setEffects(effects);
        final double actual = person.calculateMoveAwayChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateMoveAwayChance_zeroPoundOne() {
        final double expected = 0.1;
        person.setFounder(false);
        final ArrayList<Effect> effects = new ArrayList<>();
        person.setEffects(effects);
        final double actual = person.calculateMoveAwayChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateMoveAwayChance_zeroPoundZero() {
        final double expected = 0.0;
        person.setFounder(false);
        final ArrayList<Effect> effects = new ArrayList<>();
        effects.add(Effect.POLICE_STATION);
        effects.add(Effect.POLICE_STATION);
        person.setEffects(effects);
        final double actual = person.calculateMoveAwayChance();

        assertEquals(expected,actual);
    }

    @Test
    void calculateDistanceToWork() {
        final int expected = 0;
        final double actual = person.calculateDistanceToWork();
        assertEquals(expected,actual);
    }


    @Test
    void addEffect() {
    }

    @Test
    void removeEffect() {
    }

    @Test
    void isRetired() {
        this.person.setAge(70);
        assertTrue(person.isRetired());
    }

    @Test
    void isNotRetired() {
        this.person.setAge(60);
        assertFalse(person.isRetired());
    }

    @Test
    void decease() {
        this.person.moveAway();
        this.person.decease();
        assertEquals(this.person.getName() ,"Deceased");
    }

    @Test
    void moveAway() {
    }

    @Test
    void getAge() {
    }

    @Test
    void setAge() {
    }

    @Test
    void getName() {
    }

    @Test
    void setName() {
    }

    @Test
    void getEffects() {
    }

    @Test
    void setEffects() {
    }

    @Test
    void getHome() {
    }

    @Test
    void setHome() {
    }

    @Test
    void getWorkplace() {
    }

    @Test
    void setWorkplace() {
    }

    @Test
    void isFounder() {
    }

    @Test
    void setFounder() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}