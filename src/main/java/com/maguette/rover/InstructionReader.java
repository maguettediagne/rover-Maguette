package com.maguette.rover;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class InstructionReader {

    /**
     * Renvoie les lignes utiles du fichier. Les lignes vides sont ignorées.
     * Un fichier valide a toujours un nombre impair de lignes : le plateau, puis deux lignes par rover.
     */
    public List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        for (String line : readFile(fileName)) {
            if (!line.isBlank()) {
                lines.add(line.strip());
            }
        }
        if (lines.isEmpty()) {
            throw new RoverException("Le fichier est vide.");
        }
        if (lines.size() % 2 == 0) {
            throw new RoverException("Chaque rover doit avoir une ligne de position suivie d'une ligne de commandes.");
        }
        return lines;
    }

    public Plateau parsePlateau(String line) {
        String[] parts = split(line, 2, "xMax yMax");
        return new Plateau(parseNumber(parts[0], line), parseNumber(parts[1], line));
    }

    public Rover parseRover(String line, Plateau plateau) {
        String[] parts = split(line, 3, "x y direction");
        Position position = new Position(parseNumber(parts[0], line), parseNumber(parts[1], line));
        return new Rover(plateau, position, parseDirection(parts[2], line));
    }

    public List<Command> parseCommands(String line) {
        List<Command> commands = new ArrayList<>();
        for (char letter : line.toCharArray()) {
            commands.add(parseCommand(letter, line));
        }
        return commands;
    }

    private List<String> readFile(String fileName) {
        try {
            return Files.readAllLines(Path.of(fileName));
        } catch (InvalidPathException e) {
            throw new RoverException("Chemin de fichier invalide : " + fileName, e);
        } catch (NoSuchFileException e) {
            throw new RoverException("Fichier introuvable : " + fileName, e);
        } catch (IOException e) {
            throw new RoverException("Impossible de lire le fichier : " + fileName, e);
        }
    }

    private String[] split(String line, int expectedCount, String expectedFormat) {
        String[] parts = line.split("\\s+");
        if (parts.length != expectedCount) {
            throw new RoverException("Ligne invalide : \"%s\". Format attendu : \"%s\".".formatted(line, expectedFormat));
        }
        return parts;
    }

    private int parseNumber(String value, String line) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RoverException("Nombre invalide \"%s\" dans \"%s\".".formatted(value, line));
        }
    }

    private Direction parseDirection(String value, String line) {
        return switch (value) {
            case "N" -> Direction.N;
            case "E" -> Direction.E;
            case "S" -> Direction.S;
            case "W" -> Direction.W;
            default -> throw new RoverException(
                    "Direction inconnue \"%s\" dans \"%s\". Valeurs possibles : N, E, S, W.".formatted(value, line));
        };
    }

    private Command parseCommand(char letter, String line) {
        return switch (letter) {
            case 'L' -> Command.L;
            case 'R' -> Command.R;
            case 'M' -> Command.M;
            default -> throw new RoverException(
                    "Commande inconnue \"%s\" dans \"%s\". Valeurs possibles : L, R, M.".formatted(letter, line));
        };
    }
}
