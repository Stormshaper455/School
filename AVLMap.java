import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class AVLMap extends Application {

    private TextArea output = new TextArea(); // where stuff shows up
    private AVLTree avl = new AVLTree(); // my tree
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

        ComboBox<String> startBox = new ComboBox<>();
        ComboBox<String> endBox = new ComboBox<>();

        startBox.getItems().addAll(cities); // add all cities
        endBox.getItems().addAll(cities);

        startBox.setValue("Arad"); // default start
        endBox.setValue("Bucharest"); // default end

        Button dijkstraBtn = new Button("Dijkstra"); // shortest path 1
        Button bellmanBtn = new Button("Bellman Ford"); // shortest path 2
        Button clearBtn = new Button("Clear"); // reset everything

        output.setPrefHeight(420);
        output.setEditable(false); // no typing here

        dijkstraBtn.setOnAction(e -> {
            runShortestPath(startBox.getValue(), endBox.getValue(), "Dijkstra"); // run it
        });

        bellmanBtn.setOnAction(e -> {
            runShortestPath(startBox.getValue(), endBox.getValue(), "Bellman-Ford"); // run it
        });

        clearBtn.setOnAction(e -> {
            avl = new AVLTree(); // wipe tree
            output.setText("tree gone lol"); // reset message
        });

        HBox controls = new HBox(10);
        controls.getChildren().addAll(
                new Label("Start:"), startBox,
                new Label("End:"), endBox,
                dijkstraBtn, bellmanBtn, clearBtn
        );

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 15; -fx-font-size: 14;"); // spacing stuff

        root.getChildren().addAll(
                new Label("AVL Map"),
                controls,
                output
        );

        Scene scene = new Scene(root, 950, 520);

        stage.setTitle("AVL Map"); // window title
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

        avl = new AVLTree(); // fresh tree every time

        for (int distance : result.allDistances.values()) {
            if (distance != Integer.MAX_VALUE) {
                avl.insert(distance); // throw distances into tree
            }
        }

        output.setText("");
        output.appendText("Using: " + algorithm + "\n\n");

        output.appendText("Start: " + start + "\n");
        output.appendText("End: " + end + "\n\n");

        output.appendText("Distance: " + result.distance + "\n");
        output.appendText("Path: " + result.path + "\n\n");

        output.appendText("All distances:\n");

        for (String city : result.allDistances.keySet()) {
            output.appendText(city + " = " + result.allDistances.get(city) + "\n");
        }

        output.appendText("\nTree in order:\n");
        output.appendText(avl.inOrder() + "\n");

        output.appendText("\nTree shape:\n");
        output.appendText(avl.printTree());
    }

    private void buildRomaniaMap() {
        for (String city : cities) {
            graph.put(city, new ArrayList<>()); // empty list for each
        }

        // add roads
        addRoad("Arad", "Zerind", 75);
        addRoad("Arad", "Sibiu", 140);
        addRoad("Arad", "Timisoara", 118);
        addRoad("Zerind", "Oradea", 71);
        addRoad("Oradea", "Sibiu", 151);
        addRoad("Timisoara", "Lugoj", 111);
        addRoad("Lugoj", "Mehadia", 70);
        addRoad("Mehadia", "Drobeta", 75);
        addRoad("Drobeta", "Craiova", 120);
        addRoad("Craiova", "Rimnicu Vilcea", 146);
        addRoad("Craiova", "Pitesti", 138);
        addRoad("Sibiu", "Fagaras", 99);
        addRoad("Sibiu", "Rimnicu Vilcea", 80);
        addRoad("Rimnicu Vilcea", "Pitesti", 97);
        addRoad("Fagaras", "Bucharest", 211);
        addRoad("Pitesti", "Bucharest", 101);
        addRoad("Bucharest", "Giurgiu", 90);
        addRoad("Bucharest", "Urziceni", 85);
        addRoad("Urziceni", "Hirsova", 98);
        addRoad("Hirsova", "Eforie", 86);
        addRoad("Urziceni", "Vaslui", 142);
        addRoad("Vaslui", "Iasi", 92);
        addRoad("Iasi", "Neamt", 87);
    }

    private void addRoad(String c1, String c2, int d) {
        graph.get(c1).add(new Edge(c2, d)); // one way
        graph.get(c2).add(new Edge(c1, d)); // other way
    }

    private Result dijkstra(String start, String end) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String city : graph.keySet()) {
            dist.put(city, Integer.MAX_VALUE); // start with big number
        }

        dist.put(start, 0); // start is 0

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String cur = pq.poll(); // closest city

            for (Edge e : graph.get(cur)) {
                if (dist.get(cur) == Integer.MAX_VALUE) continue; // skip weird cases

                int newDist = dist.get(cur) + e.weight;

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
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String city : graph.keySet()) {
            dist.put(city, Integer.MAX_VALUE);
        }

        dist.put(start, 0);

        for (int i = 0; i < cities.length - 1; i++) { // repeat a bunch
            for (String city : graph.keySet()) {

                if (dist.get(city) == Integer.MAX_VALUE) continue;

                for (Edge e : graph.get(city)) {
                    int newDist = dist.get(city) + e.weight;

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
            path.add(cur); //  backwards
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
        int weight;

        Edge(String t, int w) {
            to = t;
            weight = w;
        }
    }

    static class Result {
        int distance;
        String path;
        Map<String, Integer> allDistances;

        Result(int d, String p, Map<String, Integer> m) {
            distance = d;
            path = p;
            allDistances = m;
        }
    }

    static class AVLTree {
        Node root;

        void insert(int value) {
            root = insert(root, value); // recu
        }

        private Node insert(Node node, int value) {
            if (node == null) return new Node(value); // mnew node

            if (value < node.value) {
                node.left = insert(node.left, value);
            } else if (value > node.value) {
                node.right = insert(node.right, value);
            } else {
                return node;  // makes no copies
            }

            node.height = 1 + Math.max(height(node.left), height(node.right));

            int balance = height(node.left) - height(node.right); // check balance

            if (balance > 1 && value < node.left.value) return rotateRight(node); // LL
            if (balance < -1 && value > node.right.value) return rotateLeft(node); // RR

            if (balance > 1 && value > node.left.value) { // LR
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }

            if (balance < -1 && value < node.right.value) { //
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }

            return node;
        }

        private Node rotateRight(Node y) {
            Node x = y.left;
            Node temp = x.right;

            x.right = y;
            y.left = temp;

            y.height = 1 + Math.max(height(y.left), height(y.right));
            x.height = 1 + Math.max(height(x.left), height(x.right));

            return x;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            Node temp = y.left;

            y.left = x;
            x.right = temp;

            x.height = 1 + Math.max(height(x.left), height(x.right));
            y.height = 1 + Math.max(height(y.left), height(y.right));

            return y;
        }

        private int height(Node n) {
            return (n == null) ? 0 : n.height;
        }

        String inOrder() {
            return inOrder(root).trim(); // forma output
        }

        private String inOrder(Node n) {
            if (n == null) return "";
            return inOrder(n.left) + n.value + " " + inOrder(n.right);
        }

        String printTree() {
            if (root == null) return "empty tree";
            return printTree(root, "", true);
        }

        private String printTree(Node n, String prefix, boolean isTail) {
            if (n == null) return "";

            String res = prefix + (isTail ? "└── " : "├── ") + n.value + "\n";

            if (n.left != null || n.right != null) {
                res += printTree(n.left, prefix + (isTail ? "    " : "│   "), false);
                res += printTree(n.right, prefix + (isTail ? "    " : "│   "), true);
            }

            return res;
        }
    }

    static class Node {
        int value;
        int height;
        Node left, right;

        Node(int v) {
            value = v;
            height = 1; // start height
        }
    }

    public static void main(String[] args) {
        launch(args); // run it
    }
}