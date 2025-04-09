package com.github.onikw.smarttrafficsimulator.model;

public enum Position {
    N,E,S,W;

    public static Position stringToEnum(String direction){
        return switch (direction){
            case "north" -> N;
            case "south" -> S;
            case "west" -> W;
            case "east" -> E;
            default -> throw new IllegalArgumentException("No such argument");
        };
    }

    public static String enumToString(Position position){
        return switch (position){
            case N -> "N";
            case S -> "S";
            case E -> "E";
            case W -> "W";

        };
    }


    public Position getNext() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
        };
    }

    public Position getPrev() {
        return switch (this) {
            case N -> W;
            case W -> S;
            case S -> E;
            case E -> N;
        };
    }

    public Position getOpposite(){
        return switch (this) {
            case N -> S;
            case W -> E;
            case S -> N;
            case E -> W;
        };
    }








}
