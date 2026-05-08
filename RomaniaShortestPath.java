import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class RomaniaShortestPath extends Application {

    private TextArea output = new TextArea(); // where stuff shows up
    private Map<String, List<Edge>> graph = new HashMap<>(); // romania map

    // all cities
    private String[] cities = {
            "Arad", "Zerind", "Oradea", "Sibiu", "Timisoara",
            "Lugoj", "Mehadia", "Drobeta", "Craiova",
            "Rimnicu Vilcea", "Fagaras", "Pitesti", "Bucharest",
            "Giurgiu", "Urziceni", "Hirsova", "Eforie",
            "Vaslui", "Iasi", "Neamt"
    };

    @Override
    public void start(Stage stage) {
        buildRomaniaMap(); // build the map first

        Button dijkstraBtn = new Button("Run Dijkstra"); // shortest path 1
        Button bellmanBtn = new Button("Run Bellman-Ford"); // shortest path 2
        Button bothBtn = new Button("Run Both"); // assignment button
        Button clearBtn = new Button("Clear"); // reset output

        output.setPrefHeight(420);
        output.setEditable(false); // no typing here

        dijkstraBtn.setOnAction(e -> {
            runShortestPath("Arad", "Bucharest", "Dijkstra"); // run it
        });

        bellmanBtn.setOnAction(e -> {
            runShortestPath("Arad", "Bucharest", "Bellman-Ford"); // run it
        });

        bothBtn.setOnAction(e -> {
            output.setText("");
            runShortestPath("Arad", "Bucharest", "Dijkstra");
            output.appendText("\n--------------------------------\n\n");
            runShortestPath("Arad", "Bucharest", "Bellman-Ford");
        });

        clearBtn.setOnAction(e -> {
            output.setText(""); // bye bye text
        });

        HBox controls = new HBox(10);
        controls.getChildren().addAll(dijkstraBtn, bellmanBtn, bothBtn, clearBtn);

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 15; -fx-font-size: 14;"); // spacing stuff

        root.getChildren().addAll(
                new Label("Shortest Path from Arad to Bucharest"),
                controls,
                output
        );

        Scene scene = new Scene(root, 900, 520);

        stage.setTitle("Romania Shortest Path"); // window title
        stage.setScene(scene);
        stage.show();
    }

    private void runShortestPath(String start, String end, String algorithm) {
        Result result;

        if (algorithm.equals("Dijkstra")) {
            result = dijkstra(start, end); // normal one
        } else {
            result = bellmanFord(start, end); // slower but works
        }

        output.appendText("Using: " + algorithm + "\n\n");

        output.appendText("Start: " + start + "\n");
        output.appendText("End: " + end + "\n\n");

        output.appendText("Shortest Distance: " + result.distance + "\n");
        output.appendText("Shortest Path: " + result.path + "\n\n");

        output.appendText("All distances from Arad:\n");

        for (String city : result.allDistances.keySet()) {
            output.appendText(city + " = " + result.allDistances.get(city) + "\n");
        }
    }

    private void buildRomaniaMap() {
        for (String city : cities) {
            graph.put(city, new ArrayList<>()); // empty list for each city
        }

        // add roads from the slide
        addRoad("Arad", "Zerind", 51.9);
        addRoad("Arad", "Timisoara", 48.4);
        addRoad("Arad", "Sibiu", 223.2);

        addRoad("Zerind", "Oradea", 57.6);

        addRoad("Oradea", "Urziceni", 81.6);
        addRoad("Oradea", "Lugoj", 108.6);
        addRoad("Oradea", "Sibiu", 227.1);

        addRoad("Timisoara", "Lugoj", 54);
        addRoad("Timisoara", "Mehadia", 128.9);

        addRoad("Lugoj", "Mehadia", 93.5);
        addRoad("Lugoj", "Sibiu", 203);

        addRoad("Mehadia", "Drobeta", 38.3);
        addRoad("Mehadia", "Rimnicu Vilcea", 160.5);

        addRoad("Drobeta", "Craiova", 97.5);

        addRoad("Craiova", "Rimnicu Vilcea", 99.3);
        addRoad("Craiova", "Pitesti", 103.5);
        addRoad("Craiova", "Bucharest", 183);
        addRoad("Craiova", "Giurgiu", 178.2);

        addRoad("Sibiu", "Rimnicu Vilcea", 78);
        addRoad("Sibiu", "Fagaras", 64.3);

        addRoad("Rimnicu Vilcea", "Pitesti", 47.2);

        addRoad("Fagaras", "Pitesti", 109.8);
        addRoad("Fagaras", "Bucharest", 178.3);
        addRoad("Fagaras", "Urziceni", 286.1);
        addRoad("Fagaras", "Neamt", 174.9);
        addRoad("Fagaras", "Vaslui", 229.4);

        addRoad("Pitesti", "Bucharest", 107.5);

        addRoad("Bucharest", "Giurgiu", 62.4);
        addRoad("Bucharest", "Hirsova", 149.3);
        addRoad("Bucharest", "Vaslui", 274.9);
        addRoad("Bucharest", "Neamt", 289.1);

        addRoad("Giurgiu", "Eforie", 216.5);

        addRoad("Hirsova", "Eforie", 90.6);
        addRoad("Hirsova", "Vaslui", 217.3);

        addRoad("Urziceni", "Neamt", 315);

        addRoad("Neamt", "Iasi", 85.8);
        addRoad("Neamt", "Vaslui", 105.5);

        addRoad("Iasi", "Vaslui", 58.4);
    }

    private void addRoad(String c1, String c2, double d) {
        graph.get(c1).add(new Edge(c2, d)); // one way
        graph.get(c2).add(new Edge(c1, d)); // other way
    }

    private Result dijkstra(String start, String end) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String city : graph.keySet()) {
            dist.put(city, Double.MAX_VALUE); // start with big number
        }

        dist.put(start, 0.0); // start is 0

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String cur = pq.poll(); // closest city

            for (Edge e : graph.get(cur)) {
                if (dist.get(cur) == Double.MAX_VALUE) continue; // skip weird cases

                double newDist = dist.get(cur) + e.weight;

                if (newDist < dist.get(e.to)) {
                    dist.put(e.to, newDist);
                    prev.put(e.to, cur);
                    pq.remove(e.to); // update it
                    pq.add(e.to);
                }
            }
        }

        return new Result(dist.get(end), buildPath(prev, start, end), dist);
    }

    private Result bellmanFord(String start, String end) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String city : graph.keySet()) {
            dist.put(city, Double.MAX_VALUE); // start big
        }

        dist.put(start, 0.0); // start is 0

        for (int i = 0; i < cities.length - 1; i++) { // repeat a bunch
            for (String city : graph.keySet()) {

                if (dist.get(city) == Double.MAX_VALUE) continue;

                for (Edge e : graph.get(city)) {
                    double newDist = dist.get(city) + e.weight;

                    if (newDist < dist.get(e.to)) {
                        dist.put(e.to, newDist);
                        prev.put(e.to, city);
                    }
                }
            }
        }

        return new Result(dist.get(end), buildPath(prev, start, end), dist);
    }

    private String buildPath(Map<String, String> prev, String start, String end) {
        ArrayList<String> path = new ArrayList<>();
        String cur = end;

        while (cur != null) {
            path.add(cur); // backwards
            cur = prev.get(cur);
        }

        Collections.reverse(path);

        if (path.isEmpty() || !path.get(0).equals(start)) {
            return "no path??";
        }

        return String.join(" -> ", path);
    }

    static class Edge {
        String to;
        double weight;

        Edge(String t, double w) {
            to = t;
            weight = w;
        }
    }

    static class Result {
        double distance;
        String path;
        Map<String, Double> allDistances;

        Result(double d, String p, Map<String, Double> m) {
            distance = d;
            path = p;
            allDistances = m;
        }
    }

    public static void main(String[] args) {
        launch(args); // run it
    }
}