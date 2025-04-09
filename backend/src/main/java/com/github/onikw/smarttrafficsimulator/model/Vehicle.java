package com.github.onikw.smarttrafficsimulator.model;

import com.github.onikw.smarttrafficsimulator.model.Turn;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Vehicle {
    private String vehicleId;
    private Position startRoad;
    private Position endRoad;
    private final Turn turn;


    public Vehicle(String vehicleId, String startingPosition, String endingPosition){
        this.vehicleId = vehicleId;
        this.startRoad = Position.stringToEnum(startingPosition);
        this.endRoad = Position.stringToEnum(endingPosition);
        this.turn = Turn.getTurn(this.startRoad, this.endRoad);

    }

}