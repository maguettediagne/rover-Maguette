package com.maguette.rover;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    @ParameterizedTest
    @CsvSource({
            "N, 2, 3",
            "E, 3, 2",
            "S, 2, 1",
            "W, 1, 2"
    })
    void next_moves_one_cell_in_the_given_direction(Direction direction, int expectedX, int expectedY) {
        Position start = new Position(2, 2);

        assertEquals(new Position(expectedX, expectedY), start.next(direction));
    }
}
