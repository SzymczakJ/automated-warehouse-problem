package solution;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class SolverTest {
    SimpleWeightedGraph<Position, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    @Test
    public void solverTest() throws FileNotFoundException {
        InputParser inputParser = new InputParser(graph);
        String[] positionsInString = inputParser.readJobsFile("Przykłady/job-1.txt");
        Position botStartingPosition = inputParser.stringToPosition(positionsInString[0]);
        Position stationPosition = inputParser.stringToPosition(positionsInString[1]);

        inputParser.parseGridFileToGraph("Przykłady/grid-1.txt");
        Solver solver = new Solver(graph);
        solver.solve(stationPosition, botStartingPosition, inputParser.getSetOfProductPositions());

//        checking if solver computed right path(correct weight and number of steps)
        assertEquals(solver.totalSteps, 8);
        assertEquals(solver.totalWeight, 10.5);
    }
}
