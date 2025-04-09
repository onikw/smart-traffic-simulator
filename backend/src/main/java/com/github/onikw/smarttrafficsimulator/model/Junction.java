package com.github.onikw.smarttrafficsimulator.model;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;


@Data
public class Junction {

    private static final int MAX_PASSED_CARS_ON_GREEN = 6;

    private EnumMap<Position, Queue<Vehicle>> trafficQueues;
    private EnumMap<Position, Integer> numberOfVehicles;

    private final List<Position> verticaList = List.of(Position.N, Position.S);
    private final List<Position> horizontalList = List.of(Position.W, Position.E);

    private LightColor verticalLight = LightColor.GREEN;
    private LightColor horizontalLight = LightColor.RED;

    private int lightTimer = 0;

    private List<Position> directionsToLetGo = null;
    private List<Vehicle> passingVehicles = new ArrayList<>();
    private int carsPassedInPhase = 0;

    SimulationStepDetailed simulationStep;
    public Junction() {
        trafficQueues = new EnumMap<>(Position.class);
        numberOfVehicles = new EnumMap<>(Position.class);
        for (Position direction : Position.values()) {
            trafficQueues.put(direction, new LinkedList<>());
            numberOfVehicles.put(direction, 0);
        }
    }

    public List<String> getPassingVehiclesStrings() {
        return passingVehicles.stream()
                .map(Vehicle::getVehicleId)
                .collect(Collectors.toList());
    }

    public void placeVehicle(Vehicle vehicle) {
        Queue<Vehicle> q = trafficQueues.get(vehicle.getStartRoad());
        q.add(vehicle);
        numberOfVehicles.put(vehicle.getStartRoad(), numberOfVehicles.get(vehicle.getStartRoad()) + 1);
    }

    public void handleTraffic(SimulationStepDetailed simulationStep) {
        passingVehicles.clear();
        this.simulationStep = simulationStep;

        if (verticalLight == LightColor.GREEN && horizontalLight == LightColor.RED) {
            // Jeśli pozioma nie ma więcej pojazdów lub limit przejazdów nie został osiągnięty
            if (!isHorizontalMoreThanVertical() &&
                    (carsPassedInPhase < MAX_PASSED_CARS_ON_GREEN || getHorizontalCount() == 0)) {
                directionsToLetGo = verticaList;
            } else {
                // Zmieniamy światło na poziome
                directionsToLetGo = horizontalList;
                carsPassedInPhase = 0;
                verticalLight = LightColor.RED;
                horizontalLight = LightColor.GREEN;
            }

        } else if (verticalLight == LightColor.RED && horizontalLight == LightColor.GREEN) {
            if (isHorizontalMoreThanVertical() &&
                    (carsPassedInPhase < MAX_PASSED_CARS_ON_GREEN || getVerticalCount() == 0)) {
                directionsToLetGo = horizontalList;
            } else {
                // Zmieniamy światło na pionowe
                directionsToLetGo = verticaList;
                carsPassedInPhase = 0;
                verticalLight = LightColor.GREEN;
                horizontalLight = LightColor.RED;
            }
        }

        carsPassedInPhase += allowTrafficOn(directionsToLetGo);

        simulationStep.setVehiclesWaiting(getHorizontalCount()+getVerticalCount());

    }

    public int getVerticalCount() {
        return verticaList.stream().mapToInt(numberOfVehicles::get).sum();
    }

    public int getHorizontalCount() {
        return horizontalList.stream().mapToInt(numberOfVehicles::get).sum();
    }

    private boolean isHorizontalMoreThanVertical() {
        return getHorizontalCount() > getVerticalCount();
    }

    private int allowTrafficOn(List<Position> directions) {
        int passed = 0;

        for (Position dir : directions) {
            Queue<Vehicle> q = trafficQueues.get(dir);
            if (!q.isEmpty()) {
                Vehicle v = q.poll();
                if (v != null) {
                    String message =  v.getVehicleId() + " z kierunku " + Position.enumToString(v.getStartRoad()) + " do: " + Position.enumToString(v.getEndRoad());
                    simulationStep.addVehicleDirections(message);

                    passingVehicles.add(v);
                    numberOfVehicles.put(dir, numberOfVehicles.get(dir) - 1);
                    passed++;
                }
            }
        }

        return passed;
    }
}
