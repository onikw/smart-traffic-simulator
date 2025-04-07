package com.github.onikw.smarttrafficsimulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Vehicle {
    private String vehicleId;
    private String startRoad;
    private String endRoad;


}