/**
 * ITEC 3150 - 01 Spring 2026
 * Itech 3150
 * Algorithms
 * Joshua Wilson
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // If no file is passed in, default to operations.txt
        String filename = (args.length > 0) ? args[0] : "operations.txt";

        StackLL<Integer> stack = new StackLL<>();
        QueueLL<Integer> queue = new QueueLL<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // Read each command from the file
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\s+");
                String cmd = parts[0].toUpperCase();

                // Process commands for stack and queue
                switch (cmd) {
                    case "PUSH":
                        if (parts.length >= 2)
                            stack.push(Integer.parseInt(parts[1]));
                        break;

                    case "POP":
                        stack.pop();
                        break;

                    case "ENQ":
                        if (parts.length >= 2)
                            queue.enqueue(Integer.parseInt(parts[1]));
                        break;

                    case "DEQ":
                        queue.dequeue();
                        break;

                    default:
                        // Ignore any unknown commands
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number in file.");
        }

        // Print final results clearly for the rubric
        System.out.println("FINAL STACK (top first): " + (stack.isEmpty() ? "(empty)" : stack));
        System.out.println("FINAL QUEUE (front first): " + (queue.isEmpty() ? "(empty)" : queue));
    }
}
