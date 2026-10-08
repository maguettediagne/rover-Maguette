package com.maguette.rover;

public record Position(int x, int y) {

    public Position next(Direction direction) {
        return new Position(x + direction.dx(), y + direction.dy());
    }
}
