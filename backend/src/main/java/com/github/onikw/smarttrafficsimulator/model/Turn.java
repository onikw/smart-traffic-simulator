package com.github.onikw.smarttrafficsimulator.model;


public enum Turn {
    STRAIGHT,
    RIGHT,
    LEFT;

    public static Turn getTurn(Position start, Position end) {

        if (end == start.getOpposite())
            return STRAIGHT;

        if (start.getNext() == end) {
            return LEFT;
        }
        if (start.getPrev() == end) {
            return RIGHT;
        }

        return null;
    }
}
