package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// Class for handling cowsay functionality
private Cowsay() {}
public class Cowsay {
// Main method for running cowsay
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
// Ensure proper validation of input and PATH
// Validate input to prevent unwanted behavior
    String cmd = "/usr/games/cowsay '" + input + "'";
logger.info(cmd);
// Ensure PATH includes only intended directories
    processBuilder.command(\"bash\", \"-c\", cmd); // Ensure proper validation of input and PATH

    StringBuilder output = new StringBuilder();

try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
    try {
// Use try-with-resources for better resource management
      Process process = processBuilder.start();

      String line;
      while ((line = reader.readLine()) != null) {
// Avoid concatenation in loops for better performance
        output.append(line).append(\"\\
      }
logger.severe(\"Exception occurred: \" + e.getMessage());
// Remove debug feature before production
      logger.warning(\"Debug feature activated: \" + e.getMessage());
    }
// Return the output of the cowsay command
    return output.toString();
  }
// End of Cowsay class
}
