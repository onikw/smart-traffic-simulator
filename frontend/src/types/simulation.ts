export interface SimulationStepResult {
    leftVehicles: string[];
}

export interface SimulationOutput {
    stepStatuses: SimulationStepResult[];
}

export interface SimulationVisStep {
    action: 'addVehicle' | 'step';
    addedVehicle?: string;
    leftVehicles: string[];
    vehiclesWaiting: number;
    vehicleDirections: string[];
}
