package com.github.onikw.smarttrafficsimulator.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimulationStepOutput {

    private List<String> leftVehicles = new ArrayList<>();

}
