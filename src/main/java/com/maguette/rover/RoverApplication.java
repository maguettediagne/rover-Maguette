package com.maguette.rover;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RoverApplication implements CommandLineRunner, ExitCodeGenerator {

    private final InstructionReader reader;
    private int exitCode = 0;

    public RoverApplication(InstructionReader reader) {
        this.reader = reader;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(RoverApplication.class, args)));
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            System.err.println("Usage : java -jar rover.jar <fichier>");
            exitCode = 1;
            return;
        }
        try {
            for (String result : moveRovers(args[0])) {
                System.out.println(result);
            }
        } catch (RoverException e) {
            System.err.println("Erreur : " + e.getMessage());
            exitCode = 1;
        }
    }

    /**
     * Déplace les rovers l'un après l'autre, dans l'ordre du fichier.
     * Les résultats sont renvoyés à la fin : si une erreur survient, aucune position partielle n'est affichée.
     */
    List<String> moveRovers(String fileName) {
        List<String> lines = reader.readLines(fileName);
        Plateau plateau = reader.parsePlateau(lines.getFirst());
        List<String> results = new ArrayList<>();
        for (int i = 1; i < lines.size(); i += 2) {
            Rover rover = reader.parseRover(lines.get(i), plateau);
            for (Command command : reader.parseCommands(lines.get(i + 1))) {
                rover.execute(command);
            }
            results.add(format(rover));
        }
        return results;
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    private static String format(Rover rover) {
        return "%d %d %s".formatted(rover.position().x(), rover.position().y(), rover.direction());
    }
}
