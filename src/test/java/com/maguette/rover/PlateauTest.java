package com.maguette.rover;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlateauTest {

    private final Plateau plateau = new Plateau(5, 5);

    @ParameterizedTest
    @CsvSource({"0, 0", "5, 5", "0, 5", "5, 0", "2, 3"})
    void contains_positions_within_inclusive_bounds(int x, int y) {
        assertTrue(plateau.contains(new Position(x, y)));
    }

    @ParameterizedTest
    @CsvSource({"-1, 0", "0, -1", "6, 0", "0, 6"})
    void does_not_contain_positions_outside_bounds(int x, int y) {
        assertFalse(plateau.contains(new Position(x, y)));
    }

    @Test
    void a_single_cell_plateau_is_valid() {
        assertTrue(new Plateau(0, 0).contains(new Position(0, 0)));
    }

    @Test
    void negative_limits_are_rejected() {
        assertThrows(RoverException.class, () -> new Plateau(-1, 5));
        assertThrows(RoverException.class, () -> new Plateau(5, -1));
    }
}
