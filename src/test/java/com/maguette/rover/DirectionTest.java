package com.maguette.rover;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @ParameterizedTest
    @CsvSource({"N, W", "W, S", "S, E", "E, N"})
    void left_turns_90_degrees_counter_clockwise(Direction from, Direction expected) {
        assertEquals(expected, from.left());
    }

    @ParameterizedTest
    @CsvSource({"N, E", "E, S", "S, W", "W, N"})
    void right_turns_90_degrees_clockwise(Direction from, Direction expected) {
        assertEquals(expected, from.right());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void four_turns_in_the_same_way_return_to_the_start(Direction start) {
        assertEquals(start, start.left().left().left().left());
        assertEquals(start, start.right().right().right().right());
    }

    @Test
    void north_increases_y_and_east_increases_x() {
        assertEquals(0, Direction.N.dx());
        assertEquals(1, Direction.N.dy());
        assertEquals(1, Direction.E.dx());
        assertEquals(0, Direction.E.dy());
    }
}
