export interface SimulationStepResult {
    leftVehicles: string[];
}

export interface SimulationOutput {
    stepStatuses: SimulationStepResult[];
}
