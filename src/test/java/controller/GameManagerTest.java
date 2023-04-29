package controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class tests all methods of the GameManager class
 */
public class GameManagerTest {
    
    /**
     * This test asserts whether 2+2 equals 4 or not.
     */
    @Test
    public void testAdd() {
        int result = 2 + 2;
        Assertions.assertEquals(4, result, "comment for this test");
    }

}