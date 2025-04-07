package com.github.onikw.smarttrafficsimulator.model;

import lombok.Data;

@Data
public class SimulationCommand {
    private String type;
    private String vehicleId;
    private String startRoad;
    private String endRoad;
}