
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Maze {

//	instance variables you may need
//	a variable storing the graph, a variable storing the id of the starting node, a variable storing the id of the end node
//	a variable storing the read number of coins, maybe even a variable storing the path so far so that you don't perform accidental 
//	(and unnecessary cycles).
//	if you maintain nodes on a path in a list, be careful to make a list of GraphNodes, 
//	otherwise removal from the list is going to behave in a weird way. 
//	REMEMBER your nodes have a field mark.. maybe that field could be useful to avoid cycles...
    private Graph graph;
    private int coins;
    private GraphNode start;
    private GraphNode exit;

    public Maze(String inputFile) throws MazeException {
//		initialize your graph variable by reading the input file!
//		to maintain your code as clean and easy to debug as possible use the provided private helper method
        try {
            readInput(new BufferedReader(new FileReader(inputFile)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public Graph getGraph() {
//		return your graph
    }

    public Iterator<GraphNode> solve() {
//		simply call your private DFS. If you come up with a different approach that's ok too.
//		remember to always return an Iterator or null
    }

    private Iterator<GraphNode> DFS(int k, GraphNode go) throws GraphException {
//		perform a DFS of your graph. Reduce your k which represents the remaining coins
//		start with the base case
//		remember to return null if you didn't find a path
    }

    private void readInput(BufferedReader inputReader) throws IOException, GraphException {
        //		Read the values S, A, L, and k
        //		pay attention when iterating over the input.. All testing input will be correctly formatted
        //		remember to identify the starting and ending rooms
        //		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
        //		To maintain this method cleaner, you may use the private helper method insertEdge
        int S = inputReader.read(); //Read S
        int A = inputReader.read();
        int L = inputReader.read();
        int K = inputReader.read();
        this.graph = new Graph(A * L);
        this.coins = K;
        for (int i = 0; i < L; i++) {
            String line = inputReader.readLine();

            if (i % 2 == 0) {
                for (int j = 0; j < A; j++) {
                    switch (line.charAt(j)) {
                        case 's':
                            start = graph.getNode(i * A + j);
                            break;
                        case 'x':
                            exit = graph.getNode(i * A + j);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                for (int j = 0; j < A; j++) {
                    switch (line.charAt(j)) {
                        case 'x':
                            exit = graph.getNode(i * A + j);
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    }

    private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
//		select the nodes and insert the appropriate edge.
    }

}
