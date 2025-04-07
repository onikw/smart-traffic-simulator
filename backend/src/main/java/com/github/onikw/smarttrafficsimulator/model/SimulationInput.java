package com.github.onikw.smarttrafficsimulator.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SimulationInput {

    @JsonProperty("commands")
    private List<SimulationCommand> simulationInput;
}
