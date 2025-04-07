package com.github.onikw.smarttrafficsimulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onikw.smarttrafficsimulator.model.SimulationInput;
import com.github.onikw.smarttrafficsimulator.model.SimulationOutput;
import com.github.onikw.smarttrafficsimulator.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api")
public class SimulationController {

    private final ObjectMapper objectMapper;

    private final SimulationService simulationService;

    @Autowired
    public SimulationController(ObjectMapper objectMapper, SimulationService simulationService) {
        this.objectMapper = objectMapper;
        this.simulationService = simulationService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<?> simulate(@RequestParam("file") MultipartFile file,
                                      @RequestParam("speed") double speed) {
        try {
            SimulationInput request = objectMapper.readValue(file.getInputStream(), SimulationInput.class);
            SimulationOutput result = simulationService.runSimulation(request, speed);
            objectMapper.writeValue(new File("output.json"), result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Błąd przetwarzania JSON: " + e.getMessage());
        }
    }

}
