package com.maguette.rover;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoverTest {

    private final Plateau plateau = new Plateau(5, 5);

    @Test
    void L_turns_left_without_moving() {
        Rover rover = new Rover(plateau, new Position(1, 2), Direction.N);

        rover.execute(Command.L);

        assertEquals(new Position(1, 2), rover.position());
        assertEquals(Direction.W, rover.direction());
    }

    @Test
    void R_turns_right_without_moving() {
        Rover rover = new Rover(plateau, new Position(1, 2), Direction.N);

        rover.execute(Command.R);

        assertEquals(new Position(1, 2), rover.position());
        assertEquals(Direction.E, rover.direction());
    }

    @Test
    void M_moves_one_cell_forward_and_keeps_the_direction() {
        Rover rover = new Rover(plateau, new Position(1, 2), Direction.N);

        rover.execute(Command.M);

        assertEquals(new Position(1, 3), rover.position());
        assertEquals(Direction.N, rover.direction());
    }

    @Test
    void M_can_reach_the_edge_of_the_plateau() {
        Rover rover = new Rover(plateau, new Position(5, 4), Direction.N);

        rover.execute(Command.M);

        assertEquals(new Position(5, 5), rover.position());
    }

    @Test
    void M_out_of_the_plateau_is_refused_and_the_rover_keeps_its_position() {
        Rover rover = new Rover(plateau, new Position(5, 5), Direction.N);

        RoverException exception = assertThrows(RoverException.class, () -> rover.execute(Command.M));

        assertEquals(new Position(5, 5), rover.position());
        assertEquals(Direction.N, rover.direction());
        assertEquals("Mouvement refusé : le rover en 5 5 N sortirait du plateau en 5 6.", exception.getMessage());
    }

    @Test
    void M_out_of_the_plateau_is_refused_in_every_direction() {
        Rover southWest = new Rover(plateau, new Position(0, 0), Direction.S);
        assertThrows(RoverException.class, () -> southWest.execute(Command.M));

        Rover west = new Rover(plateau, new Position(0, 0), Direction.W);
        assertThrows(RoverException.class, () -> west.execute(Command.M));

        Rover east = new Rover(plateau, new Position(5, 5), Direction.E);
        assertThrows(RoverException.class, () -> east.execute(Command.M));
    }

    @Test
    void a_rover_cannot_start_outside_the_plateau() {
        RoverException exception = assertThrows(RoverException.class,
                () -> new Rover(plateau, new Position(6, 0), Direction.N));

        assertEquals("La position de départ 6 0 est hors du plateau.", exception.getMessage());
    }
}
