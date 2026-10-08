package com.maguette.rover;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoverApplicationTest {

    private final RoverApplication application = new RoverApplication(new InstructionReader());

    @TempDir
    Path tempDir;

    @Test
    void moves_the_rovers_of_the_kata_example_one_after_the_other() throws IOException {
        Path file = write("""
                5 5
                1 2 N
                LMLMLMLMM
                3 3 E
                MMRMMRMRRM
                """);

        assertEquals(List.of("1 3 N", "5 1 E"), application.moveRovers(file.toString()));
    }

    @Test
    void a_move_out_of_the_plateau_stops_the_execution() throws IOException {
        Path file = write("""
                5 5
                1 4 N
                MMM
                """);

        RoverException exception = assertThrows(RoverException.class, () -> application.moveRovers(file.toString()));
        assertEquals("Mouvement refusé : le rover en 1 5 N sortirait du plateau en 1 6.", exception.getMessage());
    }

    @Test
    void exit_code_is_0_when_everything_works() throws IOException {
        Path file = write("5 5\n1 2 N\nM\n");

        application.run(file.toString());

        assertEquals(0, application.getExitCode());
    }

    @Test
    void exit_code_is_1_when_the_file_is_missing() {
        application.run(tempDir.resolve("absent.txt").toString());

        assertEquals(1, application.getExitCode());
    }

    @Test
    void exit_code_is_1_without_argument() {
        application.run();

        assertEquals(1, application.getExitCode());
    }

    private Path write(String content) throws IOException {
        return Files.writeString(tempDir.resolve("input.txt"), content);
    }
}
