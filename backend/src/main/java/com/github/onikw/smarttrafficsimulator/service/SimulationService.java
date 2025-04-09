package com.github.onikw.smarttrafficsimulator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onikw.smarttrafficsimulator.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;


@Service

public class SimulationService {


    private final ObjectMapper objectMapper;
    Junction junction = new Junction();

    @Autowired
    public SimulationService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }


    public SimulationOutput runSimulation(SimulationInput input) {
        SimulationOutput simulationOutput = new SimulationOutput();
        int stepCounter = 1;

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


            try {
                File dir = new File("/tmp/status");
                if (!dir.exists()) dir.mkdirs();

                File file = new File(dir, "step" + stepCounter++ + ".json");
                objectMapper.writeValue(file, simulationStep);
            } catch (Exception e) {
                e.printStackTrace();
            }


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
