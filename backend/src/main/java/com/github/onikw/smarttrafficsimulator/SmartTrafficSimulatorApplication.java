package com.github.onikw.smarttrafficsimulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onikw.smarttrafficsimulator.model.JSONServiceConfig;
import com.github.onikw.smarttrafficsimulator.model.SimulationInput;
import com.github.onikw.smarttrafficsimulator.model.SimulationOutput;
import com.github.onikw.smarttrafficsimulator.service.SimulationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class SmartTrafficSimulatorApplication {


    //Jeśli podamy plik wejściowy oraz wyjściowy program zwróci wynik symulacji w pliku wyjściowym.
    //W przypadku braku argumentów uruchomiona będzie aplikacja webowa
    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = JSONServiceConfig.objectMapper();
        if (args.length == 2) {
            String outputFilePath = args[1];
            String inputFilePath = args[0];

            SimulationInput input = objectMapper.readValue(new File(inputFilePath), SimulationInput.class);




            SimulationService service = new SimulationService(objectMapper,"o");


            SimulationOutput output = service.runSimulation(input);


            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilePath), output);


            System.out.println("Symulacja zakończona. Wynik zapisany w: " + outputFilePath);
        }
        else
        {
            SpringApplication.run(SmartTrafficSimulatorApplication.class, args);
        }
    }
}
