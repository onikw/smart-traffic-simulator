package com.github.onikw.smarttrafficsimulator.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimulationStepResult {
    private List<String> leftVehicles = new ArrayList<>();

    public void setNextVehicle(String leftVehicle)
    {
        this.leftVehicles.add(leftVehicle);
    }
}