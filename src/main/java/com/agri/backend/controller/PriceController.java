package com.agri.backend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("price")
public class PriceController {

    @PostMapping("")
    public ResponseEntity<String> receivePriceData(@RequestBody PriceData priceData) {
        try {
            // Get the commodity name from the request
            String commodityName = priceData.getCommodity();

            // Capitalize: first letter uppercase, rest lowercase
            if (commodityName != null && !commodityName.isEmpty()) {
                commodityName = commodityName.substring(0, 1).toUpperCase() + commodityName.substring(1).toLowerCase();
            } else {
                return ResponseEntity.badRequest().body("Commodity name is required");
            }

            // Path to your Python script
            String pythonScriptPath = "../../../../../python/parser.py"; // TODO: Replace with actual path

            // Run the Python script with the commodity name as an argument
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, commodityName);

            // Redirect error stream to the output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Capture the output from the Python script
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Wait for the script to finish
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return ResponseEntity.status(500).body("Failed to run Python script. Exit code: " + exitCode);
            }

            // Clean up and send the result
            String result = output.toString().trim();
            return ResponseEntity.ok(result);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error running Python script: " + e.getMessage());
        }
    }
}

class PriceData {
    private String commodity;

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }
}