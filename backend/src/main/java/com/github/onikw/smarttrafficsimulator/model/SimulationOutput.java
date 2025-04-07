package com.github.onikw.smarttrafficsimulator.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimulationOutput {
    private List<SimulationStepResult> simulationResults= new ArrayList<>();


    public void setNextSimulationStep(SimulationStepResult simulationStepResult)
    {
        simulationResults.add(simulationStepResult);

    }


}