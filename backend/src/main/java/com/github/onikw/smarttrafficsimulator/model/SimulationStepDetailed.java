package com.github.onikw.smarttrafficsimulator.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SimulationStepDetailed {
    private String action; // "addVehicle" lub "step"
    private String addedVehicle; // tylko jeśli action = addVehicle
    private List<String> leftVehicles = new ArrayList<>(); // tylko jeśli action = step
    private int vehiclesWaiting; // opcjonalne
    private List<String> vehicleDirections = new ArrayList<>(); // np. vehicle1: from->to

    public void addVehicleDirections(String message)
    {
        vehicleDirections.add(message);
    }

}