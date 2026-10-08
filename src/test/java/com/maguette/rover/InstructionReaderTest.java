package com.maguette.rover;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstructionReaderTest {

    private final InstructionReader reader = new InstructionReader();
    private final Plateau plateau = new Plateau(5, 5);

    @TempDir
    Path tempDir;

    @Test
    void readLines_ignores_blank_lines_and_surrounding_spaces() throws IOException {
        Path file = Files.writeString(tempDir.resolve("input.txt"), "\n  5 5 \n\n1 2 N\nM\n\n");

        assertEquals(List.of("5 5", "1 2 N", "M"), reader.readLines(file.toString()));
    }

    @Test
    void readLines_rejects_an_empty_file() throws IOException {
        Path file = Files.writeString(tempDir.resolve("input.txt"), "\n\n");

        RoverException exception = assertThrows(RoverException.class, () -> reader.readLines(file.toString()));
        assertEquals("Le fichier est vide.", exception.getMessage());
    }

    @Test
    void readLines_rejects_a_rover_without_its_command_line() throws IOException {
        Path file = Files.writeString(tempDir.resolve("input.txt"), "5 5\n1 2 N\n");

        assertThrows(RoverException.class, () -> reader.readLines(file.toString()));
    }

    @Test
    void readLines_reports_a_missing_file() {
        String missing = tempDir.resolve("absent.txt").toString();

        RoverException exception = assertThrows(RoverException.class, () -> reader.readLines(missing));
        assertEquals("Fichier introuvable : " + missing, exception.getMessage());
    }

    @Test
    void readLines_reports_an_invalid_path() {
        String invalidPath = "input\u0000.txt";

        RoverException exception = assertThrows(RoverException.class, () -> reader.readLines(invalidPath));
        assertEquals("Chemin de fichier invalide : " + invalidPath, exception.getMessage());
    }

    @Test
    void readLines_reports_an_unreadable_file() {
        assertThrows(RoverException.class, () -> reader.readLines(tempDir.toString()));
    }

    @Test
    void parsePlateau_reads_the_limits() {
        assertEquals(new Plateau(5, 5), reader.parsePlateau("5 5"));
    }

    @Test
    void parsePlateau_rejects_a_wrong_number_of_values() {
        RoverException exception = assertThrows(RoverException.class, () -> reader.parsePlateau("5"));
        assertEquals("Ligne invalide : \"5\". Format attendu : \"xMax yMax\".", exception.getMessage());
    }

    @Test
    void parsePlateau_rejects_a_value_that_is_not_a_number() {
        RoverException exception = assertThrows(RoverException.class, () -> reader.parsePlateau("5 x"));
        assertEquals("Nombre invalide \"x\" dans \"5 x\".", exception.getMessage());
    }

    @Test
    void parseRover_reads_the_position_and_the_direction() {
        Rover rover = reader.parseRover("1 2 N", plateau);

        assertEquals(new Position(1, 2), rover.position());
        assertEquals(Direction.N, rover.direction());
    }

    @Test
    void parseRover_rejects_a_wrong_number_of_values() {
        assertThrows(RoverException.class, () -> reader.parseRover("1 2", plateau));
    }

    @Test
    void parseRover_rejects_an_unknown_direction() {
        RoverException exception = assertThrows(RoverException.class, () -> reader.parseRover("1 2 X", plateau));
        assertEquals("Direction inconnue \"X\" dans \"1 2 X\". Valeurs possibles : N, E, S, W.", exception.getMessage());
    }

    @Test
    void parseRover_rejects_a_start_outside_the_plateau() {
        assertThrows(RoverException.class, () -> reader.parseRover("9 9 N", plateau));
    }

    @Test
    void parseCommands_reads_each_letter() {
        assertEquals(List.of(Command.L, Command.M, Command.R), reader.parseCommands("LMR"));
    }

    @Test
    void parseCommands_rejects_an_unknown_command() {
        RoverException exception = assertThrows(RoverException.class, () -> reader.parseCommands("MXM"));
        assertEquals("Commande inconnue \"X\" dans \"MXM\". Valeurs possibles : L, R, M.", exception.getMessage());
    }
}
