package solution;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.Set;

public class Solver {
    private final SimpleWeightedGraph<Position, DefaultWeightedEdge> graph;
    private GraphPath<Position, DefaultWeightedEdge> startToProductPath;
    private GraphPath<Position, DefaultWeightedEdge> productToStationPath;
    public double totalWeight;
    public int totalSteps;

    public Solver(SimpleWeightedGraph<Position, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public void solve(Position stationPosition, Position startingPosition, Set<Position> productPositions) {
        DijkstraShortestPath<Position, DefaultWeightedEdge> shortestPathAlgo =
                new DijkstraShortestPath<>(graph);

        ShortestPathAlgorithm.SingleSourcePaths<Position, DefaultWeightedEdge> pathsFromStart =
                shortestPathAlgo.getPaths(startingPosition);
        double minimalTotalPath = Double.POSITIVE_INFINITY;

        GraphPath<Position, DefaultWeightedEdge> pathFromProduct;
        for (Position productPosition: productPositions) {
            Position gridUnderProductPosition = new Position(-productPosition.x, -productPosition.y);
            pathFromProduct = shortestPathAlgo.getPath(gridUnderProductPosition, stationPosition);
            double currentPathWeight = pathsFromStart.getWeight(productPosition) + pathFromProduct.getWeight();
            if (minimalTotalPath > currentPathWeight) {
                minimalTotalPath = currentPathWeight;
                startToProductPath = pathsFromStart.getPath(productPosition);
                productToStationPath = pathFromProduct;
            }
        }

        printOutput(startToProductPath, productToStationPath);
    }

    private void printOutput(GraphPath<Position, DefaultWeightedEdge> pathFromStart, GraphPath<Position, DefaultWeightedEdge> pathFromProduct) {
        List<Position> pathFromStartToProductList = pathFromStart.getVertexList();
        List<Position> pathFromProductToStationList = pathFromProduct.getVertexList();
        int pathLength = pathFromStartToProductList.size() + pathFromProductToStationList.size() - 3;
        totalSteps = pathLength;
        System.out.println(pathLength);

        double totalPathWeight = pathFromStart.getWeight() + pathFromProduct.getWeight();
        totalWeight = totalPathWeight;
        System.out.println(totalPathWeight);


        for (int i = 0; i < pathFromStartToProductList.size() - 1; i++) {
            System.out.println(pathFromStartToProductList.get(i));
        }

        for (int i = 1; i < pathFromProductToStationList.size(); i++) {
            System.out.println(pathFromProductToStationList.get(i));
        }
    }
}
