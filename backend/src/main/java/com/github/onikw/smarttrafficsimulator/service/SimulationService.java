package com.github.onikw.smarttrafficsimulator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onikw.smarttrafficsimulator.model.*;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public SimulationOutput runSimulation(SimulationInput input, double speed) {

        SimulationOutput simulationOutput = new SimulationOutput();
        int stepCounter = 1;

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

                // Zbieramy ID pojazdów oczekujących (opcjonalne — może do debug)
                for (String dir : directions) {
                    for (Vehicle vehicle : roads.get(dir)) {
                        stepResult.setNextVehicle(vehicle.getVehicleId()); // jeśli chcesz przechowywać np. w osobnym polu
                    }
                }

                // Przepuszczamy pojazdy na aktualnie zielonej drodze + drodze naprzeciwko
                String currentRoad = directions.get(currentGreenRoadIndex);
                Queue<Vehicle> queue = roads.get(currentRoad);
                if (!queue.isEmpty()) {
                    Vehicle v = queue.poll();
                    stepResult.getLeftVehicles().add(v.getVehicleId());
                }

                String oppositeRoad = directions.get((currentGreenRoadIndex + 2) % 4);
                Queue<Vehicle> oppositeQueue = roads.get(oppositeRoad);
                if (!oppositeQueue.isEmpty()) {
                    Vehicle v = oppositeQueue.poll();
                    stepResult.getLeftVehicles().add(v.getVehicleId());
                }

                // Dodaj wynik kroku do wyniku ogólnego
                simulationOutput.setNextSimulationStep(stepResult);

                // Zapisz do pliku JSON (dla frontendu)
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    File file = new File("src/main/resources/static/status/step" + stepCounter++ + ".json");
                    file.getParentFile().mkdirs();
                    mapper.writeValue(file, stepResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Delay zgodnie z prędkością symulacji
                try {
                    long delayMs = (long) (1000.0 / speed);
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                rotateLights();
            }
        }

        return simulationOutput;
    }


    private void rotateLights() {
        currentGreenRoadIndex = (currentGreenRoadIndex + 1) % 2;
    }
}
