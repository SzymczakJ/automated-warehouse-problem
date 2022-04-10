package solution;

import org.jgrapht.graph.*;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Wrong number of arguments");
            return;
        }
        SimpleWeightedGraph<Position, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        String[] positionsInString;
        InputParser inputParser = new InputParser(graph);
        positionsInString = inputParser.readJobsFile(args[1]);
        Position botStartingPosition = inputParser.stringToPosition(positionsInString[0]);
        Position stationPosition = inputParser.stringToPosition(positionsInString[1]);
        inputParser.parseGridFileToGraph(args[0]);
        Solver solver = new Solver(graph);
        solver.solve(stationPosition, botStartingPosition, inputParser.getSetOfProductPositions());
    }
}
