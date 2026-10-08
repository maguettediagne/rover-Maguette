package com.maguette.rover;

/**
 * Plateau rectangulaire de (0,0) à (xMax,yMax), bornes incluses.
 */
public record Plateau(int xMax, int yMax) {

    public Plateau {
        if (xMax < 0 || yMax < 0) {
            throw new RoverException(
                    "Les limites du plateau doivent être positives ou nulles : %d %d.".formatted(xMax, yMax));
        }
    }

    public boolean contains(Position position) {
        return position.x() >= 0 && position.x() <= xMax
                && position.y() >= 0 && position.y() <= yMax;
    }
}
