package com.github.onikw.smarttrafficsimulator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onikw.smarttrafficsimulator.model.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service

public class SimulationService {

    @Getter
    private final List<SimulationStepDetailed> savedSteps = new ArrayList<>();
    private final ObjectMapper objectMapper;
    Junction junction = new Junction();


    public SimulationService(ObjectMapper objectMapper,String s)
    {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public SimulationService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }



    public SimulationOutput runSimulation(SimulationInput input) {
        SimulationOutput simulationOutput = new SimulationOutput();
        int stepCounter = 1;

        savedSteps.clear();

        for (SimulationCommand command : input.getSimulationInput()) {
            SimulationStepOutput stepResult = new SimulationStepOutput(); // Służy do zapisywania oczekiwanego outputu(jedynie auta opuszczające skrzyżowanie)
            SimulationStepDetailed simulationStep = new SimulationStepDetailed();// Zawiera informacje o danym kroku symulacji (przydatne do frontendu)


            if ("addVehicle".equals(command.getType())) {

                simulationStep.setAction("addVehicle");
                Vehicle vehicle = addNewWehicle(command);
                junction.placeVehicle(vehicle);
                simulationStep.setVehiclesWaiting(junction.getHorizontalCount()+junction.getVerticalCount());
                simulationStep.setAddedVehicle(vehicle.getVehicleId());


            }

            else if ("step".equals(command.getType())) {
                simulationStep.setAction("step");

                junction.handleTraffic(simulationStep);
                stepResult.setLeftVehicles(junction.getPassingVehiclesStrings());


                simulationOutput.setNextSimulationStep(stepResult);
                simulationStep.setLeftVehicles(junction.getPassingVehiclesStrings());
            }


            savedSteps.add(simulationStep); // dodaj do pamięci

        }

        return simulationOutput;
    }

    private Vehicle addNewWehicle(SimulationCommand command) {
        return new Vehicle(
                command.getVehicleId(),
                command.getStartRoad(),
                command.getEndRoad()
        );
    }


}
