package com.github.onikw.smarttrafficsimulator.service;

import com.github.onikw.smarttrafficsimulator.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimulationService {


    private final Map<String, Queue<Vehicle>> roads = new HashMap<>();

    private final List<String> directions = List.of("north", "south", "east", "west");

    private int currentGreenRoadIndex = 0;

    public SimulationService() {
        for (String dir : directions) {
            roads.put(dir, new LinkedList<>());
        }
    }

    public SimulationOutput runSimulation(SimulationInput input) {

        SimulationOutput simulationOutput = new SimulationOutput();



        for (SimulationCommand command : input.getSimulationInput()) {
            if ("addVehicle".equals(command.getType())) {
                Vehicle vehicle = new Vehicle(
                        command.getVehicleId(),
                        command.getStartRoad(),
                        command.getEndRoad()
                );
                roads.get(vehicle.getStartRoad()).add(vehicle);
            }

            else if ("step".equals(command.getType())) {
                SimulationStepResult stepResult = new SimulationStepResult();

                for (String dir : directions)
                {
                    for(Vehicle vehicle : roads.get(dir)) {
                        stepResult.setNextVehicle(vehicle.getVehicleId());
                    }
                }


                String currentRoad = directions.get(currentGreenRoadIndex);
                Queue<Vehicle> queue = roads.get(currentRoad);

                if (!queue.isEmpty()) {
                    queue.poll();
                }
                currentRoad = directions.get(currentGreenRoadIndex+2);
                queue = roads.get(currentRoad);

                if (!queue.isEmpty()) {
                    queue.poll();
                }


                simulationOutput.setNextSimulationStep(stepResult);
                rotateLights();
            }
        }

        return simulationOutput;
    }

    private void rotateLights() {
        currentGreenRoadIndex = (currentGreenRoadIndex + 1) % 2;
    }
}
