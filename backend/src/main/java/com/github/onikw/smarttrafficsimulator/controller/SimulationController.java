package com.github.onikw.smarttrafficsimulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.onikw.smarttrafficsimulator.model.SimulationInput;
import com.github.onikw.smarttrafficsimulator.model.SimulationOutput;
import com.github.onikw.smarttrafficsimulator.model.SimulationStepDetailed;
import com.github.onikw.smarttrafficsimulator.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SimulationController {

    private final ObjectMapper objectMapper;

    private final SimulationService simulationService;




    @Autowired
    public SimulationController(ObjectMapper objectMapper, SimulationService simulationService) {
        this.objectMapper = objectMapper;
        this.simulationService = simulationService;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @PostMapping("/simulate")
    public ResponseEntity<?> simulate(@RequestParam("file") MultipartFile file){
        try {
            SimulationInput request = objectMapper.readValue(file.getInputStream(), SimulationInput.class);
            SimulationOutput result = simulationService.runSimulation(request);
            objectMapper.writeValue(new File("output.json"), result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Błąd przetwarzania JSON: " + e.getMessage());
        }
    }

    @GetMapping("/steps/{stepNumber}")
        public ResponseEntity<SimulationStepDetailed> getStep(@PathVariable int stepNumber) {
            List<SimulationStepDetailed> steps = simulationService.getSavedSteps();
            if (stepNumber < 1 || stepNumber > steps.size()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(steps.get(stepNumber - 1));
        }





}
