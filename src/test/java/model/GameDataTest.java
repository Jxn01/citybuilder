package model;

import com.google.common.graph.MutableGraph;
import model.buildings.playerbuilt.Road;
import model.field.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    GameData gameData;

    @BeforeEach
    void setUp() {
        this.gameData = new GameData("UNKNOW", 1,1,1,1);
    }

    @Test
    void init_a_GameData() {
        this.gameData.setCurrentDate("2021.01.10");
        this.gameData.setInGameStartDate("2021.01.11");
        this.gameData.setInGameCurrentDate("2021.01.12");
        this.gameData.setInGameCurrentDate("120");
        this.gameData.setCityName("Louisville");
        this.gameData.setGameOver(false);
        this.gameData.setYearlyTaxes(210);
        Field[][] fields = new Field[10][10];
        this.gameData.setFields(fields);
        this.gameData.setGraph(null);
        this.gameData.setPlayTime("1");
        this.gameData.setPeople(new ArrayList<>());
        this.gameData.setId("1");
        this.gameData.setBudget(1);
        this.gameData.addToBudget(1);


        assertDoesNotThrow(() -> this.gameData.getBudget());
        assertDoesNotThrow(() -> this.gameData.subtractFromBudget(1));
        assertDoesNotThrow(() -> this.gameData.getPopulation());
    }

    @Test
    void testHashCode() {
        assertDoesNotThrow(() -> this.gameData.hashCode()>0);
    }
}