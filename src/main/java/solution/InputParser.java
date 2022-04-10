package solution;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InputParser {
    private final SimpleWeightedGraph<Position, DefaultWeightedEdge> graph;
    private BufferedReader bufferedReader;
    private int xSize;
    private int ySize;
    private String[] arrayRepresentationOfGraph;
    private String productID;
    private Set<Position> setOfProductPositions = new HashSet<>();
    Map<Position, Integer> closestToGridProducts = new HashMap<>();

    public InputParser(SimpleWeightedGraph<Position, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public String[] readJobsFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader, 100000);
            String[] res = new String[3];

            try {
                res[0] = bufferedReader.readLine();
                res[1] = bufferedReader.readLine();
                productID = bufferedReader.readLine();
                res[2] = productID;

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        } catch (FileNotFoundException e) {
            System.out.println("No such file!");
            e.printStackTrace();
        }
        return null;
    }

    public Position stringToPosition(String arg) {
        String[] splitArgs = arg.split(" ");
        return new Position(Integer.parseInt(splitArgs[1]), Integer.parseInt(splitArgs[0]));
    }

    public void parseGridFileToGraph(String filePath) throws FileNotFoundException {
        FileReader fileReader = new FileReader(filePath);
        bufferedReader = new BufferedReader(fileReader, 100000);

        createGraphWithoutEdges();
        addEdgesToGraph();
        addAndConnectProductVertices();

        try {
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGraphWithoutEdges() {
        String line = "";
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] arguments = line.split(" ");
        xSize = Integer.parseInt(arguments[1]);
        ySize = Integer.parseInt(arguments[0]);

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                graph.addVertex(new Position(i, j));
            }
        }
    }

    private void addEdgesToGraph() {
        arrayRepresentationOfGraph = new String[ySize];

        for (int i = 0; i < xSize; i++) {
            try {
                arrayRepresentationOfGraph[i] = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                addAdjacentEdges(new Position(i, j));
            }
        }
    }

    private double calculateEdgeWeight(Position firstPosition, Position secondPosition) {
        String possibleValues = "HBSO";

        if (arrayRepresentationOfGraph[firstPosition.x].charAt(firstPosition.y) == possibleValues.charAt(3) ||
                arrayRepresentationOfGraph[secondPosition.x].charAt(secondPosition.y) == possibleValues.charAt(3)) return 0;

        if (arrayRepresentationOfGraph[firstPosition.x].charAt(firstPosition.y) == possibleValues.charAt(2) ||
                arrayRepresentationOfGraph[secondPosition.x].charAt(secondPosition.y) == possibleValues.charAt(2))
            return 2;

        if (arrayRepresentationOfGraph[firstPosition.x].charAt(firstPosition.y) == possibleValues.charAt(1) ||
                arrayRepresentationOfGraph[secondPosition.x].charAt(secondPosition.y) == possibleValues.charAt(1))
            return 1;

        return 0.5;
    }

    private void addAdjacentEdges(Position position) {
        Position adjacentPosition;
        DefaultWeightedEdge edge;
        double weight;
        if (position.x - 1 > 0) {
            adjacentPosition = new Position(position.x - 1, position.y);
            weight = calculateEdgeWeight(position, adjacentPosition);
            if (weight != 0) {
                edge = graph.addEdge(position, adjacentPosition);
                if (edge != null) {
                    graph.setEdgeWeight(edge, calculateEdgeWeight(position, adjacentPosition));
                }
            }
        }

        if (position.x + 1 < xSize) {
            adjacentPosition = new Position(position.x + 1, position.y);
            weight = calculateEdgeWeight(position, adjacentPosition);
            if (weight != 0) {
                edge = graph.addEdge(position, adjacentPosition);
                if (edge != null) {
                    graph.setEdgeWeight(edge, calculateEdgeWeight(position, adjacentPosition));
                }
            }
        }

        if (position.y - 1 > 0) {
            adjacentPosition = new Position(position.x, position.y - 1);
            weight = calculateEdgeWeight(position, adjacentPosition);
            if (weight != 0) {
                edge = graph.addEdge(position, adjacentPosition);
                if (edge != null) {
                    graph.setEdgeWeight(edge, calculateEdgeWeight(position, adjacentPosition));
                }
            }
        }

        if (position.y + 1 < ySize) {
            adjacentPosition = new Position(position.x, position.y + 1);
            weight = calculateEdgeWeight(position, adjacentPosition);
            if (weight != 0) {
                edge = graph.addEdge(position, adjacentPosition);
                if (edge != null) {
                    graph.setEdgeWeight(edge, calculateEdgeWeight(position, adjacentPosition));
                }
            }
        }
    }

    private void addAndConnectProductVertices() {
        String line = "";
        while (true) {
            try {
                line = bufferedReader.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null)
                break;
            addPositionsToClosestProductsMap(line);
        }
        addClosestProductVertices();
    }

    private void addPositionsToClosestProductsMap(String line) {
        String[] splitArguments = line.split(" ");
        if (!Objects.equals(splitArguments[0], productID)) return;

        int productX = Integer.parseInt(splitArguments[2]);
        int productY = Integer.parseInt(splitArguments[1]);
        int productPlaceInStorage = Integer.parseInt(splitArguments[3]);
        Position productPosition = new Position(-productX, -productY);
        int weight = calculateStorageAccessTime(new Position(productX, productY), productPlaceInStorage);
        if (closestToGridProducts.containsKey(productPosition)) {
            if (closestToGridProducts.get(productPosition) > weight)
            closestToGridProducts.replace(productPosition, weight);
        }
        else {
            closestToGridProducts.put(productPosition, weight);
        }
    }

    private void addClosestProductVertices() {
        for (Map.Entry<Position, Integer> set: closestToGridProducts.entrySet()) {
            Position productPosition = set.getKey();
            graph.addVertex(productPosition);
            setOfProductPositions.add(productPosition);
            DefaultWeightedEdge edge = graph.addEdge(productPosition, new Position(-productPosition.x, -productPosition.y));
            graph.setEdgeWeight(edge, set.getValue());
        }
    }

    private int calculateStorageAccessTime(Position position, int productPlaceInStorage) {
        String possibleValues = "HBSO";
        if (arrayRepresentationOfGraph[position.x].charAt(position.y) == possibleValues.charAt(0)) {
            return 3 * productPlaceInStorage + 4;
        }
        if (arrayRepresentationOfGraph[position.x].charAt(position.y) == possibleValues.charAt(1)) {
            return 2 * productPlaceInStorage + 2;
        }
        if (arrayRepresentationOfGraph[position.x].charAt(position.y) == possibleValues.charAt(2)) {
            return productPlaceInStorage + 1;
        }
        else {
            return 100000;
        }
    }

    public Set<Position> getSetOfProductPositions() {
        return setOfProductPositions;
    }
}

