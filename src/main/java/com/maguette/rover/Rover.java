package com.maguette.rover;

public class Rover {

    private final Plateau plateau;
    private Position position;
    private Direction direction;

    public Rover(Plateau plateau, Position position, Direction direction) {
        if (!plateau.contains(position)) {
            throw new RoverException("La position de départ %d %d est hors du plateau."
                    .formatted(position.x(), position.y()));
        }
        this.plateau = plateau;
        this.position = position;
        this.direction = direction;
    }

    public void execute(Command command) {
        switch (command) {
            case L -> direction = direction.left();
            case R -> direction = direction.right();
            case M -> moveForward();
        }
    }

    // La case suivante est vérifiée avant le déplacement : un mouvement refusé laisse le rover à sa place.
    private void moveForward() {
        Position next = position.next(direction);
        if (!plateau.contains(next)) {
            throw new RoverException("Mouvement refusé : le rover en %d %d %s sortirait du plateau en %d %d."
                    .formatted(position.x(), position.y(), direction, next.x(), next.y()));
        }
        position = next;
    }

    public Position position() {
        return position;
    }

    public Direction direction() {
        return direction;
    }
}
