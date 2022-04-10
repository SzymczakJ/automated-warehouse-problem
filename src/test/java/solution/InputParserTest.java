package solution;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.ObjectInput;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {
    SimpleWeightedGraph<Position, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    @Test
    public void graphCreationTest() throws FileNotFoundException {
        InputParser inputParser = new InputParser(graph);

//        checking if the position of station and bot was read correctly
        String[] positionsInString = inputParser.readJobsFile("Przykłady/job-1.txt");
        Position botStartingPosition = inputParser.stringToPosition(positionsInString[0]);
        Position stationPosition = inputParser.stringToPosition(positionsInString[1]);
        assertEquals(new Position(1, 1), botStartingPosition);
        assertEquals(new Position(0, 0), stationPosition);

        inputParser.parseGridFileToGraph("Przykłady/grid-1.txt");

//        checking if there is correct ammount of edges
        assertEquals(graph.vertexSet().size(), 14);
        assertEquals(graph.edgeSet().size(), 16);

//        checking if edges have correct weights
        double weight = graph.getEdgeWeight(graph.getEdge(new Position(0, 0), new Position(1, 0)));
        assertEquals(weight, 0.5);
        weight = graph.getEdgeWeight(graph.getEdge(new Position(0, 1), new Position(1, 1)));
        assertEquals(weight, 1);
        weight = graph.getEdgeWeight(graph.getEdge(new Position(1, 3), new Position(2, 3)));
        assertEquals(weight, 2);
        weight = graph.getEdgeWeight(graph.getEdge(new Position(1, 0), new Position(1, 1)));
        assertEquals(weight, 1);
        weight = graph.getEdgeWeight(graph.getEdge(new Position(1, 1), new Position(1, 2)));
        assertEquals(weight, 1);

//        checking if special grid-storage edges have correct weights
        weight = graph.getEdgeWeight(graph.getEdge(new Position(2, 3), new Position(-2, -3)));
        assertEquals(weight, 2);
        weight = graph.getEdgeWeight(graph.getEdge(new Position(2, 0), new Position(-2, 0)));
        assertEquals(weight, 10);
    }

}